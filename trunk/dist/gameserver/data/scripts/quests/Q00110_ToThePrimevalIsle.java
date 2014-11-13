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

public class Q00110_ToThePrimevalIsle extends Quest implements ScriptFile
{
	// Npcs
	private static final int ANTON = 31338;
	private static final int MARQUEZ = 32113;
	// Item
	private static final int ANCIENT_BOOK = 8777;
	
	public Q00110_ToThePrimevalIsle()
	{
		super(false);
		addStartNpc(ANTON);
		addTalkId(ANTON, MARQUEZ);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "1":
				htmltext = "scroll_seller_anton_q0110_05.htm";
				qs.setCond(1);
				qs.giveItems(ANCIENT_BOOK, 1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "2":
				if (qs.getQuestItemsCount(ANCIENT_BOOK) > 0)
				{
					htmltext = "marquez_q0110_05.htm";
					qs.playSound(SOUND_FINISH);
					qs.giveItems(ADENA_ID, 189208);
					qs.addExpAndSp(887732, 983212);
					qs.takeItems(ANCIENT_BOOK, -1);
					qs.exitCurrentQuest(false);
				}
				break;
			
			case "3":
				htmltext = "marquez_q0110_06.htm";
				qs.exitCurrentQuest(true);
				break;
		}
		
		return htmltext;
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
		final int npcId = npc.getId();
		
		if (qs.getState() == CREATED)
		{
			if (qs.getPlayer().getLevel() >= 75)
			{
				htmltext = "scroll_seller_anton_q0110_01.htm";
			}
			else
			{
				qs.exitCurrentQuest(true);
				htmltext = "scroll_seller_anton_q0110_02.htm";
			}
		}
		else if (npcId == ANTON)
		{
			if (cond == 1)
			{
				htmltext = "scroll_seller_anton_q0110_07.htm";
			}
		}
		else if (qs.getState() == STARTED)
		{
			if ((npcId == MARQUEZ) && (cond == 1))
			{
				if (qs.getQuestItemsCount(ANCIENT_BOOK) == 0)
				{
					htmltext = "marquez_q0110_07.htm";
				}
				else
				{
					htmltext = "marquez_q0110_01.htm";
				}
			}
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
