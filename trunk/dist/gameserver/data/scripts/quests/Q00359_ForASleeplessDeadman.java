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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00359_ForASleeplessDeadman extends Quest implements ScriptFile
{
	// Npc
	private static final int ORVEN = 30857;
	// Monsters
	private static final int DOOMSERVANT = 21006;
	private static final int DOOMGUARD = 21007;
	private static final int DOOMARCHER = 21008;
	private static final int DOOMTROOPER = 21009;
	// Items
	private static final int REMAINS = 5869;
	private static final int PhoenixEarrPart = 6341;
	private static final int MajEarrPart = 6342;
	private static final int PhoenixNeclPart = 6343;
	private static final int MajNeclPart = 6344;
	private static final int PhoenixRingPart = 6345;
	private static final int MajRingPart = 6346;
	private static final int DarkCryShieldPart = 5494;
	private static final int NightmareShieldPart = 5495;
	// Others
	private static final int DROP_RATE = 10;
	private static final int REQUIRED = 60;
	
	public Q00359_ForASleeplessDeadman()
	{
		super(false);
		addStartNpc(ORVEN);
		addKillId(DOOMSERVANT, DOOMGUARD, DOOMARCHER, DOOMTROOPER);
		addQuestItem(REMAINS);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30857-06.htm":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "30857-07.htm":
				qs.exitCurrentQuest(true);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "30857-08.htm":
				qs.setCond(1);
				int chance = Rnd.get(100);
				int item;
				if (chance <= 16)
				{
					item = PhoenixNeclPart;
				}
				else if (chance <= 33)
				{
					item = PhoenixEarrPart;
				}
				else if (chance <= 50)
				{
					item = PhoenixRingPart;
				}
				else if (chance <= 58)
				{
					item = MajNeclPart;
				}
				else if (chance <= 67)
				{
					item = MajEarrPart;
				}
				else if (chance <= 76)
				{
					item = MajRingPart;
				}
				else if (chance <= 84)
				{
					item = DarkCryShieldPart;
				}
				else
				{
					item = NightmareShieldPart;
				}
				qs.giveItems(item, 4, true);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int id = qs.getState();
		
		if (id == CREATED)
		{
			if (qs.getPlayer().getLevel() < 60)
			{
				qs.exitCurrentQuest(true);
				htmltext = "30857-01.htm";
			}
			else
			{
				htmltext = "30857-02.htm";
			}
		}
		else if (id == STARTED)
		{
			if (cond == 3)
			{
				htmltext = "30857-03.htm";
			}
			else if ((cond == 2) && (qs.getQuestItemsCount(REMAINS) >= REQUIRED))
			{
				qs.takeItems(REMAINS, REQUIRED);
				qs.setCond(3);
				htmltext = "30857-04.htm";
			}
		}
		else
		{
			htmltext = "30857-05.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final long count = qs.getQuestItemsCount(REMAINS);
		
		if ((count < REQUIRED) && Rnd.chance(DROP_RATE))
		{
			qs.giveItems(REMAINS, 1);
			
			if ((count + 1) >= REQUIRED)
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(2);
			}
			else
			{
				qs.playSound(SOUND_ITEMGET);
			}
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
