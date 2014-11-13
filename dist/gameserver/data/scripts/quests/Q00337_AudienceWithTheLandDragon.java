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

public class Q00337_AudienceWithTheLandDragon extends Quest implements ScriptFile
{
	public final int MOKE = 30498;
	public final int HELTON = 30678;
	public final int CHAKIRIS = 30705;
	public final int KAIENA = 30720;
	public final int GABRIELLE = 30753;
	public final int GILMORE = 30754;
	public final int THEODRIC = 30755;
	public final int KENDRA = 30851;
	public final int ORVEN = 30857;
	public final int MARSH_STALKER = 20679;
	public final int MARSH_DRAKE = 20680;
	public final int BLOOD_QUEEN = 18001;
	public final int HARIT_LIZARDMAN_SHAMAN = 20644;
	public final int HARIT_LIZARDMAN_MATRIARCH = 20645;
	public final int HAMRUT = 20649;
	public final int KRANROT = 20650;
	public final int CAVE_MAIDEN = 20134;
	public final int CAVE_KEEPER = 20246;
	public final int ABYSSAL_JEWEL_1 = 27165;
	public final int ABYSSAL_JEWEL_2 = 27166;
	public final int ABYSSAL_JEWEL_3 = 27167;
	public final int JEWEL_GUARDIAN_MARA = 27168;
	public final int JEWEL_GUARDIAN_MUSFEL = 27169;
	public final int JEWEL_GUARDIAN_PYTON = 27170;
	public final int SACRIFICE_OF_THE_SACRIFICED = 27171;
	public final int HARIT_LIZARDMAN_ZEALOT = 27172;
	public final int FEATHER_OF_GABRIELLE_ID = 3852;
	public final int STALKER_HORN_ID = 3853;
	public final int DRAKE_TALON_ID = 3854;
	public final int REMAINS_OF_SACRIFICED_ID = 3857;
	public final int TOTEM_OF_LAND_DRAGON_ID = 3858;
	public final int HAMRUT_LEG_ID = 3856;
	public final int KRANROT_SKIN_ID = 3855;
	public final int MARA_FANG_ID = 3862;
	public final int MUSFEL_FANG_ID = 3863;
	public final int FIRST_ABYSS_FRAGMENT_ID = 3859;
	public final int SECOND_ABYSS_FRAGMENT_ID = 3860;
	public final int THIRD_ABYSS_FRAGMENT_ID = 3861;
	public final int HERALD_OF_SLAYER_ID = 3890;
	public final int PORTAL_STONE_ID = 3865;
	public final int MARK_OF_WATCHMAN_ID = 3864;
	public final int[][] DROPLIST =
	{
		{
			2,
			MARSH_STALKER,
			STALKER_HORN_ID,
			1,
			50,
			1
		},
		{
			2,
			MARSH_DRAKE,
			DRAKE_TALON_ID,
			1,
			50,
			1
		},
		{
			4,
			SACRIFICE_OF_THE_SACRIFICED,
			REMAINS_OF_SACRIFICED_ID,
			1,
			50,
			1
		},
		{
			6,
			HARIT_LIZARDMAN_ZEALOT,
			TOTEM_OF_LAND_DRAGON_ID,
			1,
			50,
			1
		},
		{
			8,
			HAMRUT,
			HAMRUT_LEG_ID,
			1,
			50,
			1
		},
		{
			8,
			KRANROT,
			KRANROT_SKIN_ID,
			1,
			50,
			1
		},
		{
			11,
			JEWEL_GUARDIAN_MARA,
			MARA_FANG_ID,
			1,
			50,
			1
		},
		{
			11,
			ABYSSAL_JEWEL_1,
			FIRST_ABYSS_FRAGMENT_ID,
			1,
			100,
			1
		},
		{
			13,
			JEWEL_GUARDIAN_MUSFEL,
			MUSFEL_FANG_ID,
			1,
			50,
			1
		},
		{
			13,
			ABYSSAL_JEWEL_2,
			SECOND_ABYSS_FRAGMENT_ID,
			1,
			100,
			1
		},
		{
			16,
			ABYSSAL_JEWEL_3,
			THIRD_ABYSS_FRAGMENT_ID,
			1,
			100,
			1
		},
	};
	public final int[][] SPAWNLIST =
	{
		{
			4,
			BLOOD_QUEEN,
			SACRIFICE_OF_THE_SACRIFICED,
			6
		},
		{
			6,
			HARIT_LIZARDMAN_SHAMAN,
			HARIT_LIZARDMAN_ZEALOT,
			1
		},
		{
			6,
			HARIT_LIZARDMAN_MATRIARCH,
			HARIT_LIZARDMAN_ZEALOT,
			1
		},
		{
			11,
			ABYSSAL_JEWEL_1,
			JEWEL_GUARDIAN_MARA,
			4
		},
		{
			13,
			ABYSSAL_JEWEL_2,
			JEWEL_GUARDIAN_MUSFEL,
			4
		},
		{
			16,
			CAVE_KEEPER,
			ABYSSAL_JEWEL_3,
			1
		},
		{
			16,
			CAVE_MAIDEN,
			ABYSSAL_JEWEL_3,
			1
		},
		{
			16,
			ABYSSAL_JEWEL_3,
			JEWEL_GUARDIAN_PYTON,
			6
		},
	};
	
