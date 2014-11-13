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
import lineage2.gameserver.scripts.ScriptFile;

public class Q00016_TheComingDarkness extends Quest implements ScriptFile
{
	private static final int HIERARCH = 31517;
	private static final int[][] ALTAR_LIST =
	{
		{
			31512,
			1
		},
		{
			31513,
			2
		},
		{
			31514,
			3
		},
		{
			31515,
			4
		},
		{
			31516,
			5
		}
	};
	public final int CRYSTAL_OF_SEAL = 7167;
	
	public Q00016_TheComingDarkness()
	{
		super(false);
		addStartNpc(HIERARCH);
		
		for (int[] element : ALTAR_LIST)
		{
			addTalkId(element[0]);
		}
		
		addQuestItem(CRYSTAL_OF_SEAL);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("31517-02.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.giveItems(CRYSTAL_OF_SEAL, 5);
			qs.playSound(SOUND_ACCEPT);
		}
		
		for (int[] element : ALTAR_LIST)
		{
			if (event.equals(String.valueOf(element[0]) + "-02.htm"))
			{
				qs.takeItems(CRYSTAL_OF_SEAL, 1);
				qs.setCond(Integer.valueOf(element[1] + 1));
				qs.playSound(SOUND_MIDDLE);
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		if (cond < 1)
		{
			if (qs.getPlayer().getLevel() < 61)
			{
				htmltext = "31517-00.htm";
				qs.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "31517-01.htm";
			}
		}
		else if ((cond > 0) && (cond < 6) && (qs.getQuestItemsCount(CRYSTAL_OF_SEAL) > 0))
		{
			htmltext = "31517-02r.htm";
		}
		else if ((cond > 0) && (cond < 6) && (qs.getQuestItemsCount(CRYSTAL_OF_SEAL) < 1))
		{
			htmltext = "31517-proeb.htm";
			qs.exitCurrentQuest(false);
		}
		else if ((cond > 5) && (qs.getQuestItemsCount(CRYSTAL_OF_SEAL) < 1))
		{
			htmltext = "31517-03.htm";
			qs.addExpAndSp(1795524, 1679808);
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(false);
		}
		
		for (int[] element : ALTAR_LIST)
		{
			if (npcId == element[0])
			{
				if (cond == element[1])
				{
					if (qs.getQuestItemsCount(CRYSTAL_OF_SEAL) > 0)
					{
						htmltext = String.valueOf(element[0]) + "-01.htm";
					}
					else
					{
						htmltext = String.valueOf(element[0]) + "-03.htm";
					}
				}
				else if (cond == (element[1] + 1))
				{
					htmltext = String.valueOf(element[0]) + "-04.htm";
				}
			}
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
