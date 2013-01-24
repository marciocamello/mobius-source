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
package ai.talkingisland;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

public class RubentisSubAI extends DefaultAI
{
	protected Location[] _points;
	private int _lastPoint = 0;
	
	public RubentisSubAI(NpcInstance actor)
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
		if (Rnd.chance(59))
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
				ns = NpcString.HUNTING_AT_THE_BEACH_IS_A_BAD_IDEA;
				break;
			case 2:
				ns = NpcString.ONLY_THE_STRONG_SURVIVE_AT_YE_SAGIRA_RUINS;
				break;
			case 3:
				ns = NpcString.HUNTING_AT_THE_BEACH_IS_A_BAD_IDEA;
				break;
			case 4:
				ns = NpcString.ONLY_THE_STRONG_SURVIVE_AT_YE_SAGIRA_RUINS;
				break;
			case 5:
				ns = NpcString.HUNTING_AT_THE_BEACH_IS_A_BAD_IDEA;
				break;
			default:
				ns = NpcString.ONLY_THE_STRONG_SURVIVE_AT_YE_SAGIRA_RUINS;
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
