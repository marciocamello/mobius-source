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
package ai.GuillotineFortress;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/*
 * User: Mangol
 * Date: 17.12.12
 * Time: 19:39
 * Location: Guillotine Fortress
 */
public class GuillotineNpcPriest extends DefaultAI
{
	public GuillotineNpcPriest(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
		super.onEvtScriptEvent(event, arg1, arg2);
		
		if (event.equalsIgnoreCase("SHOUT_PRIEST_1"))
		{
			Functions.npcSayInRange(getActor(), 1000, NpcString.IT_LEFT_NOTHING_BEHIND);
		}
		else if (event.equalsIgnoreCase("SHOUT_PRIEST_2"))
		{
			Functions.npcSayInRange(getActor(), 1000, NpcString.IM_IN_A_PICKLE_WE_CANT_GO_BACK_LETS_LOOK_FURTHER);
		}
		else if (event.equalsIgnoreCase("SHOUT_PRIEST_3"))
		{
			Functions.npcSayInRange(getActor(), 1000, NpcString.WELL_BEGIN_INTERNAL_PURIFICATION_PROCESS);
		}
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
