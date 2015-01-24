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
import lineage2.gameserver.utils.Util;

public class Q10359_SakumsTrace extends Quest implements ScriptFile
{
	private static final int Guild = 31795;
	private static final int Fred = 33179;
	private static final int Reins = 30288;
	private static final int Raimon = 30289;
	private static final int Tobias = 30297;
	private static final int Drikus = 30505;
	private static final int Mendius = 30504;
	private static final int Gershfin = 32196;
	private static final int Elinia = 30155;
	private static final int Ershandel = 30158;
	private static final int Frag = 17586;
	private static final int[] huntl =
	{
		20067,
		20070,
		20072
	};
	private static final int[] hunth =
	{
		23097,
		23098,
		23026,
		20192
	};
	
	public Q10359_SakumsTrace()
	{
		super(false);
		addStartNpc(Guild);
		addTalkId(Guild, Fred, Reins, Raimon, Tobias, Drikus, Mendius, Gershfin, Elinia, Ershandel);
		addKillId(huntl);
		addKillId(hunth);
		addQuestItem(Frag);
		addLevelCheck(34, 40);
		addQuestCompletedCheck(Q10336_DividedSakumKanilov.class);
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
			
			case "qet_rev":
				qs.getPlayer().addExpAndSp(900000, 22000);
				qs.giveItems(57, 1080);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				switch (qs.getPlayer().getRace())
				{
					case human:
						if (qs.getPlayer().isMageClass())
						{
							htmltext = "2-3re.htm";
						}
						else
						{
							htmltext = "2-3r.htm";
						}
						break;
					
					case elf:
						if (qs.getPlayer().isMageClass())
						{
							htmltext = "2-3e.htm";
						}
						else
						{
							htmltext = "2-3ew.htm";
						}
						break;
					
					case darkelf:
						htmltext = "2-3t.htm";
						break;
					
					case orc:
						htmltext = "2-3d.htm";
						break;
					
					case dwarf:
						htmltext = "2-3m.htm";
						break;
					
					case kamael:
						htmltext = "2-3g.htm";
						break;
				}
				break;
			
			case "1-3.htm":
				qs.setCond(2);
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
			case Guild:
				if (qs.isCompleted())
				{
					htmltext = TODO_FIND_HTML;
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "0-1.htm";
				}
				else if ((cond == 1) || (cond == 2) || (cond == 3))
				{
					htmltext = "0-4.htm";
				}
				break;
			
			case Fred:
				if (qs.isCompleted())
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 1)
				{
					htmltext = "1-1.htm";
				}
				else if (cond == 2)
				{
					htmltext = "1-4.htm";
				}
				else if (cond == 3)
				{
					switch (qs.getPlayer().getRace())
					{
						case human:
							if (qs.getPlayer().isMageClass())
							{
								htmltext = "1-5re.htm";
								qs.setCond(4);
								addTalkId(Raimon);
							}
							else
							{
								htmltext = "1-5r.htm";
								qs.setCond(5);
								addTalkId(Reins);
							}
							break;
						
						case elf:
							if (qs.getPlayer().isMageClass())
							{
								htmltext = "1-5e.htm";
								qs.setCond(11);
								addTalkId(Elinia);
							}
							else
							{
								htmltext = "1-5ew.htm";
								qs.setCond(10);
								addTalkId(Ershandel);
							}
							break;
						
						case darkelf:
							htmltext = "1-5t.htm";
							qs.setCond(6);
							addTalkId(Tobias);
							break;
						
						case orc:
							htmltext = "1-5d.htm";
							qs.setCond(7);
							addTalkId(Drikus);
							break;
						
						case dwarf:
							htmltext = "1-5m.htm";
							qs.setCond(8);
							addTalkId(Mendius);
							break;
						
						case kamael:
							htmltext = "1-5g.htm";
							qs.setCond(9);
							addTalkId(Gershfin);
							break;
					}
				}
				break;
			
			case Raimon:
				if ((qs.getPlayer().getRace() == Race.human) && qs.getPlayer().isMageClass())
				{
					if (qs.isCompleted())
					{
						htmltext = "2re-c.htm";
					}
					else if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 4)
					{
						htmltext = "2-1re.htm";
					}
				}
				break;
			
			case Reins:
				if ((qs.getPlayer().getRace() == Race.human) && !qs.getPlayer().isMageClass())
				{
					if (qs.isCompleted())
					{
						htmltext = "2r-c.htm";
					}
					else if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 5)
					{
						htmltext = "2-1r.htm";
					}
				}
				break;
			
			case Tobias:
				if (qs.getPlayer().getRace() == Race.darkelf)
				{
					if (qs.isCompleted())
					{
						htmltext = "2t-c.htm";
					}
					else if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 6)
					{
						htmltext = "2-1t.htm";
					}
				}
				break;
			
			case Drikus:
				if (qs.getPlayer().getRace() == Race.orc)
				{
					if (qs.isCompleted())
					{
						htmltext = "2d-c.htm";
					}
					else if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 7)
					{
						htmltext = "2-1d.htm";
					}
				}
				break;
			
			case Gershfin:
				if (qs.getPlayer().getRace() == Race.kamael)
				{
					if (qs.isCompleted())
					{
						htmltext = "2g-c.htm";
					}
					else if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 9)
					{
						htmltext = "2-1g.htm";
					}
				}
				break;
			
			case Elinia:
				if ((qs.getPlayer().getRace() == Race.elf) && !qs.getPlayer().isMageClass())
				{
					if (qs.isCompleted())
					{
						htmltext = "2ew-c.htm";
					}
					else if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 10)
					{
						htmltext = "2-1e.htm";
					}
				}
				break;
			
			case Ershandel:
				if ((qs.getPlayer().getRace() == Race.elf) && qs.getPlayer().isMageClass())
				{
					if (qs.isCompleted())
					{
						htmltext = "2e-c.htm";
					}
					else if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 11)
					{
						htmltext = "2-1ew.htm";
					}
				}
				break;
			
			case Mendius:
				if (qs.getPlayer().getRace() == Race.dwarf)
				{
					if (qs.isCompleted())
					{
						htmltext = "2m-c.htm";
					}
					else if (cond == 0)
					{
						htmltext = TODO_FIND_HTML;
					}
					else if (cond == 8)
					{
						htmltext = "2-1m.htm";
					}
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		
		if ((qs.getCond() == 2) && (qs.getQuestItemsCount(Frag) < 20))
		{
			if (Util.contains(huntl, npcId))
			{
				qs.rollAndGive(Frag, 1, 15);
			}
			else if (Util.contains(hunth, npcId))
			{
				qs.rollAndGive(Frag, 1, 35);
			}
		}
		
		if (qs.getQuestItemsCount(Frag) >= 20)
		{
			qs.setCond(3);
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
