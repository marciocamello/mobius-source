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

public class Q00377_ExplorationOfTheGiantsCavePart2 extends Quest implements ScriptFile
{
	private static final int DROP_RATE = 20;
	private static final int ANC_BOOK = 14847;
	private static final int DICT2 = 5892;
	private static final int[][] EXCHANGE =
	{
		{
			5945,
			5946,
			5947,
			5948,
			5949
		},
		{
			5950,
			5951,
			5952,
			5953,
			5954
		}
	};
	private static final int HR_SOBLING = 31147;
	private static final int[] MOBS =
	{
		22661,
		22662,
		22663,
		22664,
		22665,
		22666,
		22667,
		22668,
		22669,
	};
	
	public Q00377_ExplorationOfTheGiantsCavePart2()
	{
		super(true);
		addStartNpc(HR_SOBLING);
		addKillId(MOBS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "yes":
				htmltext = "Starting.htm";
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "0":
				htmltext = "ext_msg.htm";
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
				break;
			
			case "show":
				htmltext = "no_items.htm";
				for (int[] i : EXCHANGE)
				{
					long count = Long.MAX_VALUE;
					
					for (int j : i)
					{
						count = Math.min(count, qs.getQuestItemsCount(j));
					}
					
					if (count > 0)
					{
						htmltext = "tnx4items.htm";
						
						for (int j : i)
						{
							qs.takeItems(j, count);
						}
						
						for (int n = 0; n < count; n++)
						{
							int luck = Rnd.get(100);
							int item = 0;
							
							if (luck > 75)
							{
								item = 5420;
							}
							else if (luck > 50)
							{
								item = 5422;
							}
							else if (luck > 25)
							{
								item = 5336;
							}
							else
							{
								item = 5338;
							}
							
							qs.giveItems(item, 1);
						}
					}
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int id = qs.getState();
		
		if (qs.getQuestItemsCount(DICT2) == 0)
		{
			qs.exitCurrentQuest(true);
		}
		else if (id == CREATED)
		{
			htmltext = "start.htm";
			
			if (qs.getPlayer().getLevel() < 75)
			{
				qs.exitCurrentQuest(true);
				htmltext = "error_1.htm";
			}
		}
		else if (id == STARTED)
		{
			if (qs.getQuestItemsCount(ANC_BOOK) != 0)
			{
				htmltext = "checkout.htm";
			}
			else
			{
				htmltext = "checkout2.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			qs.rollAndGive(ANC_BOOK, 1, DROP_RATE);
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
