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

public class Q00420_LittleWing extends Quest implements ScriptFile
{
	private static final int Cooper = 30829;
	private static final int Cronos = 30610;
	private static final int Byron = 30711;
	private static final int Maria = 30608;
	private static final int Mimyu = 30747;
	private static final int Exarion = 30748;
	private static final int Zwov = 30749;
	private static final int Kalibran = 30750;
	private static final int Suzet = 30751;
	private static final int Shamhai = 30752;
	private static final int Enchanted_Valey_First = 20589;
	private static final int Enchanted_Valey_Last = 20599;
	private static final int Toad_Lord = 20231;
	private static final int Marsh_Spider = 20233;
	private static final int Leto_Lizardman_Warrior = 20580;
	private static final int Road_Scavenger = 20551;
	private static final int Breka_Orc_Overlord = 20270;
	private static final int Dead_Seeker = 20202;
	private static final int Coal = 1870;
	private static final int Charcoal = 1871;
	private static final int Silver_Nugget = 1873;
	private static final int Stone_of_Purity = 1875;
	private static final int GemstoneD = 2130;
	private static final int GemstoneC = 2131;
	private static final int Dragonflute_of_Wind = 3500;
	private static final int Dragonflute_of_Twilight = 3502;
	private static final int Hatchlings_Soft_Leather = 3912;
	private static final int Hatchlings_Mithril_Coat = 3918;
	private static final int Food_For_Hatchling = 4038;
	private static final int Fairy_Dust = 3499;
	private static final int Fairy_Stone = 3816;
	private static final int Deluxe_Fairy_Stone = 3817;
	private static final int Fairy_Stone_List = 3818;
	private static final int Deluxe_Fairy_Stone_List = 3819;
	private static final int Toad_Lord_Back_Skin = 3820;
	private static final int Juice_of_Monkshood = 3821;
	private static final int Scale_of_Drake_Exarion = 3822;
	private static final int Scale_of_Drake_Zwov = 3824;
	private static final int Scale_of_Drake_Kalibran = 3826;
	private static final int Scale_of_Wyvern_Suzet = 3828;
	private static final int Scale_of_Wyvern_Shamhai = 3830;
	private static final int Egg_of_Drake_Exarion = 3823;
	private static final int Egg_of_Drake_Zwov = 3825;
	private static final int Egg_of_Drake_Kalibran = 3827;
	private static final int Egg_of_Wyvern_Suzet = 3829;
	private static final int Egg_of_Wyvern_Shamhai = 3831;
	private static final int Toad_Lord_Back_Skin_Chance = 30;
	private static final int Egg_Chance = 50;
	private static final int Pet_Armor_Chance = 35;
	private static final int[][] Fairy_Stone_Items =
	{
		{
			Coal,
			10
		},
		{
			Charcoal,
			10
		},
		{
			GemstoneD,
			1
		},
		{
			Silver_Nugget,
			3
		},
		{
			Toad_Lord_Back_Skin,
			10
		}
	};
	private static final int[][] Delux_Fairy_Stone_Items =
	{
		{
			Coal,
			10
		},
		{
			Charcoal,
			10
		},
		{
			GemstoneC,
			1
		},
		{
			Stone_of_Purity,
			1
		},
		{
			Silver_Nugget,
			5
		},
		{
			Toad_Lord_Back_Skin,
			20
		}
	};
	private static final int[][] wyrms =
	{
		{
			Leto_Lizardman_Warrior,
			Exarion,
			Scale_of_Drake_Exarion,
			Egg_of_Drake_Exarion
		},
		{
			Marsh_Spider,
			Zwov,
			Scale_of_Drake_Zwov,
			Egg_of_Drake_Zwov
		},
		{
			Road_Scavenger,
			Kalibran,
			Scale_of_Drake_Kalibran,
			Egg_of_Drake_Kalibran
		},
		{
			Breka_Orc_Overlord,
			Suzet,
			Scale_of_Wyvern_Suzet,
			Egg_of_Wyvern_Suzet
		},
		{
			Dead_Seeker,
			Shamhai,
			Scale_of_Wyvern_Shamhai,
			Egg_of_Wyvern_Shamhai
		}
	};
	
