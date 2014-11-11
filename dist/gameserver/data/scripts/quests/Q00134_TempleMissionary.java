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

public class Q00134_TempleMissionary extends Quest implements ScriptFile
{
	// Npcs
	private final static int Glyvka = 30067;
	private final static int Rouke = 31418;
	// Monsters
	private final static int Cruma_Marshlands_Traitor = 27339;
	private final static int[] mobs =
	{
		20157,
		20229,
		20230,
		20231,
		20232,
		20233,
		20234
	};
	// Items
	private final static int Giants_Experimental_Tool_Fragment = 10335;
	private final static int Giants_Experimental_Tool = 10336;
	private final static int Giants_Technology_Report = 10337;
	private final static int Roukes_Report = 10338;
	private final static int Badge_Temple_Missionary = 10339;
	private final static int Giants_Experimental_Tool_Fragment_chance = 66;
	private final static int Cruma_Marshlands_Traitor_spawnchance = 45;
	
	public Q00134_TempleMissionary()
	{
		super(false);
		addStartNpc(Glyvka);
		addTalkId(Rouke);
		addKillId(mobs);
		addKillId(Cruma_Marshlands_Traitor);
		addQuestItem(Giants_Experimental_Tool_Fragment, Giants_Experimental_Tool, Giants_Technology_Report, Roukes_Report);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final int _state = qs.getState();
		
		switch (event)
		{
			case "glyvka_q0134_03.htm":
				if (_state == CREATED)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "glyvka_q0134_06.htm":
				if (_state == STARTED)
				{
					qs.setCond(2);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "glyvka_q0134_11.htm":
				if ((_state == STARTED) && (qs.getCond() == 5))
				{
					qs.playSound(SOUND_FINISH);
					qs.unset("Report");
					qs.giveItems(ADENA_ID, 15100);
					qs.giveItems(Badge_Temple_Missionary, 1);
					qs.exitCurrentQuest(false);
				}
				break;
			
			case "scroll_seller_rouke_q0134_03.htm":
				if (_state == STARTED)
				{
					qs.setCond(3);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "scroll_seller_rouke_q0134_09.htm":
				if ((_state == STARTED) && (qs.getInt("Report") == 1))
				{
					qs.setCond(5);
					qs.playSound(SOUND_MIDDLE);
					qs.giveItems(Roukes_Report, 1);
					qs.unset("Report");
				}
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		final int npcId = npc.getId();
		final int cond = st.getCond();
		final int _state = st.getState();
		
		switch (_state)
		{
			case COMPLETED:
				return "completed";
				
			case CREATED:
				if (npcId != Glyvka)
				{
					return "noquest";
				}
				if (st.getPlayer().getLevel() < 35)
				{
					st.exitCurrentQuest(true);
					return "glyvka_q0134_02.htm";
				}
				st.setCond(0);
				return "glyvka_q0134_01.htm";
				
			case STARTED:
				switch (npcId)
				{
					case Glyvka:
						if (cond == 1)
						{
							return "glyvka_q0134_03.htm";
						}
						if (cond == 5)
						{
							if (st.getInt("Report") == 1)
							{
								return "glyvka_q0134_09.htm";
							}
							
							if (st.getQuestItemsCount(Roukes_Report) > 0)
							{
								st.takeItems(Roukes_Report, -1);
								st.set("Report", "1");
								return "glyvka_q0134_08.htm";
							}
							
							return "noquest";
						}
						return "glyvka_q0134_07.htm";
						
					case Rouke:
						switch (cond)
						{
							case 2:
								return "scroll_seller_rouke_q0134_02.htm";
								
							case 5:
								return "scroll_seller_rouke_q0134_10.htm";
								
							case 3:
								long Tools = st.getQuestItemsCount(Giants_Experimental_Tool_Fragment) / 10;
								if (Tools < 1)
								{
									return "scroll_seller_rouke_q0134_04.htm";
								}
								st.takeItems(Giants_Experimental_Tool_Fragment, Tools * 10);
								st.giveItems(Giants_Experimental_Tool, Tools);
								return "scroll_seller_rouke_q0134_05.htm";
								
							case 4:
								if (st.getInt("Report") == 1)
								{
									return "scroll_seller_rouke_q0134_07.htm";
								}
								if (st.getQuestItemsCount(Giants_Technology_Report) > 2)
								{
									st.takeItems(Giants_Experimental_Tool_Fragment, -1);
									st.takeItems(Giants_Experimental_Tool, -1);
									st.takeItems(Giants_Technology_Report, -1);
									st.set("Report", "1");
									return "scroll_seller_rouke_q0134_06.htm";
								}
								return "noquest";
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
			if (npc.getId() == Cruma_Marshlands_Traitor)
			{
				qs.giveItems(Giants_Technology_Report, 1);
				
				if (qs.getQuestItemsCount(Giants_Technology_Report) < 3)
				{
					qs.playSound(SOUND_ITEMGET);
				}
				else
				{
					qs.playSound(SOUND_MIDDLE);
					qs.setCond(4);
				}
			}
			else if (qs.getQuestItemsCount(Giants_Experimental_Tool) < 1)
			{
				if (Rnd.chance(Giants_Experimental_Tool_Fragment_chance))
				{
					qs.giveItems(Giants_Experimental_Tool_Fragment, 1);
				}
			}
			else
			{
				qs.takeItems(Giants_Experimental_Tool, 1);
				
				if (Rnd.chance(Cruma_Marshlands_Traitor_spawnchance))
				{
					qs.addSpawn(Cruma_Marshlands_Traitor, qs.getPlayer().getX(), qs.getPlayer().getY(), qs.getPlayer().getZ(), 0, 100, 900000);
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
