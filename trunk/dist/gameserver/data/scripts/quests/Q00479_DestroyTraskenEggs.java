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

public class Q00479_DestroyTraskenEggs extends Quest implements ScriptFile
{
	// npc
	public static final int DAICHIR = 30537;
	public static final String A_LIST = "a_list";
	
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
	
	public Q00479_DestroyTraskenEggs()
	{
		super(true);
		addStartNpc(DAICHIR);
		addTalkId(DAICHIR);
		addKillNpcWithLog(1, A_LIST, 5, 19080);
		addLevelCheck(95, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
		
		if (event.equalsIgnoreCase("30537-9.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		
		if (event.equalsIgnoreCase("30537-16.htm"))
		{
			st.unset("cond");
			st.giveItems(57, 993824);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(this);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		int npcId = npc.getId();
		int state = st.getState();
		int cond = st.getCond();
		
		if (npcId == DAICHIR)
		{
			if (state == 1)
			{
				if (player.getLevel() < 95)
				{
					return "30537-lvl.htm";
				}
				
				if (!st.isNowAvailableByTime())
				{
					return "30537-comp.htm";
				}
				
				return "30537.htm";
			}
			
			if (state == 2)
			{
				if (cond == 1)
				{
					return "30537-12.htm";
				}
				
				if (cond == 2)
				{
					return "30537-14.htm";
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
			st.setCond(2);
		}
		
		return null;
	}
}