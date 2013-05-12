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

public class _494_IncarnationOfGreedZellakaGroup extends Quest implements ScriptFile
{
	public static final int KARTIA_RESEARCH = 33647;
	public static final int CHALAKA = 25882;
	
	public _494_IncarnationOfGreedZellakaGroup()
	{
		super(true);
		addStartNpc(KARTIA_RESEARCH);
		addKillId(CHALAKA);
		addLevelCheck(85, 89);
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("33647-2.htm"))
		{
			st.setCond(1);
			st.setState(2);
			st.playSound("ItemSound.quest_accept");
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		if (npcId == KARTIA_RESEARCH)
		{
			if (state == 1)
			{
				if (!isAvailableFor(st.getPlayer()) || !st.isNowAvailableByTime())
				{
					return "33647-3.htm";
				}
				return "33647.htm";
			}
			if (state == 2)
			{
				if (cond == 1)
				{
					return "33647-2.htm";
				}
				if (cond == 2)
				{
					st.giveItems(34927, 1L);
					st.unset("cond");
					st.playSound("ItemSound.quest_finish");
					st.exitCurrentQuest(this);
					return "33647-4.htm";
				}
			}
		}
		return "noquest";
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
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if ((cond != 1) || (npc == null))
		{
			return null;
		}
		st.setCond(2);
		return null;
	}
}