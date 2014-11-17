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

public class Q10309_MysteriousLetter extends Quest implements ScriptFile
{
	// Npcs
	private static final int Alisha = 31303;
	private static final int Advent = 33463;
	private static final int Waizard = 31522;
	private static final int Tifaren = 31334;
	private static final int Letter = 19493;
	
	public Q10309_MysteriousLetter()
	{
		super(false);
		addStartNpc(Advent);
		addTalkId(Advent, Alisha, Waizard, Tifaren);
		addLevelCheck(65, 69);
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
				htmltext = "0-3.htm";
				break;
			
			case "3-2.htm":
				qs.getPlayer().addExpAndSp(4952910, 4894920);
				qs.giveItems(57, 276000);
				qs.takeAllItems(Letter);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "2-2.htm":
				qs.setCond(3);
				qs.takeAllItems(Letter);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(Letter, 1, false);
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
			case Advent:
				if (qs.isCompleted())
				{
					htmltext = "0-c.htm";
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "0-1.htm";
				}
				else if (cond == 1)
				{
					htmltext = "0-3.htm";
				}
				else
				{
					htmltext = TODO_FIND_HTML;
				}
				break;
			
			case Alisha:
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
					htmltext = "1-1.htm";
					qs.giveItems(Letter, 1, false);
					qs.setCond(2);
				}
				else if (cond == 2)
				{
					htmltext = "1-2.htm";
				}
				break;
			
			case Tifaren:
				if (qs.isCompleted())
				{
					htmltext = "2-c.htm";
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 2)
				{
					htmltext = "2-1.htm";
				}
				else if (cond == 3)
				{
					htmltext = "2-3.htm";
				}
				break;
			
			case Waizard:
				if (qs.isCompleted())
				{
					htmltext = "3-c.htm";
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if ((cond == 1) || (cond == 2))
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 3)
				{
					htmltext = "3-1.htm";
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
