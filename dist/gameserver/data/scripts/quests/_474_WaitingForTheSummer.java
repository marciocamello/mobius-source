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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

import org.apache.commons.lang3.ArrayUtils;

public class _474_WaitingForTheSummer extends Quest implements ScriptFile
{
	private static final int CON1 = 33463;
	private static final int CON2 = 31981;
	private static final int CON3 = 19490;
	private static final int CON4 = 19491;
	private static final int CON5 = 19492;
	private static final int[] CON6 =
	{
		22093,
		22094
	};
	private static final int[] CON7 =
	{
		22095,
		22096
	};
	private static final int[] CON8 =
	{
		22097,
		22098
	};
	
	public _474_WaitingForTheSummer()
	{
		super(false);
		addStartNpc(CON1);
		addTalkId(CON1, CON2);
		addKillId(CON6);
		addKillId(CON7);
		addKillId(CON8);
		addQuestItem(CON3, CON5, CON4);
		addLevelCheck(60, 64);
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
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		String str = event;
		int i = -1;
		switch (str.hashCode())
		{
			case 1786654643:
				if (!str.equals("33463-04.htm"))
				{
					break;
				}
				i = 0;
		}
		switch (i)
		{
			case 0:
				st.setState(STARTED);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == CON1)
		{
			if (st.getState() == CREATED)
			{
				htmltext = "33463-01.htm";
			}
			else if (st.getState() == STARTED)
			{
				if (cond == 1)
				{
					htmltext = "33463-05.htm";
				}
				else
				{
					htmltext = "33463-06.htm";
				}
			}
			else if (st.getState() == COMPLETED)
			{
				htmltext = "Quest is complited.";
			}
		}
		else if (npc.getNpcId() == CON2)
		{
			if (st.isStarted())
			{
				if (cond == 1)
				{
					htmltext = "31981-02.htm";
				}
				else if (cond == 2)
				{
					htmltext = "31981-01.htm";
					st.addExpAndSp(1879400, 1782000);
					st.giveItems(57, 194000);
					st.exitCurrentQuest(false);
				}
			}
			else if (st.isCompleted())
			{
				htmltext = "31981-03.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if ((cond == 1) && (Rnd.chance(50)))
		{
			if (ArrayUtils.contains(CON8, npcId))
			{
				if (st.getQuestItemsCount(CON5) < 30)
				{
					st.giveItems(CON5, 1);
					st.playSound("ItemSound.quest_itemget");
				}
			}
			else if (ArrayUtils.contains(CON7, npcId))
			{
				if (st.getQuestItemsCount(CON4) < 30)
				{
					st.giveItems(CON4, 1);
					st.playSound("ItemSound.quest_itemget");
				}
			}
			else if (ArrayUtils.contains(CON6, npcId))
			{
				if (st.getQuestItemsCount(CON3) < 30)
				{
					st.giveItems(CON3, 1);
					st.playSound("ItemSound.quest_itemget");
				}
			}
			if ((st.getQuestItemsCount(CON5) >= 30) && (st.getQuestItemsCount(CON4) >= 30) && (st.getQuestItemsCount(CON3) >= 30))
			{
				st.setCond(2);
				st.playSound("ItemSound.quest_middle");
			}
		}
		return null;
	}
	
	public boolean isDailyQuest()
	{
		return true;
	}
}
