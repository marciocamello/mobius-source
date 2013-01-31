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

public class _380_BringOutTheFlavorOfIngredients extends Quest implements ScriptFile
{
	private static final int Rollant = 30069;
	private static final int RitronsFruit = 5895;
	private static final int MoonFaceFlower = 5896;
	private static final int LeechFluids = 5897;
	private static final int Antidote = 1831;
	private static final int RitronsDessertRecipe = 5959;
	private static final int RitronJelly = 5960;
	private static final int RitronsDessertRecipeChance = 55;
	private static final int DireWolf = 20205;
	private static final int KadifWerewolf = 20206;
	private static final int GiantMistLeech = 20225;
	private static final int[][] DROPLIST_COND =
	{
		{
			1,
			0,
			DireWolf,
			0,
			RitronsFruit,
			4,
			10,
			1
		},
		{
			1,
			0,
			KadifWerewolf,
			0,
			MoonFaceFlower,
			20,
			50,
			1
		},
		{
			1,
			0,
			GiantMistLeech,
			0,
			LeechFluids,
			10,
			50,
			1
		}
	};
	
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
	
	public _380_BringOutTheFlavorOfIngredients()
	{
		super(false);
		addStartNpc(Rollant);
		for (int[] element : DROPLIST_COND)
		{
			addKillId(element[2]);
			addQuestItem(element[4]);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("rollant_q0380_05.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("rollant_q0380_12.htm"))
		{
			st.giveItems(RitronsDessertRecipe, 1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int cond = st.getCond();
		if (npcId == Rollant)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 24)
				{
					htmltext = "rollant_q0380_02.htm";
				}
				else
				{
					htmltext = "rollant_q0380_01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "rollant_q0380_06.htm";
			}
			else if ((cond == 2) && (st.getQuestItemsCount(Antidote) >= 2))
			{
				st.takeItems(Antidote, 2);
				st.takeItems(RitronsFruit, -1);
				st.takeItems(MoonFaceFlower, -1);
				st.takeItems(LeechFluids, -1);
				htmltext = "rollant_q0380_07.htm";
				st.setCond(3);
				st.setState(STARTED);
			}
			else if (cond == 2)
			{
				htmltext = "rollant_q0380_06.htm";
			}
			else if (cond == 3)
			{
				htmltext = "rollant_q0380_08.htm";
				st.setCond(4);
			}
			else if (cond == 4)
			{
				htmltext = "rollant_q0380_09.htm";
				st.setCond(5);
			}
			if (cond == 5)
			{
				htmltext = "rollant_q0380_10.htm";
				st.setCond(6);
			}
			if (cond == 6)
			{
				st.giveItems(RitronJelly, 1);
				if (Rnd.chance(RitronsDessertRecipeChance))
				{
					htmltext = "rollant_q0380_11.htm";
				}
				else
				{
					htmltext = "rollant_q0380_14.htm";
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(true);
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		for (int[] element : DROPLIST_COND)
		{
			if ((cond == element[0]) && (npcId == element[2]))
			{
				if ((element[3] == 0) || (st.getQuestItemsCount(element[3]) > 0))
				{
					if (element[5] == 0)
					{
						st.rollAndGive(element[4], element[7], element[6]);
					}
					else if (st.rollAndGive(element[4], element[7], element[7], element[5], element[6]))
					{
						if ((element[1] != cond) && (element[1] != 0))
						{
							st.setCond(Integer.valueOf(element[1]));
							st.setState(STARTED);
						}
					}
				}
			}
		}
		if ((cond == 1) && (st.getQuestItemsCount(RitronsFruit) >= 4) && (st.getQuestItemsCount(MoonFaceFlower) >= 20) && (st.getQuestItemsCount(LeechFluids) >= 10))
		{
			st.setCond(2);
			st.setState(STARTED);
		}
		return null;
	}
}
