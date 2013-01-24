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
package ai.dragonvalley;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.ArrayUtils;

public class Patrollers extends Fighter
{
	protected Location[] _points;
	private final int[] _teleporters =
	{
		22857,
		22833,
		22834
	};
	private int _lastPoint = 0;
	private boolean _firstThought = true;
	
	public Patrollers(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = Integer.MAX_VALUE - 10;
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	@Override
	public boolean checkAggression(Creature target)
	{
		NpcInstance actor = getActor();
		if (target.isPlayable() && !target.isDead() && !target.isInvisible())
		{
			actor.getAggroList().addDamageHate(target, 0, 1);
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		}
		return true;
	}
	
	@Override
	protected boolean thinkActive()
	{
		if (super.thinkActive())
		{
			return true;
		}
		if (!getActor().isMoving)
		{
			startMoveTask();
		}
		return true;
	}
	
	@Override
	protected void onEvtArrived()
	{
		startMoveTask();
		super.onEvtArrived();
	}
	
	private void startMoveTask()
	{
		NpcInstance npc = getActor();
		if (_firstThought)
		{
			_lastPoint = getIndex(Location.findNearest(npc, _points));
			_firstThought = false;
		}
		else
		{
			_lastPoint++;
		}
		if (_lastPoint >= _points.length)
		{
			_lastPoint = 0;
			if (ArrayUtils.contains(_teleporters, npc.getNpcId()))
			{
				npc.teleToLocation(_points[_lastPoint]);
			}
		}
		npc.setRunning();
		if (Rnd.chance(30))
		{
			npc.altOnMagicUseTimer(npc, SkillTable.getInstance().getInfo(6757, 1));
		}
		try
		{
			addTaskMove(Location.findPointToStay(_points[_lastPoint], 250, npc.getGeoIndex()), true);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
		}
		doTask();
	}
	
	private int getIndex(Location loc)
	{
		for (int i = 0; i < _points.length; i++)
		{
			if (_points[i] == loc)
			{
				return i;
			}
		}
		return 0;
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
	
	@Override
	protected boolean maybeMoveToHome()
	{
		return false;
	}
	
	@Override
	protected void teleportHome()
	{
	}
	
	@Override
	protected void returnHome(boolean clearAggro, boolean teleport)
	{
		super.returnHome(clearAggro, teleport);
		clearTasks();
		_firstThought = true;
		startMoveTask();
	}
}
