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
package ai.Octavis;

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius and Nache
 */

public final class OctavisFollower extends DefaultAI
{
	private static final int OCTAVIS_LIGHT_FIRST = 29191;
	private static final int OCTAVIS_LIGHT_BEAST = 29192;
	private static final int OCTAVIS_HARD_FIRST = 29209;
	private static final int OCTAVIS_HARD_BEAST = 29210;
	
	private static final Skill OCTAVIS_RAIN_OF_ARROWS = SkillTable.getInstance().getInfo(14285, 1);
	
	private static final int DRIFT_DISTANCE = 10;
	
	private long _wait_timeout = 0;
	private HardReference<? extends Creature> _octavisRef = HardReferences.emptyRef();
	
	public OctavisFollower(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	@Override
	protected boolean randomAnimation()
	{
		return false;
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		Creature octavis = _octavisRef.get();
		if (octavis == null)
		{
			if (System.currentTimeMillis() > _wait_timeout)
			{
				_wait_timeout = System.currentTimeMillis() + 1000;
				for (NpcInstance npc : World.getAroundNpc(actor))
				{
					if ((npc.getId() == OCTAVIS_LIGHT_BEAST) || (npc.getId() == OCTAVIS_HARD_BEAST))
					{
						_octavisRef = npc.getRef();
						return true;
					}
				}
			}
		}
		else if (!actor.isMoving)
		{
			int x = (octavis.getX() + Rnd.get(2 * DRIFT_DISTANCE)) - DRIFT_DISTANCE;
			int y = (octavis.getY() + Rnd.get(2 * DRIFT_DISTANCE)) - DRIFT_DISTANCE;
			int z = octavis.getZ();
			actor.setRunning();
			actor.moveToLocation(x, y, z, 0, true);
			startSkillAttackTask();
			return true;
		}
		return false;
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		NpcInstance npc = getActor();
		if ((npc.getId() == OCTAVIS_LIGHT_FIRST) || (npc.getId() == OCTAVIS_HARD_FIRST))
		{
			double hp = npc.getCurrentHp() / npc.getMaxHp();
			if ((hp <= 0.5D) && (npc.getNpcState() == 0))
			{
				int effect = (int) (hp * 10.0D);
				if ((effect < 5) && (effect > 0))
				{
					npc.setNpcState(effect);
				}
				else if (effect == 0)
				{
					npc.setNpcState(5);
				}
			}
			else if ((npc.getCurrentHp() / npc.getMaxHp()) <= 0.01D)
			{
				npc.setNpcState(6);
				npc.setNpcState(0);
			}
			else if (((npc.getCurrentHp() / npc.getMaxHp()) > 0.5D) && (npc.getNpcState() == 1))
			{
				npc.setNpcState(6);
				npc.setNpcState(0);
			}
		}
		doTask();
	}
	
	private void startSkillAttackTask()
	{
		NpcInstance npc = getActor();
		
		Skill skill = OCTAVIS_RAIN_OF_ARROWS; // TODO[K] - Skills lighting Octavis
		for (Creature creature : npc.getAroundCharacters(10000, 100))
		{
			if (Rnd.chance(1))
			{
				npc.altOnMagicUseTimer(creature, skill);
			}
		}
		doTask();
	}
	
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
	}
}