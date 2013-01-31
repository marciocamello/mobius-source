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

public class _355_FamilyHonor extends Quest implements ScriptFile
{
	private static final int GALIBREDO = 30181;
	private static final int PATRIN = 30929;
	private static final int CHANCE_FOR_GALFREDOS_BUST = 80;
	private static final int CHANCE_FOR_GODDESS_BUST = 30;
	private static final int GALFREDOS_BUST = 4252;
	private static final int BUST_OF_ANCIENT_GODDESS = 4349;
	private static final int WORK_OF_BERONA = 4350;
	private static final int STATUE_PROTOTYPE = 4351;
	private static final int STATUE_ORIGINAL = 4352;
	private static final int STATUE_REPLICA = 4353;
	private static final int STATUE_FORGERY = 4354;
	
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
	
	public _355_FamilyHonor()
	{
		super(true);
		addStartNpc(GALIBREDO);
		addTalkId(PATRIN);
		addKillId(20767);
		addKillId(20768);
		addKillId(20769);
		addKillId(20770);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equals("galicbredo_q0355_03.htm"))
		{
			return htmltext;
		}
		else if (event.equals("galicbredo_q0355_04.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("galicbredo_q0355_07.htm"))
		{
			long count = st.getQuestItemsCount(BUST_OF_ANCIENT_GODDESS);
			st.takeItems(BUST_OF_ANCIENT_GODDESS, count);
			st.giveItems(WORK_OF_BERONA, count);
		}
		else if (event.equals("patrin_q0355_01.htm"))
		{
			return htmltext;
		}
		else if (event.equals("patrin_q0355_01a.htm"))
		{
			return htmltext;
		}
		else if (event.equals("appraise"))
		{
			int appraising = Rnd.get(100);
			if (appraising < 20)
			{
				htmltext = "patrin_q0355_07.htm";
				st.takeItems(WORK_OF_BERONA, 1);
			}
			else if (appraising < 40)
			{
				htmltext = "patrin_q0355_05.htm";
				st.takeItems(WORK_OF_BERONA, 1);
				st.giveItems(STATUE_REPLICA, 1);
			}
			else if (appraising < 60)
			{
				htmltext = "patrin_q0355_04.htm";
				st.takeItems(WORK_OF_BERONA, 1);
				st.giveItems(STATUE_ORIGINAL, 1);
			}
			else if (appraising < 80)
			{
				htmltext = "galicbredo_q0355_10.htm";
				st.takeItems(WORK_OF_BERONA, 1);
				st.giveItems(STATUE_FORGERY, 1);
			}
			else if (appraising < 100)
			{
				htmltext = "galicbredo_q0355_11.htm";
				st.takeItems(WORK_OF_BERONA, 1);
				st.giveItems(STATUE_PROTOTYPE, 1);
			}
		}
		else if (event.equals("galicbredo_q0355_09.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		long count = st.getQuestItemsCount(GALFREDOS_BUST);
		if (npcId == GALIBREDO)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 36)
				{
					htmltext = "galicbredo_q0355_02.htm";
				}
				else
				{
					htmltext = "galicbredo_q0355_01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				if (count > 0)
				{
					long reward = count * 232;
					if (count >= 100)
					{
						reward = reward + 5000;
					}
					st.takeItems(GALFREDOS_BUST, count);
					st.giveItems(ADENA_ID, reward);
					htmltext = "galicbredo_q0355_07a.htm";
				}
				else
				{
					htmltext = "galicbredo_q0355_08.htm";
				}
			}
		}
		else if (npcId == PATRIN)
		{
			if (st.getQuestItemsCount(WORK_OF_BERONA) > 0)
			{
				htmltext = "patrin_q0355_01.htm";
			}
			else
			{
				htmltext = "<html><head><body>You have nothing to appraise.</body></html>";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getCond() == 1)
		{
			if (Rnd.chance(CHANCE_FOR_GALFREDOS_BUST))
			{
				st.giveItems(GALFREDOS_BUST, 1);
				st.playSound(SOUND_ITEMGET);
			}
			if (Rnd.chance(CHANCE_FOR_GODDESS_BUST))
			{
				st.giveItems(BUST_OF_ANCIENT_GODDESS, 1);
			}
		}
		return null;
	}
}
