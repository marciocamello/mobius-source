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

/**
 * @author Sergey Ibryaev aka Artful
 */
public class Q00338_AlligatorHunter extends Quest implements ScriptFile
{
	// NPC
	private static final int Enverun = 30892;
	// QuestItems
	private static final int AlligatorLeather = 4337;
	// MOB
	private static final int CrokianLad = 20804;
	private static final int DailaonLad = 20805;
	private static final int CrokianLadWarrior = 20806;
	private static final int FarhiteLad = 20807;
	private static final int NosLad = 20808;
	private static final int SwampTribe = 20991;
	// Drop Cond
	// # [COND, NEWCOND, ID, REQUIRED, ITEM, NEED_COUNT, CHANCE, DROP]
	private static final int[][] DROPLIST_COND =
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
	
	public Q00338_AlligatorHunter()
	{
		super(false);
		addStartNpc(Enverun);
		// Mob Drop
		for (int[] aDROPLIST_COND : DROPLIST_COND)
		{
			addKillId(aDROPLIST_COND[2]);
		}
		addQuestItem(AlligatorLeather);
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
		for (int[] aDROPLIST_COND : DROPLIST_COND)
		{
			if ((cond == aDROPLIST_COND[0]) && (npcId == aDROPLIST_COND[2]))
			{
				if ((aDROPLIST_COND[3] == 0) || (st.getQuestItemsCount(aDROPLIST_COND[3]) > 0))
				{
					if (aDROPLIST_COND[5] == 0)
					{
						st.rollAndGive(aDROPLIST_COND[4], aDROPLIST_COND[7], aDROPLIST_COND[6]);
					}
					else if (st.rollAndGive(aDROPLIST_COND[4], aDROPLIST_COND[7], aDROPLIST_COND[7], aDROPLIST_COND[5], aDROPLIST_COND[6]))
					{
						if ((aDROPLIST_COND[1] != cond) && (aDROPLIST_COND[1] != 0))
						{
							st.setCond(aDROPLIST_COND[1]);
							st.setState(STARTED);
						}
					}
				}
			}
		}
		return null;
	}
}