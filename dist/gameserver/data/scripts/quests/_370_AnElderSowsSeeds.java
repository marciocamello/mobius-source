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

public class _370_AnElderSowsSeeds extends Quest implements ScriptFile
{
	private static int CASIAN = 30612;
	private static int[] MOBS =
	{
		20082,
		20084,
		20086,
		20089,
		20090
	};
	private static int SPB_PAGE = 5916;
	private static int[] CHAPTERS =
	{
		5917,
		5918,
		5919,
		5920
	};
	
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
	
	public _370_AnElderSowsSeeds()
	{
		super(false);
		addStartNpc(CASIAN);
		for (int npcId : MOBS)
		{
			addKillId(npcId);
		}
		addQuestItem(SPB_PAGE);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("30612-1.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30612-6.htm"))
		{
			if ((st.getQuestItemsCount(CHAPTERS[0]) > 0) && (st.getQuestItemsCount(CHAPTERS[1]) > 0) && (st.getQuestItemsCount(CHAPTERS[2]) > 0) && (st.getQuestItemsCount(CHAPTERS[3]) > 0))
			{
				long mincount = st.getQuestItemsCount(CHAPTERS[0]);
				for (int itemId : CHAPTERS)
				{
					mincount = Math.min(mincount, st.getQuestItemsCount(itemId));
				}
				for (int itemId : CHAPTERS)
				{
					st.takeItems(itemId, mincount);
				}
				st.giveItems(ADENA_ID, 3600 * mincount);
				htmltext = "30612-8.htm";
			}
			else
			{
				htmltext = "30612-4.htm";
			}
		}
		else if (event.equalsIgnoreCase("30612-9.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		if (st.getState() == CREATED)
		{
			if (st.getPlayer().getLevel() < 28)
			{
				htmltext = "30612-0a.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "30612-0.htm";
			}
		}
		else if (cond == 1)
		{
			htmltext = "30612-4.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getState() != STARTED)
		{
			return null;
		}
		if (Rnd.chance(Math.min((int) (15 * st.getRateQuestsReward()), 100)))
		{
			st.giveItems(SPB_PAGE, 1);
			st.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
