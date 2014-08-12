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

public class Q00476_PlainMission extends Quest implements ScriptFile
{
	// npc
	public static final int GUIDE = 33463;
	public static final int ANDREI = 31292;
	public static final String A_LIST = "a_list";
	public static final String B_LIST = "b_list";
	public static final String C_LIST = "c_list";
	public static final String D_LIST = "d_list";
	
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
	
	public Q00476_PlainMission()
	{
		super(true);
		addStartNpc(GUIDE);
		addTalkId(ANDREI);
		addKillNpcWithLog(1, A_LIST, 12, 21278, 21279, 21280);
		addKillNpcWithLog(1, B_LIST, 12, 21282, 21283, 21284);
		addKillNpcWithLog(1, C_LIST, 12, 21286, 21287, 21288);
		addKillNpcWithLog(1, D_LIST, 12, 21290, 21291, 21292);
		addLevelCheck(65, 69);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
		
		if (event.equalsIgnoreCase("33463-3.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		st.getPlayer();
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		
		if (npcId == GUIDE)
		{
			if (state == 1)
			{
				if (!st.isNowAvailableByTime())
				{
					return "33463-comp.htm";
				}
				
				return "33463.htm";
			}
			
			if (state == 2)
			{
				if (cond == 1)
				{
					return "33463-4.htm";
				}
				
				if (cond == 2)
				{
					return "33463-5.htm";
				}
			}
		}
		
		if ((npcId == ANDREI) && (state == 2))
		{
			if (cond == 1)
			{
				return "31292-1.htm";
			}
			
			if (cond == 2)
			{
				st.giveItems(57, 142200);
				st.addExpAndSp(4685175, 3376245);
				st.unset("cond");
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(this);
				return "31292.htm"; // no further html do here
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		
		if ((cond != 1) || (npc == null))
		{
			return null;
		}
		
		boolean doneKill = updateKill(npc, st);
		
		if (doneKill)
		{
			st.unset(A_LIST);
			st.unset(B_LIST);
			st.unset(C_LIST);
			st.unset(D_LIST);
			st.setCond(2);
		}
		
		return null;
	}
}