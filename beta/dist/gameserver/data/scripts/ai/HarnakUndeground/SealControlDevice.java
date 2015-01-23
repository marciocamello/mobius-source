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
package ai.HarnakUndeground;

import instances.HarnakUndergroundRuins;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.scripts.Functions;

public final class SealControlDevice extends DefaultAI
{
	private final boolean SHOUT;
	
	public SealControlDevice(NpcInstance actor)
	{
		super(actor);
		SHOUT = getActor().getParameter("shout", false);
	}
	
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		
		if (SHOUT)
		{
			addTimer(1, 1);
		}
	}
	
	@Override
	protected void onEvtMenuSelected(Player player, int ask, int reply)
	{
		if ((ask == 10338) && (reply == 1))
		{
			if (getActor().getNpcState() != 2)
			{
				getActor().setNpcState(2);
			}
			
			broadCastScriptEvent("SEAL_ACTIVATED", 3000);
		}
	}
	
	@Override
	protected void onEvtScriptEvent(String event, Object arg1, Object arg2)
	{
		if (event.equals("FAIL_INSTANCE"))
		{
			getActor().deleteMe();
		}
	}
	
	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		super.onEvtTimer(timerId, arg1, arg2);
		
		if (!isActive())
		{
			return;
		}
		
		Reflection r = getActor().getReflection();
		
		if (!(r instanceof HarnakUndergroundRuins))
		{
			return;
		}
		
		switch (timerId)
		{
			case 1:
				Functions.npcSayInRange(getActor(), 1500, NpcStringId.DISABLE_DEVICE_WILL_GO_OUT_OF_CONTROL_IN_1_MINUTE);
				addTimer(2, 10000);
				break;
			
			case 2:
				Functions.npcSayInRange(getActor(), 1500, NpcStringId.SECONDS_ARE_REMAINING41);
				addTimer(3, 10000);
				break;
			
			case 3:
				Functions.npcSayInRange(getActor(), 1500, NpcStringId.SECONDS_ARE_REMAINING42);
				addTimer(4, 10000);
				break;
			
			case 4:
				Functions.npcSayInRange(getActor(), 1500, NpcStringId.SECONDS_ARE_REMAINING43);
				addTimer(5, 10000);
				break;
			
			case 5:
				Functions.npcSayInRange(getActor(), 1500, NpcStringId.SECONDS_ARE_REMAINING44);
				addTimer(6, 10000);
				break;
			
			case 6:
				Functions.npcSayInRange(getActor(), 1500, NpcStringId.SECONDS_ARE_REMAINING45);
				addTimer(7, 5000);
				break;
			
			case 7:
				Functions.npcSayInRange(getActor(), 1500, NpcStringId.SECONDS);
				addTimer(8, 1000);
				break;
			
			case 8:
				Functions.npcSayInRange(getActor(), 1500, NpcStringId.SECONDS2);
				addTimer(9, 1000);
				break;
			
			case 9:
				Functions.npcSayInRange(getActor(), 1500, NpcStringId.SECONDS3);
				addTimer(10, 1000);
				break;
			
			case 10:
				Functions.npcSayInRange(getActor(), 1500, NpcStringId.SECONDS4);
				addTimer(11, 1000);
				break;
			
			case 11:
				Functions.npcSayInRange(getActor(), 1500, NpcStringId.SECOND);
				break;
		}
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
