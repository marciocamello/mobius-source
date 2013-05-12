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
package ai.DwarvenVillageAttack;

import java.util.List;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

public class Torocco extends Dwarvs
{
	private static final int ROGIN_ID = 19193;
	
	public Torocco(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
		super.onEvtScriptEvent(event, arg1, arg2);
		
		if (event.equalsIgnoreCase("TOROCCO_1"))
		{
			addTimer(1, 1200);
		}
	}
	
	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		super.onEvtTimer(timerId, arg1, arg2);
		
		Reflection r = getActor().getReflection();
		if (r == ReflectionManager.DEFAULT)
		{
			return;
		}
		
		switch (timerId)
		{
			case 1:
				Functions.npcSayInRange(getActor(), 1500, NpcString.ROGIN_IM_HERE);
				List<NpcInstance> list = r.getAllByNpcId(ROGIN_ID, true);
				if (list.size() > 0)
				{
					NpcInstance rogin = list.get(0);
					rogin.getAI().notifyEvent(CtrlEvent.EVT_SCRIPT_EVENT, "ROGIN_1");
				}
				break;
		}
	}
}
