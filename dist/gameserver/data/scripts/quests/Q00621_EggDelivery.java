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

public class Q00621_EggDelivery extends Quest implements ScriptFile
{
	// Npcs
	private static final int JEREMY = 31521;
	private static final int VALENTINE = 31584;
	private static final int PULIN = 31543;
	private static final int NAFF = 31544;
	private static final int CROCUS = 31545;
	private static final int KUBER = 31546;
	private static final int BEOLIN = 31547;
	// Items
	private static final int BoiledEgg = 7206;
	private static final int FeeOfBoiledEgg = 7196;
	private static final int HastePotion = 734;
	private static final int RecipeSealedTateossianRing = 6849;
	private static final int RecipeSealedTateossianEarring = 6847;
	private static final int RecipeSealedTateossianNecklace = 6851;
	// Other
	private static final int Tateossian_CHANCE = 20;
	
	public Q00621_EggDelivery()
	{
		super(false);
		addStartNpc(JEREMY);
		addTalkId(VALENTINE, PULIN, NAFF, CROCUS, KUBER, BEOLIN);
		addQuestItem(BoiledEgg, FeeOfBoiledEgg);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final int cond = qs.getCond();
		final long BoiledEgg_count = qs.getQuestItemsCount(BoiledEgg);
		
		switch (event)
		{
			case "jeremy_q0621_0104.htm":
				if (qs.getState() == CREATED)
				{
					qs.takeItems(BoiledEgg, -1);
					qs.takeItems(FeeOfBoiledEgg, -1);
					qs.setState(STARTED);
					qs.setCond(1);
					qs.giveItems(BoiledEgg, 5);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "pulin_q0621_0201.htm":
				if ((cond == 1) && (BoiledEgg_count > 0))
				{
					takeEgg(qs, 2);
				}
				break;
			
			case "naff_q0621_0301.htm":
				if ((cond == 2) && (BoiledEgg_count > 0))
				{
					takeEgg(qs, 3);
				}
				break;
			
			case "crocus_q0621_0401.htm":
				if ((cond == 3) && (BoiledEgg_count > 0))
				{
					takeEgg(qs, 4);
				}
				break;
			
			case "kuber_q0621_0501.htm":
				if ((cond == 4) && (BoiledEgg_count > 0))
				{
					takeEgg(qs, 5);
				}
				break;
			
			case "beolin_q0621_0601.htm":
				if ((cond == 5) && (BoiledEgg_count > 0))
				{
					takeEgg(qs, 6);
				}
				break;
			
			case "jeremy_q0621_0701.htm":
				if ((cond == 6) && (qs.getQuestItemsCount(FeeOfBoiledEgg) >= 5))
				{
					qs.setCond(7);
				}
				break;
			
			case "brewer_valentine_q0621_0801.htm":
				if ((cond == 7) && (qs.getQuestItemsCount(FeeOfBoiledEgg) >= 5))
				{
					qs.takeItems(BoiledEgg, -1);
					qs.takeItems(FeeOfBoiledEgg, -1);
					
					if (Rnd.chance(Tateossian_CHANCE))
					{
						if (Rnd.chance(40))
						{
							qs.giveItems(RecipeSealedTateossianRing, 1);
						}
						else if (Rnd.chance(40))
						{
							qs.giveItems(RecipeSealedTateossianEarring, 1);
						}
						else
						{
							qs.giveItems(RecipeSealedTateossianNecklace, 1);
						}
					}
					else
					{
						qs.giveItems(ADENA_ID, 18800);
						qs.giveItems(HastePotion, 1, true);
					}
					
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
				}
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		final long BoiledEgg_count = qs.getQuestItemsCount(BoiledEgg);
		final long FeeOfBoiledEgg_count = qs.getQuestItemsCount(FeeOfBoiledEgg);
		
		if (qs.getState() == CREATED)
		{
			if (npcId != JEREMY)
			{
				return htmltext;
			}
			
			if (qs.getPlayer().getLevel() >= 68)
			{
				qs.setCond(0);
				return "jeremy_q0621_0101.htm";
			}
			
			qs.exitCurrentQuest(true);
			return "jeremy_q0621_0103.htm";
		}
		
		switch (npcId)
		{
			case PULIN:
				if ((cond == 1) && (BoiledEgg_count > 0))
				{
					htmltext = "pulin_q0621_0101.htm";
				}
				break;
			
			case NAFF:
				if ((cond == 2) && (BoiledEgg_count > 0))
				{
					htmltext = "naff_q0621_0201.htm";
				}
				break;
			
			case CROCUS:
				if ((cond == 3) && (BoiledEgg_count > 0))
				{
					htmltext = "crocus_q0621_0301.htm";
				}
				break;
			
			case KUBER:
				if ((cond == 4) && (BoiledEgg_count > 0))
				{
					htmltext = "kuber_q0621_0401.htm";
				}
				break;
			
			case BEOLIN:
				if ((cond == 5) && (BoiledEgg_count > 0))
				{
					htmltext = "beolin_q0621_0501.htm";
				}
				break;
			
			case VALENTINE:
				if ((cond == 7) && (FeeOfBoiledEgg_count >= 5))
				{
					htmltext = "brewer_valentine_q0621_0701.htm";
				}
				break;
			
			case JEREMY:
				if ((cond == 6) && (FeeOfBoiledEgg_count >= 5))
				{
					htmltext = "jeremy_q0621_0601.htm";
				}
				else if ((cond == 7) && (FeeOfBoiledEgg_count >= 5))
				{
					htmltext = "jeremy_q0621_0703.htm";
				}
				else if ((cond > 0) && (BoiledEgg_count > 0))
				{
					htmltext = "jeremy_q0621_0104.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	private static void takeEgg(QuestState qs, int setcond)
	{
		qs.setCond(Integer.valueOf(setcond));
		qs.takeItems(BoiledEgg, 1);
		qs.giveItems(FeeOfBoiledEgg, 1);
		qs.playSound(SOUND_MIDDLE);
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
