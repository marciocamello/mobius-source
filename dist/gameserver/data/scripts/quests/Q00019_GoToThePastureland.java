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

public class Q00019_GoToThePastureland extends Quest implements ScriptFile
{
	// Npcs
	final int VLADIMIR = 31302;
	final int TUNATUN = 31537;
	// Item
	final int BEAST_MEAT = 7547;
	
	public Q00019_GoToThePastureland()
	{
		super(false);
		addStartNpc(VLADIMIR);
		addTalkId(VLADIMIR, TUNATUN);
		addQuestItem(BEAST_MEAT);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "trader_vladimir_q0019_0104.htm":
				qs.giveItems(BEAST_MEAT, 1);
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "beast_herder_tunatun_q0019_0201.htm":
				qs.takeItems(BEAST_MEAT, -1);
				qs.addExpAndSp(1456218, 1672008);
				qs.giveItems(ADENA_ID, 299928);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case VLADIMIR:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() >= 63)
					{
						htmltext = "trader_vladimir_q0019_0101.htm";
					}
					else
					{
						htmltext = "trader_vladimir_q0019_0103.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "trader_vladimir_q0019_0105.htm";
				}
				break;
			
			case TUNATUN:
				if (qs.getQuestItemsCount(BEAST_MEAT) >= 1)
				{
					htmltext = "beast_herder_tunatun_q0019_0101.htm";
				}
				else
				{
					htmltext = "beast_herder_tunatun_q0019_0202.htm";
					qs.exitCurrentQuest(true);
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
