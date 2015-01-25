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

/**
 * @author Yorie
 */
// TODO: General, this quest should be checked for all races. Now it's checked only for elves
public class Q10325_SearchingForNewPower extends Quest implements ScriptFile
{
	private final static int GALLINT = 32980;
	/* Race depended talk NPCs */
	private final static int TALBOT = 32156; // Human
	private final static int CINDET = 32148; // Elves
	private final static int BLACK = 32161; // Dark Elves
	private final static int HERZ = 32151; // Orcs
	private final static int KINCAID = 32159; // Dwarves
	private final static int XONIA = 32144; // Kamael
	/* ~Race dependent talk NPCs~ */
	private final static int SPIRITSHOT = 2509;
	private final static int SOULSHOT = 1835;
	
	public Q10325_SearchingForNewPower()
	{
		super(false);
		addStartNpc(GALLINT);
		addTalkId(GALLINT, BLACK, KINCAID, CINDET, TALBOT, HERZ, XONIA);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("quest_accept"))
		{
			htmltext = "gallint_q10325_3.htm";
			final Race race = qs.getPlayer().getRace();
			
			switch (race)
			{
				case darkelf:
					htmltext = "gallint_q10325_3_darkelves.htm";
					qs.setCond(4);
					break;
				
				case dwarf:
					htmltext = "gallint_q10325_3_dwarves.htm";
					qs.setCond(6);
					break;
				
				case elf:
					htmltext = "gallint_q10325_3_elves.htm";
					qs.setCond(3);
					break;
				
				case human:
					htmltext = "gallint_q10325_3_human.htm";
					qs.setCond(2);
					break;
				
				case kamael:
					htmltext = "gallint_q10325_3_kamael.htm";
					qs.setCond(7);
					break;
				
				case orc:
					htmltext = "gallint_q10325_3_orcs.htm";
					qs.setCond(5);
					break;
			}
			
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final QuestState reqQuest = qs.getPlayer().getQuestState("Q10324_FindingMagisterGallint");
		final Race race = qs.getPlayer().getRace();
		// Add talk NPC by player race
		/*
		 * if(cond == 0 && !talkerAdded) { talkerAdded = true; if(race == Race.DARKELF) addTalkId(BLACK); else if(race == Race.DWARF) addTalkId(KINCAID); else if(race == Race.ELF) addTalkId(CINDET); else if(race == Race.HUMAN) addTalkId(TALBOT); else if(race == Race.KAMAEL) addTalkId(XONIA); else
		 * if(race == Race.ORC) addTalkId(HERZ); addTalkId(GALLINT); }
		 */
		final int currentCond = qs.getCond();
		
		switch (npc.getId())
		{
			case GALLINT:
				htmltext = "gallint_q10325_1.htm";
				
				if (cond >= 8)
				{
					htmltext = "gallint_q10325_4.htm";
					qs.giveItems(57, 120);
					qs.getPlayer().addExpAndSp(4654, 5);
					qs.playSound(SOUND_FINISH);
					
					if (qs.getPlayer().isMageClass())
					{
						qs.giveItems(SPIRITSHOT, 1000);
					}
					else
					{
						qs.giveItems(SOULSHOT, 1000);
					}
					
					qs.exitCurrentQuest(false);
				}
				else if (cond > 0)
				{
					htmltext = "gallint_q10325_taken.htm";
				}
				else if ((reqQuest == null) || !reqQuest.isCompleted())
				{
					htmltext = "You need to complete quest Finding Magister Gallint"; // TODO: Unknown text here
				}
				break;
			
			case TALBOT:
				if (race == Race.human)
				{
					if (currentCond == 2)
					{
						htmltext = "talbot_q10325_1.htm";
						qs.setCond(qs.getCond() + 6);
					}
					else
					{
						htmltext = "talbot_q10325_taken.htm";
					}
				}
				break;
			
			case CINDET:
				if (race == Race.elf)
				{
					if (currentCond == 3)
					{
						htmltext = "cindet_q10325_1.htm";
						qs.setCond(qs.getCond() + 6);
					}
					else
					{
						htmltext = "cindet_q10325_taken.htm";
					}
				}
				break;
			
			case BLACK:
				if (race == Race.darkelf)
				{
					if (currentCond == 4)
					{
						htmltext = "black_q10325_1.htm";
						qs.setCond(qs.getCond() + 6);
					}
					else
					{
						htmltext = "black_q10325_taken.htm";
					}
				}
				break;
			
			case HERZ:
				if (race == Race.orc)
				{
					if (currentCond == 5)
					{
						htmltext = "herz_q10325_1.htm";
						qs.setCond(qs.getCond() + 6);
					}
					else
					{
						htmltext = "herz_q10325_taken.htm";
					}
				}
				break;
			
			case KINCAID:
				if (race == Race.dwarf)
				{
					if (currentCond == 6)
					{
						htmltext = "kincaid_q10325_1.htm";
						qs.setCond(qs.getCond() + 6);
					}
					else
					{
						htmltext = "kincaid_q10325_taken.htm";
					}
				}
				break;
			
			case XONIA:
				if (race == Race.kamael)
				{
					if (currentCond == 7)
					{
						htmltext = "xonia_q10325_1.htm";
						qs.setCond(qs.getCond() + 6);
					}
					else
					{
						htmltext = "xonia_q10325_taken.htm";
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