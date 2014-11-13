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

public class Q00044_HelpTheSon extends Quest implements ScriptFile
{
	// Npcs
	private static final int LUNDY = 30827;
	private static final int DRIKUS = 30505;
	// Monsters
	private static final int MAILLE_GUARD = 20921;
	private static final int MAILLE_SCOUT = 20920;
	private static final int MAILLE_LIZARDMAN = 20919;
	// Items
	private static final int WORK_HAMMER = 168;
	private static final int GEMSTONE_FRAGMENT = 7552;
	private static final int GEMSTONE = 7553;
	private static final int PET_TICKET = 7585;
	
	public Q00044_HelpTheSon()
	{
		super(false);
		addStartNpc(LUNDY);
		addTalkId(LUNDY, DRIKUS);
		addKillId(MAILLE_GUARD, MAILLE_SCOUT, MAILLE_LIZARDMAN);
		addQuestItem(GEMSTONE_FRAGMENT);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "1":
				htmltext = "pet_manager_lundy_q0044_0104.htm";
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "3":
				if (qs.getQuestItemsCount(WORK_HAMMER) > 0)
				{
					htmltext = "pet_manager_lundy_q0044_0201.htm";
					qs.takeItems(WORK_HAMMER, 1);
					qs.setCond(2);
				}
				break;
			
			case "4":
				if (qs.getQuestItemsCount(GEMSTONE_FRAGMENT) >= 30)
				{
					htmltext = "pet_manager_lundy_q0044_0301.htm";
					qs.takeItems(GEMSTONE_FRAGMENT, -1);
					qs.giveItems(GEMSTONE, 1);
					qs.setCond(4);
				}
				break;
			
			case "5":
				if (qs.getQuestItemsCount(GEMSTONE) > 0)
				{
					htmltext = "high_prefect_drikus_q0044_0401.htm";
					qs.takeItems(GEMSTONE, 1);
					qs.setCond(5);
				}
				break;
			
			case "7":
				htmltext = "pet_manager_lundy_q0044_0501.htm";
				qs.giveItems(PET_TICKET, 1);
				qs.exitCurrentQuest(false);
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
		
		switch (qs.getState())
		{
			case CREATED:
				if (qs.getPlayer().getLevel() >= 24)
				{
					htmltext = "pet_manager_lundy_q0044_0101.htm";
				}
				else
				{
					qs.exitCurrentQuest(true);
					htmltext = "pet_manager_lundy_q0044_0103.htm";
				}
				break;
			
			case STARTED:
				if (npcId == LUNDY)
				{
					switch (cond)
					{
						case 1:
							if (qs.getQuestItemsCount(WORK_HAMMER) == 0)
							{
								htmltext = "pet_manager_lundy_q0044_0106.htm";
							}
							else
							{
								htmltext = "pet_manager_lundy_q0044_0105.htm";
							}
							break;
						
						case 2:
							htmltext = "pet_manager_lundy_q0044_0204.htm";
							break;
						
						case 3:
							htmltext = "pet_manager_lundy_q0044_0203.htm";
							break;
						
						case 4:
							htmltext = "pet_manager_lundy_q0044_0303.htm";
							break;
						
						case 5:
							htmltext = "pet_manager_lundy_q0044_0401.htm";
							break;
					}
				}
				else if (npcId == DRIKUS)
				{
					if ((cond == 4) && (qs.getQuestItemsCount(GEMSTONE) > 0))
					{
						htmltext = "high_prefect_drikus_q0044_0301.htm";
					}
					else if (cond == 5)
					{
						htmltext = "high_prefect_drikus_q0044_0403.htm";
					}
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 2) && (qs.getQuestItemsCount(GEMSTONE_FRAGMENT) < 30))
		{
			qs.giveItems(GEMSTONE_FRAGMENT, 1);
			
			if (qs.getQuestItemsCount(GEMSTONE_FRAGMENT) >= 30)
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(3);
				qs.playSound(SOUND_ITEMGET);
			}
			else
			{
				qs.playSound(SOUND_ITEMGET);
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
