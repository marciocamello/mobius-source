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

public class _651_RunawayYouth extends Quest implements ScriptFile
{
	private static int IVAN = 32014;
	private static int BATIDAE = 31989;
	protected NpcInstance _npc;
	private static int SOE = 736;
	
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
	
	public _651_RunawayYouth()
	{
		super(false);
		addStartNpc(IVAN);
		addTalkId(BATIDAE);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("runaway_boy_ivan_q0651_03.htm"))
		{
			if (st.getQuestItemsCount(SOE) > 0)
			{
				st.setCond(1);
				st.setState(STARTED);
				st.playSound(SOUND_ACCEPT);
				st.takeItems(SOE, 1);
				htmltext = "runaway_boy_ivan_q0651_04.htm";
				st.startQuestTimer("ivan_timer", 20000);
			}
		}
		else if (event.equalsIgnoreCase("runaway_boy_ivan_q0651_05.htm"))
		{
			st.exitCurrentQuest(true);
			st.playSound(SOUND_GIVEUP);
		}
		else if (event.equalsIgnoreCase("ivan_timer"))
		{
			_npc.deleteMe();
			htmltext = null;
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if ((npcId == IVAN) && (cond == 0))
		{
			if (st.getPlayer().getLevel() >= 26)
			{
				htmltext = "runaway_boy_ivan_q0651_01.htm";
			}
			else
			{
				htmltext = "runaway_boy_ivan_q0651_01a.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if ((npcId == BATIDAE) && (cond == 1))
		{
			htmltext = "fisher_batidae_q0651_01.htm";
			st.giveItems(ADENA_ID, Math.round(2883 * st.getRateQuestsReward()));
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
}
