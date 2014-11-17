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

public class Q10317_OrbisWitch extends Quest implements ScriptFile
{
	// Npcs
	private static final int OPERA = 32946;
	private static final int LYDIA = 32892;
	
	public Q10317_OrbisWitch()
	{
		super(PARTY_NONE);
		addStartNpc(OPERA);
		addTalkId(LYDIA);
		addLevelCheck(95, 99);
		addQuestCompletedCheck(Q10316_UndecayingMemoryOfThePast.class);
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
			case "32946-08.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32892-02.htm":
				qs.playSound(SOUND_FINISH);
				qs.addExpAndSp(74128050, 3319695);
				qs.giveItems(ADENA_ID, 506760, true);
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
		final QuestState previous = player.getQuestState(Q10316_UndecayingMemoryOfThePast.class);
		
		switch (npc.getId())
		{
			case OPERA:
				if ((previous == null) || (!previous.isCompleted()) || (player.getLevel() < 95))
				{
					qs.exitCurrentQuest(true);
					return "32946-03.htm";
				}
				switch (qs.getState())
				{
					case COMPLETED:
						htmltext = "32946-02.htm";
						break;
					
					case CREATED:
						htmltext = "32946-01.htm";
						break;
					
					case STARTED:
						if (qs.getCond() != 1)
						{
							break;
						}
						htmltext = "32946-09.htm";
						break;
				}
				break;
			
			case LYDIA:
				if (qs.isStarted())
				{
					if (qs.getCond() == 1)
					{
						htmltext = "32892-01.htm";
					}
				}
				else if (qs.isCompleted())
				{
					htmltext = "32892-03.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		final QuestState qs = player.getQuestState(Q10317_OrbisWitch.class);
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
