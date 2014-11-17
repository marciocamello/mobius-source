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

public class Q10274_CollectingInTheAir extends Quest implements ScriptFile
{
	// Npc
	private final static int Lekon = 32557;
	// Items
	private final static int ExtractedCoarseRedStarStone = 13858;
	private final static int ExtractedCoarseBlueStarStone = 13859;
	private final static int ExtractedCoarseGreenStarStone = 13860;
	private final static int StarStoneExtractionScroll = 13844;
	// Other
	private final static int ExpertTextStarStoneExtractionSkillLevel1 = 13728;
	
	public Q10274_CollectingInTheAir()
	{
		super(false);
		addStartNpc(Lekon);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("32557-03.htm"))
		{
			qs.setCond(1);
			qs.giveItems(StarStoneExtractionScroll, 8);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int id = qs.getState();
		
		if (id == COMPLETED)
		{
			htmltext = "32557-0a.htm";
		}
		else if (id == CREATED)
		{
			final QuestState state = qs.getPlayer().getQuestState(Q10273_GoodDayToFly.class);
			
			if ((state != null) && state.isCompleted() && (qs.getPlayer().getLevel() >= 75))
			{
				htmltext = "32557-01.htm";
			}
			else
			{
				htmltext = "32557-00.htm";
			}
		}
		else if ((qs.getQuestItemsCount(ExtractedCoarseRedStarStone) + qs.getQuestItemsCount(ExtractedCoarseBlueStarStone) + qs.getQuestItemsCount(ExtractedCoarseGreenStarStone)) >= 8)
		{
			htmltext = "32557-05.htm";
			qs.takeAllItems(ExtractedCoarseRedStarStone, ExtractedCoarseBlueStarStone, ExtractedCoarseGreenStarStone);
			qs.giveItems(ExpertTextStarStoneExtractionSkillLevel1, 1);
			qs.addExpAndSp(6660000, 7375000);
			qs.exitCurrentQuest(false);
			qs.playSound(SOUND_FINISH);
		}
		else
		{
			htmltext = "32557-04.htm";
		}
		
		return htmltext;
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
