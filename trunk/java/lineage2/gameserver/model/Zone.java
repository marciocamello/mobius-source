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
package lineage2.gameserver.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lineage2.commons.collections.LazyArrayList;
import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.listener.Listener;
import lineage2.commons.listener.ListenerList;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncAdd;
import lineage2.gameserver.taskmanager.EffectTaskManager;
import lineage2.gameserver.templates.ZoneTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;

public class Zone
{
	public static final Zone[] EMPTY_L2ZONE_ARRAY = new Zone[0];
	
	public static enum ZoneType
	{
		AirshipController,
		SIEGE,
		RESIDENCE,
		HEADQUARTER,
		FISHING,
		CHANGED_ZONE,
		water,
		battle_zone,
		damage,
		instant_skill,
		mother_tree,
		peace_zone,
		poison,
		ssq_zone,
		swamp,
		no_escape,
		no_landing,
		no_restart,
		no_summon,
		dummy,
		offshore,
		epic,
		JUMPING,
	}
	
	public enum ZoneTarget
	{
		pc,
		npc,
		only_pc
	}
	
	public static final String BLOCKED_ACTION_PRIVATE_STORE = "open_private_store";
	public static final String BLOCKED_ACTION_PRIVATE_WORKSHOP = "open_private_workshop";
	public static final String BLOCKED_ACTION_DROP_MERCHANT_GUARD = "drop_merchant_guard";
	public static final String BLOCKED_ACTION_SAVE_BOOKMARK = "save_bookmark";
	public static final String BLOCKED_ACTION_USE_BOOKMARK = "use_bookmark";
	public static final String BLOCKED_ACTION_MINIMAP = "open_minimap";
	
	private abstract class ZoneTimer extends RunnableImpl
	{
		protected Creature cha;
		protected Future<?> future;
		protected boolean active;
		
		public ZoneTimer(Creature cha)
		{
			this.cha = cha;
		}
		
		public void start()
		{
			active = true;
			future = EffectTaskManager.getInstance().schedule(this, getTemplate().getInitialDelay() * 1000L);
		}
		
		public void stop()
		{
			active = false;
			if (future != null)
			{
				future.cancel(false);
				future = null;
			}
		}
		
		public void next()
		{
			if (!active)
			{
				return;
			}
			if ((getTemplate().getUnitTick() == 0) && (getTemplate().getRandomTick() == 0))
			{
				return;
			}
			future = EffectTaskManager.getInstance().schedule(this, (getTemplate().getUnitTick() + Rnd.get(0, getTemplate().getRandomTick())) * 1000L);
		}
		
		@Override
		public abstract void runImpl() throws Exception;
	}
	
	private class SkillTimer extends ZoneTimer
	{
		public SkillTimer(Creature cha)
		{
			super(cha);
		}
		
		@Override
		public void runImpl()
		{
			if (!isActive())
			{
				return;
			}
			if (!checkTarget(cha))
			{
				return;
			}
			Skill skill = getZoneSkill();
			if (skill == null)
			{
				return;
			}
			if (Rnd.chance(getTemplate().getSkillProb()) && !cha.isDead())
			{
				skill.getEffects(cha, cha, false, false);
			}
			next();
		}
	}
	
	private class DamageTimer extends ZoneTimer
	{
		public DamageTimer(Creature cha)
		{
			super(cha);
		}
		
		@Override
		public void runImpl()
		{
			if (!isActive())
			{
				return;
			}
			if (!checkTarget(cha))
			{
				return;
			}
			int hp = getDamageOnHP();
			int mp = getDamageOnMP();
			int message = getDamageMessageId();
			if ((hp == 0) && (mp == 0))
			{
				return;
			}
			if (hp > 0)
			{
				cha.reduceCurrentHp(hp, 0, cha, null, false, false, true, false, false, false, true);
				if (message > 0)
				{
					cha.sendPacket(new SystemMessage(message).addNumber(hp));
				}
			}
			if (mp > 0)
			{
				cha.reduceCurrentMp(mp, null);
				if (message > 0)
				{
					cha.sendPacket(new SystemMessage(message).addNumber(mp));
				}
			}
			next();
		}
	}
	
