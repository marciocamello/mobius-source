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
package lineage2.gameserver.model;

import java.util.List;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.EventOwner;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.MinionInstance;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.taskmanager.SpawnTaskManager;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.templates.spawn.SpawnRange;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Spawner extends EventOwner implements Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger _log = LoggerFactory.getLogger(Spawner.class);
	protected static final int MIN_RESPAWN_DELAY = 20;
	protected int _maximumCount;
	protected int _referenceCount;
	protected int _currentCount;
	protected int _scheduledCount;
	protected int _respawnDelay, _respawnDelayRandom, _nativeRespawnDelay;
	protected int _respawnTime;
	protected boolean _doRespawn;
	protected NpcInstance _lastSpawn;
	protected List<NpcInstance> _spawned;
	protected Reflection _reflection = ReflectionManager.DEFAULT;
	
	public void decreaseScheduledCount()
	{
		if (_scheduledCount > 0)
		{
			_scheduledCount--;
		}
	}
	
	public boolean isDoRespawn()
	{
		return _doRespawn;
	}
	
	public Reflection getReflection()
	{
		return _reflection;
	}
	
	public void setReflection(Reflection reflection)
	{
		_reflection = reflection;
	}
	
	public int getRespawnDelay()
	{
		return _respawnDelay;
	}
	
	public int getNativeRespawnDelay()
	{
		return _nativeRespawnDelay;
	}
	
	public int getRespawnDelayRandom()
	{
		return _respawnDelayRandom;
	}
	
	public int getRespawnDelayWithRnd()
	{
		return _respawnDelayRandom == 0 ? _respawnDelay : Rnd.get(_respawnDelay - _respawnDelayRandom, _respawnDelay);
	}
	
	public int getRespawnTime()
	{
		return _respawnTime;
	}
	
	public NpcInstance getLastSpawn()
	{
		return _lastSpawn;
	}
	
	public void setAmount(int amount)
	{
		if (_referenceCount == 0)
		{
			_referenceCount = amount;
		}
		_maximumCount = amount;
	}
	
	public void deleteAll()
	{
		stopRespawn();
		for (NpcInstance npc : _spawned)
		{
			npc.deleteMe();
		}
		_spawned.clear();
		_respawnTime = 0;
		_scheduledCount = 0;
		_currentCount = 0;
	}
	
	public abstract void decreaseCount(NpcInstance oldNpc);
	
	public abstract NpcInstance doSpawn(boolean spawn);
	
	public abstract void respawnNpc(NpcInstance oldNpc);
	
	protected abstract NpcInstance initNpc(NpcInstance mob, boolean spawn, MultiValueSet<String> set);
	
	public abstract int getCurrentNpcId();
	
	public abstract SpawnRange getCurrentSpawnRange();
	
	public int init()
	{
		while ((_currentCount + _scheduledCount) < _maximumCount)
		{
			doSpawn(false);
		}
		_doRespawn = true;
		return _currentCount;
	}
	
	public NpcInstance spawnOne()
	{
		return doSpawn(false);
	}
	
	public void stopRespawn()
	{
		_doRespawn = false;
	}
	
	public void startRespawn()
	{
		_doRespawn = true;
	}
	
	public List<NpcInstance> getAllSpawned()
	{
		return _spawned;
	}
	
	public NpcInstance getFirstSpawned()
	{
		List<NpcInstance> npcs = getAllSpawned();
		return npcs.size() > 0 ? npcs.get(0) : null;
	}
	
	public void setRespawnDelay(int respawnDelay, int respawnDelayRandom)
	{
		if (respawnDelay < 0)
		{
			_log.warn("respawn delay is negative");
		}
		_nativeRespawnDelay = respawnDelay;
		_respawnDelay = respawnDelay;
		_respawnDelayRandom = respawnDelayRandom;
	}
	
	public void setRespawnDelay(int respawnDelay)
	{
		setRespawnDelay(respawnDelay, 0);
	}
	
	public void setRespawnTime(int respawnTime)
	{
		_respawnTime = respawnTime;
	}
	
	protected NpcInstance doSpawn0(NpcTemplate template, boolean spawn, MultiValueSet<String> set)
	{
		if (template.isInstanceOf(PetInstance.class) || template.isInstanceOf(MinionInstance.class))
		{
			_currentCount++;
			return null;
		}
		NpcInstance tmp = template.getNewInstance();
		if (tmp == null)
		{
			return null;
		}
		if (!spawn)
		{
			spawn = _respawnTime <= ((System.currentTimeMillis() / 1000) + MIN_RESPAWN_DELAY);
		}
		return initNpc(tmp, spawn, set);
	}
	
	protected NpcInstance initNpc0(NpcInstance mob, Location newLoc, boolean spawn, MultiValueSet<String> set)
	{
		mob.setParameters(set);
		mob.setCurrentHpMp(mob.getMaxHp(), mob.getMaxMp(), true);
		mob.setSpawn(this);
		mob.setSpawnedLoc(newLoc);
		mob.setUnderground(GeoEngine.getHeight(newLoc, getReflection().getGeoIndex()) < GeoEngine.getHeight(newLoc.clone().changeZ(5000), getReflection().getGeoIndex()));
		for (GlobalEvent e : getEvents())
		{
			mob.addEvent(e);
		}
		if (spawn)
		{
			mob.setReflection(getReflection());
			if (mob.isMonster())
			{
				((MonsterInstance) mob).setChampion();
			}
			mob.spawnMe(newLoc);
			_currentCount++;
		}
		else
		{
			mob.setLoc(newLoc);
			_scheduledCount++;
			SpawnTaskManager.getInstance().addSpawnTask(mob, (_respawnTime * 1000L) - System.currentTimeMillis());
		}
		_spawned.add(mob);
		_lastSpawn = mob;
		return mob;
	}
	
	public void decreaseCount0(NpcTemplate template, NpcInstance spawnedNpc, long deadTime)
	{
		_currentCount--;
		if (_currentCount < 0)
		{
			_currentCount = 0;
		}
		if (_respawnDelay == 0)
		{
			return;
		}
		if (_doRespawn && ((_scheduledCount + _currentCount) < _maximumCount))
		{
			_scheduledCount++;
			long delay = (long) (template.isRaid ? Config.ALT_RAID_RESPAWN_MULTIPLIER * getRespawnDelayWithRnd() : getRespawnDelayWithRnd()) * 1000L;
			delay = Math.max(1000, delay - deadTime);
			_respawnTime = (int) ((System.currentTimeMillis() + delay) / 1000);
			SpawnTaskManager.getInstance().addSpawnTask(spawnedNpc, delay);
		}
	}
}
