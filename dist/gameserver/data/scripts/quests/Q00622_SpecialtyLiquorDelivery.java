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

public class Q00622_SpecialtyLiquorDelivery extends Quest implements ScriptFile
{
	// Npcs
	private static final int JEREMY = 31521;
	private static final int LIETTA = 31267;
	private static final int PULIN = 31543;
	private static final int NAFF = 31544;
	private static final int CROCUS = 31545;
	private static final int KUBER = 31546;
	private static final int BEOLIN = 31547;
	// Items
	private static final int SpecialDrink = 7207;
	private static final int FeeOfSpecialDrink = 7198;
	private static final int RecipeSealedTateossianRing = 6849;
	private static final int RecipeSealedTateossianEarring = 6847;
	private static final int RecipeSealedTateossianNecklace = 6851;
	private static final int HastePotion = 734;
	// Other
	private static final int Tateossian_CHANCE = 20;
	
	public Q00622_SpecialtyLiquorDelivery()
	{
		super(false);
		addStartNpc(JEREMY);
		addTalkId(LIETTA, PULIN, NAFF, CROCUS, KUBER, BEOLIN);
		addQuestItem(SpecialDrink, FeeOfSpecialDrink);
	}
	
	private static void takeDrink(QuestState qs, int setcond)
	{
		qs.setCond(Integer.valueOf(setcond));
		qs.takeItems(SpecialDrink, 1);
		qs.giveItems(FeeOfSpecialDrink, 1);
		qs.playSound(SOUND_MIDDLE);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		final int cond = st.getCond();
		final long SpecialDrink_count = st.getQuestItemsCount(SpecialDrink);
		
		switch (event)
		{
			case "jeremy_q0622_0104.htm":
				if (st.getState() == CREATED)
				{
					st.setState(STARTED);
					st.setCond(1);
					st.takeItems(SpecialDrink, -1);
					st.takeItems(FeeOfSpecialDrink, -1);
					st.giveItems(SpecialDrink, 5);
					st.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "beolin_q0622_0201.htm":
				if ((cond == 1) && (SpecialDrink_count > 0))
				{
					takeDrink(st, 2);
				}
				break;
			
			case "kuber_q0622_0301.htm":
				if ((cond == 2) && (SpecialDrink_count > 0))
				{
					takeDrink(st, 3);
				}
				break;
			
			case "crocus_q0622_0401.htm":
				if ((cond == 3) && (SpecialDrink_count > 0))
				{
					takeDrink(st, 4);
				}
				break;
			
			case "naff_q0622_0501.htm":
				if ((cond == 4) && (SpecialDrink_count > 0))
				{
					takeDrink(st, 5);
				}
				break;
			
			case "pulin_q0622_0601.htm":
				if ((cond == 5) && (SpecialDrink_count > 0))
				{
					takeDrink(st, 6);
				}
				break;
			
			case "jeremy_q0622_0701.htm":
				if ((cond == 6) && (st.getQuestItemsCount(FeeOfSpecialDrink) >= 5))
				{
					st.setCond(7);
				}
				break;
			
			case "warehouse_keeper_lietta_q0622_0801.htm":
				if ((cond == 7) && (st.getQuestItemsCount(FeeOfSpecialDrink) >= 5))
				{
					st.takeItems(SpecialDrink, -1);
					st.takeItems(FeeOfSpecialDrink, -1);
					
					if (Rnd.chance(Tateossian_CHANCE))
					{
						if (Rnd.chance(40))
						{
							st.giveItems(RecipeSealedTateossianRing, 1);
						}
						else if (Rnd.chance(40))
						{
							st.giveItems(RecipeSealedTateossianEarring, 1);
						}
						else
						{
							st.giveItems(RecipeSealedTateossianNecklace, 1);
						}
					}
					else
					{
						st.giveItems(ADENA_ID, 18800);
						st.giveItems(HastePotion, 1, true);
					}
					
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(true);
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
		final long SpecialDrink_count = qs.getQuestItemsCount(SpecialDrink);
		final long FeeOfSpecialDrink_count = qs.getQuestItemsCount(FeeOfSpecialDrink);
		
		if (qs.getState() == CREATED)
		{
			if (npcId != JEREMY)
			{
				return htmltext;
			}
			
			if (qs.getPlayer().getLevel() >= 68)
			{
				qs.setCond(0);
				return "jeremy_q0622_0101.htm";
			}
			
			qs.exitCurrentQuest(true);
			return "jeremy_q0622_0103.htm";
		}
		
		switch (npcId)
		{
			case BEOLIN:
				if ((cond == 1) && (SpecialDrink_count > 0))
				{
					htmltext = "beolin_q0622_0101.htm";
				}
				break;
			
			case KUBER:
				if ((cond == 2) && (SpecialDrink_count > 0))
				{
					htmltext = "kuber_q0622_0201.htm";
				}
				break;
			
			case CROCUS:
				if ((cond == 3) && (SpecialDrink_count > 0))
				{
					htmltext = "crocus_q0622_0301.htm";
				}
				break;
			
			case NAFF:
				if ((cond == 4) && (SpecialDrink_count > 0))
				{
					htmltext = "naff_q0622_0401.htm";
				}
				break;
			
			case PULIN:
				if ((cond == 5) && (SpecialDrink_count > 0))
				{
					htmltext = "pulin_q0622_0501.htm";
				}
				break;
			
			case LIETTA:
				if ((cond == 7) && (FeeOfSpecialDrink_count >= 5))
				{
					htmltext = "warehouse_keeper_lietta_q0622_0701.htm";
				}
				break;
			
			case JEREMY:
				if ((cond == 6) && (FeeOfSpecialDrink_count >= 5))
				{
					htmltext = "jeremy_q0622_0601.htm";
				}
				else if ((cond == 7) && (FeeOfSpecialDrink_count >= 5))
				{
					htmltext = "jeremy_q0622_0703.htm";
				}
				else if ((cond > 0) && (SpecialDrink_count > 0))
				{
					htmltext = "jeremy_q0622_0104.htm";
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
