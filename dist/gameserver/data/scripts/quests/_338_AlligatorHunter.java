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

public class _338_AlligatorHunter extends Quest implements ScriptFile
{
	private static final int Enverun = 30892;
	private static final int AlligatorLeather = 4337;
	private static final int CrokianLad = 20804;
	private static final int DailaonLad = 20805;
	private static final int CrokianLadWarrior = 20806;
	private static final int FarhiteLad = 20807;
	private static final int NosLad = 20808;
	private static final int SwampTribe = 20991;
	public final int[][] DROPLIST_COND =
	{
		{
			1,
			0,
			CrokianLad,
			0,
			AlligatorLeather,
			0,
			60,
			1
		},
		{
			1,
			0,
			DailaonLad,
			0,
			AlligatorLeather,
			0,
			60,
			1
		},
		{
			1,
			0,
			CrokianLadWarrior,
			0,
			AlligatorLeather,
			0,
			60,
			1
		},
		{
			1,
			0,
			FarhiteLad,
			0,
			AlligatorLeather,
			0,
			60,
			1
		},
		{
			1,
			0,
			NosLad,
			0,
			AlligatorLeather,
			0,
			60,
			1
		},
		{
			1,
			0,
			SwampTribe,
			0,
			AlligatorLeather,
			0,
			60,
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
	
	public _338_AlligatorHunter()
	{
		super(false);
		addStartNpc(Enverun);
		for (int[] element : DROPLIST_COND)
		{
			addKillId(element[2]);
		}
		addQuestItem(AlligatorLeather);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("30892-02.htm"))
		{
			st.playSound(SOUND_ACCEPT);
			st.setCond(1);
			st.setState(STARTED);
		}
		else if (event.equalsIgnoreCase("30892-02-afmenu.htm"))
		{
			long AdenaCount = st.getQuestItemsCount(AlligatorLeather) * 40;
			st.takeItems(AlligatorLeather, -1);
			st.giveItems(ADENA_ID, AdenaCount);
		}
		else if (event.equalsIgnoreCase("quit"))
		{
			if (st.getQuestItemsCount(AlligatorLeather) >= 1)
			{
				long AdenaCount = st.getQuestItemsCount(AlligatorLeather) * 40;
				st.takeItems(AlligatorLeather, -1);
				st.giveItems(ADENA_ID, AdenaCount);
				htmltext = "30892-havequit.htm";
			}
			else
			{
				htmltext = "30892-havent.htm";
			}
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "<html><body>I have nothing to say you</body></html>";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == Enverun)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 40)
				{
					htmltext = "30892-01.htm";
				}
				else
				{
					htmltext = "30892-00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (st.getQuestItemsCount(AlligatorLeather) == 0)
			{
				htmltext = "30892-02-rep.htm";
			}
			else
			{
				htmltext = "30892-menu.htm";
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
