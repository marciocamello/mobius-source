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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

public class Q00489_InThisQuietPlace extends Quest implements ScriptFile
{
	// Npcs
	private static final int Bastian = 31280;
	private static final int Adventurer = 33463;
	// Item
	private static final int TraceofEvil = 19501;
	// Monsters
	private static final int[] Monsters =
	{
		21646,
		21647,
		21648,
		21649,
		21650,
		21651
	};
	// Other
	private static final int chance = 50;
	
	public Q00489_InThisQuietPlace()
	{
		super(false);
		addStartNpc(Adventurer);
		addTalkId(Adventurer, Bastian);
		addKillId(Monsters);
		addQuestItem(TraceofEvil);
		addLevelCheck(75, 79);
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
				htmltext = "0-4.htm";
				break;
			
			case "qet_rev":
				htmltext = "1-3.htm";
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
			case Adventurer:
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
						htmltext = "0-nc.htm";
					}
				}
				else if ((cond == 1) || (cond == 2))
				{
					htmltext = "0-5.htm";
				}
				break;
			
			case Bastian:
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
						htmltext = "1-nc.htm";
					}
				}
				else if (cond == 1)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 2)
				{
					htmltext = "1-1.htm";
					qs.getPlayer().addExpAndSp(19890000, 22602330);
					qs.giveItems(57, 426045);
					qs.takeAllItems(TraceofEvil);
					qs.exitCurrentQuest(this);
					qs.playSound(SOUND_FINISH);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && Util.contains(Monsters, npc.getId()) && (qs.getQuestItemsCount(TraceofEvil) < 77))
		{
			qs.rollAndGive(TraceofEvil, 1, chance);
			qs.playSound(SOUND_ITEMGET);
		}
		
		if (qs.getQuestItemsCount(TraceofEvil) >= 77)
		{
			qs.setCond(2);
			qs.playSound(SOUND_MIDDLE);
		}
		
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		final QuestState qs = player.getQuestState(Q00489_InThisQuietPlace.class);
		return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailableByTime());
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
