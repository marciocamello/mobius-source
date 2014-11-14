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

public class Q00694_BreakThroughTheHallOfSuffering extends Quest implements ScriptFile
{
	// Npc
	private static final int TEPIOS = 32603;
	// Items
	private static final int MARK_OF_KEUCEREUS_STAGE_1 = 13691;
	private static final int MARK_OF_KEUCEREUS_STAGE_2 = 13692;
	
	public Q00694_BreakThroughTheHallOfSuffering()
	{
		super(PARTY_ALL);
		addStartNpc(TEPIOS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("32603-04.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final Player player = qs.getPlayer();
		
		switch (qs.getCond())
		{
			case 0:
				if ((player.getLevel() < 75) || (player.getLevel() > 82))
				{
					if ((qs.getQuestItemsCount(MARK_OF_KEUCEREUS_STAGE_1) == 0) && (qs.getQuestItemsCount(MARK_OF_KEUCEREUS_STAGE_2) == 0) && (player.getLevel() > 82))
					{
						qs.giveItems(MARK_OF_KEUCEREUS_STAGE_1, 1);
					}
					
					qs.exitCurrentQuest(true);
					htmltext = "32603-00.htm";
				}
				else
				{
					htmltext = "32603-01.htm";
				}
				break;
			
			case 1:
				htmltext = "32603-05.htm";
				break;
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
