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
 * @author blacksmoke, Krash
 */
public class Q10733_TheTestForSurvival extends Quest implements ScriptFile
{
	private static final int Gereth = 33932;
	private static final int Dia = 34005;
	private static final int Katalin = 33943;
	private static final int Ayanthe = 33942;
	
	private static final int Gereth_Recommendtion = 39519;
	
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
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				qs.showTutorialHTML(TutorialShowHtml.QT_027, TutorialShowHtml.TYPE_WINDOW);
				qs.giveItems(Gereth_Recommendtion, 1);
				htmltext = "33932-2.htm";
				break;
			
			case "quest_fighter_cont":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				htmltext = "34005-3.htm";
				break;
			
			case "quest_wizard_cont":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				htmltext = "34005-6.htm";
				break;
			
			case "qet_rev":
				htmltext = "33943-2.htm";
				qs.giveItems(57, 5000);
				qs.takeItems(Gereth_Recommendtion, 1);
				qs.getPlayer().addExpAndSp(295, 2);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		
		switch (npcId)
		{
			case Gereth:
				switch (cond)
				{
					case 0:
						if (isAvailableFor(qs.getPlayer()))
						{
							htmltext = "33932-1.htm";
						}
						break;
					
					case 1:
						htmltext = "33932-3.htm";
						break;
					
					default:
						htmltext = "noqu.htm";
						break;
				}
				break;
			
			case Dia:
				if (cond == 0)
				{
					htmltext = "34005-nc.htm";
				}
				else if ((cond == 1) && (qs.getQuestItemsCount(Gereth_Recommendtion) > 0))
				{
					if (qs.getPlayer().getClassId().getId() == 182) // Ertheia Fighter
					{
						htmltext = "34005-1.htm";
					}
					else if (qs.getPlayer().getClassId().getId() == 183) // Ertheia Wizard
					{
						htmltext = "34005-4.htm";
					}
				}
				break;
			
			case Katalin:
				if (cond == 0)
				{
					htmltext = "33943-4.htm";
				}
				else if ((cond == 2) && (qs.getQuestItemsCount(Gereth_Recommendtion) > 0))
				{
					htmltext = "33943-1.htm";
				}
				break;
			
			case Ayanthe:
				if (cond == 0)
				{
					htmltext = "33942-4.htm";
				}
				else if ((cond == 3) && (qs.getQuestItemsCount(Gereth_Recommendtion) > 0))
				{
					htmltext = "33942-1.htm";
				}
				break;
		}
		
		return htmltext;
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
