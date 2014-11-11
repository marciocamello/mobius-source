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

public class Q00028_ChestCaughtWithIcyAirBait extends Quest implements ScriptFile
{
	// Npcs
	private final static int OFulle = 31572;
	private final static int Kiki = 31442;
	// Items
	private final static int BigYellowTreasureChest = 6503;
	private final static int KikisLetter = 7626;
	private final static int ElvenRing = 881;
	
	public Q00028_ChestCaughtWithIcyAirBait()
	{
		super(false);
		addStartNpc(OFulle);
		addTalkId(Kiki);
		addQuestItem(KikisLetter);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "fisher_ofulle_q0028_0104.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "fisher_ofulle_q0028_0201.htm":
				if (qs.getQuestItemsCount(BigYellowTreasureChest) > 0)
				{
					qs.setCond(2);
					qs.takeItems(BigYellowTreasureChest, 1);
					qs.giveItems(KikisLetter, 1);
					qs.playSound(SOUND_MIDDLE);
				}
				else
				{
					htmltext = "fisher_ofulle_q0028_0202.htm";
				}
				break;
			
			case "mineral_trader_kiki_q0028_0301.htm":
				if (qs.getQuestItemsCount(KikisLetter) == 1)
				{
					htmltext = "mineral_trader_kiki_q0028_0301.htm";
					qs.takeItems(KikisLetter, -1);
					qs.giveItems(ElvenRing, 1);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
				else
				{
					htmltext = "mineral_trader_kiki_q0028_0302.htm";
					qs.exitCurrentQuest(true);
				}
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
			case OFulle:
				if (qs.getState() == CREATED)
				{
					if (qs.getPlayer().getLevel() < 36)
					{
						htmltext = "fisher_ofulle_q0028_0101.htm";
						qs.exitCurrentQuest(true);
					}
					else
					{
						QuestState OFullesSpecialBait = qs.getPlayer().getQuestState(Q00051_OFullesSpecialBait.class);
						
						if (OFullesSpecialBait != null)
						{
							if (OFullesSpecialBait.isCompleted())
							{
								htmltext = "fisher_ofulle_q0028_0101.htm";
							}
							else
							{
								htmltext = "fisher_ofulle_q0028_0102.htm";
								qs.exitCurrentQuest(true);
							}
						}
						else
						{
							htmltext = "fisher_ofulle_q0028_0103.htm";
							qs.exitCurrentQuest(true);
						}
					}
				}
				else if (cond == 1)
				{
					htmltext = "fisher_ofulle_q0028_0105.htm";
					
					if (qs.getQuestItemsCount(BigYellowTreasureChest) == 0)
					{
						htmltext = "fisher_ofulle_q0028_0106.htm";
					}
				}
				else if (cond == 2)
				{
					htmltext = "fisher_ofulle_q0028_0203.htm";
				}
				break;
			
			case Kiki:
				if (cond == 2)
				{
					htmltext = "mineral_trader_kiki_q0028_0201.htm";
				}
				else
				{
					htmltext = "mineral_trader_kiki_q0028_0302.htm";
				}
				break;
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
