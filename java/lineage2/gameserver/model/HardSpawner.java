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
import java.util.concurrent.CopyOnWriteArrayList;

import lineage2.commons.collections.MultiValueSet;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.spawn.SpawnNpcInfo;
import lineage2.gameserver.templates.spawn.SpawnRange;
import lineage2.gameserver.templates.spawn.SpawnTemplate;

public class HardSpawner extends Spawner
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final SpawnTemplate _template;
	private int _pointIndex;
	private int _npcIndex;
	private final List<NpcInstance> _reSpawned = new CopyOnWriteArrayList<>();
	
	public HardSpawner(SpawnTemplate template)
	{
		_template = template;
		_spawned = new CopyOnWriteArrayList<>();
	}
	
	@Override
	public void decreaseCount(NpcInstance oldNpc)
	{
		oldNpc.setSpawn(null);
		oldNpc.deleteMe();
		_spawned.remove(oldNpc);
		SpawnNpcInfo npcInfo = getNextNpcInfo();
		NpcInstance npc = npcInfo.getTemplate().getNewInstance();
		npc.setSpawn(this);
		_reSpawned.add(npc);
		decreaseCount0(npcInfo.getTemplate(), npc, oldNpc.getDeadTime());
	}
	
	@Override
	public NpcInstance doSpawn(boolean spawn)
	{
		SpawnNpcInfo npcInfo = getNextNpcInfo();
		return doSpawn0(npcInfo.getTemplate(), spawn, npcInfo.getParameters());
	}
	
	@Override
	protected NpcInstance initNpc(NpcInstance mob, boolean spawn, MultiValueSet<String> set)
	{
		_reSpawned.remove(mob);
		SpawnRange range = _template.getSpawnRange(getNextRangeId());
		mob.setSpawnRange(range);
		return initNpc0(mob, range.getRandomLoc(getReflection().getGeoIndex()), spawn, set);
	}
	
	@Override
	public int getCurrentNpcId()
	{
		SpawnNpcInfo npcInfo = _template.getNpcId(_npcIndex);
		return npcInfo.getTemplate().npcId;
	}
	
	@Override
	public SpawnRange getCurrentSpawnRange()
	{
		return _template.getSpawnRange(_pointIndex);
	}
	
	@Override
	public void respawnNpc(NpcInstance oldNpc)
	{
		initNpc(oldNpc, true, StatsSet.EMPTY);
	}
	
	@Override
	public void deleteAll()
	{
		super.deleteAll();
		for (NpcInstance npc : _reSpawned)
		{
			npc.setSpawn(null);
			npc.deleteMe();
		}
		_reSpawned.clear();
	}
	
	private synchronized SpawnNpcInfo getNextNpcInfo()
	{
		int old = _npcIndex++;
		if (_npcIndex >= _template.getNpcSize())
		{
			_npcIndex = 0;
		}
		SpawnNpcInfo npcInfo = _template.getNpcId(old);
		if (npcInfo.getMax() > 0)
		{
			int count = 0;
			for (NpcInstance npc : _spawned)
			{
				if (npc.getNpcId() == npcInfo.getTemplate().getNpcId())
				{
					count++;
				}
			}
			if (count >= npcInfo.getMax())
			{
				return getNextNpcInfo();
			}
		}
		return npcInfo;
	}
	
	private synchronized int getNextRangeId()
	{
		int old = _pointIndex++;
		if (_pointIndex >= _template.getSpawnRangeSize())
		{
			_pointIndex = 0;
		}
		return old;
	}
	
	@Override
	public HardSpawner clone()
	{
		HardSpawner spawnDat = new HardSpawner(_template);
		spawnDat.setAmount(_maximumCount);
		spawnDat.setRespawnDelay(_respawnDelay, _respawnDelayRandom);
		spawnDat.setRespawnTime(0);
		return spawnDat;
	}
}
