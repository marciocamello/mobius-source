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

public class Q00017_LightAndDarkness extends Quest implements ScriptFile
{
	// Npcs
	private static final int HIERARCH = 31517;
	private static final int ALTAR1 = 31508;
	private static final int ALTAR2 = 31509;
	private static final int ALTAR3 = 31510;
	private static final int ALTAR4 = 31511;
	// Item
	private static final int BLOOD_OF_SAINT = 7168;
	
	public Q00017_LightAndDarkness()
	{
		super(false);
		addStartNpc(HIERARCH);
		addTalkId(ALTAR1, ALTAR2, ALTAR3, ALTAR4);
		addQuestItem(BLOOD_OF_SAINT);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "dark_presbyter_q0017_04.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.giveItems(BLOOD_OF_SAINT, 4);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "blessed_altar1_q0017_02.htm":
				qs.takeItems(BLOOD_OF_SAINT, 1);
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "blessed_altar2_q0017_02.htm":
				qs.takeItems(BLOOD_OF_SAINT, 1);
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "blessed_altar3_q0017_02.htm":
				qs.takeItems(BLOOD_OF_SAINT, 1);
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "blessed_altar4_q0017_02.htm":
				qs.takeItems(BLOOD_OF_SAINT, 1);
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
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
		
		switch (npcId)
		{
			case HIERARCH:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() >= 61)
					{
						htmltext = "dark_presbyter_q0017_01.htm";
					}
					else
					{
						htmltext = "dark_presbyter_q0017_03.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if ((cond > 0) && (cond < 5) && (qs.getQuestItemsCount(BLOOD_OF_SAINT) > 0))
				{
					htmltext = "dark_presbyter_q0017_05.htm";
				}
				else if ((cond > 0) && (cond < 5) && (qs.getQuestItemsCount(BLOOD_OF_SAINT) == 0))
				{
					htmltext = "dark_presbyter_q0017_06.htm";
					qs.setCond(0);
					qs.exitCurrentQuest(false);
				}
				else if ((cond == 5) && (qs.getQuestItemsCount(BLOOD_OF_SAINT) == 0))
				{
					htmltext = "dark_presbyter_q0017_07.htm";
					qs.addExpAndSp(1469840, 1358340);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
				break;
			
			case ALTAR1:
				if (cond == 1)
				{
					if (qs.getQuestItemsCount(BLOOD_OF_SAINT) != 0)
					{
						htmltext = "blessed_altar1_q0017_01.htm";
					}
					else
					{
						htmltext = "blessed_altar1_q0017_03.htm";
					}
				}
				else if (cond == 2)
				{
					htmltext = "blessed_altar1_q0017_05.htm";
				}
				break;
			
			case ALTAR2:
				if (cond == 2)
				{
					if (qs.getQuestItemsCount(BLOOD_OF_SAINT) != 0)
					{
						htmltext = "blessed_altar2_q0017_01.htm";
					}
					else
					{
						htmltext = "blessed_altar2_q0017_03.htm";
					}
				}
				else if (cond == 3)
				{
					htmltext = "blessed_altar2_q0017_05.htm";
				}
				break;
			
			case ALTAR3:
				if (cond == 3)
				{
					if (qs.getQuestItemsCount(BLOOD_OF_SAINT) != 0)
					{
						htmltext = "blessed_altar3_q0017_01.htm";
					}
					else
					{
						htmltext = "blessed_altar3_q0017_03.htm";
					}
				}
				else if (cond == 4)
				{
					htmltext = "blessed_altar3_q0017_05.htm";
				}
				break;
			
			case ALTAR4:
				if (cond == 4)
				{
					if (qs.getQuestItemsCount(BLOOD_OF_SAINT) != 0)
					{
						htmltext = "blessed_altar4_q0017_01.htm";
					}
					else
					{
						htmltext = "blessed_altar4_q0017_03.htm";
					}
				}
				else if (cond == 5)
				{
					htmltext = "blessed_altar4_q0017_05.htm";
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