	public class ZoneListenerList extends ListenerList<Zone>
	{
		public void onEnter(Creature actor)
		{
			if (!getListeners().isEmpty())
			{
				for (Listener<Zone> listener : getListeners())
				{
					((OnZoneEnterLeaveListener) listener).onZoneEnter(Zone.this, actor);
				}
			}
		}
		
		public void onLeave(Creature actor)
		{
			if (!getListeners().isEmpty())
			{
				for (Listener<Zone> listener : getListeners())
				{
					((OnZoneEnterLeaveListener) listener).onZoneLeave(Zone.this, actor);
				}
			}
		}
	}
	
	private ZoneType _type;
	private boolean _active;
	private final MultiValueSet<String> _params;
	private final ZoneTemplate _template;
	private Reflection _reflection;
	private final ZoneListenerList listeners = new ZoneListenerList();
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();
	private final List<Creature> _objects = new LazyArrayList<>(32);
	private final Map<Creature, ZoneTimer> _zoneTimers = new ConcurrentHashMap<>();
	public final static int ZONE_STATS_ORDER = 0x40;
	
	public Zone(ZoneTemplate template)
	{
		this(template.getType(), template);
	}
	
	public Zone(ZoneType type, ZoneTemplate template)
	{
		_type = type;
		_template = template;
		_params = template.getParams();
	}
	
	public ZoneTemplate getTemplate()
	{
		return _template;
	}
	
	public final String getName()
	{
		return getTemplate().getName();
	}
	
	public ZoneType getType()
	{
		return _type;
	}
	
	public void setType(ZoneType type)
	{
		_type = type;
	}
	
	public Territory getTerritory()
	{
		return getTemplate().getTerritory();
	}
	
	public final int getEnteringMessageId()
	{
		return getTemplate().getEnteringMessageId();
	}
	
	public final int getLeavingMessageId()
	{
		return getTemplate().getLeavingMessageId();
	}
	
	public Skill getZoneSkill()
	{
		return getTemplate().getZoneSkill();
	}
	
	public ZoneTarget getZoneTarget()
	{
		return getTemplate().getZoneTarget();
	}
	
	public Race getAffectRace()
	{
		return getTemplate().getAffectRace();
	}
	
	public int getDamageMessageId()
	{
		return getTemplate().getDamageMessageId();
	}
	
	public int getDamageOnHP()
	{
		return getTemplate().getDamageOnHP();
	}
	
	public int getDamageOnMP()
	{
		return getTemplate().getDamageOnMP();
	}
	
	public double getMoveBonus()
	{
		return getTemplate().getMoveBonus();
	}
	
	public double getRegenBonusHP()
	{
		return getTemplate().getRegenBonusHP();
	}
	
	public double getRegenBonusMP()
	{
		return getTemplate().getRegenBonusMP();
	}
	
	public long getRestartTime()
	{
		return getTemplate().getRestartTime();
	}
	
	public List<Location> getRestartPoints()
	{
		return getTemplate().getRestartPoints();
	}
	
	public List<Location> getPKRestartPoints()
	{
		return getTemplate().getPKRestartPoints();
	}
	
	public Location getSpawn()
	{
		if (getRestartPoints() == null)
		{
			return null;
		}
		Location loc = getRestartPoints().get(Rnd.get(getRestartPoints().size()));
		return loc.clone();
	}
	
	public Location getPKSpawn()
	{
		if (getPKRestartPoints() == null)
		{
			return getSpawn();
		}
		Location loc = getPKRestartPoints().get(Rnd.get(getPKRestartPoints().size()));
		return loc.clone();
	}
	
	public boolean checkIfInZone(int x, int y)
	{
		return getTerritory().isInside(x, y);
	}
	
	public boolean checkIfInZone(int x, int y, int z)
	{
		return checkIfInZone(x, y, z, getReflection());
	}
	
	public boolean checkIfInZone(int x, int y, int z, Reflection reflection)
	{
		return isActive() && (_reflection == reflection) && getTerritory().isInside(x, y, z);
	}
	
