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
package events.TheFlowOfTheHorror;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

public class MonstersAI extends Fighter
{
	private List<Location> _points = new ArrayList<>();
	private int current_point = -1;
	
	public void setPoints(List<Location> points)
	{
		_points = points;
	}
	
	public MonstersAI(NpcInstance actor)
	{
		super(actor);
		AI_TASK_ATTACK_DELAY = 500;
		MAX_PURSUE_RANGE = 30000;
	}
	
	@Override
	public int getMaxAttackTimeout()
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if ((actor == null) || actor.isDead())
		{
			return true;
		}
		if (_def_think)
		{
			doTask();
			return true;
		}
		if ((current_point > -1) || Rnd.chance(5))
		{
			if (current_point >= (_points.size() - 1))
			{
				Creature target = GameObjectsStorage.getByNpcId(30754);
				if ((target != null) && !target.isDead())
				{
					clearTasks();
					setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
					return true;
				}
				return true;
			}
			current_point++;
			actor.setRunning();
			clearTasks();
			addTaskMove(_points.get(current_point), true);
			doTask();
			return true;
		}
		if (randomAnimation())
		{
			return true;
		}
		return false;
	}
}
