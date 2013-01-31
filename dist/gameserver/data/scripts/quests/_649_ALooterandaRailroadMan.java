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

public class _649_ALooterandaRailroadMan extends Quest implements ScriptFile
{
	private static final int OBI = 32052;
	private static final int THIEF_GUILD_MARK = 8099;
	private static final int[][] DROPLIST_COND =
	{
		{
			1,
			2,
			22017,
			0,
			THIEF_GUILD_MARK,
			200,
			50,
			1
		},
		{
			1,
			2,
			22018,
			0,
			THIEF_GUILD_MARK,
			200,
			50,
			1
		},
		{
			1,
			2,
			22019,
			0,
			THIEF_GUILD_MARK,
			200,
			50,
			1
		},
		{
			1,
			2,
			22021,
			0,
			THIEF_GUILD_MARK,
			200,
			50,
			1
		},
		{
			1,
			2,
			22022,
			0,
			THIEF_GUILD_MARK,
			200,
			50,
			1
		},
		{
			1,
			2,
			22023,
			0,
			THIEF_GUILD_MARK,
			200,
			50,
			1
		},
		{
			1,
			2,
			22024,
			0,
			THIEF_GUILD_MARK,
			200,
			50,
			1
		},
		{
			1,
			2,
			22026,
			0,
			THIEF_GUILD_MARK,
			200,
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
	
	public _649_ALooterandaRailroadMan()
	{
		super(true);
		addStartNpc(OBI);
		for (int[] element : DROPLIST_COND)
		{
			addKillId(element[2]);
		}
		addQuestItem(THIEF_GUILD_MARK);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "railman_obi_q0649_0103.htm";
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("649_3"))
		{
			if (st.getQuestItemsCount(THIEF_GUILD_MARK) == 200)
			{
				htmltext = "railman_obi_q0649_0201.htm";
				st.takeItems(THIEF_GUILD_MARK, -1);
				st.giveItems(ADENA_ID, 21698, true);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
			{
				st.setCond(1);
				htmltext = "railman_obi_q0649_0202.htm";
			}
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
		if (npcId == OBI)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() < 30)
				{
					htmltext = "railman_obi_q0649_0102.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "railman_obi_q0649_0101.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "railman_obi_q0649_0106.htm";
			}
			else if ((cond == 2) && (st.getQuestItemsCount(THIEF_GUILD_MARK) == 200))
			{
				htmltext = "railman_obi_q0649_0105.htm";
			}
			else
			{
				htmltext = "railman_obi_q0649_0106.htm";
				st.setCond(1);
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
