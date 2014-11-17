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

public class Q10372_PurgatoryVolvere extends Quest implements ScriptFile
{
	private static final int andreig = 31292;
	private static final int gerkenshtein = 33648;
	private static final int Essence = 34766;
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
	private static final int Bloody = 23185;
	private static final int chance = 10;
	
	public Q10372_PurgatoryVolvere()
	{
		super(false);
		addStartNpc(gerkenshtein);
		addTalkId(gerkenshtein, andreig);
		addKillId(Bloody);
		addQuestItem(Essence);
		addLevelCheck(76, 81);
		addQuestCompletedCheck(Q10371_GraspThyPower.class);
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
			
			case "firec":
				htmltext = "1-4.htm";
				qs.getPlayer().addExpAndSp(23009000, 26440100);
				qs.giveItems(9552, 1);
				qs.takeAllItems(Essence);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "waterc":
				htmltext = "1-4.htm";
				qs.getPlayer().addExpAndSp(23009000, 26440100);
				qs.giveItems(9553, 1);
				qs.takeAllItems(Essence);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "windc":
				htmltext = "1-4.htm";
				qs.getPlayer().addExpAndSp(23009000, 26440100);
				qs.giveItems(9555, 1);
				qs.takeAllItems(Essence);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "earth":
				htmltext = "1-4.htm";
				qs.getPlayer().addExpAndSp(23009000, 26440100);
				qs.giveItems(9554, 1);
				qs.takeAllItems(Essence);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "darkc":
				htmltext = "1-4.htm";
				qs.getPlayer().addExpAndSp(23009000, 26440100);
				qs.giveItems(9556, 1);
				qs.takeAllItems(Essence);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "holyc":
				htmltext = "1-4.htm";
				qs.getPlayer().addExpAndSp(23009000, 26440100);
				qs.giveItems(9557, 1);
				qs.takeAllItems(Essence);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
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
			case gerkenshtein:
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
					htmltext = "0-5.htm";
				}
				else if (cond == 2)
				{
					htmltext = "0-6.htm";
					qs.setCond(3);
					qs.giveItems(34767, 1, false);
					qs.takeAllItems(Essence);
					qs.playSound(SOUND_MIDDLE);
				}
				else if (cond == 3)
				{
					htmltext = "0-3.htm";
				}
				break;
			
			case andreig:
				if (qs.isCompleted())
				{
					htmltext = "1-4.htm";
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 1)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 2)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 3)
				{
					htmltext = "1-1.htm";
					qs.takeAllItems(34767);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && (npc.getId() == Bloody) && (qs.getQuestItemsCount(Essence) < 10))
		{
			qs.rollAndGive(Essence, 1, chance);
			qs.playSound(SOUND_ITEMGET);
		}
		
		if (qs.getQuestItemsCount(Essence) >= 10)
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
