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

import lineage2.gameserver.enums.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10366_RuinsStatusUpdate extends Quest implements ScriptFile
{
	// Npcs
	private static final int Revian = 32147;
	private static final int Tuk = 32150;
	private static final int Prana = 32153;
	private static final int Devon = 32160;
	private static final int Moka = 32157;
	private static final int Valpor = 32146;
	private static final int Sebion = 32978;
	
	public Q10366_RuinsStatusUpdate()
	{
		super(false);
		addStartNpc(Sebion);
		addTalkId(Sebion, Revian, Tuk, Prana, Devon, Moka, Valpor);
		addLevelCheck(16, 25);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "qet_rev":
				qs.getPlayer().addExpAndSp(150000, 36);
				qs.giveItems(57, 750);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				{
					switch (qs.getPlayer().getRace())
					{
						case human:
							htmltext = "1-5h.htm";
							break;
						
						case elf:
							htmltext = "1-5e.htm";
							break;
						
						case darkelf:
							htmltext = "1-5de.htm";
							break;
						
						case dwarf:
							htmltext = "1-5d.htm";
							break;
						
						case kamael:
							htmltext = "1-5k.htm";
							break;
						
						case orc:
							htmltext = "1-5o.htm";
							break;
					}
				}
				break;
			
			case "quest_ac":
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				switch (qs.getPlayer().getRace())
				{
					case human:
						qs.setCond(2);
						htmltext = "0-4h.htm";
						break;
					
					case elf:
						qs.setCond(3);
						htmltext = "0-4e.htm";
						break;
					
					case darkelf:
						qs.setCond(4);
						htmltext = "0-4de.htm";
						break;
					
					case dwarf:
						qs.setCond(6);
						htmltext = "0-4d.htm";
						break;
					
					case kamael:
						qs.setCond(7);
						htmltext = "0-4k.htm";
						break;
					
					case orc:
						qs.setCond(5);
						htmltext = "0-4o.htm";
						break;
				}
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
			case Sebion:
				if (qs.isCompleted())
				{
					htmltext = TODO_FIND_HTML;
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "start.htm";
				}
				else if ((cond == 2) || (cond == 3) || (cond == 4) || (cond == 5) || (cond == 6) || (cond == 7))
				{
					switch (qs.getPlayer().getRace())
					{
						case human:
							htmltext = "0-5h.htm";
							break;
						
						case elf:
							htmltext = "0-5e.htm";
							break;
						
						case darkelf:
							htmltext = "0-5de.htm";
							break;
						
						case dwarf:
							htmltext = "0-5d.htm";
							break;
						
						case kamael:
							htmltext = "0-5k.htm";
							break;
						
						case orc:
							htmltext = "0-5o.htm";
							break;
					}
				}
				else
				{
					htmltext = "0-nc.htm";
				}
				break;
			
			case Devon:
				if (qs.isCompleted())
				{
					htmltext = "1-5dec.htm";
				}
				else if (qs.getPlayer().getRace() == Race.darkelf)
				{
					if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 4)
					{
						htmltext = "1-3de.htm";
					}
				}
				else
				{
					htmltext = "1-2de.htm";
				}
				break;
			
			case Revian:
				if (qs.isCompleted())
				{
					htmltext = "1-5ec.htm";
				}
				else if (qs.getPlayer().getRace() == Race.elf)
				{
					if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 3)
					{
						htmltext = "1-3e.htm";
					}
				}
				else
				{
					htmltext = "1-2e.htm";
				}
				break;
			
			case Prana:
				if (qs.isCompleted())
				{
					htmltext = "1-5hc.htm";
				}
				else if (qs.getPlayer().getRace() == Race.human)
				{
					if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 2)
					{
						htmltext = "1-3h.htm";
					}
				}
				else
				{
					htmltext = "1-2h.htm";
				}
				break;
			
			case Valpor:
				if (qs.isCompleted())
				{
					htmltext = "1-5kc.htm";
				}
				else if (qs.getPlayer().getRace() == Race.kamael)
				{
					if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 7)
					{
						htmltext = "1-3k.htm";
					}
				}
				else
				{
					htmltext = "1-2k.htm";
				}
				break;
			
			case Moka:
				if (qs.isCompleted())
				{
					htmltext = "1-5dc.htm";
				}
				else if (qs.getPlayer().getRace() == Race.dwarf)
				{
					if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 6)
					{
						htmltext = "1-3d.htm";
					}
				}
				else
				{
					htmltext = "1-2d.htm";
				}
				break;
			
			case Tuk:
				if (qs.isCompleted())
				{
					htmltext = "1-5oc.htm";
				}
				else if (qs.getPlayer().getRace() == Race.orc)
				{
					if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 5)
					{
						htmltext = "1-3o.htm";
					}
				}
				else
				{
					htmltext = "1-2o.htm";
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
