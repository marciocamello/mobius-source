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
package ai.talkingisland;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

public class MeiSubAI extends DefaultAI
{
	protected Location[] _points;
	private int _lastPoint = 0;
	
	public MeiSubAI(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	@Override
	protected boolean thinkActive()
	{
		if (!_def_think)
		{
			startMoveTask();
		}
		return true;
	}
	
	@Override
	protected void onEvtArrived()
	{
		startMoveTask();
		if (Rnd.chance(52))
		{
			sayRndMsg();
		}
		super.onEvtArrived();
	}
	
	private void startMoveTask()
	{
		_lastPoint++;
		if (_lastPoint >= _points.length)
		{
			_lastPoint = 0;
		}
		addTaskMove(_points[_lastPoint], false);
		doTask();
	}
	
	private void sayRndMsg()
	{
		NpcInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		NpcString ns;
		switch (Rnd.get(6))
		{
			case 1:
				ns = NpcString.IF_YOU_IGNORE_THE_TRAINING_GROUNDS;
				break;
			case 2:
				ns = NpcString.IF_YOU_IGNORE_THE_TRAINING_GROUNDS;
				break;
			case 3:
				ns = NpcString.IF_YOU_IGNORE_THE_TRAINING_GROUNDS;
				break;
			case 4:
				ns = NpcString.IF_YOU_IGNORE_THE_TRAINING_GROUNDS;
				break;
			case 5:
				ns = NpcString.IF_YOU_IGNORE_THE_TRAINING_GROUNDS;
				break;
			default:
				ns = NpcString.IF_YOU_IGNORE_THE_TRAINING_GROUNDS;
				break;
		}
		Functions.npcSay(actor, ns);
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
