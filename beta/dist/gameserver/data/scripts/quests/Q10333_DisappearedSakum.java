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

public class Q10333_DisappearedSakum extends Quest implements ScriptFile
{
	private static final int Shnain = 33508;
	private static final int Bent = 33176;
	private static final int Batis = 30332;
	private static final int Lizard = 20030;
	private static final int Vooko = 20017;
	private static final int Spider = 23094;
	private static final int Bigspider = 20038;
	private static final int Arach = 20050;
	private static final String Vooko_item = "vooko";
	private static final String Lizard_item = "lizard";
	private static final int Mark = 17583;
	
	public Q10333_DisappearedSakum()
	{
		super(false);
		addStartNpc(Batis);
		addTalkId(Batis, Bent, Shnain);
		addKillNpcWithLog(2, Vooko_item, 5, Vooko);
		addKillNpcWithLog(2, Lizard_item, 7, Lizard);
		addKillId(Spider, Bigspider, Arach);
		addQuestItem(Mark);
		addLevelCheck(18, 40);
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
				htmltext = "0-5.htm";
				break;
			
			case "qet_rev":
				htmltext = "2-3.htm";
				qs.getPlayer().addExpAndSp(130000, 50000);
				qs.giveItems(57, 80000);
				qs.takeAllItems(Mark);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "1-3.htm":
				qs.setCond(2);
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
		
		switch (npc.getId())
		{
			case Batis:
				if (qs.isCompleted())
				{
					htmltext = "0-c.htm";
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "start.htm";
				}
				else if ((cond == 1) || (cond == 2) || (cond == 3))
				{
					htmltext = "0-6.htm";
				}
				else
				{
					htmltext = "0-nc.htm";
				}
				break;
			
			case Bent:
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
				else if ((cond == 2) || (cond == 3))
				{
					htmltext = "1-3.htm";
				}
				break;
			
			case Shnain:
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
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 2)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 3)
				{
					htmltext = "2-1.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		switch (npc.getId())
		{
			case Spider:
			case Bigspider:
			case Arach:
				if ((qs.getQuestItemsCount(Mark) < 5) && (qs.getCond() == 2))
				{
					qs.giveItems(Mark, 1, false);
				}
				break;
		}
		
		if (updateKill(npc, qs) && (qs.getQuestItemsCount(Mark) >= 5))
		{
			qs.unset(Vooko_item);
			qs.unset(Lizard_item);
			qs.setCond(3);
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
