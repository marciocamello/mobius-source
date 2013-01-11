/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.model.entity.residence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lineage2.commons.dao.JdbcEntity;
import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Residence implements JdbcEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public class ResidenceCycleTask extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			chanceCycle();
			update();
		}
	}
	
	private static final Logger _log = LoggerFactory.getLogger(Residence.class);
	public static final long CYCLE_TIME = 60 * 60 * 1000L;
	protected final int _id;
	protected final String _name;
	protected Clan _owner;
	protected Zone _zone;
	protected List<ResidenceFunction> _functions = new ArrayList<>();
	protected List<Skill> _skills = new ArrayList<>();
	protected SiegeEvent<?, ?> _siegeEvent;
	protected Calendar _siegeDate = Calendar.getInstance();
	protected Calendar _lastSiegeDate = Calendar.getInstance();
	protected Calendar _ownDate = Calendar.getInstance();
	protected ScheduledFuture<?> _cycleTask;
	private int _cycle;
	private int _rewardCount;
	private int _paidCycle;
	protected JdbcEntityState _jdbcEntityState = JdbcEntityState.CREATED;
	protected List<Location> _banishPoints = new ArrayList<>();
	protected List<Location> _ownerRestartPoints = new ArrayList<>();
	protected List<Location> _otherRestartPoints = new ArrayList<>();
	protected List<Location> _chaosRestartPoints = new ArrayList<>();
	
	public Residence(StatsSet set)
	{
		_id = set.getInteger("id");
		_name = set.getString("name");
	}
	
	public abstract ResidenceType getType();
	
	public void init()
	{
		initZone();
		initEvent();
		loadData();
		loadFunctions();
		rewardSkills();
		startCycleTask();
	}
	
	protected void initZone()
	{
		_zone = ReflectionUtils.getZone("residence_" + _id);
		_zone.setParam("residence", this);
	}
	
	protected void initEvent()
	{
		_siegeEvent = EventHolder.getInstance().getEvent(EventType.SIEGE_EVENT, _id);
	}
	
	@SuppressWarnings("unchecked")
	public <E extends SiegeEvent> E getSiegeEvent()
	{
		return (E) _siegeEvent;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getOwnerId()
	{
		return _owner == null ? 0 : _owner.getClanId();
	}
	
	public Clan getOwner()
	{
		return _owner;
	}
	
	public Zone getZone()
	{
		return _zone;
	}
	
	protected abstract void loadData();
	
	public abstract void changeOwner(Clan clan);
	
	public Calendar getOwnDate()
	{
		return _ownDate;
	}
	
	public Calendar getSiegeDate()
	{
		return _siegeDate;
	}
	
	public Calendar getLastSiegeDate()
	{
		return _lastSiegeDate;
	}
	
	public void addSkill(Skill skill)
	{
		_skills.add(skill);
	}
	
	public void addFunction(ResidenceFunction function)
	{
		_functions.add(function);
	}
	
	public boolean checkIfInZone(Location loc, Reflection ref)
	{
		return checkIfInZone(loc.x, loc.y, loc.z, ref);
	}
	
	public boolean checkIfInZone(int x, int y, int z, Reflection ref)
	{
		return (getZone() != null) && getZone().checkIfInZone(x, y, z, ref);
	}
	
	public void banishForeigner()
	{
		for (Player player : _zone.getInsidePlayers())
		{
			if (player.getClanId() == getOwnerId())
			{
				continue;
			}
			player.teleToLocation(getBanishPoint());
		}
	}
	
	public void rewardSkills()
	{
		Clan owner = getOwner();
		if (owner != null)
		{
			for (Skill skill : _skills)
			{
				owner.addSkill(skill, false);
				owner.broadcastToOnlineMembers(new SystemMessage2(SystemMsg.THE_CLAN_SKILL_S1_HAS_BEEN_ADDED).addSkillName(skill));
			}
		}
	}
	
	public void removeSkills()
	{
		Clan owner = getOwner();
		if (owner != null)
		{
			for (Skill skill : _skills)
			{
				owner.removeSkill(skill.getId());
			}
		}
	}
	
	protected void loadFunctions()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM residence_functions WHERE id = ?");
			statement.setInt(1, getId());
			rs = statement.executeQuery();
			while (rs.next())
			{
				ResidenceFunction function = getFunction(rs.getInt("type"));
				function.setLvl(rs.getInt("lvl"));
				function.setEndTimeInMillis(rs.getInt("endTime") * 1000L);
				function.setInDebt(rs.getBoolean("inDebt"));
				function.setActive(true);
				startAutoTaskForFunction(function);
			}
		}
		catch (Exception e)
		{
			_log.warn("Residence: loadFunctions(): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}
	
	public boolean isFunctionActive(int type)
	{
		ResidenceFunction function = getFunction(type);
		if ((function != null) && function.isActive() && (function.getLevel() > 0))
		{
			return true;
		}
		return false;
	}
	
	public ResidenceFunction getFunction(int type)
	{
		for (int i = 0; i < _functions.size(); i++)
		{
			if (_functions.get(i).getType() == type)
			{
				return _functions.get(i);
			}
		}
		return null;
	}
	
	public boolean updateFunctions(int type, int level)
	{
		Clan clan = getOwner();
		if (clan == null)
		{
			return false;
		}
		long count = clan.getAdenaCount();
		ResidenceFunction function = getFunction(type);
		if (function == null)
		{
			return false;
		}
		if (function.isActive() && (function.getLevel() == level))
		{
			return true;
		}
		int lease = level == 0 ? 0 : getFunction(type).getLease(level);
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			if (!function.isActive())
			{
				if (count >= lease)
				{
					clan.getWarehouse().destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, lease);
				}
				else
				{
					return false;
				}
				long time = Calendar.getInstance().getTimeInMillis() + 86400000;
				statement = con.prepareStatement("REPLACE residence_functions SET id=?, type=?, lvl=?, endTime=?");
				statement.setInt(1, getId());
				statement.setInt(2, type);
				statement.setInt(3, level);
				statement.setInt(4, (int) (time / 1000));
				statement.execute();
				function.setLvl(level);
				function.setEndTimeInMillis(time);
				function.setActive(true);
				startAutoTaskForFunction(function);
			}
			else
			{
				if (count >= (lease - getFunction(type).getLease()))
				{
					if (lease > getFunction(type).getLease())
					{
						clan.getWarehouse().destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, lease - getFunction(type).getLease());
					}
				}
				else
				{
					return false;
				}
				statement = con.prepareStatement("REPLACE residence_functions SET id=?, type=?, lvl=?");
				statement.setInt(1, getId());
				statement.setInt(2, type);
				statement.setInt(3, level);
				statement.execute();
				function.setLvl(level);
			}
		}
		catch (Exception e)
		{
			_log.warn("Exception: SiegeUnit.updateFunctions(int type, int lvl, int lease, long rate, long time, boolean addNew): " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}
	
	public void removeFunction(int type)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM residence_functions WHERE id=? AND type=?");
			statement.setInt(1, getId());
			statement.setInt(2, type);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("Exception: removeFunctions(int type): " + e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	void startAutoTaskForFunction(ResidenceFunction function)
	{
		if (getOwnerId() == 0)
		{
			return;
		}
		Clan clan = getOwner();
		if (clan == null)
		{
			return;
		}
		if (function.getEndTimeInMillis() > System.currentTimeMillis())
		{
			ThreadPoolManager.getInstance().schedule(new AutoTaskForFunctions(function), function.getEndTimeInMillis() - System.currentTimeMillis());
		}
		else if (function.isInDebt() && (clan.getAdenaCount() >= function.getLease()))
		{
			clan.getWarehouse().destroyItemByItemId(ItemTemplate.ITEM_ID_ADENA, function.getLease());
			function.updateRentTime(false);
			ThreadPoolManager.getInstance().schedule(new AutoTaskForFunctions(function), function.getEndTimeInMillis() - System.currentTimeMillis());
		}
		else if (!function.isInDebt())
		{
			function.setInDebt(true);
			function.updateRentTime(true);
			ThreadPoolManager.getInstance().schedule(new AutoTaskForFunctions(function), function.getEndTimeInMillis() - System.currentTimeMillis());
		}
		else
		{
			function.setLvl(0);
			function.setActive(false);
			removeFunction(function.getType());
		}
	}
	
	private class AutoTaskForFunctions extends RunnableImpl
	{
		ResidenceFunction _function;
		
		public AutoTaskForFunctions(ResidenceFunction function)
		{
			_function = function;
		}
		
		@Override
		public void runImpl()
		{
			startAutoTaskForFunction(_function);
		}
	}
	
	@Override
	public void setJdbcState(JdbcEntityState state)
	{
		_jdbcEntityState = state;
	}
	
	@Override
	public JdbcEntityState getJdbcState()
	{
		return _jdbcEntityState;
	}
	
	@Override
	public void save()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void delete()
	{
		throw new UnsupportedOperationException();
	}
	
	public void cancelCycleTask()
	{
		_cycle = 0;
		_paidCycle = 0;
		_rewardCount = 0;
		if (_cycleTask != null)
		{
			_cycleTask.cancel(false);
			_cycleTask = null;
		}
		setJdbcState(JdbcEntityState.UPDATED);
	}
	
	public void startCycleTask()
	{
		if (_owner == null)
		{
			return;
		}
		long ownedTime = getOwnDate().getTimeInMillis();
		if (ownedTime == 0)
		{
			return;
		}
		long diff = System.currentTimeMillis() - ownedTime;
		while (diff >= CYCLE_TIME)
		{
			diff -= CYCLE_TIME;
		}
		_cycleTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new ResidenceCycleTask(), diff, CYCLE_TIME);
	}
	
	public void chanceCycle()
	{
		setCycle(getCycle() + 1);
		setJdbcState(JdbcEntityState.UPDATED);
	}
	
	public List<Skill> getSkills()
	{
		return _skills;
	}
	
	public void addBanishPoint(Location loc)
	{
		_banishPoints.add(loc);
	}
	
	public void addOwnerRestartPoint(Location loc)
	{
		_ownerRestartPoints.add(loc);
	}
	
	public void addOtherRestartPoint(Location loc)
	{
		_otherRestartPoints.add(loc);
	}
	
	public void addChaosRestartPoint(Location loc)
	{
		_chaosRestartPoints.add(loc);
	}
	
	public Location getBanishPoint()
	{
		if (_banishPoints.isEmpty())
		{
			return null;
		}
		return _banishPoints.get(Rnd.get(_banishPoints.size()));
	}
	
	public Location getOwnerRestartPoint()
	{
		if (_ownerRestartPoints.isEmpty())
		{
			return null;
		}
		return _ownerRestartPoints.get(Rnd.get(_ownerRestartPoints.size()));
	}
	
	public Location getOtherRestartPoint()
	{
		if (_otherRestartPoints.isEmpty())
		{
			return null;
		}
		return _otherRestartPoints.get(Rnd.get(_otherRestartPoints.size()));
	}
	
	public Location getChaosRestartPoint()
	{
		if (_chaosRestartPoints.isEmpty())
		{
			return null;
		}
		return _chaosRestartPoints.get(Rnd.get(_chaosRestartPoints.size()));
	}
	
	public Location getNotOwnerRestartPoint(Player player)
	{
		return player.getKarma() < 0 ? getChaosRestartPoint() : getOtherRestartPoint();
	}
	
	public int getCycle()
	{
		return _cycle;
	}
	
	public long getCycleDelay()
	{
		if (_cycleTask == null)
		{
			return 0;
		}
		return _cycleTask.getDelay(TimeUnit.SECONDS);
	}
	
	public void setCycle(int cycle)
	{
		_cycle = cycle;
	}
	
	public int getPaidCycle()
	{
		return _paidCycle;
	}
	
	public void setPaidCycle(int paidCycle)
	{
		_paidCycle = paidCycle;
	}
	
	public int getRewardCount()
	{
		return _rewardCount;
	}
	
	public void setRewardCount(int rewardCount)
	{
		_rewardCount = rewardCount;
	}
}
