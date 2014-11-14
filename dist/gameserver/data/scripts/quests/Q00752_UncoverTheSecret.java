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
public class Q00752_UncoverTheSecret extends Quest implements ScriptFile
{
	// Items
	private static final int SOUL = 36074;
	private static final int INIE = 36075;
	// Rewards
	private static final int SCROLL = 36082;
	private static final int HESET = 33780;
	
	public Q00752_UncoverTheSecret()
	{
		super(false);
		addTalkId(HESET);
		addQuestItem(SOUL, INIE);
		addKillId(23252, 23253, 23254, 23257, 23255, 23256, 23258, 23259);
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
				qs.takeAllItems(SOUL);
				qs.takeAllItems(INIE);
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
		final QuestState Mj = qs.getPlayer().getQuestState(Q10386_MysteriousJourney.class);
		
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
		
		if ((qs.getQuestItemsCount(SOUL) < 10) && Rnd.chance(10))
		{
			qs.giveItems(SOUL, 1);
		}
		
		if ((qs.getQuestItemsCount(INIE) < 20) && Rnd.chance(10))
		{
			qs.giveItems(INIE, 1);
		}
		
		if ((qs.getQuestItemsCount(SOUL) >= 10) && (qs.getQuestItemsCount(INIE) >= 20))
		{
			qs.setCond(2);
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