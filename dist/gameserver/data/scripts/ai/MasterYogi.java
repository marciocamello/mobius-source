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

import java.util.List;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

public class MasterYogi extends DefaultAI
{
	private long wait_timeout1 = 0;
	private long wait_timeout2 = 0;
	private int range = 0;
	
	public MasterYogi(NpcInstance actor)
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
		if (range <= 0)
		{
			List<NpcInstance> around = actor.getAroundNpc(6000, 300);
			if ((around != null) && !around.isEmpty())
			{
				double distance;
				for (NpcInstance npc : around)
				{
					if (npc.getNpcId() == 32599)
					{
						distance = actor.getDistance(npc) * 0.50;
						if (((range > 0) && (distance < range)) || (range == 0))
						{
							range = (int) distance;
						}
					}
				}
			}
			else
			{
				range = 3000;
			}
		}
		if (System.currentTimeMillis() > wait_timeout1)
		{
			wait_timeout1 = System.currentTimeMillis() + 180000;
			Functions.npcSayInRangeCustomMessage(actor, range, "scripts.ai.MasterYogi.Hey");
			return true;
		}
		if (System.currentTimeMillis() > wait_timeout2)
		{
			wait_timeout2 = System.currentTimeMillis() + 300000;
			Functions.npcSayInRangeCustomMessage(actor, range, "scripts.ai.MasterYogi.Hohoho");
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
