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

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00648_AnIceMerchantsDream extends Quest implements ScriptFile
{
	// Npcs
	private static final int Rafforty = 32020;
	private static final int Ice_Shelf = 32023;
	// Items
	private static final int Silver_Hemocyte = 8057;
	private static final int Silver_Ice_Crystal = 8077;
	private static final int Black_Ice_Crystal = 8078;
	private static final int Silver_Hemocyte_Chance = 10;
	private static final int Silver2Black_Chance = 30;
	private static final List<Integer> silver2black = new ArrayList<>();
	
	public Q00648_AnIceMerchantsDream()
	{
		super(true);
		addStartNpc(Rafforty);
		addStartNpc(Ice_Shelf);
		
		for (int i = 22080; i <= 22098; i++)
		{
			if (i != 22095)
			{
				addKillId(i);
			}
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final int _state = qs.getState();
		
		if (event.equals("repre_q0648_04.htm") && (_state == CREATED))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("repre_q0648_22.htm") && (_state == STARTED))
		{
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(true);
		}
		
		if (_state != STARTED)
		{
			return event;
		}
		
		final long Silver_Ice_Crystal_Count = qs.getQuestItemsCount(Silver_Ice_Crystal);
		final long Black_Ice_Crystal_Count = qs.getQuestItemsCount(Black_Ice_Crystal);
		final Integer char_obj_id = qs.getPlayer().getObjectId();
		
		switch (event)
		{
			case "repre_q0648_14.htm":
				long reward = (Silver_Ice_Crystal_Count * 300) + (Black_Ice_Crystal_Count * 1200);
				if (reward > 0)
				{
					qs.takeItems(Silver_Ice_Crystal, -1);
					qs.takeItems(Black_Ice_Crystal, -1);
					qs.giveItems(ADENA_ID, reward);
				}
				else
				{
					return "repre_q0648_15.htm";
				}
				break;
			
			case "ice_lathe_q0648_06.htm":
				synchronized (silver2black)
				{
					if (silver2black.contains(char_obj_id))
					{
						return event;
					}
					else if (Silver_Ice_Crystal_Count > 0)
					{
						silver2black.add(char_obj_id);
					}
					else
					{
						return "cheat.htm";
					}
				}
				qs.takeItems(Silver_Ice_Crystal, 1);
				qs.playSound(SOUND_BROKEN_KEY);
				break;
			
			case "ice_lathe_q0648_08.htm":
				synchronized (silver2black)
				{
					if (silver2black.contains(char_obj_id))
					{
						while (silver2black.contains(char_obj_id))
						{
							silver2black.remove(char_obj_id);
						}
					}
					else
					{
						return "cheat.htm";
					}
				}
				if (Rnd.chance(Silver2Black_Chance))
				{
					qs.giveItems(Black_Ice_Crystal, 1);
					qs.playSound(SOUND_ENCHANT_SUCESS);
				}
				else
				{
					qs.playSound(SOUND_ENCHANT_FAILED);
					return "ice_lathe_q0648_09.htm";
				}
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final int _state = qs.getState();
		final int npcId = npc.getId();
		int cond = qs.getCond();
		
		if (_state == CREATED)
		{
			if (npcId == Rafforty)
			{
				if (qs.getPlayer().getLevel() >= 53)
				{
					qs.setCond(0);
					return "repre_q0648_03.htm";
				}
				
				qs.exitCurrentQuest(true);
				return "repre_q0648_01.htm";
			}
			
			if (npcId == Ice_Shelf)
			{
				return "ice_lathe_q0648_01.htm";
			}
		}
		
		if (_state != STARTED)
		{
			return "noquest";
		}
		
		final long Silver_Ice_Crystal_Count = qs.getQuestItemsCount(Silver_Ice_Crystal);
		final long Black_Ice_Crystal_Count = qs.getQuestItemsCount(Black_Ice_Crystal);
		
		if (npcId == Ice_Shelf)
		{
			return Silver_Ice_Crystal_Count > 0 ? "ice_lathe_q0648_03.htm" : "ice_lathe_q0648_02.htm";
		}
		else if (npcId == Rafforty)
		{
			final QuestState st_115 = qs.getPlayer().getQuestState(Q00115_TheOtherSideOfTruth.class);
			
			if ((st_115 != null) && st_115.isCompleted())
			{
				cond = 2;
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
			}
			
			if (cond == 1)
			{
				if ((Silver_Ice_Crystal_Count > 0) || (Black_Ice_Crystal_Count > 0))
				{
					return "repre_q0648_10.htm";
				}
				
				return "repre_q0648_08.htm";
			}
			else if (cond == 2)
			{
				return (Silver_Ice_Crystal_Count > 0) || (Black_Ice_Crystal_Count > 0) ? "repre_q0648_11.htm" : "repre_q0648_09.htm";
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		if (cond > 0)
		{
			qs.rollAndGive(Silver_Ice_Crystal, 1, npc.getId() - 22050);
			
			if (cond == 2)
			{
				qs.rollAndGive(Silver_Hemocyte, 1, Silver_Hemocyte_Chance);
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
