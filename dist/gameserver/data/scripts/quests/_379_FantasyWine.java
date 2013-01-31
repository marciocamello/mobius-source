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

public class _379_FantasyWine extends Quest implements ScriptFile
{
	public final int HARLAN = 30074;
	public final int Enku_Orc_Champion = 20291;
	public final int Enku_Orc_Shaman = 20292;
	public final int LEAF_OF_EUCALYPTUS = 5893;
	public final int STONE_OF_CHILL = 5894;
	public final int[] REWARD =
	{
		5956,
		5957,
		5958
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
	
	public _379_FantasyWine()
	{
		super(false);
		addStartNpc(HARLAN);
		addKillId(Enku_Orc_Champion);
		addKillId(Enku_Orc_Shaman);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("hitsran_q0379_06.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("reward"))
		{
			st.takeItems(LEAF_OF_EUCALYPTUS, -1);
			st.takeItems(STONE_OF_CHILL, -1);
			int rand = Rnd.get(100);
			if (rand < 25)
			{
				st.giveItems(REWARD[0], 1);
				htmltext = "hitsran_q0379_11.htm";
			}
			else if (rand < 50)
			{
				st.giveItems(REWARD[1], 1);
				htmltext = "hitsran_q0379_12.htm";
			}
			else
			{
				st.giveItems(REWARD[2], 1);
				htmltext = "hitsran_q0379_13.htm";
			}
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if (event.equalsIgnoreCase("hitsran_q0379_05.htm"))
		{
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int id = st.getState();
		int cond = 0;
		if (id != CREATED)
		{
			cond = st.getCond();
		}
		if (npcId == HARLAN)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() < 20)
				{
					htmltext = "hitsran_q0379_01.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "hitsran_q0379_02.htm";
				}
			}
			else if (cond == 1)
			{
				if ((st.getQuestItemsCount(LEAF_OF_EUCALYPTUS) < 80) && (st.getQuestItemsCount(STONE_OF_CHILL) < 100))
				{
					htmltext = "hitsran_q0379_07.htm";
				}
				else if ((st.getQuestItemsCount(LEAF_OF_EUCALYPTUS) == 80) && (st.getQuestItemsCount(STONE_OF_CHILL) < 100))
				{
					htmltext = "hitsran_q0379_08.htm";
				}
				else if ((st.getQuestItemsCount(LEAF_OF_EUCALYPTUS) < 80) && (st.getQuestItemsCount(STONE_OF_CHILL) == 100))
				{
					htmltext = "hitsran_q0379_09.htm";
				}
				else
				{
					htmltext = "hitsran_q0379_02.htm";
				}
			}
			else if (cond == 2)
			{
				if ((st.getQuestItemsCount(LEAF_OF_EUCALYPTUS) >= 80) && (st.getQuestItemsCount(STONE_OF_CHILL) >= 100))
				{
					htmltext = "hitsran_q0379_10.htm";
				}
				else
				{
					st.setCond(1);
					if ((st.getQuestItemsCount(LEAF_OF_EUCALYPTUS) < 80) && (st.getQuestItemsCount(STONE_OF_CHILL) < 100))
					{
						htmltext = "hitsran_q0379_07.htm";
					}
					else if ((st.getQuestItemsCount(LEAF_OF_EUCALYPTUS) >= 80) && (st.getQuestItemsCount(STONE_OF_CHILL) < 100))
					{
						htmltext = "hitsran_q0379_08.htm";
					}
					else if ((st.getQuestItemsCount(LEAF_OF_EUCALYPTUS) < 80) && (st.getQuestItemsCount(STONE_OF_CHILL) >= 100))
					{
						htmltext = "hitsran_q0379_09.htm";
					}
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		if (st.getCond() == 1)
		{
			if ((npcId == Enku_Orc_Champion) && (st.getQuestItemsCount(LEAF_OF_EUCALYPTUS) < 80))
			{
				st.giveItems(LEAF_OF_EUCALYPTUS, 1);
			}
			else if ((npcId == Enku_Orc_Shaman) && (st.getQuestItemsCount(STONE_OF_CHILL) < 100))
			{
				st.giveItems(STONE_OF_CHILL, 1);
			}
			if ((st.getQuestItemsCount(LEAF_OF_EUCALYPTUS) >= 80) && (st.getQuestItemsCount(STONE_OF_CHILL) >= 100))
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
			}
			else
			{
				st.playSound(SOUND_ITEMGET);
			}
		}
		return null;
	}
}
