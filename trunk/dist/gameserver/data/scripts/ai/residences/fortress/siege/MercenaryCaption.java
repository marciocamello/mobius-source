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
package ai.residences.fortress.siege;

import java.util.Collections;
import java.util.List;

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.events.impl.FortressSiegeEvent;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import npc.model.residences.fortress.siege.MercenaryCaptionInstance;

public class MercenaryCaption extends Fighter
{
	private List<Location> _points = Collections.emptyList();
	private int _tick = -1;
	
	public MercenaryCaption(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = 100;
	}
	
	@Override
	public void onEvtSpawn()
	{
		super.onEvtSpawn();
		NpcInstance actor = getActor();
		Fortress f = actor.getFortress();
		FortressSiegeEvent event = f.getSiegeEvent();
		_points = event.getObjects(FortressSiegeEvent.MERCENARY_POINTS);
	}
	
	@Override
	protected boolean thinkActive()
	{
		NpcInstance actor = getActor();
		if (actor.isActionsDisabled())
		{
			return true;
		}
		if (_def_think)
		{
			if (doTask())
			{
				clearTasks();
			}
			return true;
		}
		if (randomWalk())
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void onEvtArrived()
	{
		if (_tick != -1)
		{
			startMove(false);
		}
	}
	
	@Override
	public void onEvtAttacked(Creature attacker, int damage)
	{
		_tick = -1;
		super.onEvtAttacked(attacker, damage);
	}
	
	public void startMove(boolean init)
	{
		if (init)
		{
			_tick = 0;
		}
		if (_tick == -1)
		{
			return;
		}
		if (_tick < _points.size())
		{
			addTaskMove(_points.get(_tick++), true);
			doTask();
		}
	}
	
	@Override
	public MercenaryCaptionInstance getActor()
	{
		return (MercenaryCaptionInstance) super.getActor();
	}
}
