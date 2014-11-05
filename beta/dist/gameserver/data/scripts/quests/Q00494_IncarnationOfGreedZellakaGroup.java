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

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author cruel
 * @category Daily quest. Party
 * @version http://l2on.net/?c=quests&id=494
 */
public class Q00494_IncarnationOfGreedZellakaGroup extends Quest implements ScriptFile
{
	private static final int KARTIA_RESEARCHER = 33647;
	private static final int KARTIA_RB = 25882;
	private static final int KARTIA_BOX = 34927;
	
	public Q00494_IncarnationOfGreedZellakaGroup()
	{
		super(true);
		addStartNpc(KARTIA_RESEARCHER);
		addTalkId(KARTIA_RESEARCHER);
		addKillId(KARTIA_RB);
		addLevelCheck(85, 89);
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
		if (event.equalsIgnoreCase("33647-04.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("33647-7.htm"))
		{
			st.giveItems(KARTIA_BOX, 1);
			st.setState(COMPLETED);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(this);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getId();
		int cond = st.getCond();
		
		if (npcId == KARTIA_RESEARCHER)
		{
			if (cond == 0)
			{
				if ((st.getPlayer().getLevel() >= 85) && (st.getPlayer().getLevel() < 90))
				{
					if (st.isNowAvailableByTime())
					{
						htmltext = "33647-01.htm";
					}
					else
					{
						htmltext = "33647-08.htm";
					}
				}
				else
				{
					htmltext = "33647-01a.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "33647-05.htm";
			}
			else if (cond == 2)
			{
				htmltext = "33647-06.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		Party party = st.getPlayer().getParty();
		
		if (party != null)
		{
			for (Player member : party.getPartyMembers())
			{
				QuestState qs = member.getQuestState(getClass());
				
				if ((qs != null) && qs.isStarted())
				{
					if ((qs.getCond() == 1) && (npc.getId() == KARTIA_RB))
					{
						qs.setCond(2);
					}
				}
			}
		}
		else
		{
			if ((cond == 1) && (npc.getId() == KARTIA_RB))
			{
				st.setCond(2);
			}
		}
		
		return null;
	}
}