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
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author blacksmoke
 */
public class Q10733_TheTestForSurvival extends Quest implements ScriptFile
{
	private static final int Gereth = 33932;
	private static final int Dia = 34005;
	private static final int Katalin = 33943;
	private static final int Ayanthe = 33942;
	
	private static final int Gereth_Recommendtion = 39519;
	
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
	
	public Q10733_TheTestForSurvival()
	{
		super(false);
		addStartNpc(Gereth);
		addTalkId(Gereth, Dia, Katalin, Ayanthe);
		addQuestItem(Gereth_Recommendtion);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(Q10732_AForeignLand.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equalsIgnoreCase("quest_ac"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			st.showTutorialHTML(TutorialShowHtml.QT_027, TutorialShowHtml.TYPE_WINDOW);
			st.giveItems(Gereth_Recommendtion, 1);
			htmltext = "33932-2.htm";
		}
		if (event.equalsIgnoreCase("quest_cont"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
			htmltext = "34005-3.htm";
		}
		if (event.equalsIgnoreCase("qet_rev"))
		{
			htmltext = "33943-2.htm";
			st.giveItems(57, 5000);
			st.takeItems(Gereth_Recommendtion, 1);
			st.getPlayer().addExpAndSp(295, 2);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getId();
		String htmltext = "noquest";
		
		if (npcId == Gereth)
		{
			if (st.isCompleted())
			{
				htmltext = "quest_completed.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "33932-1.htm";
			}
			else if (cond == 1)
			{
				htmltext = "33932-3.htm";
			}
			else
			{
				htmltext = "noqu.htm";
			}
		}
		else if (npcId == Dia)
		{
			if (st.isCompleted())
			{
				htmltext = "quest_completed.htm";
			}
			else if (cond == 0)
			{
				htmltext = "34005-nc.htm";
			}
			else if ((cond == 1) && (st.getQuestItemsCount(Gereth_Recommendtion) > 0))
			{
				// TODO check if is different html for Ertheia Fighter / Wizard
				if (st.getPlayer().getClassId().getId() == 182) // Ertheia Fighter
				{
					htmltext = "34005-1.htm";
				}
				else if (st.getPlayer().getClassId().getId() == 183) // Ertheia Wizard
				{
					htmltext = "34005-4.htm";
				}
			}
		}
		
		else if (npcId == Katalin)
		{
			if (st.isCompleted())
			{
				htmltext = "quest_completed.htm";
			}
			else if (cond == 0)
			{
				htmltext = "33943-4.htm";
			}
			else if ((cond == 2) && (st.getQuestItemsCount(Gereth_Recommendtion) > 0))
			{
				htmltext = "33943-1.htm";
			}
		}
		else if (npcId == Ayanthe)
		{
			// TODO need html files for Ertheia wizard
		}
		return htmltext;
	}
}
