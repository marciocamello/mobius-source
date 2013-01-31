/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.model.entity.residence;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.dao.DominionDAO;
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeRunnerEvent;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.StatsSet;

import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.TreeIntSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Dominion extends Residence
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _flags.
	 */
	private final IntSet _flags = new TreeIntSet();
	/**
	 * Field _castle.
	 */
	private Castle _castle;
	/**
	 * Field _lordObjectId.
	 */
	private int _lordObjectId;
	
	/**
	 * Constructor for Dominion.
	 * @param set StatsSet
	 */
	public Dominion(StatsSet set)
	{
		super(set);
	}
	
	/**
	 * Method init.
	 */
	@Override
	public void init()
	{
		initEvent();
		_castle = ResidenceHolder.getInstance().getResidence(Castle.class, getId() - 80);
		_castle.setDominion(this);
		loadData();
		_siegeDate.setTimeInMillis(0);
		if (getOwner() != null)
		{
			DominionSiegeRunnerEvent runnerEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 1);
			runnerEvent.registerDominion(this);
		}
	}
	
	/**
	 * Method rewardSkills.
	 */
	@Override
	public void rewardSkills()
	{
		Clan owner = getOwner();
		if (owner != null)
		{
			if (!_flags.contains(getId()))
			{
				return;
			}
			for (int dominionId : _flags.toArray())
			{
				Dominion dominion = ResidenceHolder.getInstance().getResidence(Dominion.class, dominionId);
				for (Skill skill : dominion.getSkills())
				{
					owner.addSkill(skill, false);
					owner.broadcastToOnlineMembers(new SystemMessage2(SystemMsg.THE_CLAN_SKILL_S1_HAS_BEEN_ADDED).addSkillName(skill));
				}
			}
		}
	}
	
	/**
	 * Method removeSkills.
	 */
	@Override
	public void removeSkills()
	{
		Clan owner = getOwner();
		if (owner != null)
		{
			for (int dominionId : _flags.toArray())
			{
				Dominion dominion = ResidenceHolder.getInstance().getResidence(Dominion.class, dominionId);
				for (Skill skill : dominion.getSkills())
				{
					owner.removeSkill(skill.getId());
				}
			}
		}
	}
	
	/**
	 * Method addFlag.
	 * @param dominionId int
	 */
	public void addFlag(int dominionId)
	{
		_flags.add(dominionId);
	}
	
	/**
	 * Method removeFlag.
	 * @param dominionId int
	 */
	public void removeFlag(int dominionId)
	{
		_flags.remove(dominionId);
	}
	
	/**
	 * Method getFlags.
	 * @return int[]
	 */
	public int[] getFlags()
	{
		return _flags.toArray();
	}
	
	/**
	 * Method getType.
	 * @return ResidenceType
	 */
	@Override
	public ResidenceType getType()
	{
		return ResidenceType.Dominion;
	}
	
	/**
	 * Method loadData.
	 */
	@Override
	protected void loadData()
	{
		DominionDAO.getInstance().select(this);
	}
	
	/**
	 * Method changeOwner.
	 * @param clan Clan
	 */
	@Override
	public void changeOwner(Clan clan)
	{
		int newLordObjectId;
		if (clan == null)
		{
			if (_lordObjectId > 0)
			{
				newLordObjectId = 0;
			}
			else
			{
				return;
			}
		}
		else
		{
			newLordObjectId = clan.getLeaderId();
			SystemMessage2 message = new SystemMessage2(SystemMsg.CLAN_LORD_C2_WHO_LEADS_CLAN_S1_HAS_BEEN_DECLARED_THE_LORD_OF_THE_S3_TERRITORY).addName(clan.getLeader().getPlayer()).addString(clan.getName()).addResidenceName(getCastle());
			for (Player player : GameObjectsStorage.getAllPlayersForIterate())
			{
				player.sendPacket(message);
			}
		}
		_lordObjectId = newLordObjectId;
		setJdbcState(JdbcEntityState.UPDATED);
		update();
		for (NpcInstance npc : GameObjectsStorage.getAllNpcsForIterate())
		{
			if (npc.getDominion() == this)
			{
				npc.broadcastCharInfoImpl();
			}
		}
	}
	
	/**
	 * Method getLordObjectId.
	 * @return int
	 */
	public int getLordObjectId()
	{
		return _lordObjectId;
	}
	
	/**
	 * Method getOwner.
	 * @return Clan
	 */
	@Override
	public Clan getOwner()
	{
		return _castle.getOwner();
	}
	
	/**
	 * Method getOwnerId.
	 * @return int
	 */
	@Override
	public int getOwnerId()
	{
		return _castle.getOwnerId();
	}
	
	/**
	 * Method getCastle.
	 * @return Castle
	 */
	public Castle getCastle()
	{
		return _castle;
	}
	
	/**
	 * Method update.
	 * @see lineage2.commons.dao.JdbcEntity#update()
	 */
	@Override
	public void update()
	{
		DominionDAO.getInstance().update(this);
	}
	
	/**
	 * Method setLordObjectId.
	 * @param lordObjectId int
	 */
	public void setLordObjectId(int lordObjectId)
	{
		_lordObjectId = lordObjectId;
	}
}
