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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10315_ToThePrisonOfDarkness extends Quest implements ScriptFile
{
	// Npcs
	private static final int SLAKI = 32893;
	private static final int OPERA = 32946;
	
	public Q10315_ToThePrisonOfDarkness()
	{
		super(false);
		addStartNpc(SLAKI);
		addTalkId(OPERA);
		addLevelCheck(90, 99);
		addQuestCompletedCheck(Q10306_TheCorruptedLeader.class);
		addQuestCompletedCheck(Q10311_PeacefulDaysAreOver.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (qs == null)
		{
			return "noquest";
		}
		
		switch (event)
		{
			case "32893-06.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32946-05.htm":
				qs.playSound(SOUND_FINISH);
				qs.addExpAndSp(4038093, 1708398);
				qs.giveItems(57, 279513, true);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		if (qs == null)
		{
			return htmltext;
		}
		
		final Player player = qs.getPlayer();
		final QuestState previous = player.getQuestState(Q10306_TheCorruptedLeader.class);
		final QuestState previous2 = player.getQuestState(Q10311_PeacefulDaysAreOver.class);
		
		switch (npc.getId())
		{
			case SLAKI:
				if ((previous == null) || (!previous.isCompleted()) || (previous2 == null) || (!previous2.isCompleted()) || (player.getLevel() < 90))
				{
					qs.exitCurrentQuest(true);
					return "32893-03.htm";
				}
				switch (qs.getState())
				{
					case COMPLETED:
						htmltext = "32893-04.htm";
						break;
					
					case CREATED:
						htmltext = "32893-01.htm";
						break;
					
					case STARTED:
						if (qs.getCond() != 1)
						{
							break;
						}
						
						htmltext = "32893-07.htm";
				}
				break;
			
			case OPERA:
				if (qs.isStarted())
				{
					if (qs.getCond() == 1)
					{
						htmltext = "32946-01.htm";
					}
				}
				else if (qs.isCompleted())
				{
					htmltext = "32946-03.htm";
				}
				else
				{
					htmltext = "32946-02.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		final QuestState qs = player.getQuestState(Q10315_ToThePrisonOfDarkness.class);
		return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailableByTime());
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
