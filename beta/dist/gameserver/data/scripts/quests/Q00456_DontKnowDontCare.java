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

public class Q00456_DontKnowDontCare extends Quest implements ScriptFile
{
	private static final int[] SeparatedSoul =
	{
		32864,
		32865,
		32866,
		32867,
		32868,
		32869,
		32870
	};
	private static final int DrakeLordsEssence = 17251;
	private static final int BehemothLeadersEssence = 17252;
	private static final int DragonBeastsEssence = 17253;
	private static final int DrakeLordCorpse = 32884;
	private static final int BehemothLeaderCorpse = 32885;
	private static final int DragonBeastCorpse = 32886;
	private static final int[] weapons =
	{
		15558,
		15559,
		15560,
		15561,
		15562,
		15563,
		15564,
		15565,
		15566,
		15567,
		15568,
		15569,
		15570,
		15571
	};
	private static final int[] armors =
	{
		15743,
		15744,
		15745,
		15746,
		15747,
		15748,
		15749,
		15750,
		15751,
		15752,
		15753,
		15754,
		15755,
		15756,
		15757,
		15759,
		15758
	};
	private static final int[] accessory =
	{
		15763,
		15764,
		15765
	};
	private static final int[] scrolls =
	{
		6577,
		6578,
		959
	};
	private static final int[] reward_attr_crystal =
	{
		4342,
		4343,
		4344,
		4345,
		4346,
		4347
	};
	private static final int gemstone_s = 2134;
	
	public Q00456_DontKnowDontCare()
	{
		super(PARTY_ALL);
		addStartNpc(SeparatedSoul);
		addTalkId(DrakeLordCorpse, BehemothLeaderCorpse, DragonBeastCorpse);
		addQuestItem(DrakeLordsEssence, BehemothLeadersEssence, DragonBeastsEssence);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "sepsoul_q456_05.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "take_essense":
				if (qs.getCond() == 1)
				{
					switch (npc.getId())
					{
						case DrakeLordCorpse:
							if (qs.getQuestItemsCount(DrakeLordsEssence) < 1)
							{
								qs.giveItems(DrakeLordsEssence, 1);
							}
							break;
						
						case BehemothLeaderCorpse:
							if (qs.getQuestItemsCount(BehemothLeadersEssence) < 1)
							{
								qs.giveItems(BehemothLeadersEssence, 1);
							}
							break;
						
						case DragonBeastCorpse:
							if (qs.getQuestItemsCount(DragonBeastsEssence) < 1)
							{
								qs.giveItems(DragonBeastsEssence, 1);
							}
							break;
						
						default:
							break;
					}
					
					if ((qs.getQuestItemsCount(DrakeLordsEssence) > 0) && (qs.getQuestItemsCount(BehemothLeadersEssence) > 0) && (qs.getQuestItemsCount(DragonBeastsEssence) > 0))
					{
						qs.setCond(2);
					}
				}
				return null;
				
			case "sepsoul_q456_08.htm":
				qs.takeAllItems(DrakeLordsEssence);
				qs.takeAllItems(BehemothLeadersEssence);
				qs.takeAllItems(DragonBeastsEssence);
				if (Rnd.chance(30))
				{
					qs.giveItems(weapons[Rnd.get(weapons.length)], 1);
				}
				else if (Rnd.chance(50))
				{
					qs.giveItems(armors[Rnd.get(armors.length)], 1);
				}
				else
				{
					qs.giveItems(accessory[Rnd.get(accessory.length)], 1);
				}
				if (Rnd.chance(30))
				{
					qs.giveItems(scrolls[Rnd.get(scrolls.length)], 1);
				}
				if (Rnd.chance(70))
				{
					qs.giveItems(reward_attr_crystal[Rnd.get(reward_attr_crystal.length)], 1);
				}
				qs.giveItems(gemstone_s, 3);
				qs.setState(COMPLETED);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if (Util.contains(SeparatedSoul, npc.getId()))
		{
			switch (qs.getState())
			{
				case CREATED:
					if (qs.isNowAvailableByTime())
					{
						if (qs.getPlayer().getLevel() >= 80)
						{
							htmltext = "sepsoul_q456_01.htm";
						}
						else
						{
							htmltext = "sepsoul_q456_00.htm";
							qs.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "sepsoul_q456_00a.htm";
					}
					break;
				
				case STARTED:
					if (cond == 1)
					{
						htmltext = "sepsoul_q456_06.htm";
					}
					else if (cond == 2)
					{
						htmltext = "sepsoul_q456_07.htm";
					}
					break;
			}
		}
		
		return htmltext;
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
