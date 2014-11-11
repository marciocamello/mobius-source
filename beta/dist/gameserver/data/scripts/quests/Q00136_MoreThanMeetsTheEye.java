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

public class Q00136_MoreThanMeetsTheEye extends Quest implements ScriptFile
{
	// Npcs
	private static final int HARDIN = 30832;
	private static final int ERRICKIN = 30701;
	private static final int CLAYTON = 30464;
	// Items
	private static final int TransformSealbook = 9648;
	private static final int Ectoplasm = 9787;
	private static final int StabilizedEctoplasm = 9786;
	private static final int HardinsInstructions = 9788;
	private static final int GlassJaguarCrystal = 9789;
	private static final int BlankSealbook = 9790;
	private static final int[][] DROPLIST_COND =
	{
		{
			3,
			4,
			20636,
			0,
			Ectoplasm,
			35,
			100,
			1
		},
		{
			3,
			4,
			20637,
			0,
			Ectoplasm,
			35,
			100,
			1
		},
		{
			3,
			4,
			20638,
			0,
			Ectoplasm,
			35,
			100,
			1
		},
		{
			3,
			4,
			20639,
			0,
			Ectoplasm,
			35,
			100,
			2
		},
		{
			7,
			8,
			20250,
			0,
			GlassJaguarCrystal,
			5,
			100,
			1
		}
	};
	
	public Q00136_MoreThanMeetsTheEye()
	{
		super(false);
		addStartNpc(HARDIN);
		addTalkId(HARDIN, ERRICKIN, CLAYTON);
		addQuestItem(StabilizedEctoplasm, HardinsInstructions, BlankSealbook, Ectoplasm, GlassJaguarCrystal);
		
		for (int[] element : DROPLIST_COND)
		{
			addKillId(element[2]);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "hardin_q0136_08.htm":
				qs.setCond(2);
				qs.set("id", "0");
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "magister_errickin_q0136_03.htm":
				qs.setCond(3);
				qs.setState(STARTED);
				break;
			
			case "hardin_q0136_16.htm":
				qs.giveItems(HardinsInstructions, 1);
				qs.setCond(6);
				qs.setState(STARTED);
				break;
			
			case "magister_clayton_q0136_10.htm":
				qs.setCond(7);
				qs.setState(STARTED);
				break;
			
			case "hardin_q0136_23.htm":
				qs.playSound(SOUND_FINISH);
				qs.giveItems(TransformSealbook, 1);
				qs.giveItems(ADENA_ID, 67550, true);
				qs.unset("id");
				qs.unset("cond");
				qs.exitCurrentQuest(false);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case HARDIN:
				switch (cond)
				{
					case 0:
						if (qs.getPlayer().getLevel() >= 50)
						{
							qs.setCond(1);
							htmltext = "hardin_q0136_01.htm";
						}
						else
						{
							htmltext = "hardin_q0136_02.htm";
							qs.exitCurrentQuest(true);
						}
						break;
					
					case 2:
					case 3:
					case 4:
						htmltext = "hardin_q0136_09.htm";
						break;
					
					case 5:
						qs.takeItems(StabilizedEctoplasm, -1);
						htmltext = "hardin_q0136_10.htm";
						break;
					
					case 6:
						htmltext = "hardin_q0136_17.htm";
						break;
					
					case 9:
						qs.takeItems(BlankSealbook, -1);
						htmltext = "hardin_q0136_18.htm";
						break;
				}
				break;
			
			case ERRICKIN:
				switch (cond)
				{
					case 2:
						htmltext = "magister_errickin_q0136_02.htm";
						break;
					
					case 3:
						htmltext = "magister_errickin_q0136_03.htm";
						break;
					
					case 4:
						if ((qs.getQuestItemsCount(Ectoplasm) < 35) && (qs.getInt("id") == 0))
						{
							qs.setCond(3);
							htmltext = "magister_errickin_q0136_03.htm";
						}
						else if (qs.getInt("id") == 0)
						{
							qs.takeItems(Ectoplasm, -1);
							htmltext = "magister_errickin_q0136_05.htm";
							qs.set("id", "1");
						}
						else if (qs.getInt("id") == 1)
						{
							htmltext = "magister_errickin_q0136_06.htm";
							qs.giveItems(StabilizedEctoplasm, 1);
							qs.set("id", "0");
							qs.setCond(5);
							qs.setState(STARTED);
						}
						break;
					
					case 5:
						htmltext = "magister_errickin_q0136_07.htm";
						break;
				}
				break;
			
			case CLAYTON:
				switch (cond)
				{
					case 6:
						qs.takeItems(HardinsInstructions, -1);
						htmltext = "magister_clayton_q0136_09.htm";
						break;
					
					case 7:
						htmltext = "magister_clayton_q0136_12.htm";
						break;
					
					case 8:
						if (qs.getQuestItemsCount(GlassJaguarCrystal) < 5)
						{
							htmltext = "magister_clayton_q0136_12.htm";
							qs.setCond(7);
						}
						else
						{
							htmltext = "magister_clayton_q0136_13.htm";
							qs.takeItems(GlassJaguarCrystal, -1);
							qs.giveItems(BlankSealbook, 1);
							qs.setCond(9);
							qs.setState(STARTED);
						}
						break;
					
					case 9:
						htmltext = "magister_clayton_q0136_14.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		
		for (int[] element : DROPLIST_COND)
		{
			if ((cond == element[0]) && (npcId == element[2]))
			{
				if ((element[3] == 0) || (qs.getQuestItemsCount(element[3]) > 0))
				{
					long count = qs.getQuestItemsCount(element[4]);
					
					if ((element[5] > count) && Rnd.chance(element[6]))
					{
						long random = 0;
						
						if (element[7] > 1)
						{
							random = Rnd.get(element[7]) + 1;
							
							if ((count + random) > element[5])
							{
								random = element[5] - count;
							}
						}
						else
						{
							random = 1;
						}
						
						if (cond == 3)
						{
							if (random == 1)
							{
								if (Rnd.chance(15))
								{
									random = 2;
								}
							}
							else if (Rnd.chance(15))
							{
								random = 3;
							}
							
							if ((count + random) > element[5])
							{
								random = element[5] - count;
							}
						}
						
						qs.giveItems(element[4], random);
						
						if ((count + random) == element[5])
						{
							qs.playSound(SOUND_MIDDLE);
							
							if (element[1] != 0)
							{
								qs.setCond(Integer.valueOf(element[1]));
								qs.setState(STARTED);
							}
						}
						else
						{
							qs.playSound(SOUND_ITEMGET);
						}
					}
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
