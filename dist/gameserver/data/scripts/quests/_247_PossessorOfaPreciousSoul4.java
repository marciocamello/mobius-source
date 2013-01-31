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

import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _247_PossessorOfaPreciousSoul4 extends Quest implements ScriptFile
{
	private static int CARADINE = 31740;
	private static int LADY_OF_LAKE = 31745;
	private static int CARADINE_LETTER_LAST = 7679;
	private static int NOBLESS_TIARA = 7694;
	
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
	
	public _247_PossessorOfaPreciousSoul4()
	{
		super(false);
		addStartNpc(CARADINE);
		addTalkId(LADY_OF_LAKE);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int cond = st.getCond();
		if ((cond == 0) && event.equals("caradine_q0247_03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (cond == 1)
		{
			if (event.equals("caradine_q0247_04.htm"))
			{
				return htmltext;
			}
			else if (event.equals("caradine_q0247_05.htm"))
			{
				st.setCond(2);
				st.takeItems(CARADINE_LETTER_LAST, 1);
				st.getPlayer().teleToLocation(143230, 44030, -3030);
				return htmltext;
			}
		}
		else if (cond == 2)
		{
			if (event.equals("caradine_q0247_06.htm"))
			{
				return htmltext;
			}
			else if (event.equals("caradine_q0247_05.htm"))
			{
				st.getPlayer().teleToLocation(143230, 44030, -3030);
				return htmltext;
			}
			else if (event.equals("lady_of_the_lake_q0247_02.htm"))
			{
				return htmltext;
			}
			else if (event.equals("lady_of_the_lake_q0247_03.htm"))
			{
				return htmltext;
			}
			else if (event.equals("lady_of_the_lake_q0247_04.htm"))
			{
				return htmltext;
			}
			else if (event.equals("lady_of_the_lake_q0247_05.htm"))
			{
				if (st.getPlayer().getLevel() >= 75)
				{
					st.giveItems(NOBLESS_TIARA, 1);
					st.addExpAndSp(93836, 0);
					st.playSound(SOUND_FINISH);
					st.unset("cond");
					st.exitCurrentQuest(false);
					Olympiad.addNoble(st.getPlayer());
					st.getPlayer().setNoble(true);
					st.getPlayer().updatePledgeClass();
					st.getPlayer().updateNobleSkills();
					st.getPlayer().sendSkillList();
					st.getPlayer().broadcastUserInfo(true);
				}
				else
				{
					htmltext = "lady_of_the_lake_q0247_06.htm";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		if (!st.getPlayer().isBaseClassActive())
		{
			return "Base class only!";
		}
		if (st.getPlayer().getSubClassList().size() < 2)
		{
			return "You do not have subclass!";
		}
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int id = st.getState();
		int cond = st.getCond();
		if (npcId == CARADINE)
		{
			QuestState previous = st.getPlayer().getQuestState(_246_PossessorOfaPreciousSoul3.class);
			if ((id == CREATED) && (previous != null) && (previous.getState() == COMPLETED))
			{
				if (st.getPlayer().getLevel() < 75)
				{
					htmltext = "caradine_q0247_02.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "caradine_q0247_01.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "caradine_q0247_03.htm";
			}
			else if (cond == 2)
			{
				htmltext = "caradine_q0247_06.htm";
			}
		}
		else if ((npcId == LADY_OF_LAKE) && (cond == 2))
		{
			if (st.getPlayer().getLevel() >= 75)
			{
				htmltext = "lady_of_the_lake_q0247_01.htm";
			}
			else
			{
				htmltext = "lady_of_the_lake_q0247_06.htm";
			}
		}
		return htmltext;
	}
}
