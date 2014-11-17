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

public class Q10311_PeacefulDaysAreOver extends Quest implements ScriptFile
{
	// Npcs
	private static final int SLAKI = 32893;
	private static final int SELINA = 33032;
	
	public Q10311_PeacefulDaysAreOver()
	{
		super(PARTY_NONE);
		addStartNpc(SELINA);
		addTalkId(SLAKI);
		addQuestCompletedCheck(Q10312_AbandonedGodsCreature.class);
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
			case "33031-06.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32893-05.htm":
				qs.addExpAndSp(7168395, 3140085);
				qs.giveItems(57, 489220, true);
				qs.playSound(SOUND_FINISH);
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
		final QuestState previous = player.getQuestState(Q10312_AbandonedGodsCreature.class);
		
		switch (npc.getId())
		{
			case SELINA:
				if ((previous == null) || (!previous.isCompleted()) || (player.getLevel() < 90))
				{
					qs.exitCurrentQuest(true);
					return "33032-03.htm";
				}
				switch (qs.getState())
				{
					case COMPLETED:
						htmltext = "33032-02.htm";
						break;
					
					case CREATED:
						htmltext = "33032-01.htm";
						break;
					
					case STARTED:
						if (qs.getCond() != 1)
						{
							break;
						}
						htmltext = "33032-07.htm";
				}
				break;
			
			case SLAKI:
				if (qs.isStarted())
				{
					if (qs.getCond() == 1)
					{
						htmltext = "32893-01.htm";
					}
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		final QuestState qs = player.getQuestState(Q10311_PeacefulDaysAreOver.class);
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