	public Q00337_AudienceWithTheLandDragon()
	{
		super(false);
		addStartNpc(GABRIELLE);
		addTalkId(MOKE, HELTON, CHAKIRIS, KAIENA, GILMORE, THEODRIC, KENDRA, ORVEN);
		addKillId(BLOOD_QUEEN, MARSH_STALKER, MARSH_DRAKE, SACRIFICE_OF_THE_SACRIFICED, HARIT_LIZARDMAN_SHAMAN, HARIT_LIZARDMAN_MATRIARCH, HARIT_LIZARDMAN_ZEALOT, HAMRUT, KRANROT, ABYSSAL_JEWEL_1, ABYSSAL_JEWEL_2, CAVE_KEEPER, CAVE_MAIDEN, ABYSSAL_JEWEL_3, JEWEL_GUARDIAN_MARA, JEWEL_GUARDIAN_MUSFEL, JEWEL_GUARDIAN_PYTON);
		addAttackId(ABYSSAL_JEWEL_1, ABYSSAL_JEWEL_2, ABYSSAL_JEWEL_3);
		addQuestItem(FEATHER_OF_GABRIELLE_ID, HERALD_OF_SLAYER_ID, STALKER_HORN_ID, DRAKE_TALON_ID, REMAINS_OF_SACRIFICED_ID, TOTEM_OF_LAND_DRAGON_ID, HAMRUT_LEG_ID, KRANROT_SKIN_ID, MARA_FANG_ID, FIRST_ABYSS_FRAGMENT_ID, MUSFEL_FANG_ID, SECOND_ABYSS_FRAGMENT_ID, THIRD_ABYSS_FRAGMENT_ID, MARK_OF_WATCHMAN_ID);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "1":
				qs.set("step", "1");
				qs.setCond(1);
				qs.set("guard", "0");
				qs.setState(STARTED);
				qs.giveItems(FEATHER_OF_GABRIELLE_ID, 1);
				htmltext = "30753-02.htm";
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "2":
				qs.set("step", "2");
				htmltext = "30720-02.htm";
				break;
			
			case "4":
				qs.set("step", "4");
				htmltext = "30857-02.htm";
				break;
			
			case "6":
				qs.set("step", "6");
				htmltext = "30851-02.htm";
				break;
			
			case "8":
				qs.set("step", "8");
				htmltext = "30705-02.htm";
				break;
			
			case "10":
				qs.takeItems(MARK_OF_WATCHMAN_ID, -1);
				qs.set("step", "10");
				qs.setCond(2);
				htmltext = "30753-05.htm";
				break;
			
			case "11":
				qs.set("step", "11");
				htmltext = "30498-02.htm";
				break;
			
			case "13":
				qs.set("step", "13");
				htmltext = "30678-02.htm";
				break;
			
			case "15":
				qs.set("step", "15");
				qs.setCond(3);
				htmltext = "30753-06.htm";
				qs.takeItems(MARK_OF_WATCHMAN_ID, -1);
				qs.takeItems(FEATHER_OF_GABRIELLE_ID, -1);
				qs.giveItems(HERALD_OF_SLAYER_ID, 1);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "16":
				qs.set("step", "16");
				qs.setCond(4);
				htmltext = "30754-02.htm";
				qs.takeItems(HERALD_OF_SLAYER_ID, -1);
				qs.playSound(SOUND_MIDDLE);
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
		final int step = qs.getInt("step");
		
		switch (npcId)
		{
			case GABRIELLE:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() < 50)
					{
						htmltext = "30753-00.htm";
						qs.exitCurrentQuest(true);
					}
					else
					{
						htmltext = "30753-01.htm";
					}
				}
				else if (step < 9)
				{
					htmltext = "30753-02.htm";
				}
				else if (step == 9)
				{
					htmltext = "30753-03.htm";
				}
				else if ((step > 9) && (step < 14))
				{
					htmltext = "30753-05.htm";
				}
				else if (step == 14)
				{
					htmltext = "30753-04.htm";
				}
				else if (step > 14)
				{
					htmltext = "30753-06.htm";
				}
				break;
			
