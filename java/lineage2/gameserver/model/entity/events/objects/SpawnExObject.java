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
package lineage2.gameserver.model.entity.events.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lineage2.gameserver.instancemanager.SpawnManager;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.NpcInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpawnExObject implements SpawnableObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger _log = LoggerFactory.getLogger(SpawnExObject.class);
	private final List<Spawner> _spawns;
	private boolean _spawned;
	private final String _name;
	
	public SpawnExObject(String name)
	{
		_name = name;
		_spawns = SpawnManager.getInstance().getSpawners(_name);
		if (_spawns.isEmpty())
		{
			_log.info("SpawnExObject: not found spawn group: " + name);
		}
	}
	
	@Override
	public void spawnObject(GlobalEvent event)
	{
		if (_spawned)
		{
			_log.info("SpawnExObject: can't spawn twice: " + _name + "; event: " + event, new Exception());
		}
		else
		{
			for (Spawner spawn : _spawns)
			{
				if (event.isInProgress())
				{
					spawn.addEvent(event);
				}
				else
				{
					spawn.removeEvent(event);
				}
				spawn.setReflection(event.getReflection());
				spawn.init();
			}
			_spawned = true;
		}
	}
	
	@Override
	public void despawnObject(GlobalEvent event)
	{
		if (!_spawned)
		{
			return;
		}
		_spawned = false;
		for (Spawner spawn : _spawns)
		{
			spawn.removeEvent(event);
			spawn.deleteAll();
		}
	}
	
	@Override
	public void refreshObject(GlobalEvent event)
	{
		for (NpcInstance npc : getAllSpawned())
		{
			if (event.isInProgress())
			{
				npc.addEvent(event);
			}
			else
			{
				npc.removeEvent(event);
			}
		}
	}
	
	public List<Spawner> getSpawns()
	{
		return _spawns;
	}
	
	public List<NpcInstance> getAllSpawned()
	{
		List<NpcInstance> npcs = new ArrayList<>();
		for (Spawner spawn : _spawns)
		{
			npcs.addAll(spawn.getAllSpawned());
		}
		return npcs.isEmpty() ? Collections.<NpcInstance> emptyList() : npcs;
	}
	
	public NpcInstance getFirstSpawned()
	{
		List<NpcInstance> npcs = getAllSpawned();
		return npcs.size() > 0 ? npcs.get(0) : null;
	}
	
	public boolean isSpawned()
	{
		return _spawned;
	}
}
