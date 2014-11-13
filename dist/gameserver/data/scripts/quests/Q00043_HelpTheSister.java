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

public class Q00043_HelpTheSister extends Quest implements ScriptFile
{
	// Npcs
	private static final int COOPER = 30829;
	private static final int GALLADUCCI = 30097;
	// Monsters
	private static final int SPECTER = 20171;
	private static final int SORROW_MAIDEN = 20197;
	// Items
	private static final int CRAFTED_DAGGER = 220;
	private static final int MAP_PIECE = 7550;
	private static final int MAP = 7551;
	private static final int PET_TICKET = 7584;
	// Other
	private static final int MAX_COUNT = 30;
	
	public Q00043_HelpTheSister()
	{
		super(false);
		addStartNpc(COOPER);
		addTalkId(GALLADUCCI);
		addKillId(SPECTER);
		addKillId(SORROW_MAIDEN);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "1":
				htmltext = "pet_manager_cooper_q0043_0104.htm";
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "3":
				if (qs.getQuestItemsCount(CRAFTED_DAGGER) > 0)
				{
					htmltext = "pet_manager_cooper_q0043_0201.htm";
					qs.takeItems(CRAFTED_DAGGER, 1);
					qs.setCond(2);
				}
				break;
			
			case "4":
				if (qs.getQuestItemsCount(MAP_PIECE) >= MAX_COUNT)
				{
					htmltext = "pet_manager_cooper_q0043_0301.htm";
					qs.takeItems(MAP_PIECE, MAX_COUNT);
					qs.giveItems(MAP, 1);
					qs.setCond(4);
				}
				break;
			
			case "5":
				if (qs.getQuestItemsCount(MAP) > 0)
				{
					htmltext = "galladuchi_q0043_0401.htm";
					qs.takeItems(MAP, 1);
					qs.setCond(5);
				}
				break;
			
			case "7":
				htmltext = "pet_manager_cooper_q0043_0501.htm";
				qs.giveItems(PET_TICKET, 1);
				qs.setCond(0);
				qs.exitCurrentQuest(false);
				break;
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
		
		switch (qs.getState())
		{
			case CREATED:
				if (qs.getPlayer().getLevel() >= 26)
				{
					htmltext = "pet_manager_cooper_q0043_0101.htm";
				}
				else
				{
					qs.exitCurrentQuest(true);
					htmltext = "pet_manager_cooper_q0043_0103.htm";
				}
				break;
			
			case STARTED:
				if (npcId == COOPER)
				{
					switch (cond)
					{
						case 1:
							if (qs.getQuestItemsCount(CRAFTED_DAGGER) == 0)
							{
								htmltext = "pet_manager_cooper_q0043_0106.htm";
							}
							else
							{
								htmltext = "pet_manager_cooper_q0043_0105.htm";
							}
							break;
						
						case 2:
							htmltext = "pet_manager_cooper_q0043_0204.htm";
							break;
						
						case 3:
							htmltext = "pet_manager_cooper_q0043_0203.htm";
							break;
						
						case 4:
							htmltext = "pet_manager_cooper_q0043_0303.htm";
							break;
						
						case 5:
							htmltext = "pet_manager_cooper_q0043_0401.htm";
							break;
					}
				}
				else if (npcId == GALLADUCCI)
				{
					if ((cond == 4) && (qs.getQuestItemsCount(MAP) > 0))
					{
						htmltext = "galladuchi_q0043_0301.htm";
					}
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 2)
		{
			final long pieces = qs.getQuestItemsCount(MAP_PIECE);
			
			if (pieces < MAX_COUNT)
			{
				qs.giveItems(MAP_PIECE, 1);
				
				if (pieces < (MAX_COUNT - 1))
				{
					qs.playSound(SOUND_ITEMGET);
				}
				else
				{
					qs.playSound(SOUND_MIDDLE);
					qs.setCond(3);
				}
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