			case KAIENA:
				if ((cond == 1) && (step < 4))
				{
					if ((qs.getQuestItemsCount(STALKER_HORN_ID) < 1) && (qs.getQuestItemsCount(DRAKE_TALON_ID) < 1) && (step == 1))
					{
						htmltext = "30720-01.htm";
					}
					else if ((qs.getQuestItemsCount(STALKER_HORN_ID) > 0) && (qs.getQuestItemsCount(DRAKE_TALON_ID) > 0))
					{
						htmltext = "30720-03.htm";
						qs.takeItems(STALKER_HORN_ID, -1);
						qs.takeItems(DRAKE_TALON_ID, -1);
						qs.giveItems(MARK_OF_WATCHMAN_ID, 1);
						qs.set("step", "3");
						qs.playSound(SOUND_MIDDLE);
					}
					else if (step == 2)
					{
						htmltext = "30720-02.htm";
					}
					else if (step == 3)
					{
						htmltext = "30720-03.htm";
					}
				}
				break;
			
			case ORVEN:
				if ((cond == 1) && (step > 2) && (step < 6))
				{
					if ((qs.getQuestItemsCount(REMAINS_OF_SACRIFICED_ID) < 1) && (step == 3))
					{
						htmltext = "30857-01.htm";
					}
					else if (qs.getQuestItemsCount(REMAINS_OF_SACRIFICED_ID) > 0)
					{
						htmltext = "30857-03.htm";
						qs.takeItems(REMAINS_OF_SACRIFICED_ID, -1);
						qs.giveItems(MARK_OF_WATCHMAN_ID, 1);
						qs.set("step", "5");
						qs.playSound(SOUND_MIDDLE);
					}
					else if (step == 4)
					{
						htmltext = "30857-02.htm";
					}
					else if (step == 5)
					{
						htmltext = "30857-03.htm";
					}
				}
				break;
			
			case KENDRA:
				if ((cond == 1) && (step > 4) && (step < 8))
				{
					if ((qs.getQuestItemsCount(TOTEM_OF_LAND_DRAGON_ID) < 1) && (step == 5))
					{
						htmltext = "30851-01.htm";
					}
					else if (qs.getQuestItemsCount(TOTEM_OF_LAND_DRAGON_ID) > 0)
					{
						htmltext = "30851-03.htm";
						qs.takeItems(TOTEM_OF_LAND_DRAGON_ID, -1);
						qs.giveItems(MARK_OF_WATCHMAN_ID, 1);
						qs.set("step", "7");
						qs.playSound(SOUND_MIDDLE);
					}
					else if (step == 6)
					{
						htmltext = "30851-02.htm";
					}
					else if (step == 7)
					{
						htmltext = "30851-03.htm";
					}
				}
				break;
			
			case CHAKIRIS:
				if ((cond == 1) && (step > 6) && (step < 10))
				{
					if ((qs.getQuestItemsCount(HAMRUT_LEG_ID) < 1) && (qs.getQuestItemsCount(KRANROT_SKIN_ID) < 1) && (step == 7))
					{
						htmltext = "30705-01.htm";
					}
					else if ((qs.getQuestItemsCount(HAMRUT_LEG_ID) > 0) && (qs.getQuestItemsCount(KRANROT_SKIN_ID) > 0))
					{
						htmltext = "30705-03.htm";
						qs.takeItems(HAMRUT_LEG_ID, -1);
						qs.takeItems(KRANROT_SKIN_ID, -1);
						qs.giveItems(MARK_OF_WATCHMAN_ID, 1);
						qs.set("step", "9");
						qs.playSound(SOUND_MIDDLE);
					}
					else if (step == 8)
					{
						htmltext = "30705-02.htm";
					}
					else if (step == 9)
					{
						htmltext = "30705-03.htm";
					}
				}
				break;
			
