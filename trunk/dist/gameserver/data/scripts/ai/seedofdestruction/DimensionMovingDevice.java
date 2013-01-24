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
package ai.seedofdestruction;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

public class DimensionMovingDevice extends DefaultAI
{
	private static final int MOBS_WAVE_DELAY = 120 * 1000;
	private long spawnTime = 0;
	private static final int[] MOBS =
	{
		22538,
		22540,
		22547,
		22542,
		22548
	};
	private final List<NpcInstance> _npcs = new ArrayList<>();
	
	public DimensionMovingDevice(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		spawnTime = 0;
		_npcs.clear();
		super.onEvtDead(killer);
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if ((spawnTime + MOBS_WAVE_DELAY) < System.currentTimeMillis())
		{
			if (_npcs.size() < 100)
			{
				for (int id : MOBS)
				{
					NpcInstance mob = actor.getReflection().addSpawnWithoutRespawn(id, actor.getLoc(), 0);
					_npcs.add(mob);
				}
			}
			spawnTime = System.currentTimeMillis();
			return true;
		}
		return true;
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
	}
	
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
	}
}
