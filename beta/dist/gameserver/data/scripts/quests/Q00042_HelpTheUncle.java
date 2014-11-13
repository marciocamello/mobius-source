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

public class Q00042_HelpTheUncle extends Quest implements ScriptFile
{
	// Npcs
	private static final int WATERS = 30828;
	private static final int SOPHYA = 30735;
	// Monsters
	private static final int MONSTER_EYE_DESTROYER = 20068;
	private static final int MONSTER_EYE_GAZER = 20266;
	// Items
	private static final int TRIDENT = 291;
	private static final int MAP_PIECE = 7548;
	private static final int MAP = 7549;
	private static final int PET_TICKET = 7583;
	// Other
	private static final int MAX_COUNT = 30;
	
	public Q00042_HelpTheUncle()
	{
		super(false);
		addStartNpc(WATERS);
		addTalkId(WATERS, SOPHYA);
		addKillId(MONSTER_EYE_DESTROYER, MONSTER_EYE_GAZER);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "1":
				htmltext = "pet_manager_waters_q0042_0104.htm";
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "3":
				if (qs.getQuestItemsCount(TRIDENT) > 0)
				{
					htmltext = "pet_manager_waters_q0042_0201.htm";
					qs.takeItems(TRIDENT, 1);
					qs.setCond(2);
				}
				break;
			
			case "4":
				if (qs.getQuestItemsCount(MAP_PIECE) >= MAX_COUNT)
				{
					htmltext = "pet_manager_waters_q0042_0301.htm";
					qs.takeItems(MAP_PIECE, MAX_COUNT);
					qs.giveItems(MAP, 1);
					qs.setCond(4);
				}
				break;
			
			case "5":
				if (qs.getQuestItemsCount(MAP) > 0)
				{
					htmltext = "sophia_q0042_0401.htm";
					qs.takeItems(MAP, 1);
					qs.setCond(5);
				}
				break;
			
			case "7":
				htmltext = "pet_manager_waters_q0042_0501.htm";
				qs.giveItems(PET_TICKET, 1);
				qs.unset("cond");
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
		
		switch (qs.getCond())
		{
			case CREATED:
				if (qs.getPlayer().getLevel() >= 25)
				{
					htmltext = "pet_manager_waters_q0042_0101.htm";
				}
				else
				{
					htmltext = "pet_manager_waters_q0042_0103.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case STARTED:
				if (npcId == WATERS)
				{
					switch (cond)
					{
						case 1:
							if (qs.getQuestItemsCount(TRIDENT) == 0)
							{
								htmltext = "pet_manager_waters_q0042_0106.htm";
							}
							else
							{
								htmltext = "pet_manager_waters_q0042_0105.htm";
							}
							break;
						
						case 2:
							htmltext = "pet_manager_waters_q0042_0204.htm";
							break;
						
						case 3:
							htmltext = "pet_manager_waters_q0042_0203.htm";
							break;
						
						case 4:
							htmltext = "pet_manager_waters_q0042_0303.htm";
							break;
						
						case 5:
							htmltext = "pet_manager_waters_q0042_0401.htm";
							break;
					}
				}
				else if (npcId == SOPHYA)
				{
					if ((cond == 4) && (qs.getQuestItemsCount(MAP) > 0))
					{
						htmltext = "sophia_q0042_0301.htm";
					}
					else if (cond == 5)
					{
						htmltext = "sophia_q0042_0402.htm";
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
			
			if (pieces < (MAX_COUNT - 1))
			{
				qs.giveItems(MAP_PIECE, 1);
				qs.playSound(SOUND_ITEMGET);
			}
			else if (pieces == (MAX_COUNT - 1))
			{
				qs.giveItems(MAP_PIECE, 1);
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(3);
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