			case MOKE:
				if ((cond == 2) && (step < 13))
				{
					if ((qs.getQuestItemsCount(MARA_FANG_ID) < 1) && (qs.getQuestItemsCount(FIRST_ABYSS_FRAGMENT_ID) < 1) && (step == 10))
					{
						htmltext = "30498-01.htm";
					}
					else if ((qs.getQuestItemsCount(MARA_FANG_ID) > 0) && (qs.getQuestItemsCount(FIRST_ABYSS_FRAGMENT_ID) > 0))
					{
						htmltext = "30498-03.htm";
						qs.takeItems(MARA_FANG_ID, -1);
						qs.takeItems(FIRST_ABYSS_FRAGMENT_ID, -1);
						qs.giveItems(MARK_OF_WATCHMAN_ID, 1);
						qs.set("step", "12");
						qs.playSound(SOUND_MIDDLE);
					}
					else if (step == 11)
					{
						htmltext = "30498-02.htm";
					}
					else if (step == 12)
					{
						htmltext = "30498-03.htm";
					}
				}
				break;
			
			case HELTON:
				if ((cond == 2) && (step > 11) && (step < 15))
				{
					if ((qs.getQuestItemsCount(MUSFEL_FANG_ID) < 1) && (qs.getQuestItemsCount(SECOND_ABYSS_FRAGMENT_ID) < 1) && (step == 12))
					{
						htmltext = "30678-01.htm";
					}
					else if ((qs.getQuestItemsCount(MUSFEL_FANG_ID) > 0) && (qs.getQuestItemsCount(SECOND_ABYSS_FRAGMENT_ID) > 0))
					{
						htmltext = "30678-03.htm";
						qs.takeItems(MUSFEL_FANG_ID, -1);
						qs.takeItems(SECOND_ABYSS_FRAGMENT_ID, -1);
						qs.giveItems(MARK_OF_WATCHMAN_ID, 1);
						qs.set("step", "14");
						qs.playSound(SOUND_MIDDLE);
					}
					else if (step == 13)
					{
						htmltext = "30678-02.htm";
					}
					else if (step == 14)
					{
						htmltext = "30678-03.htm";
					}
				}
				break;
			
			case GILMORE:
				if (step < 17)
				{
					if ((qs.getQuestItemsCount(HERALD_OF_SLAYER_ID) > 0) && (cond == 3))
					{
						htmltext = "30754-01.htm";
					}
					else if (cond == 4)
					{
						htmltext = "30754-02.htm";
					}
				}
				break;
			
			case THEODRIC:
				if ((cond == 4) && (step == 16))
				{
					if (qs.getQuestItemsCount(THIRD_ABYSS_FRAGMENT_ID) < 1)
					{
						htmltext = "30755-02.htm";
					}
					else
					{
						htmltext = "30755-01.htm";
						qs.takeItems(THIRD_ABYSS_FRAGMENT_ID, -1);
						qs.unset("step");
						qs.unset("cond");
						qs.unset("guard");
						qs.exitCurrentQuest(true);
						qs.giveItems(PORTAL_STONE_ID, 1);
						qs.playSound(SOUND_FINISH);
					}
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onAttack(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int step = qs.getInt("step");
		
		for (int[] element : SPAWNLIST)
		{
			if ((npcId == element[1]) && (step == element[0]) && (npc.getCurrentHpPercents() < 50) && (qs.getInt("guard") == 0))
			{
				for (int j = 0; j < element[3]; j++)
				{
					qs.addSpawn(element[2]);
				}
				
				qs.playSound(SOUND_BEFORE_BATTLE);
				qs.set("guard", "1");
			}
		}
		
		return null;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int step = qs.getInt("step");
		
		for (int[] element : DROPLIST)
		{
			if ((npcId == element[1]) && (step == element[0]) && (qs.getQuestItemsCount(element[2]) < element[3]) && Rnd.chance(element[4]))
			{
				qs.giveItems(element[2], element[5]);
				qs.playSound(SOUND_ITEMGET);
			}
		}
		
		for (int[] element : SPAWNLIST)
		{
			if ((step == element[0]) && (npcId == element[1]) && Rnd.chance(50) && (qs.getInt("guard") == 0))
			{
				for (int j = 0; j < element[3]; j++)
				{
					qs.addSpawn(element[2]);
				}
				
				qs.playSound(SOUND_BEFORE_BATTLE);
			}
			
			if ((step == element[0]) && (npcId == element[1]) && (qs.getInt("guard") == 1))
			{
				qs.set("guard", "0");
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
