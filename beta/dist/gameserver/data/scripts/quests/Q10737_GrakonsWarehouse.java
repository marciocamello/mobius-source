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

/**
 * @author: Krash
 */
public class Q10737_GrakonsWarehouse extends Quest implements ScriptFile
{
	// Npcs
	private static final int Grakon = 33947;
	private static final int Katalin = 33943;
	private static final int Ayanthe = 33942;
	// Items
	private static final int Apprentice_Support_Box = 39520;
	private static final int Apprentice_Adventurer_Staff = 7816;
	private static final int Apprentice_Adventurer_Fists = 7819;
	
	public Q10737_GrakonsWarehouse()
	{
		super(false);
		addStartNpc(Katalin, Ayanthe);
		addTalkId(Katalin, Ayanthe, Grakon);
		addQuestItem(Apprentice_Support_Box);
		addLevelCheck(5, 20);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_fighter_cont":
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				qs.giveItems(Apprentice_Support_Box, 1);
				htmltext = "33943-3.htm";
				break;
			
			case "quest_wizard_cont":
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				qs.giveItems(Apprentice_Support_Box, 1);
				htmltext = "33242-3.htm";
				break;
			
			case "qet_rev":
				if (qs.getPlayer().getClassId().getId() == 182) // Ertheia Fighter
				{
					qs.giveItems(57, 11000);
					qs.getPlayer().addExpAndSp(2650, 0);
					qs.giveItems(Apprentice_Adventurer_Fists, 1);
					htmltext = "33947-4.htm";
				}
				else if (qs.getPlayer().getClassId().getId() == 183) // Ertheia Wizard
				{
					qs.giveItems(57, 11000);
					qs.getPlayer().addExpAndSp(2650, 0);
					qs.giveItems(Apprentice_Adventurer_Staff, 1);
					htmltext = "33947-8.htm";
				}
				qs.takeItems(Apprentice_Support_Box, 1);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
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
		
		switch (npcId)
		{
			case Grakon:
				if ((cond == 0) && (qs.getQuestItemsCount(Apprentice_Support_Box) > 0))
				{
					if (qs.getPlayer().getClassId().getId() == 182) // Ertheia Fighter
					{
						htmltext = "33947-1.htm";
					}
					else if (qs.getPlayer().getClassId().getId() == 183) // Ertheia Wizard
					{
						htmltext = "33947-5.htm";
					}
				}
				break;
			
			case Katalin:
				if (cond == 0)
				{
					htmltext = "33943-1.htm";
				}
				break;
			
			case Ayanthe:
				if (cond == 0)
				{
					htmltext = "33942-1.htm";
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