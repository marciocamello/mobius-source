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

public class _640_TheZeroHour extends Quest implements ScriptFile
{
	// npc
	public static final int KAHMAN = 31554;
	
	public static final int QUEEN_FANG = 14859;
	
	public static final int QUEEN = 25671; // queen sheed
	
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
	
	public _640_TheZeroHour()
	{
		super(true);
		addStartNpc(KAHMAN);
		
		addQuestCompletedCheck(_109_InSearchOfTheNest.class);
		addQuestItem(QUEEN_FANG);
		addKillId(QUEEN);
		addLevelCheck(81, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("31554-4.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		if (event.equalsIgnoreCase("32892-10.htm"))
		{
			if (st.getQuestItemsCount(QUEEN_FANG) <= 0)
			{
				return "32892-8.htm";
			}
			st.giveItems(14849, 1); // treasure chest
			st.addExpAndSp(2850000, 3315000);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
			return "32892-7.htm";
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
		if (state == COMPLETED)
		{
			return "31554-comp.htm";
		}
		if (player.getLevel() < 81)
		{
			return "31554-lvl.htm";
		}
		if (npcId == KAHMAN)
		{
			if (cond == 0)
			{
				return "31554.htm";
			}
			if (cond == 1)
			{
				return "31554-6.htm";
			}
		}
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		if ((npcId == QUEEN) && (st.getQuestItemsCount(QUEEN_FANG) == 0))
		{
			st.giveItems(QUEEN_FANG, 1);
		}
		return null;
	}
}