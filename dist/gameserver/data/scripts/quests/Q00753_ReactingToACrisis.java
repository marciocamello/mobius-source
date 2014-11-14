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

/**
 * @author Iqman
 */
public class Q00753_ReactingToACrisis extends Quest implements ScriptFile
{
	// Items
	private static final int KEY = 36054;
	// Rewards
	private static final int SCROLL = 36082;
	private static final int BERNA = 33796;
	private static final String GOLEM_KILL = "Golem_kill";
	
	public Q00753_ReactingToACrisis()
	{
		super(false);
		addTalkId(BERNA);
		addQuestItem(KEY);
		addKillId(23270, 23271, 23272, 23272, 23274, 23275, 23276);
		addKillNpcWithLog(1, GOLEM_KILL, 5, 19296);
		addLevelCheck(93, 100);
		addQuestCompletedCheck(Q10386_MysteriousJourney.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "accepted.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "endquest.htm":
				qs.getPlayer().unsetVar("q753doneKill");
				qs.takeAllItems(KEY);
				qs.getPlayer().addExpAndSp(408665250, 40866525);
				qs.giveItems(SCROLL, 1);
				qs.exitCurrentQuest(this);
				qs.playSound(SOUND_FINISH);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		QuestState Mj = qs.getPlayer().getQuestState(Q10386_MysteriousJourney.class);
		
		if ((Mj == null) || !Mj.isCompleted())
		{
			return "You cannot procceed with this quest until you have completed the Mystrerious Journey quest";
		}
		
		if (qs.isNowAvailable())
		{
			switch (qs.getCond())
			{
				case 0:
					htmltext = "start.htm";
					break;
				
				case 1:
					htmltext = "notcollected.htm";
					break;
				
				case 2:
					htmltext = "collected.htm";
					break;
			}
		}
		else
		{
			htmltext = "You have completed this quest today, come back tomorow at 6:30!";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs == null)
		{
			return null;
		}
		
		if (qs.getState() != STARTED)
		{
			return null;
		}
		
		if (qs.getCond() != 1)
		{
			return null;
		}
		
		if ((qs.getQuestItemsCount(KEY) < 30) && Rnd.chance(10))
		{
			qs.giveItems(KEY, 1);
		}
		
		if ((qs.getQuestItemsCount(KEY) >= 30) && qs.getPlayer().getVarB("q753doneKill"))
		{
			qs.setCond(2);
		}
		
		if ((npc.getId() == 19296) && !qs.getPlayer().getVarB("q753doneKill"))
		{
			if (updateKill(npc, qs))
			{
				qs.unset(GOLEM_KILL);
				
				if (qs.getQuestItemsCount(KEY) >= 30)
				{
					qs.setCond(2);
				}
				else
				{
					qs.getPlayer().setVar("q753doneKill", "1", -1);
				}
			}
		}
		
		return null;
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
}