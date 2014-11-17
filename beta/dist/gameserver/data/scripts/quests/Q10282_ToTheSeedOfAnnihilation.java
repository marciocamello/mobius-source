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

public class Q10282_ToTheSeedOfAnnihilation extends Quest implements ScriptFile
{
	// Npcs
	private final static int KBALDIR = 32733;
	private final static int KLEMIS = 32734;
	// Item
	private final static int SOA_ORDERS = 15512;
	
	public Q10282_ToTheSeedOfAnnihilation()
	{
		super(false);
		addStartNpc(KBALDIR);
		addTalkId(KBALDIR);
		addTalkId(KLEMIS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "32733-07.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.giveItems(SOA_ORDERS, 1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32734-02.htm":
				qs.unset("cond");
				qs.giveItems(57, 212182);
				qs.addExpAndSp(1148480, 99110);
				qs.takeItems(SOA_ORDERS, -1);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int npcId = npc.getId();
		
		switch (qs.getState())
		{
			case COMPLETED:
				if (npcId == KBALDIR)
				{
					htmltext = "32733-09.htm";
				}
				else if (npcId == KLEMIS)
				{
					htmltext = "32734-03.htm";
				}
				break;
			
			case CREATED:
				if (qs.getPlayer().getLevel() >= 84)
				{
					htmltext = "32733-01.htm";
				}
				else
				{
					htmltext = "32733-00.htm";
				}
				break;
			
			default:
				if (qs.getCond() == 1)
				{
					if (npcId == KBALDIR)
					{
						htmltext = "32733-08.htm";
					}
					else if (npcId == KLEMIS)
					{
						htmltext = "32734-01.htm";
					}
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
