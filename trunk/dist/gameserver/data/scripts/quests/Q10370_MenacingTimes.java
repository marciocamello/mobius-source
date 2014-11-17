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

public class Q10370_MenacingTimes extends Quest implements ScriptFile
{
	private static final int winoin = 30856;
	private static final int andreig = 31292;
	private static final int gerkenshtein = 33648;
	private static final int[] classesav =
	{
		88,
		89,
		90,
		91,
		92,
		93,
		94,
		95,
		96,
		97,
		98,
		99,
		100,
		101,
		102,
		103,
		104,
		105,
		106,
		107,
		108,
		109,
		110,
		111,
		112,
		113,
		114,
		115,
		116,
		117,
		118,
		136,
		135,
		134,
		132,
		133
	};
	private static final int[] mobs =
	{
		21647,
		21649,
		21650
	};
	private static final int chance = 10;
	private static final int Ashes = 34765;
	
	public Q10370_MenacingTimes()
	{
		super(false);
		addStartNpc(winoin);
		addTalkId(winoin, gerkenshtein, andreig);
		addKillId(mobs);
		addQuestItem(Ashes);
		addLevelCheck(76, 81);
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
			
			case "1-3.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "2-2.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int classid = qs.getPlayer().getClassId().getId();
		
		switch (npc.getId())
		{
			case winoin:
				if (qs.isCompleted())
				{
					htmltext = "0-c.htm";
				}
				else if (cond == 0)
				{
					if (isAvailableFor(qs.getPlayer()) && Util.contains(classesav, classid))
					{
						htmltext = "start.htm";
					}
					else
					{
						htmltext = TODO_FIND_HTML;
					}
				}
				else if (cond == 1)
				{
					htmltext = "0-3.htm";
				}
				break;
			
			case andreig:
				if (qs.isCompleted())
				{
					htmltext = "1-c.htm";
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
				break;
			
			case gerkenshtein:
				if (qs.isCompleted())
				{
					htmltext = "2-c.htm";
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 2)
				{
					htmltext = "2-1.htm";
				}
				else if (cond == 3)
				{
					htmltext = "2-4.htm";
				}
				else if (cond == 4)
				{
					htmltext = "2-3.htm";
					qs.getPlayer().addExpAndSp(22451400, 25202500);
					qs.giveItems(57, 479620);
					qs.takeAllItems(Ashes);
					qs.exitCurrentQuest(false);
					qs.playSound(SOUND_FINISH);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 3) && Util.contains(mobs, npc.getId()) && (qs.getQuestItemsCount(Ashes) < 30))
		{
			qs.rollAndGive(Ashes, 1, chance);
			qs.playSound(SOUND_ITEMGET);
		}
		
		if (qs.getQuestItemsCount(Ashes) >= 30)
		{
			qs.setCond(4);
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
