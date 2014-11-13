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

public class Q00031_SecretBuriedInTheSwamp extends Quest implements ScriptFile
{
	// Npcs
	private final static int ABERCROMBIE = 31555;
	private final static int FORGOTTEN_MONUMENT_1 = 31661;
	private final static int FORGOTTEN_MONUMENT_2 = 31662;
	private final static int FORGOTTEN_MONUMENT_3 = 31663;
	private final static int FORGOTTEN_MONUMENT_4 = 31664;
	private final static int CORPSE_OF_DWARF = 31665;
	// Item
	private final static int KRORINS_JOURNAL = 7252;
	
	public Q00031_SecretBuriedInTheSwamp()
	{
		super(false);
		addStartNpc(ABERCROMBIE);
		addTalkId(FORGOTTEN_MONUMENT_1, FORGOTTEN_MONUMENT_2, FORGOTTEN_MONUMENT_3, FORGOTTEN_MONUMENT_4);
		addQuestItem(KRORINS_JOURNAL);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final int cond = qs.getCond();
		String htmltext = event;
		
		switch (event)
		{
			case "31555-1.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "31665-1.htm":
				if (cond == 1)
				{
					qs.setCond(2);
					qs.playSound(SOUND_ITEMGET);
					qs.giveItems(KRORINS_JOURNAL, 1);
				}
				break;
			
			case "31555-4.htm":
				if (cond == 2)
				{
					qs.setCond(3);
				}
				break;
			
			case "31661-1.htm":
				if (cond == 3)
				{
					qs.setCond(4);
				}
				break;
			
			case "31662-1.htm":
				if (cond == 4)
				{
					qs.setCond(5);
				}
				break;
			
			case "31663-1.htm":
				if (cond == 5)
				{
					qs.setCond(6);
				}
				break;
			
			case "31664-1.htm":
				if (cond == 6)
				{
					qs.setCond(7);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "31555-7.htm":
				if (cond == 7)
				{
					qs.takeItems(KRORINS_JOURNAL, -1);
					qs.addExpAndSp(1650970, 1631640);
					qs.giveItems(ADENA_ID, 343430);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		if (qs.isCompleted())
		{
			return "completed";
		}
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case ABERCROMBIE:
				switch (cond)
				{
					case 0:
						if (qs.getPlayer().getLevel() >= 66)
						{
							htmltext = "31555-0.htm";
						}
						else
						{
							htmltext = "31555-0a.htm";
							qs.exitCurrentQuest(true);
						}
						break;
					
					case 1:
						htmltext = "31555-2.htm";
						break;
					
					case 2:
						htmltext = "31555-3.htm";
						break;
					
					case 3:
						htmltext = "31555-5.htm";
						break;
					
					case 7:
						htmltext = "31555-6.htm";
						break;
				}
				break;
			
			case CORPSE_OF_DWARF:
				if (cond == 1)
				{
					htmltext = "31665-0.htm";
				}
				else if (cond == 2)
				{
					htmltext = "31665-2.htm";
				}
				break;
			
			case FORGOTTEN_MONUMENT_1:
				if (cond == 3)
				{
					htmltext = "31661-0.htm";
				}
				else if (cond > 3)
				{
					htmltext = "31661-2.htm";
				}
				break;
			
			case FORGOTTEN_MONUMENT_2:
				if (cond == 4)
				{
					htmltext = "31662-0.htm";
				}
				else if (cond > 4)
				{
					htmltext = "31662-2.htm";
				}
				break;
			
			case FORGOTTEN_MONUMENT_3:
				if (cond == 5)
				{
					htmltext = "31663-0.htm";
				}
				else if (cond > 5)
				{
					htmltext = "31663-2.htm";
				}
				break;
			
			case FORGOTTEN_MONUMENT_4:
				if (cond == 6)
				{
					htmltext = "31664-0.htm";
				}
				else if (cond > 6)
				{
					htmltext = "31664-2.htm";
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