	public boolean checkIfInZone(Creature cha)
	{
		readLock.lock();
		try
		{
			return _objects.contains(cha);
		}
		finally
		{
			readLock.unlock();
		}
	}
	
	public final double findDistanceToZone(GameObject obj, boolean includeZAxis)
	{
		return findDistanceToZone(obj.getX(), obj.getY(), obj.getZ(), includeZAxis);
	}
	
	public final double findDistanceToZone(int x, int y, int z, boolean includeZAxis)
	{
		return PositionUtils.calculateDistance(x, y, z, (getTerritory().getXmax() + getTerritory().getXmin()) / 2, (getTerritory().getYmax() + getTerritory().getYmin()) / 2, (getTerritory().getZmax() + getTerritory().getZmin()) / 2, includeZAxis);
	}
	
	public void doEnter(Creature cha)
	{
		boolean added = false;
		writeLock.lock();
		try
		{
			if (!_objects.contains(cha))
			{
				added = _objects.add(cha);
			}
		}
		finally
		{
			writeLock.unlock();
		}
		if (added)
		{
			onZoneEnter(cha);
		}
	}
	
	protected void onZoneEnter(Creature actor)
	{
		checkEffects(actor, true);
		addZoneStats(actor);
		if (actor.isPlayer())
		{
			if (getEnteringMessageId() != 0)
			{
				actor.sendPacket(new SystemMessage(getEnteringMessageId()));
			}
			if (getTemplate().getEventId() != 0)
			{
				actor.sendPacket(new EventTrigger(getTemplate().getEventId(), true));
			}
			if (getTemplate().getBlockedActions() != null)
			{
				((Player) actor).blockActions(getTemplate().getBlockedActions());
			}
		}
		listeners.onEnter(actor);
	}
	
	public void doLeave(Creature cha)
	{
		boolean removed = false;
		writeLock.lock();
		try
		{
			removed = _objects.remove(cha);
		}
		finally
		{
			writeLock.unlock();
		}
		if (removed)
		{
			onZoneLeave(cha);
		}
	}
	
	protected void onZoneLeave(Creature actor)
	{
		checkEffects(actor, false);
		removeZoneStats(actor);
		if (actor.isPlayer())
		{
			if ((getLeavingMessageId() != 0) && actor.isPlayer())
			{
				actor.sendPacket(new SystemMessage(getLeavingMessageId()));
			}
			if ((getTemplate().getEventId() != 0) && actor.isPlayer())
			{
				actor.sendPacket(new EventTrigger(getTemplate().getEventId(), false));
			}
			if (getTemplate().getBlockedActions() != null)
			{
				((Player) actor).unblockActions(getTemplate().getBlockedActions());
			}
		}
		listeners.onLeave(actor);
	}
	
	private void addZoneStats(Creature cha)
	{
		if (!checkTarget(cha))
		{
			return;
		}
		if (getMoveBonus() != 0)
		{
			if (cha.isPlayable())
			{
				cha.addStatFunc(new FuncAdd(Stats.RUN_SPEED, ZONE_STATS_ORDER, this, getMoveBonus()));
				cha.sendChanges();
			}
		}
		if (getRegenBonusHP() != 0)
		{
			cha.addStatFunc(new FuncAdd(Stats.REGENERATE_HP_RATE, ZONE_STATS_ORDER, this, getRegenBonusHP()));
		}
		if (getRegenBonusMP() != 0)
		{
			cha.addStatFunc(new FuncAdd(Stats.REGENERATE_MP_RATE, ZONE_STATS_ORDER, this, getRegenBonusMP()));
		}
	}
	
	private void removeZoneStats(Creature cha)
	{
		if ((getRegenBonusHP() == 0) && (getRegenBonusMP() == 0) && (getMoveBonus() == 0))
		{
			return;
		}
		cha.removeStatsOwner(this);
		cha.sendChanges();
	}
	
