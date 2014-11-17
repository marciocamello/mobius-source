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
 * @author blacksmoke
 */
public class Q10741_ADraughtForTheCold extends Quest implements ScriptFile
{
	// Npcs
	private static final int Sivanthe = 33951;
	private static final int Leira = 33952;
	// Monsters
	private static final int Honeybee = 23452;
	private static final int Kiku = 23453;
	private static final int RobustHoneybee = 23484;
	// Items
	private static final int EmptyHoneyJar = 39527;
	private static final int SweetHoney = 39528;
	private static final int NutritiousMeat = 39529;
	
	public Q10741_ADraughtForTheCold()
	{
		super(false);
		addStartNpc(Sivanthe);
		addTalkId(Sivanthe, Leira);
		addKillId(Honeybee, Kiku, RobustHoneybee);
		addQuestItem(EmptyHoneyJar, SweetHoney, NutritiousMeat);
		addLevelCheck(10, 20);
		addClassCheck(182, 183);
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
				qs.giveItems(EmptyHoneyJar, 10);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "33951-3.htm";
				break;
			
			case "qet_rev":
				qs.giveItems(57, 2000);
				qs.takeItems(SweetHoney, 10);
				qs.takeItems(NutritiousMeat, 10);
				htmltext = "33952-2.htm";
				qs.getPlayer().addExpAndSp(22973, 2);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
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
		
		switch (npc.getId())
		{
			case Sivanthe:
				switch (cond)
				{
					case 0:
						if (isAvailableFor(qs.getPlayer()))
						{
							htmltext = "33951-1.htm";
						}
						break;
					
					case 1:
						htmltext = "33951-4.htm";
						break;
					
					default:
						htmltext = "noqu.htm";
						break;
				}
				break;
			
			case Leira:
				if (cond == 2)
				{
					htmltext = "33952-1.htm";
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
			switch (npc.getId())
			{
				case Honeybee:
				case RobustHoneybee:
					if (qs.getQuestItemsCount(EmptyHoneyJar) > 0)
					{
						qs.takeItems(EmptyHoneyJar, 1);
						qs.giveItems(SweetHoney, 1);
						qs.playSound(SOUND_ITEMGET);
					}
					break;
				
				case Kiku:
					if (qs.getQuestItemsCount(NutritiousMeat) < 10)
					{
						qs.giveItems(NutritiousMeat, 1);
						qs.playSound(SOUND_ITEMGET);
					}
					break;
			}
			
			if ((qs.getQuestItemsCount(SweetHoney) >= 10) && (qs.getQuestItemsCount(NutritiousMeat) >= 10))
			{
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
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
