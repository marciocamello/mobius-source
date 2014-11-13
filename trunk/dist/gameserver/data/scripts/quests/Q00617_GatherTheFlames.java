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

public class Q00617_GatherTheFlames extends Quest implements ScriptFile
{
	private final static int VULCAN = 31539;
	private final static int HILDA = 31271;
	private final static int TORCH = 7264;
	private final static int[][] DROPLIST =
	{
		{
			22634,
			58
		},
		{
			22635,
			58
		},
		{
			22636,
			58
		},
		{
			22637,
			58
		},
		{
			22638,
			58
		},
		{
			22639,
			61
		},
		{
			22640,
			61
		},
		{
			22641,
			61
		},
		{
			22642,
			61
		},
		{
			22643,
			61
		},
		{
			22644,
			61
		},
		{
			22645,
			63
		},
		{
			22646,
			63
		},
		{
			22647,
			63
		},
		{
			22648,
			66
		},
		{
			22649,
			66
		},
		{
			18799,
			66
		},
		{
			18800,
			66
		},
		{
			18801,
			66
		},
		{
			18802,
			74
		},
		{
			18803,
			74
		}
	};
	public static final int[] Recipes =
	{
		6881,
		6883,
		6885,
		6887,
		7580,
		6891,
		6893,
		6895,
		6897,
		6899
	};
	
	public Q00617_GatherTheFlames()
	{
		super(true);
		addStartNpc(VULCAN, HILDA);
		
		for (int[] element : DROPLIST)
		{
			addKillId(element[0]);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "warsmith_vulcan_q0617_03.htm":
				if (qs.getPlayer().getLevel() < 74)
				{
					return "warsmith_vulcan_q0617_02.htm";
				}
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				qs.setCond(1);
				break;
			
			case "blacksmith_hilda_q0617_03.htm":
				if (qs.getPlayer().getLevel() < 74)
				{
					return "blacksmith_hilda_q0617_02.htm";
				}
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				qs.setCond(1);
				break;
			
			case "warsmith_vulcan_q0617_08.htm":
				qs.playSound(SOUND_FINISH);
				qs.takeItems(TORCH, -1);
				qs.exitCurrentQuest(true);
				break;
			
			case "warsmith_vulcan_q0617_07.htm":
				if (qs.getQuestItemsCount(TORCH) < 1000)
				{
					return "warsmith_vulcan_q0617_05.htm";
				}
				qs.takeItems(TORCH, 1000);
				qs.giveItems(Recipes[Rnd.get(Recipes.length)], 1);
				qs.playSound(SOUND_MIDDLE);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		
		switch (npcId)
		{
			case VULCAN:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() < 74)
					{
						htmltext = "warsmith_vulcan_q0617_02.htm";
						qs.exitCurrentQuest(true);
					}
					else
					{
						htmltext = "warsmith_vulcan_q0617_01.htm";
					}
				}
				else
				{
					htmltext = qs.getQuestItemsCount(TORCH) < 1000 ? "warsmith_vulcan_q0617_05.htm" : "warsmith_vulcan_q0617_04.htm";
				}
				break;
			
			case HILDA:
				if (cond < 1)
				{
					htmltext = qs.getPlayer().getLevel() < 74 ? "blacksmith_hilda_q0617_02.htm" : "blacksmith_hilda_q0617_01.htm";
				}
				else
				{
					htmltext = "blacksmith_hilda_q0617_04.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		for (int[] element : DROPLIST)
		{
			if (npc.getId() == element[0])
			{
				qs.rollAndGive(TORCH, 1, element[1]);
				return null;
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