	public Q00420_LittleWing()
	{
		super(false);
		addStartNpc(Cooper);
		addTalkId(Cronos, Mimyu, Byron, Maria);
		addQuestItem(Fairy_Dust, Fairy_Stone, Deluxe_Fairy_Stone, Fairy_Stone_List, Deluxe_Fairy_Stone_List, Toad_Lord_Back_Skin, Juice_of_Monkshood, Scale_of_Drake_Exarion, Scale_of_Drake_Zwov, Scale_of_Drake_Kalibran, Scale_of_Wyvern_Suzet, Scale_of_Wyvern_Shamhai, Egg_of_Drake_Exarion, Egg_of_Drake_Zwov, Egg_of_Drake_Kalibran, Egg_of_Wyvern_Suzet, Egg_of_Wyvern_Shamhai);
		addKillId(Toad_Lord);
		
		for (int Enchanted_Valey_id = Enchanted_Valey_First; Enchanted_Valey_id <= Enchanted_Valey_Last; Enchanted_Valey_id++)
		{
			addKillId(Enchanted_Valey_id);
		}
		
		for (int[] wyrm : wyrms)
		{
			addTalkId(wyrm[1]);
			addKillId(wyrm[0]);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final int _state = qs.getState();
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "30829-02.htm":
				if (_state == CREATED)
				{
					qs.setState(STARTED);
					qs.setCond(1);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "30610-05.htm":
			case "30610-12.htm":
				if ((_state == STARTED) && (cond == 1))
				{
					qs.setCond(2);
					qs.takeItems(Fairy_Stone, -1);
					qs.takeItems(Deluxe_Fairy_Stone, -1);
					qs.takeItems(Fairy_Stone_List, -1);
					qs.takeItems(Deluxe_Fairy_Stone_List, -1);
					qs.giveItems(Fairy_Stone_List, 1);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "30610-06.htm":
			case "30610-13.htm":
				if ((_state == STARTED) && (cond == 1))
				{
					qs.setCond(2);
					qs.takeItems(Fairy_Stone, -1);
					qs.takeItems(Deluxe_Fairy_Stone, -1);
					qs.takeItems(Fairy_Stone_List, -1);
					qs.takeItems(Deluxe_Fairy_Stone_List, -1);
					qs.giveItems(Deluxe_Fairy_Stone_List, 1);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "30608-03.htm":
				if ((_state == STARTED) && (cond == 2) && (qs.getQuestItemsCount(Fairy_Stone_List) > 0))
				{
					if (CheckFairyStoneItems(qs, Fairy_Stone_Items))
					{
						qs.setCond(3);
						TakeFairyStoneItems(qs, Fairy_Stone_Items);
						qs.giveItems(Fairy_Stone, 1);
						qs.playSound(SOUND_MIDDLE);
					}
					else
					{
						return "30608-01.htm";
					}
				}
				break;
			
			case "30608-03a.htm":
				if ((_state == STARTED) && (cond == 2) && (qs.getQuestItemsCount(Deluxe_Fairy_Stone_List) > 0))
				{
					if (CheckFairyStoneItems(qs, Delux_Fairy_Stone_Items))
					{
						qs.setCond(3);
						TakeFairyStoneItems(qs, Delux_Fairy_Stone_Items);
						qs.giveItems(Deluxe_Fairy_Stone, 1);
						qs.playSound(SOUND_MIDDLE);
					}
					else
					{
						return "30608-01a.htm";
					}
				}
				break;
			
			case "30711-03.htm":
				if ((_state == STARTED) && (cond == 3) && ((qs.getQuestItemsCount(Fairy_Stone) + qs.getQuestItemsCount(Deluxe_Fairy_Stone)) > 0))
				{
					qs.setCond(4);
					qs.playSound(SOUND_MIDDLE);
					
					if (qs.getQuestItemsCount(Deluxe_Fairy_Stone) > 0)
					{
						return qs.getInt("broken") == 1 ? "30711-04a.htm" : "30711-03a.htm";
					}
					
					if (qs.getInt("broken") == 1)
					{
						return "30711-04.htm";
					}
				}
				break;
			
			case "30747-02.htm":
				if ((_state == STARTED) && (cond == 4) && (qs.getQuestItemsCount(Fairy_Stone) > 0))
				{
					qs.takeItems(Fairy_Stone, -1);
					qs.set("takedStone", "1");
				}
				break;
			
			case "30747-02a.htm":
				if ((_state == STARTED) && (cond == 4) && (qs.getQuestItemsCount(Deluxe_Fairy_Stone) > 0))
				{
					qs.takeItems(Deluxe_Fairy_Stone, -1);
					qs.set("takedStone", "2");
					qs.giveItems(Fairy_Dust, 1);
					qs.playSound(SOUND_ITEMGET);
				}
				break;
			
			case "30747-04.htm":
				if ((_state == STARTED) && (cond == 4) && (qs.getInt("takedStone") > 0))
				{
					qs.setCond(5);
					qs.unset("takedStone");
					qs.giveItems(Juice_of_Monkshood, 1);
					qs.playSound(SOUND_ITEMGET);
				}
				break;
			
			case "30748-02.htm":
				if ((cond == 5) && (_state == STARTED) && (qs.getQuestItemsCount(Juice_of_Monkshood) > 0))
				{
					qs.setCond(6);
					qs.takeItems(Juice_of_Monkshood, -1);
					qs.giveItems(3822, 1);
					qs.playSound(SOUND_ITEMGET);
				}
				break;
			
			case "30749-02.htm":
				if ((cond == 5) && (_state == STARTED) && (qs.getQuestItemsCount(Juice_of_Monkshood) > 0))
				{
					qs.setCond(6);
					qs.takeItems(Juice_of_Monkshood, -1);
					qs.giveItems(3824, 1);
					qs.playSound(SOUND_ITEMGET);
				}
				break;
			
			case "30750-02.htm":
				if ((cond == 5) && (_state == STARTED) && (qs.getQuestItemsCount(Juice_of_Monkshood) > 0))
				{
					qs.setCond(6);
					qs.takeItems(Juice_of_Monkshood, -1);
					qs.giveItems(3826, 1);
					qs.playSound(SOUND_ITEMGET);
				}
				break;
			
			case "30751-02.htm":
				if ((cond == 5) && (_state == STARTED) && (qs.getQuestItemsCount(Juice_of_Monkshood) > 0))
				{
					qs.setCond(6);
					qs.takeItems(Juice_of_Monkshood, -1);
					qs.giveItems(3828, 1);
					qs.playSound(SOUND_ITEMGET);
				}
				break;
			
			case "30752-02.htm":
				if ((cond == 5) && (_state == STARTED) && (qs.getQuestItemsCount(Juice_of_Monkshood) > 0))
				{
					qs.setCond(6);
					qs.takeItems(Juice_of_Monkshood, -1);
					qs.giveItems(3830, 1);
					qs.playSound(SOUND_ITEMGET);
				}
				break;
			
			case "30747-09.htm":
				if ((_state == STARTED) && (cond == 7))
				{
					int egg_id = 0;
					
					for (int[] wyrm : wyrms)
					{
						if ((qs.getQuestItemsCount(wyrm[2]) == 0) && (qs.getQuestItemsCount(wyrm[3]) >= 1))
						{
							egg_id = wyrm[3];
							break;
						}
					}
					
					if (egg_id == 0)
					{
						return "noquest";
					}
					
					qs.takeItems(egg_id, -1);
					qs.giveItems(Rnd.get(Dragonflute_of_Wind, Dragonflute_of_Twilight), 1);
					
					if (qs.getQuestItemsCount(Fairy_Dust) > 0)
					{
						qs.playSound(SOUND_MIDDLE);
						return "30747-09a.htm";
					}
					
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
				}
				break;
			
			case "30747-10.htm":
				if ((_state == STARTED) && (cond == 7))
				{
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
				}
				break;
			
			case "30747-11.htm":
				if ((_state == STARTED) && (cond == 7))
				{
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
					
					if (qs.getQuestItemsCount(Fairy_Dust) == 0)
					{
						return "30747-10.htm";
					}
					
					qs.takeItems(Fairy_Dust, -1);
					
					if (Rnd.chance(Pet_Armor_Chance))
					{
						int armor_id = Hatchlings_Soft_Leather + Rnd.get((int) qs.getRateQuestsReward());
						
						if (armor_id > Hatchlings_Mithril_Coat)
						{
							armor_id = Hatchlings_Mithril_Coat;
						}
						
						qs.giveItems(armor_id, 1);
					}
					else
					{
						qs.giveItems(Food_For_Hatchling, 20, true);
					}
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
		
		if (_state == CREATED)
		{
			if (npcId != Cooper)
			{
				return "noquest";
			}
			
			if (qs.getPlayer().getLevel() < 35)
			{
				qs.exitCurrentQuest(true);
				return "30829-00.htm";
			}
			
			qs.setCond(0);
			return "30829-01.htm";
		}
		
		if (_state != STARTED)
		{
			return "noquest";
		}
		
		final int cond = qs.getCond();
		final int broken = qs.getInt("broken");
		
		switch (npcId)
		{
			case Cooper:
				if (cond == 1)
				{
					return "30829-02.htm";
				}
				return "30829-03.htm";
				
			case Cronos:
				if (cond == 1)
				{
					return broken == 1 ? "30610-10.htm" : "30610-01.htm";
				}
				else if (cond == 2)
				{
					return "30610-07.htm";
				}
				else if (cond == 3)
				{
					return broken == 1 ? "30610-14.htm" : "30610-08.htm";
				}
				else if (cond == 4)
				{
					return "30610-09.htm";
				}
				else if (cond > 4)
				{
					return "30610-11.htm";
				}
				break;
			
			case Maria:
				if (cond == 2)
				{
					if (qs.getQuestItemsCount(Deluxe_Fairy_Stone_List) > 0)
					{
						return CheckFairyStoneItems(qs, Delux_Fairy_Stone_Items) ? "30608-02a.htm" : "30608-01a.htm";
					}
					
					if (qs.getQuestItemsCount(Fairy_Stone_List) > 0)
					{
						return CheckFairyStoneItems(qs, Fairy_Stone_Items) ? "30608-02.htm" : "30608-01.htm";
					}
				}
				else if (cond > 2)
				{
					return "30608-04.htm";
				}
				break;
			
			case Byron:
				if ((cond == 1) && (broken == 1))
				{
					return "30711-06.htm";
				}
				else if ((cond == 2) && (broken == 1))
				{
					return "30711-07.htm";
				}
				else if ((cond == 3) && ((qs.getQuestItemsCount(Fairy_Stone) + qs.getQuestItemsCount(Deluxe_Fairy_Stone)) > 0))
				{
					return "30711-01.htm";
				}
				else if ((cond >= 4) && (qs.getQuestItemsCount(Deluxe_Fairy_Stone) > 0))
				{
					return "30711-05a.htm";
				}
				else if ((cond >= 4) && (qs.getQuestItemsCount(Fairy_Stone) > 0))
				{
					return "30711-05.htm";
				}
				break;
			
			case Mimyu:
				if ((cond == 4) && (qs.getQuestItemsCount(Deluxe_Fairy_Stone) > 0))
				{
					return "30747-01a.htm";
				}
				else if ((cond == 4) && (qs.getQuestItemsCount(Fairy_Stone) > 0))
				{
					return "30747-01.htm";
				}
				else if (cond == 5)
				{
					return "30747-05.htm";
				}
				else if (cond == 6)
				{
					for (int[] wyrm : wyrms)
					{
						if ((qs.getQuestItemsCount(wyrm[2]) == 0) && (qs.getQuestItemsCount(wyrm[3]) >= 20))
						{
							return "30747-07.htm";
						}
					}
					
					return "30747-06.htm";
				}
				else if (cond == 7)
				{
					for (int[] wyrm : wyrms)
					{
						if ((qs.getQuestItemsCount(wyrm[2]) == 0) && (qs.getQuestItemsCount(wyrm[3]) >= 1))
						{
							return "30747-08.htm";
						}
					}
				}
				break;
			
			case Exarion:
				if ((cond == 5) && (qs.getQuestItemsCount(Juice_of_Monkshood) > 0))
				{
					return String.valueOf(npcId) + "-01.htm";
				}
				else if ((cond == 6) && (qs.getQuestItemsCount(getWyrmScale(npcId)) > 0))
				{
					int egg_id = getWyrmEgg(npcId);
					
					if (qs.getQuestItemsCount(egg_id) < 20)
					{
						return String.valueOf(npcId) + "-03.htm";
					}
					
					qs.takeItems(getWyrmScale(npcId), -1);
					qs.takeItems(egg_id, -1);
					qs.giveItems(egg_id, 1);
					qs.setCond(7);
					return String.valueOf(npcId) + "-04.htm";
				}
				else if ((cond == 7) && (qs.getQuestItemsCount(getWyrmEgg(npcId)) == 1))
				{
					return String.valueOf(npcId) + "-05.htm";
				}
				break;
			
			case Zwov:
				if ((cond == 5) && (qs.getQuestItemsCount(Juice_of_Monkshood) > 0))
				{
					return String.valueOf(npcId) + "-01.htm";
				}
				else if ((cond == 6) && (qs.getQuestItemsCount(getWyrmScale(npcId)) > 0))
				{
					int egg_id = getWyrmEgg(npcId);
					
					if (qs.getQuestItemsCount(egg_id) < 20)
					{
						return String.valueOf(npcId) + "-03.htm";
					}
					
					qs.takeItems(getWyrmScale(npcId), -1);
					qs.takeItems(egg_id, -1);
					qs.giveItems(egg_id, 1);
					qs.setCond(7);
					return String.valueOf(npcId) + "-04.htm";
				}
				else if ((cond == 7) && (qs.getQuestItemsCount(getWyrmEgg(npcId)) == 1))
				{
					return String.valueOf(npcId) + "-05.htm";
				}
				break;
			
			case Kalibran:
				if ((cond == 5) && (qs.getQuestItemsCount(Juice_of_Monkshood) > 0))
				{
					return String.valueOf(npcId) + "-01.htm";
				}
				else if ((cond == 6) && (qs.getQuestItemsCount(getWyrmScale(npcId)) > 0))
				{
					int egg_id = getWyrmEgg(npcId);
					
					if (qs.getQuestItemsCount(egg_id) < 20)
					{
						return String.valueOf(npcId) + "-03.htm";
					}
					
					qs.takeItems(getWyrmScale(npcId), -1);
					qs.takeItems(egg_id, -1);
					qs.giveItems(egg_id, 1);
					qs.setCond(7);
					return String.valueOf(npcId) + "-04.htm";
				}
				else if ((cond == 7) && (qs.getQuestItemsCount(getWyrmEgg(npcId)) == 1))
				{
					return String.valueOf(npcId) + "-05.htm";
				}
				break;
			
			case Suzet:
				if ((cond == 5) && (qs.getQuestItemsCount(Juice_of_Monkshood) > 0))
				{
					return String.valueOf(npcId) + "-01.htm";
				}
				else if ((cond == 6) && (qs.getQuestItemsCount(getWyrmScale(npcId)) > 0))
				{
					int egg_id = getWyrmEgg(npcId);
					
					if (qs.getQuestItemsCount(egg_id) < 20)
					{
						return String.valueOf(npcId) + "-03.htm";
					}
					
					qs.takeItems(getWyrmScale(npcId), -1);
					qs.takeItems(egg_id, -1);
					qs.giveItems(egg_id, 1);
					qs.setCond(7);
					return String.valueOf(npcId) + "-04.htm";
				}
				else if ((cond == 7) && (qs.getQuestItemsCount(getWyrmEgg(npcId)) == 1))
				{
					return String.valueOf(npcId) + "-05.htm";
				}
				break;
			
			case Shamhai:
				if ((cond == 5) && (qs.getQuestItemsCount(Juice_of_Monkshood) > 0))
				{
					return String.valueOf(npcId) + "-01.htm";
				}
				else if ((cond == 6) && (qs.getQuestItemsCount(getWyrmScale(npcId)) > 0))
				{
					int egg_id = getWyrmEgg(npcId);
					
					if (qs.getQuestItemsCount(egg_id) < 20)
					{
						return String.valueOf(npcId) + "-03.htm";
					}
					
					qs.takeItems(getWyrmScale(npcId), -1);
					qs.takeItems(egg_id, -1);
					qs.giveItems(egg_id, 1);
					qs.setCond(7);
					return String.valueOf(npcId) + "-04.htm";
				}
				else if ((cond == 7) && (qs.getQuestItemsCount(getWyrmEgg(npcId)) == 1))
				{
					return String.valueOf(npcId) + "-05.htm";
				}
				break;
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() != STARTED)
		{
			return null;
		}
		
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		
		if ((cond == 2) && (npcId == Toad_Lord))
		{
			int needed_skins = getNeededSkins(qs);
			
			if ((qs.getQuestItemsCount(Toad_Lord_Back_Skin) < needed_skins) && Rnd.chance(Toad_Lord_Back_Skin_Chance))
			{
				qs.giveItems(Toad_Lord_Back_Skin, 1);
				qs.playSound(qs.getQuestItemsCount(Toad_Lord_Back_Skin) < needed_skins ? SOUND_ITEMGET : SOUND_MIDDLE);
			}
			
			return null;
		}
		else if ((npcId >= Enchanted_Valey_First) && (npcId <= Enchanted_Valey_Last) && (qs.getQuestItemsCount(Deluxe_Fairy_Stone) > 0))
		{
			qs.takeItems(Deluxe_Fairy_Stone, 1);
			qs.set("broken", "1");
			qs.setCond(1);
			return "You lost fairy stone deluxe!";
		}
		
		if (cond == 6)
		{
			int wyrm_id = isWyrmStoler(npcId);
			
			if ((wyrm_id > 0) && (qs.getQuestItemsCount(getWyrmScale(wyrm_id)) > 0) && (qs.getQuestItemsCount(getWyrmEgg(wyrm_id)) < 20) && Rnd.chance(Egg_Chance))
			{
				qs.giveItems(getWyrmEgg(wyrm_id), 1);
				qs.playSound(qs.getQuestItemsCount(getWyrmEgg(wyrm_id)) < 20 ? SOUND_ITEMGET : SOUND_MIDDLE);
			}
		}
		
		return null;
	}
	
	private static int getWyrmScale(int npc_id)
	{
		for (int[] wyrm : wyrms)
		{
			if (npc_id == wyrm[1])
			{
				return wyrm[2];
			}
		}
		
		return 0;
	}
	
	private static int getWyrmEgg(int npc_id)
	{
		for (int[] wyrm : wyrms)
		{
			if (npc_id == wyrm[1])
			{
				return wyrm[3];
			}
		}
		
		return 0;
	}
	
	private static int isWyrmStoler(int npc_id)
	{
		for (int[] wyrm : wyrms)
		{
			if (npc_id == wyrm[0])
			{
				return wyrm[1];
			}
		}
		
		return 0;
	}
	
	private static int getNeededSkins(QuestState qs)
	{
		if (qs.getQuestItemsCount(Deluxe_Fairy_Stone_List) > 0)
		{
			return 20;
		}
		
		if (qs.getQuestItemsCount(Fairy_Stone_List) > 0)
		{
			return 10;
		}
		
		return -1;
	}
	
	private static boolean CheckFairyStoneItems(QuestState qs, int[][] item_list)
	{
		for (int[] _item : item_list)
		{
			if (qs.getQuestItemsCount(_item[0]) < _item[1])
			{
				return false;
			}
		}
		
		return true;
	}
	
	private static void TakeFairyStoneItems(QuestState qs, int[][] item_list)
	{
		for (int[] _item : item_list)
		{
			qs.takeItems(_item[0], _item[1]);
		}
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
