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

public class Q00459_TheVillainOfTheUndergroundMineTeredor extends Quest implements ScriptFile
{
	// Npc
	private static final int FILAUR = 30535;
	// Monster
	private static final int TEREDOR = 25785;
	// Item
	private static final int PROOF_OF_FIDELITY = 19450;
	
	public Q00459_TheVillainOfTheUndergroundMineTeredor()
	{
		super(PARTY_ALL);
		addStartNpc(FILAUR);
		addKillId(TEREDOR);
		addLevelCheck(85, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30535-04.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "30535-07.htm":
				qs.giveItems(PROOF_OF_FIDELITY, 20);
				qs.playSound("ItemSound.quest_finish");
				qs.exitCurrentQuest(this);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (qs.getState())
		{
			default:
				break;
			
			case COMPLETED:
				htmltext = "completed";
				break;
			
			case CREATED:
				if (qs.getPlayer().getLevel() >= 85)
				{
					if (isAvailableFor(qs.getPlayer()))
					{
						htmltext = "30535-00.htm";
					}
					else
					{
						htmltext = "daily";
					}
				}
				else
				{
					htmltext = ""; // low level;
				}
				break;
			
			case STARTED:
				if (qs.getCond() == 1)
				{
					htmltext = "30535-05.htm";
					break;
				}
				
				if (qs.getCond() == 2)
				{
					htmltext = "30535-06.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			qs.setCond(2);
		}
		
		return null;
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
