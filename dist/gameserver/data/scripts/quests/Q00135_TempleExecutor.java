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

public class Q00135_TempleExecutor extends Quest implements ScriptFile
{
	// Npcs
	private final static int Shegfield = 30068;
	private final static int Pano = 30078;
	private final static int Alex = 30291;
	private final static int Sonin = 31773;
	// Monsters
	private final static int[] mobs =
	{
		20781,
		21104,
		21105,
		21106,
		21107
	};
	// Items
	private final static int Stolen_Cargo = 10328;
	private final static int Hate_Crystal = 10329;
	private final static int Old_Treasure_Map = 10330;
	private final static int Sonins_Credentials = 10331;
	private final static int Panos_Credentials = 10332;
	private final static int Alexs_Credentials = 10333;
	private final static int Badge_Temple_Executor = 10334;
	
	public Q00135_TempleExecutor()
	{
		super(false);
		addStartNpc(Shegfield);
		addTalkId(Alex, Sonin, Pano);
		addKillId(mobs);
		addQuestItem(Stolen_Cargo, Hate_Crystal, Old_Treasure_Map, Sonins_Credentials, Panos_Credentials, Alexs_Credentials);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final int _state = qs.getState();
		
		switch (event)
		{
			case "shegfield_q0135_03.htm":
				if (_state == CREATED)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "shegfield_q0135_13.htm":
				if (_state == STARTED)
				{
					qs.playSound(SOUND_FINISH);
					qs.unset("Report");
					qs.giveItems(ADENA_ID, 16924);
					qs.giveItems(Badge_Temple_Executor, 1);
					qs.exitCurrentQuest(false);
				}
				break;
			
			case "shegfield_q0135_04.htm":
				if (_state == STARTED)
				{
					qs.setCond(2);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "alankell_q0135_07.htm":
				if (_state == STARTED)
				{
					qs.setCond(3);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		final int _state = qs.getState();
		
		switch (_state)
		{
			case COMPLETED:
				return "completed";
				
			case CREATED:
				if (npcId != Shegfield)
				{
					return "noquest";
				}
				if (qs.getPlayer().getLevel() < 35)
				{
					qs.exitCurrentQuest(true);
					return "shegfield_q0135_02.htm";
				}
				qs.setCond(0);
				return "shegfield_q0135_01.htm";
				
			case STARTED:
				switch (npcId)
				{
					case Shegfield:
						if (cond == 1)
						{
							return "shegfield_q0135_03.htm";
						}
						if (cond == 5)
						{
							if (qs.getInt("Report") == 1)
							{
								return "shegfield_q0135_09.htm";
							}
							
							if ((qs.getQuestItemsCount(Sonins_Credentials) > 0) && (qs.getQuestItemsCount(Panos_Credentials) > 0) && (qs.getQuestItemsCount(Alexs_Credentials) > 0))
							{
								qs.takeItems(Panos_Credentials, -1);
								qs.takeItems(Sonins_Credentials, -1);
								qs.takeItems(Alexs_Credentials, -1);
								qs.set("Report", "1");
								return "shegfield_q0135_08.htm";
							}
							
							return "noquest";
						}
						return "shegfield_q0135_06.htm";
						
					case Alex:
						switch (cond)
						{
							case 2:
								return "alankell_q0135_02.htm";
								
							case 3:
								return "alankell_q0135_08.htm";
								
							case 4:
								if ((qs.getQuestItemsCount(Sonins_Credentials) > 0) && (qs.getQuestItemsCount(Panos_Credentials) > 0))
								{
									qs.setCond(5);
									qs.takeItems(Old_Treasure_Map, -1);
									qs.giveItems(Alexs_Credentials, 1);
									qs.playSound(SOUND_MIDDLE);
									return "alankell_q0135_10.htm";
								}
								return "alankell_q0135_09.htm";
								
							case 5:
								return "alankell_q0135_11.htm";
						}
						break;
					
					case Sonin:
						if (qs.getQuestItemsCount(Stolen_Cargo) < 10)
						{
							return "warehouse_keeper_sonin_q0135_04.htm";
						}
						qs.takeItems(Stolen_Cargo, -1);
						qs.giveItems(Sonins_Credentials, 1);
						qs.playSound(SOUND_MIDDLE);
						return "warehouse_keeper_sonin_q0135_03.htm";
						
					case Pano:
						if (cond == 4)
						{
							if (qs.getQuestItemsCount(Hate_Crystal) < 10)
							{
								return "pano_q0135_04.htm";
							}
							
							qs.takeItems(Hate_Crystal, -1);
							qs.giveItems(Panos_Credentials, 1);
							qs.playSound(SOUND_MIDDLE);
							return "pano_q0135_03.htm";
						}
						break;
				}
				break;
		}
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getState() == STARTED) && (qs.getCond() == 3))
		{
			List<Integer> drops = new ArrayList<>();
			
			if (qs.getQuestItemsCount(Stolen_Cargo) < 10)
			{
				drops.add(Stolen_Cargo);
			}
			
			if (qs.getQuestItemsCount(Hate_Crystal) < 10)
			{
				drops.add(Hate_Crystal);
			}
			
			if (qs.getQuestItemsCount(Old_Treasure_Map) < 10)
			{
				drops.add(Old_Treasure_Map);
			}
			
			if (drops.isEmpty())
			{
				return null;
			}
			
			final int drop = drops.get(Rnd.get(drops.size()));
			qs.giveItems(drop, 1);
			if ((drops.size() == 1) && (qs.getQuestItemsCount(drop) >= 10))
			{
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
				return null;
			}
			
			qs.playSound(SOUND_ITEMGET);
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
