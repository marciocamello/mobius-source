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
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _271_ProofOfValor extends Quest implements ScriptFile
{
	private static final int RUKAIN = 30577;
	private static final int KASHA_WOLF_FANG_ID = 1473;
	private static final int NECKLACE_OF_VALOR_ID = 1507;
	private static final int NECKLACE_OF_COURAGE_ID = 1506;
	private static final int[][] DROPLIST_COND =
	{
		{
			1,
			2,
			20475,
			0,
			KASHA_WOLF_FANG_ID,
			50,
			25,
			2
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
	
	public _271_ProofOfValor()
	{
		super(false);
		addStartNpc(RUKAIN);
		addTalkId(RUKAIN);
		for (int[] element : DROPLIST_COND)
		{
			addKillId(element[2]);
		}
		addQuestItem(KASHA_WOLF_FANG_ID);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("praetorian_rukain_q0271_03.htm"))
		{
			st.playSound(SOUND_ACCEPT);
			if ((st.getQuestItemsCount(NECKLACE_OF_COURAGE_ID) > 0) || (st.getQuestItemsCount(NECKLACE_OF_VALOR_ID) > 0))
			{
				htmltext = "praetorian_rukain_q0271_07.htm";
			}
			st.setCond(1);
			st.setState(STARTED);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int cond = st.getCond();
		if (npcId == RUKAIN)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getRace() != Race.orc)
				{
					htmltext = "praetorian_rukain_q0271_00.htm";
					st.exitCurrentQuest(true);
				}
				else if (st.getPlayer().getLevel() < 4)
				{
					htmltext = "praetorian_rukain_q0271_01.htm";
					st.exitCurrentQuest(true);
				}
				else if ((st.getQuestItemsCount(NECKLACE_OF_COURAGE_ID) > 0) || (st.getQuestItemsCount(NECKLACE_OF_VALOR_ID) > 0))
				{
					htmltext = "praetorian_rukain_q0271_06.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "praetorian_rukain_q0271_02.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "praetorian_rukain_q0271_04.htm";
			}
			else if ((cond == 2) && (st.getQuestItemsCount(KASHA_WOLF_FANG_ID) == 50))
			{
				st.takeItems(KASHA_WOLF_FANG_ID, -1);
				if (Rnd.chance(14))
				{
					st.takeItems(NECKLACE_OF_VALOR_ID, -1);
					st.giveItems(NECKLACE_OF_VALOR_ID, 1);
				}
				else
				{
					st.takeItems(NECKLACE_OF_COURAGE_ID, -1);
					st.giveItems(NECKLACE_OF_COURAGE_ID, 1);
				}
				htmltext = "praetorian_rukain_q0271_05.htm";
				st.exitCurrentQuest(true);
			}
			else if ((cond == 2) && (st.getQuestItemsCount(KASHA_WOLF_FANG_ID) < 50))
			{
				htmltext = "praetorian_rukain_q0271_04.htm";
				st.setCond(1);
				st.setState(STARTED);
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
		return null;
	}
}
