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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

public class Q00692_HowToOpposeEvil extends Quest implements ScriptFile
{
	private static final int Dilios = 32549;
	private static final int Kutran = 32550;
	private static final int Lekon = 32557;
	private static final int NucleusofanIncompleteSoul = 13863;
	private static final int FleetSteedTroupsTotem = 13865;
	private static final int PortionofaSoul = 13866;
	private static final int BreathofTiat = 13867;
	private static final int ConcentratedSpiritEnergy = 15535;
	private static final int SpiritStoneDust = 15536;
	private static final int NucleusofaFreedSoul = 13796;
	private static final int FleetSteedTroupsCharm = 13841;
	private static final int SpiritStoneFragment = 15486;
	private static final int[] SOD =
	{
		22552,
		22541,
		22550,
		22551,
		22596,
		22544,
		22540,
		22547,
		22542,
		22543,
		22539,
		22546,
		22548,
		22536,
		22538,
		22537
	};
	private static final int[] SOI =
	{
		22509,
		22510,
		22511,
		22512,
		22513,
		22514,
		22515,
		22520,
		22522,
		22527,
		22531,
		22535,
		22516,
		22517,
		22518,
		22519,
		22521,
		22524,
		22528,
		22532,
		22530,
		22535
	};
	private static final int[] SOA =
	{
		22746,
		22747,
		22748,
		22749,
		22750,
		22751,
		22752,
		22753,
		22754,
		22755,
		22756,
		22757,
		22758,
		22759,
		22760,
		22761,
		22762,
		22763,
		22764,
		22765
	};
	
	public Q00692_HowToOpposeEvil()
	{
		super(true);
		addStartNpc(Dilios);
		addTalkId(Kutran, Lekon);
		addKillId(SOD);
		addKillId(SOI);
		addKillId(SOA);
		addQuestItem(NucleusofanIncompleteSoul, FleetSteedTroupsTotem, PortionofaSoul, BreathofTiat, ConcentratedSpiritEnergy, SpiritStoneDust);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final int cond = qs.getCond();
		
		switch (event)
		{
			case "take_test":
				if (cond == 0)
				{
					final QuestState GoodDayToFly = qs.getPlayer().getQuestState(Q10273_GoodDayToFly.class);
					
					if ((GoodDayToFly != null) && GoodDayToFly.isCompleted())
					{
						qs.setCond(2);
						qs.setState(STARTED);
						qs.playSound(SOUND_ACCEPT);
						htmltext = "dilios_q692_4.htm";
					}
					else
					{
						qs.setCond(1);
						qs.setState(STARTED);
						qs.playSound(SOUND_ACCEPT);
						htmltext = "dilios_q692_3.htm";
					}
				}
				break;
			
			case "lekon_q692_2.htm":
				if (cond == 1)
				{
					qs.exitCurrentQuest(true);
				}
				break;
			
			case "kutran_q692_2.htm":
				if (cond == 2)
				{
					qs.setCond(3);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "exchange_sod":
				if (cond == 3)
				{
					if (qs.getQuestItemsCount(FleetSteedTroupsTotem) < 5)
					{
						htmltext = "kutran_q692_7.htm";
					}
					else
					{
						int _charmstogive = Math.round(qs.getQuestItemsCount(FleetSteedTroupsTotem) / 5);
						qs.takeItems(FleetSteedTroupsTotem, 5 * _charmstogive);
						qs.giveItems(FleetSteedTroupsCharm, _charmstogive);
						htmltext = "kutran_q692_4.htm";
					}
				}
				break;
			
			case "exchange_soi":
				if (cond == 3)
				{
					if (qs.getQuestItemsCount(NucleusofanIncompleteSoul) < 5)
					{
						htmltext = "kutran_q692_7.htm";
					}
					else
					{
						int _soulstogive = Math.round(qs.getQuestItemsCount(NucleusofanIncompleteSoul) / 5);
						qs.takeItems(NucleusofanIncompleteSoul, 5 * _soulstogive);
						qs.giveItems(NucleusofaFreedSoul, _soulstogive);
						htmltext = "kutran_q692_5.htm";
					}
				}
				break;
			
			case "exchange_soa":
				if (cond == 3)
				{
					if (qs.getQuestItemsCount(SpiritStoneDust) < 5)
					{
						htmltext = "kutran_q692_7.htm";
					}
					else
					{
						int _soulstogive = Math.round(qs.getQuestItemsCount(SpiritStoneDust) / 5);
						qs.takeItems(SpiritStoneDust, 5 * _soulstogive);
						qs.giveItems(SpiritStoneFragment, _soulstogive);
						htmltext = "kutran_q692_5.htm";
					}
				}
				break;
			
			case "exchange_breath":
				if (cond == 3)
				{
					if (qs.getQuestItemsCount(BreathofTiat) == 0)
					{
						htmltext = "kutran_q692_7.htm";
					}
					else
					{
						qs.giveItems(ADENA_ID, qs.getQuestItemsCount(BreathofTiat) * 2500);
						qs.takeItems(BreathofTiat, -1);
						htmltext = "kutran_q692_5.htm";
					}
				}
				break;
			
			case "exchange_portion":
				if (cond == 3)
				{
					if (qs.getQuestItemsCount(PortionofaSoul) == 0)
					{
						htmltext = "kutran_q692_7.htm";
					}
					else
					{
						qs.giveItems(ADENA_ID, qs.getQuestItemsCount(PortionofaSoul) * 2500);
						qs.takeItems(PortionofaSoul, -1);
						htmltext = "kutran_q692_5.htm";
					}
				}
				break;
			
			case "exchange_energy":
				if (cond == 3)
				{
					if (qs.getQuestItemsCount(ConcentratedSpiritEnergy) == 0)
					{
						htmltext = "kutran_q692_7.htm";
					}
					else
					{
						qs.giveItems(ADENA_ID, qs.getQuestItemsCount(ConcentratedSpiritEnergy) * 25000);
						qs.takeItems(ConcentratedSpiritEnergy, -1);
						htmltext = "kutran_q692_5.htm";
					}
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case Dilios:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() >= 75)
					{
						htmltext = "dilios_q692_1.htm";
					}
					else
					{
						htmltext = "dilios_q692_0.htm";
						qs.exitCurrentQuest(true);
					}
				}
				break;
			
			case Kutran:
				if (cond == 2)
				{
					htmltext = "kutran_q692_1.htm";
				}
				else if (cond == 3)
				{
					htmltext = "kutran_q692_3.htm";
				}
				break;
			
			case Lekon:
				if (cond == 1)
				{
					htmltext = "lekon_q692_1.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		
		if (qs.getCond() == 3)
		{
			if (Util.contains(SOD, npcId))
			{
				qs.rollAndGive(FleetSteedTroupsTotem, (int) Config.RATE_QUESTS_REWARD * 1, 17);
			}
			else if (Util.contains(SOI, npcId))
			{
				qs.rollAndGive(NucleusofanIncompleteSoul, (int) Config.RATE_QUESTS_REWARD * 1, 17);
			}
			else if (Util.contains(SOA, npcId))
			{
				qs.rollAndGive(SpiritStoneDust, (int) Config.RATE_QUESTS_REWARD * 1, 20);
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
