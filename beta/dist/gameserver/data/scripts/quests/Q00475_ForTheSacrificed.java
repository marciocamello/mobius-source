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
import lineage2.gameserver.utils.Util;

public class Q00475_ForTheSacrificed extends Quest implements ScriptFile
{
	// Npcs
	private static final int ADVENT = 33463;
	private static final int ROSS = 30858;
	// Items
	private static final int ASH = 19495;
	// Monsters
	private static final int[] MONSTERS =
	{
		20676,
		20677,
		21108,
		21109,
		21110,
		21111,
		21112,
		21113,
		21114,
		21115,
		21116
	};
	// Others
	private static final int chance = 80;
	
	public Q00475_ForTheSacrificed()
	{
		super(PARTY_ONE);
		addStartNpc(ADVENT);
		addTalkId(ADVENT, ROSS);
		addKillId(MONSTERS);
		addQuestItem(ASH);
		addLevelCheck(65, 69);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("quest_ac"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
			htmltext = "0-4.htm";
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
			case ADVENT:
				if (cond == 0)
				{
					if (isAvailableFor(qs.getPlayer()))
					{
						if (qs.isNowAvailableByTime())
						{
							htmltext = "0-1.htm";
						}
						else
						{
							htmltext = "0-c.htm";
						}
					}
					else
					{
						htmltext = TODO_FIND_HTML;
					}
				}
				else if ((cond == 1) || (cond == 2))
				{
					htmltext = "0-5.htm";
				}
				break;
			
			case ROSS:
				if (cond == 0)
				{
					if (isAvailableFor(qs.getPlayer()))
					{
						if (qs.isNowAvailableByTime())
						{
							htmltext = TODO_FIND_HTML;
						}
						else
						{
							htmltext = "1-c.htm";
						}
					}
					else
					{
						htmltext = TODO_FIND_HTML;
					}
				}
				else if (cond == 1)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 2)
				{
					htmltext = "1-1.htm";
					qs.takeAllItems(ASH);
					qs.exitCurrentQuest(this);
					qs.playSound(SOUND_FINISH);
					qs.getPlayer().addExpAndSp(3904500, 2813550);
					qs.giveItems(57, 118500);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && Util.contains(MONSTERS, npc.getId()) && (qs.getQuestItemsCount(ASH) < 30))
		{
			qs.rollAndGive(ASH, 1, chance);
			qs.playSound(SOUND_ITEMGET);
		}
		
		if (qs.getQuestItemsCount(ASH) >= 30)
		{
			qs.setCond(2);
			qs.playSound(SOUND_MIDDLE);
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