	private void checkEffects(Creature cha, boolean enter)
	{
		if (checkTarget(cha))
		{
			if (enter)
			{
				if (getZoneSkill() != null)
				{
					ZoneTimer timer = new SkillTimer(cha);
					_zoneTimers.put(cha, timer);
					timer.start();
				}
				else if ((getDamageOnHP() > 0) || (getDamageOnHP() > 0))
				{
					ZoneTimer timer = new DamageTimer(cha);
					_zoneTimers.put(cha, timer);
					timer.start();
				}
			}
			else
			{
				ZoneTimer timer = _zoneTimers.remove(cha);
				if (timer != null)
				{
					timer.stop();
				}
				if (getZoneSkill() != null)
				{
					cha.getEffectList().stopEffect(getZoneSkill());
				}
			}
		}
	}
	
	boolean checkTarget(Creature cha)
	{
		switch (getZoneTarget())
		{
			case pc:
				if (!cha.isPlayable())
				{
					return false;
				}
				break;
			case only_pc:
				if (!cha.isPlayer())
				{
					return false;
				}
				break;
			case npc:
				if (!cha.isNpc())
				{
					return false;
				}
				break;
		}
		if (getAffectRace() != null)
		{
			Player player = cha.getPlayer();
			if (player == null)
			{
				return false;
			}
			if (player.getRace() != getAffectRace())
			{
				return false;
			}
		}
		return true;
	}
	
	public Creature[] getObjects()
	{
		readLock.lock();
		try
		{
			return _objects.toArray(new Creature[_objects.size()]);
		}
		finally
		{
			readLock.unlock();
		}
	}
	
	public List<Player> getInsidePlayers()
	{
		List<Player> result = new LazyArrayList<>();
		readLock.lock();
		try
		{
			Creature cha;
			for (int i = 0; i < _objects.size(); i++)
			{
				if (((cha = _objects.get(i)) != null) && cha.isPlayer())
				{
					result.add((Player) cha);
				}
			}
		}
		finally
		{
			readLock.unlock();
		}
		return result;
	}
	
	public List<Playable> getInsidePlayables()
	{
		List<Playable> result = new LazyArrayList<>();
		readLock.lock();
		try
		{
			Creature cha;
			for (int i = 0; i < _objects.size(); i++)
			{
				if (((cha = _objects.get(i)) != null) && cha.isPlayable())
				{
					result.add((Playable) cha);
				}
			}
		}
		finally
		{
			readLock.unlock();
		}
		return result;
	}
	
	public void setActive(boolean value)
	{
		writeLock.lock();
		try
		{
			if (_active == value)
			{
				return;
			}
			_active = value;
		}
		finally
		{
			writeLock.unlock();
		}
		if (isActive())
		{
			World.addZone(Zone.this);
		}
		else
		{
			World.removeZone(Zone.this);
		}
	}
	
	public boolean isActive()
	{
		return _active;
	}
	
	public void setReflection(Reflection reflection)
	{
		_reflection = reflection;
	}
	
	public Reflection getReflection()
	{
		return _reflection;
	}
	
	public void setParam(String name, String value)
	{
		_params.put(name, value);
	}
	
	public void setParam(String name, Object value)
	{
		_params.put(name, value);
	}
	
	public MultiValueSet<String> getParams()
	{
		return _params;
	}
	
	public <T extends Listener<Zone>> boolean addListener(T listener)
	{
		return listeners.add(listener);
	}
	
	public <T extends Listener<Zone>> boolean removeListener(T listener)
	{
		return listeners.remove(listener);
	}
	
	@Override
	public final String toString()
	{
		return "[Zone " + getType() + " name: " + getName() + "]";
	}
	
	public void broadcastPacket(L2GameServerPacket packet, boolean toAliveOnly)
	{
		List<Player> insideZoners = getInsidePlayers();
		if ((insideZoners != null) && !insideZoners.isEmpty())
		{
			for (Player player : insideZoners)
			{
				if (toAliveOnly)
				{
					if (!player.isDead())
					{
						player.broadcastPacket(packet);
					}
				}
				else
				{
					player.broadcastPacket(packet);
				}
			}
		}
	}
}
