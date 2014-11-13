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

public class Q00020_BringUpWithLove extends Quest implements ScriptFile
{
	// Npc
	private static final int TUNATUN = 31537;
	// Items
	private static final int BEAST_WHIP = 15473;
	private static final int CRYSTAL = 9553;
	private static final int JEWEL = 7185;
	
	public Q00020_BringUpWithLove()
	{
		super(false);
		addStartNpc(TUNATUN);
		addTalkId(TUNATUN);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "31537-12.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "31537-03.htm":
				if (qs.getQuestItemsCount(BEAST_WHIP) > 0)
				{
					return "31537-03a.htm";
				}
				qs.giveItems(BEAST_WHIP, 1);
				break;
			
			case "31537-15.htm":
				qs.unset("cond");
				qs.takeItems(JEWEL, -1);
				qs.addExpAndSp(26950000, 30932000);
				qs.giveItems(CRYSTAL, 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (qs.getState())
		{
			case CREATED:
				if (qs.getPlayer().getLevel() >= 82)
				{
					htmltext = "31537-01.htm";
				}
				else
				{
					htmltext = "31537-00.htm";
				}
				
				break;
			
			case STARTED:
				if (cond == 1)
				{
					htmltext = "31537-13.htm";
				}
				else if (cond == 2)
				{
					htmltext = "31537-14.htm";
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
