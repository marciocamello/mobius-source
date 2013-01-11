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
package ai;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

public class Leandro extends DefaultAI
{
	static final Location[] points =
	{
		new Location(-82428, 245204, -3720),
		new Location(-82422, 245448, -3704),
		new Location(-82080, 245401, -3720),
		new Location(-82108, 244974, -3720),
		new Location(-83595, 244051, -3728),
		new Location(-83898, 242776, -3728),
		new Location(-85966, 241371, -3728),
		new Location(-86079, 240868, -3720),
		new Location(-86076, 240392, -3712),
		new Location(-86519, 240706, -3712),
		new Location(-86343, 241130, -3720),
		new Location(-86519, 240706, -3712),
		new Location(-86076, 240392, -3712),
		new Location(-86079, 240868, -3720),
		new Location(-85966, 241371, -3728),
		new Location(-83898, 242776, -3728),
		new Location(-83595, 244051, -3728),
		new Location(-82108, 244974, -3720)
	};
	private int current_point = -1;
	private long wait_timeout = 0;
	private boolean wait = false;
	
	public Leandro(NpcInstance actor)
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
		NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return true;
		}
		if (_def_think)
		{
			doTask();
			return true;
		}
		if ((System.currentTimeMillis() > wait_timeout) && ((current_point > -1) || Rnd.chance(5)))
		{
			if (!wait)
			{
				switch (current_point)
				{
					case 5:
						wait_timeout = System.currentTimeMillis() + 30000;
						Functions.npcSay(actor, "Where has he gone?");
						wait = true;
						return true;
					case 10:
						wait_timeout = System.currentTimeMillis() + 60000;
						Functions.npcSay(actor, "Have you seen Windawood?");
						wait = true;
						return true;
				}
			}
			wait_timeout = 0;
			wait = false;
			current_point++;
			if (current_point >= points.length)
			{
				current_point = 0;
			}
			addTaskMove(points[current_point], true);
			doTask();
			return true;
		}
		if (randomAnimation())
		{
			return true;
		}
		return false;
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
