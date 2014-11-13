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
package quests;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00493_KickingOutUnwelcomeGuests extends Quest implements ScriptFile
{
	// Npc
	public static final int JORJINO = 33515;
	// Others
	public static final String A_LIST = "a_list";
	public static final String B_LIST = "b_list";
	public static final String C_LIST = "c_list";
	public static final String D_LIST = "d_list";
	public static final String E_LIST = "e_list";
	
	public Q00493_KickingOutUnwelcomeGuests()
	{
		super(true);
		addStartNpc(JORJINO);
		addTalkId(JORJINO);
		addKillNpcWithLog(1, A_LIST, 20, 23147);
		addKillNpcWithLog(1, B_LIST, 20, 23148);
		addKillNpcWithLog(1, C_LIST, 20, 23149);
		addKillNpcWithLog(1, D_LIST, 20, 23150);
		addKillNpcWithLog(1, E_LIST, 20, 23151);
		addLevelCheck(95, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "33515-4.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "33515-6.htm":
				qs.unset("cond");
				qs.addExpAndSp(560000000, 16000000);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		switch (qs.getState())
		{
			case 1:
				if (qs.getPlayer().getLevel() < 95)
				{
					return "noquest";
				}
				if (!qs.isNowAvailable())
				{
					return "noquest";
				}
				return "33515.htm";
				
			case 2:
				if (qs.getCond() == 2)
				{
					return "33515-5.htm";
				}
				break;
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && updateKill(npc, qs))
		{
			qs.unset(A_LIST);
			qs.unset(B_LIST);
			qs.unset(C_LIST);
			qs.unset(D_LIST);
			qs.unset(E_LIST);
			qs.setCond(2);
		}
		
		return null;
	}
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
}