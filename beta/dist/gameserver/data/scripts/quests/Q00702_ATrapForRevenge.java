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

public class Q00702_ATrapForRevenge extends Quest implements ScriptFile
{
	// Npcs
	private static final int PLENOS = 32563;
	private static final int TENIUS = 32555;
	// Items
	private static final int DRAKES_FLESH = 13877;
	private static final int LEONARD = 9628;
	private static final int ADAMANTINE = 9629;
	private static final int ORICHALCUM = 9630;
	// Monsters
	private static final int DRAK = 22612;
	private static final int MUTATED_DRAKE_WING = 22611;
	
	public Q00702_ATrapForRevenge()
	{
		super(true);
		addStartNpc(PLENOS);
		addTalkId(PLENOS, TENIUS);
		addKillId(DRAK, MUTATED_DRAKE_WING);
		addQuestItem(DRAKES_FLESH);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "take":
				if (cond == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
					htmltext = "plenos_q702_2.htm";
				}
				break;
			
			case "took_mission":
				if (cond == 1)
				{
					qs.setCond(2);
					htmltext = "tenius_q702_3.htm";
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "hand_over":
				if (cond == 2)
				{
					htmltext = "tenius_q702_6.htm";
					qs.takeItems(DRAKES_FLESH, -1);
					
					switch (Rnd.get(1, 3))
					{
						case 1:
							qs.giveItems(LEONARD, 3);
							break;
						
						case 2:
							qs.giveItems(ADAMANTINE, 3);
							break;
						
						case 3:
							qs.giveItems(ORICHALCUM, 3);
							break;
					}
					
					qs.giveItems(ADENA_ID, 157200);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case PLENOS:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() >= 78)
					{
						final QuestState GoodDayToFly = qs.getPlayer().getQuestState(Q10273_GoodDayToFly.class);
						
						if ((GoodDayToFly != null) && GoodDayToFly.isCompleted())
						{
							htmltext = "plenos_q702_1.htm";
						}
						else
						{
							htmltext = "plenos_q702_1a.htm";
						}
					}
					else
					{
						htmltext = "plenos_q702_1b.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "plenos_q702_1c.htm";
				}
				break;
			
			case TENIUS:
				if (cond == 1)
				{
					htmltext = "tenius_q702_1.htm";
				}
				else if ((cond == 2) && (qs.getQuestItemsCount(DRAKES_FLESH) < 100))
				{
					htmltext = "tenius_q702_4.htm";
				}
				else if ((cond == 2) && (qs.getQuestItemsCount(DRAKES_FLESH) >= 100))
				{
					htmltext = "tenius_q702_5.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		
		if ((qs.getCond() == 2) && ((npcId == DRAK) || (npcId == MUTATED_DRAKE_WING)) && (qs.getQuestItemsCount(DRAKES_FLESH) <= 100))
		{
			qs.giveItems(DRAKES_FLESH, 1);
			qs.playSound(SOUND_ITEMGET);
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
