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

public class Q10269_ToTheSeedOfDestruction extends Quest implements ScriptFile
{
	// Npcs
	private final static int Keucereus = 32548;
	private final static int Allenos = 32526;
	// Item
	private final static int Introduction = 13812;
	
	public Q10269_ToTheSeedOfDestruction()
	{
		super(false);
		addStartNpc(Keucereus);
		addTalkId(Allenos);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("32548-05.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
			qs.giveItems(Introduction, 1);
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
				if (npcId == Allenos)
				{
					htmltext = "32526-02.htm";
				}
				else
				{
					htmltext = "32548-0a.htm";
				}
				break;
			
			case CREATED:
				if (npcId == Keucereus)
				{
					if (qs.getPlayer().getLevel() < 75)
					{
						htmltext = "32548-00.htm";
					}
					else
					{
						htmltext = "32548-01.htm";
					}
				}
				break;
			
			case STARTED:
				if (npcId == Keucereus)
				{
					htmltext = "32548-06.htm";
				}
				else if (npcId == Allenos)
				{
					htmltext = "32526-01.htm";
					qs.giveItems(ADENA_ID, 710000);
					qs.addExpAndSp(6660000, 7375000);
					qs.exitCurrentQuest(false);
					qs.playSound(SOUND_FINISH);
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
