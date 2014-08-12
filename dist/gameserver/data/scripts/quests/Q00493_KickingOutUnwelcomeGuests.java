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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00493_KickingOutUnwelcomeGuests extends Quest implements ScriptFile
{
	// npc
	public static final int JORJINO = 33515;
	public static final String A_LIST = "a_list";
	public static final String B_LIST = "b_list";
	public static final String C_LIST = "c_list";
	public static final String D_LIST = "d_list";
	public static final String E_LIST = "e_list";
	
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
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("33515-4.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		
		if (event.equalsIgnoreCase("33515-6.htm"))
		{
			st.unset("cond");
			st.addExpAndSp(560000000, 16000000);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(this);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		
		if (npcId == JORJINO)
		{
			if (state == 1)
			{
				if (player.getLevel() < 95)
				{
					return "noquest";
				}
				
				if (!st.isNowAvailable())
				{
					return "noquest";
				}
				
				return "33515.htm";
			}
			
			if (state == 2)
			{
				if (cond == 2)
				{
					return "33515-5.htm";
				}
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		
		if (cond != 1)
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
			st.unset(E_LIST);
			st.setCond(2);
		}
		
		return null;
	}
}