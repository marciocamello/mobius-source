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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;

/**
 * User: Mangol Date: 17.12.12 Time: 19:08 Location: Guillotine Fortress
 */
public class GuillotineNpcInvestigator extends GuillotineNpcPriest
{
	
	public GuillotineNpcInvestigator(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunDialog(), 1000L, 600000L);
	}
	
	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		super.onEvtTimer(timerId, arg1, arg2);
		if (!isActive())
		{
			return;
		}
		switch (timerId)
		{
			case 1:
				Functions.npcSayInRange(getActor(), 1000, NpcString.NOTHING_COMES_OUT_NEITHER_FROM_INSIDE_OR_OUSIDE);
				addTimer(2, 3000);
				break;
			case 2:
				Functions.npcSayInRange(getActor(), 1000, NpcString.AS_IT_DIDNT_EXIST);
				addTimer(3, 3000);
				break;
			case 3:
				broadCastScriptEvent("SHOUT_PRIEST_1", 1000);
				addTimer(4, 3000);
				break;
			case 4:
				Functions.npcSayInRange(getActor(), 1000, NpcString.SHOULD_VE_REPORT_IT_TO_THE_KINGDOM);
				addTimer(5, 3000);
				break;
			case 5:
				broadCastScriptEvent("SHOUT_PRIEST_2", 1000);
				addTimer(6, 3000);
				break;
			case 6:
				broadCastScriptEvent("SHOUT_PRIEST_3", 1000);
				addTimer(7, 3000);
				break;
			case 7:
				Functions.npcSayInRange(getActor(), 1000, NpcString.PLEASE_33381);
				addTimer(8, 3000);
				break;
		}
	}
	
	private class RunDialog extends RunnableImpl
	{
		/**
		 * 
		 */
		public RunDialog()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void runImpl()
		{
			addTimer(1, 2000);
		}
	}
	
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
