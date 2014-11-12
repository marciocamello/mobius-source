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
import lineage2.gameserver.utils.Util;

public class Q00270_TheOneWhoEndsSilence extends Quest implements ScriptFile
{
	// Npc
	private static final int Greymore = 32757;
	// Item
	private static final int TatteredMonkClothes = 15526;
	// Monsters
	private static final int[] LowMobs =
	{
		22791,
		22790,
		22793
	};
	private static final int[] HighMobs =
	{
		22794,
		22795,
		22797,
		22798,
		22799,
		22800
	};
	
	public Q00270_TheOneWhoEndsSilence()
	{
		super(false);
		addStartNpc(Greymore);
		addKillId(LowMobs);
		addKillId(HighMobs);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "greymore_q270_03.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "showrags":
				if (qs.getQuestItemsCount(TatteredMonkClothes) < 1)
				{
					htmltext = "greymore_q270_05.htm";
				}
				else if (qs.getQuestItemsCount(TatteredMonkClothes) < 100)
				{
					htmltext = "greymore_q270_06.htm";
				}
				else if (qs.getQuestItemsCount(TatteredMonkClothes) >= 100)
				{
					htmltext = "greymore_q270_07.htm";
				}
				break;
			
			case "rags100":
				if (qs.getQuestItemsCount(TatteredMonkClothes) >= 100)
				{
					qs.takeItems(TatteredMonkClothes, 100);
					
					switch (Rnd.get(1, 21))
					{
						case 1:
							qs.giveItems(10373, 1);
							break;
						
						case 2:
							qs.giveItems(10374, 1);
							break;
						
						case 3:
							qs.giveItems(10375, 1);
							break;
						
						case 4:
							qs.giveItems(10376, 1);
							break;
						
						case 5:
							qs.giveItems(10377, 1);
							break;
						
						case 6:
							qs.giveItems(10378, 1);
							break;
						
						case 7:
							qs.giveItems(10379, 1);
							break;
						
						case 8:
							qs.giveItems(10380, 1);
							break;
						
						case 9:
							qs.giveItems(10381, 1);
							break;
						
						case 10:
							qs.giveItems(10397, 1);
							break;
						
						case 11:
							qs.giveItems(10398, 1);
							break;
						
						case 12:
							qs.giveItems(10399, 1);
							break;
						
						case 13:
							qs.giveItems(10400, 1);
							break;
						
						case 14:
							qs.giveItems(10401, 1);
							break;
						
						case 15:
							qs.giveItems(10402, 1);
							break;
						
						case 16:
							qs.giveItems(10403, 1);
							break;
						
						case 17:
							qs.giveItems(10405, 1);
							break;
						
						case 18:
							qs.giveItems(5593, 1);
							break;
						
						case 19:
							qs.giveItems(5594, 1);
							break;
						
						case 20:
							qs.giveItems(5595, 1);
							break;
						
						case 21:
							qs.giveItems(9898, 1);
							break;
					}
					
					htmltext = "greymore_q270_09.htm";
				}
				else
				{
					htmltext = "greymore_q270_08.htm";
				}
				break;
			
			case "rags200":
				if (qs.getQuestItemsCount(TatteredMonkClothes) >= 200)
				{
					qs.takeItems(TatteredMonkClothes, 200);
					
					switch (Rnd.get(1, 17))
					{
						case 1:
							qs.giveItems(10373, 1);
							break;
						
						case 2:
							qs.giveItems(10374, 1);
							break;
						
						case 3:
							qs.giveItems(10375, 1);
							break;
						
						case 4:
							qs.giveItems(10376, 1);
							break;
						
						case 5:
							qs.giveItems(10377, 1);
							break;
						
						case 6:
							qs.giveItems(10378, 1);
							break;
						
						case 7:
							qs.giveItems(10379, 1);
							break;
						
						case 8:
							qs.giveItems(10380, 1);
							break;
						
						case 9:
							qs.giveItems(10381, 1);
							break;
						
						case 10:
							qs.giveItems(10397, 1);
							break;
						
						case 11:
							qs.giveItems(10398, 1);
							break;
						
						case 12:
							qs.giveItems(10399, 1);
							break;
						
						case 13:
							qs.giveItems(10400, 1);
							break;
						
						case 14:
							qs.giveItems(10401, 1);
							break;
						
						case 15:
							qs.giveItems(10402, 1);
							break;
						
						case 16:
							qs.giveItems(10403, 1);
							break;
						
						case 17:
							qs.giveItems(10405, 1);
							break;
					}
					
					switch (Rnd.get(1, 4))
					{
						case 1:
							qs.giveItems(5593, 1);
							break;
						
						case 2:
							qs.giveItems(5594, 1);
							break;
						
						case 3:
							qs.giveItems(5595, 1);
							break;
						
						case 4:
							qs.giveItems(9898, 1);
							break;
					}
					
					htmltext = "greymore_q270_09.htm";
				}
				else
				{
					htmltext = "greymore_q270_08.htm";
				}
				break;
			
			case "rags300":
				if (qs.getQuestItemsCount(TatteredMonkClothes) >= 300)
				{
					qs.takeItems(TatteredMonkClothes, 300);
					
					switch (Rnd.get(1, 9))
					{
						case 1:
							qs.giveItems(10373, 1);
							break;
						
						case 2:
							qs.giveItems(10374, 1);
							break;
						
						case 3:
							qs.giveItems(10375, 1);
							break;
						
						case 4:
							qs.giveItems(10376, 1);
							break;
						
						case 5:
							qs.giveItems(10377, 1);
							break;
						
						case 6:
							qs.giveItems(10378, 1);
							break;
						
						case 7:
							qs.giveItems(10379, 1);
							break;
						
						case 8:
							qs.giveItems(10380, 1);
							break;
						
						case 9:
							qs.giveItems(10381, 1);
							break;
					}
					
					switch (Rnd.get(10, 17))
					{
						case 10:
							qs.giveItems(10397, 1);
							break;
						
						case 11:
							qs.giveItems(10398, 1);
							break;
						
						case 12:
							qs.giveItems(10399, 1);
							break;
						
						case 13:
							qs.giveItems(10400, 1);
							break;
						
						case 14:
							qs.giveItems(10401, 1);
							break;
						
						case 15:
							qs.giveItems(10402, 1);
							break;
						
						case 16:
							qs.giveItems(10403, 1);
							break;
						
						case 17:
							qs.giveItems(10405, 1);
							break;
					}
					
					switch (Rnd.get(1, 4))
					{
						case 1:
							qs.giveItems(5593, 1);
							break;
						
						case 2:
							qs.giveItems(5594, 1);
							break;
						
						case 3:
							qs.giveItems(5595, 1);
							break;
						
						case 4:
							qs.giveItems(9898, 1);
							break;
					}
					
					htmltext = "greymore_q270_09.htm";
				}
				else
				{
					htmltext = "greymore_q270_08.htm";
				}
				break;
			
			case "rags400":
				if (qs.getQuestItemsCount(TatteredMonkClothes) >= 400)
				{
					qs.takeItems(TatteredMonkClothes, 400);
					
					switch (Rnd.get(1, 9))
					{
						case 1:
							qs.giveItems(10373, 1);
							break;
						
						case 2:
							qs.giveItems(10374, 1);
							break;
						
						case 3:
							qs.giveItems(10375, 1);
							break;
						
						case 4:
							qs.giveItems(10376, 1);
							break;
						
						case 5:
							qs.giveItems(10377, 1);
							break;
						
						case 6:
							qs.giveItems(10378, 1);
							break;
						
						case 7:
							qs.giveItems(10379, 1);
							break;
						
						case 8:
							qs.giveItems(10380, 1);
							break;
						
						case 9:
							qs.giveItems(10381, 1);
							break;
					}
					
					switch (Rnd.get(10, 17))
					{
						case 10:
							qs.giveItems(10397, 1);
							break;
						
						case 11:
							qs.giveItems(10398, 1);
							break;
						
						case 12:
							qs.giveItems(10399, 1);
							break;
						
						case 13:
							qs.giveItems(10400, 1);
							break;
						
						case 14:
							qs.giveItems(10401, 1);
							break;
						
						case 15:
							qs.giveItems(10402, 1);
							break;
						
						case 16:
							qs.giveItems(10403, 1);
							break;
						
						case 17:
							qs.giveItems(10405, 1);
							break;
					}
					
					switch (Rnd.get(1, 4))
					{
						case 1:
							qs.giveItems(5593, 2);
							break;
						
						case 2:
							qs.giveItems(5594, 2);
							break;
						
						case 3:
							qs.giveItems(5595, 2);
							break;
						
						case 4:
							qs.giveItems(9898, 2);
							break;
					}
					
					htmltext = "greymore_q270_09.htm";
				}
				else
				{
					htmltext = "greymore_q270_08.htm";
				}
				break;
			
			case "rags500":
				if (qs.getQuestItemsCount(TatteredMonkClothes) >= 500)
				{
					qs.takeItems(TatteredMonkClothes, 500);
					
					switch (Rnd.get(1, 9))
					{
						case 1:
							qs.giveItems(10373, 2);
							break;
						
						case 2:
							qs.giveItems(10374, 2);
							break;
						
						case 3:
							qs.giveItems(10375, 2);
							break;
						
						case 4:
							qs.giveItems(10376, 2);
							break;
						
						case 5:
							qs.giveItems(10377, 2);
							break;
						
						case 6:
							qs.giveItems(10378, 2);
							break;
						
						case 7:
							qs.giveItems(10379, 2);
							break;
						
						case 8:
							qs.giveItems(10380, 2);
							break;
						
						case 9:
							qs.giveItems(10381, 2);
							break;
					}
					
					switch (Rnd.get(10, 17))
					{
						case 10:
							qs.giveItems(10397, 2);
							break;
						
						case 11:
							qs.giveItems(10398, 2);
							break;
						
						case 12:
							qs.giveItems(10399, 2);
							break;
						
						case 13:
							qs.giveItems(10400, 2);
							break;
						
						case 14:
							qs.giveItems(10401, 2);
							break;
						
						case 15:
							qs.giveItems(10402, 2);
							break;
						
						case 16:
							qs.giveItems(10403, 2);
							break;
						
						case 17:
							qs.giveItems(10405, 2);
							break;
					}
					
					switch (Rnd.get(1, 4))
					{
						case 1:
							qs.giveItems(5593, 1);
							break;
						
						case 2:
							qs.giveItems(5594, 1);
							break;
						
						case 3:
							qs.giveItems(5595, 1);
							break;
						
						case 4:
							qs.giveItems(9898, 1);
							break;
					}
					
					htmltext = "greymore_q270_09.htm";
				}
				else
				{
					htmltext = "greymore_q270_08.htm";
				}
				break;
			
			case "quit":
				htmltext = "greymore_q270_10.htm";
				qs.exitCurrentQuest(true);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		
		if (cond == 0)
		{
			final QuestState state = qs.getPlayer().getQuestState(Q10288_SecretMission.class);
			
			if ((qs.getPlayer().getLevel() >= 82) && (state != null) && state.isCompleted())
			{
				htmltext = "greymore_q270_01.htm";
			}
			else
			{
				htmltext = "greymore_q270_00.htm";
				qs.exitCurrentQuest(true);
			}
		}
		else if (cond == 1)
		{
			htmltext = "greymore_q270_04.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			if (Util.contains(LowMobs, npc.getId()) && Rnd.chance(40))
			{
				qs.giveItems(TatteredMonkClothes, 1, true);
			}
			else if (Util.contains(HighMobs, npc.getId()))
			{
				qs.giveItems(TatteredMonkClothes, 1, true);
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
