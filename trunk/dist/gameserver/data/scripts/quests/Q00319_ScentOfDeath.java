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
public class Q00319_ScentOfDeath extends Quest implements ScriptFile
{
	// NPC
	private static final int MINALESS = 30138;
	// Item
	private static final int HealingPotion = 1060;
	// Quest Item
	private static final int ZombieSkin = 1045;
	
	// Drop Cond
	// # [COND, NEWCOND, ID, REQUIRED, ITEM, NEED_COUNT, CHANCE, DROP]
	private static final int[][] DROPLIST_COND =
	{
		{
			1,
			2,
			20015,
			0,
			ZombieSkin,
			5,
			20,
			1
		},
		{
			1,
			2,
			20020,
			0,
			ZombieSkin,
			5,
			25,
			1
		}
	};
	
	public Q00319_ScentOfDeath()
	{
		super(false);
		
		addStartNpc(MINALESS);
		addTalkId(MINALESS);
		// Mob Drop
		for (int[] aDROPLIST_COND : DROPLIST_COND)
		{
			addKillId(aDROPLIST_COND[2]);
		}
		
		addQuestItem(ZombieSkin);
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
		if (event.equalsIgnoreCase("mina_q0319_04.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getId();
		String htmltext = "noquest";
		int id = st.getState();
		int cond = 0;
		if (id != CREATED)
		{
			cond = st.getCond();
		}
		if (npcId == MINALESS)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() < 11)
				{
					htmltext = "mina_q0319_02.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "mina_q0319_03.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "mina_q0319_05.htm";
			}
			else if ((cond == 2) && (st.getQuestItemsCount(ZombieSkin) >= 5))
			{
				htmltext = "mina_q0319_06.htm";
				st.takeItems(ZombieSkin, -1);
				st.giveItems(ADENA_ID, 3350);
				st.giveItems(HealingPotion, 1);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "mina_q0319_05.htm";
				st.setCond(1);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getId();
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