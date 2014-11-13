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

public class Q00376_ExplorationOfTheGiantsCavePart1 extends Quest implements ScriptFile
{
	private static final int DROP_RATE = 3;
	private static final int DROP_RATE_BOOK = 1;
	private static final int ANCIENT_PARCHMENT = 14841;
	private static final int DICT1 = 5891;
	private static final int DICT2 = 5892;
	private static final int MST_BK = 5890;
	private static final int[][] EXCHANGE =
	{
		{
			5937,
			5938,
			5939,
			5940,
			5941
		},
		{
			5346,
			5354
		},
		{
			5932,
			5933,
			5934,
			5935,
			5936
		},
		{
			5332,
			5334
		},
		{
			5922,
			5923,
			5924,
			5925,
			5926
		},
		{
			5416,
			5418
		},
		{
			5927,
			5928,
			5929,
			5930,
			5931
		},
		{
			5424,
			5340
		}
	};
	private static final int HR_SOBLING = 31147;
	private static final int WF_CLIFF = 30182;
	private static final int[] MOBS =
	{
		22670,
		22671,
		22672,
		22673,
		22674,
		22675,
		22676,
		22677,
	};
	
	public Q00376_ExplorationOfTheGiantsCavePart1()
	{
		super(true);
		addStartNpc(HR_SOBLING);
		addTalkId(WF_CLIFF);
		addKillId(MOBS);
		addQuestItem(DICT1, MST_BK);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "yes":
				htmltext = "Starting.htm";
				qs.setState(STARTED);
				qs.setCond(1);
				qs.giveItems(DICT1, 1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "no":
				htmltext = "ext_msg.htm";
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
				break;
			
			case "show":
				htmltext = "no_items.htm";
				for (int i = 0; i < EXCHANGE.length; i = i + 2)
				{
					long count = Long.MAX_VALUE;
					
					for (int j : EXCHANGE[i])
					{
						count = Math.min(count, qs.getQuestItemsCount(j));
					}
					
					if (count >= 1)
					{
						htmltext = "tnx4items.htm";
						
						for (int j : EXCHANGE[i])
						{
							qs.takeItems(j, count);
						}
						
						for (int l = 0; l < count; l++)
						{
							int item = EXCHANGE[i + 1][Rnd.get(EXCHANGE[i + 1].length)];
							qs.giveItems(item, 1);
						}
					}
				}
				break;
			
			case "myst":
				if (qs.getQuestItemsCount(MST_BK) > 0)
				{
					if (cond == 1)
					{
						qs.setState(STARTED);
						qs.setCond(2);
						htmltext = "go_part2.htm";
					}
					else if (cond == 2)
					{
						htmltext = "gogogo_2.htm";
					}
				}
				else
				{
					htmltext = "no_part2.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		if (qs.isCompleted())
		{
			return "completed";
		}
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		final int id = qs.getState();
		
		switch (npcId)
		{
			case HR_SOBLING:
				if (id == CREATED)
				{
					if (qs.getPlayer().getLevel() < 75)
					{
						qs.exitCurrentQuest(true);
						htmltext = "error_1.htm";
					}
					else
					{
						htmltext = "start.htm";
					}
				}
				else if (id == STARTED)
				{
					if (qs.getQuestItemsCount(ANCIENT_PARCHMENT) != 0)
					{
						htmltext = "checkout2.htm";
					}
					else
					{
						htmltext = "checkout.htm";
					}
				}
				break;
			
			case WF_CLIFF:
				if ((cond == 2) & (qs.getQuestItemsCount(MST_BK) > 0))
				{
					htmltext = "ok_part2.htm";
					qs.takeItems(MST_BK, -1);
					qs.giveItems(DICT2, 1);
					qs.setCond(3);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		if (cond > 0)
		{
			qs.rollAndGive(ANCIENT_PARCHMENT, 1, 1, DROP_RATE);
			
			if (cond == 1)
			{
				qs.rollAndGive(MST_BK, 1, 1, 1, DROP_RATE_BOOK);
			}
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
