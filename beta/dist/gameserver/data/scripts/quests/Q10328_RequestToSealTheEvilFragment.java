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

public class Q10328_RequestToSealTheEvilFragment extends Quest implements ScriptFile
{
	// Npcs
	private static final int Panteleon = 32972;
	private static final int Kakai = 30565;
	// Item
	private static final int Evil = 17577;
	
	public Q10328_RequestToSealTheEvilFragment()
	{
		super(false);
		addStartNpc(Panteleon);
		addTalkId(Panteleon);
		addTalkId(Kakai);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(Q10327_IntruderWhoWantsTheBookOfGiants.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "0-4.htm";
				if (qs.getQuestItemsCount(Evil) < 1)
				{
					qs.giveItems(Evil, 1, false);
				}
				break;
			
			case "qet_rev":
				htmltext = "1-3.htm";
				qs.getPlayer().addExpAndSp(13000, 5);
				qs.giveItems(57, 20000);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case Panteleon:
				if (qs.isCompleted())
				{
					htmltext = "0-c.htm";
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "start.htm";
				}
				else if (cond == 1)
				{
					htmltext = "0-5.htm";
				}
				else
				{
					htmltext = "0-nc.htm";
				}
				break;
			
			case Kakai:
				if (qs.isCompleted())
				{
					htmltext = "1-c.htm";
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 1)
				{
					qs.takeAllItems(Evil);
					htmltext = "1-1.htm";
				}
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
