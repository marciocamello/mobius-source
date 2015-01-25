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

public class Q10364_ObligationsOfTheSeeker extends Quest implements ScriptFile
{
	private static final int celin = 33451;
	private static final int avian = 22994;
	private static final int warper = 22996;
	private static final int dep = 33453;
	private static final int papper = 17578;
	private static final int walter = 33452;
	
	public Q10364_ObligationsOfTheSeeker()
	{
		super(false);
		addStartNpc(celin);
		addTalkId(celin);
		addTalkId(dep);
		addTalkId(walter);
		addKillId(warper, avian);
		addQuestItem(papper);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(Q10363_RequestOfTheSeeker.class);
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
			
			case "papper":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				htmltext = "0-3.htm";
				break;
			
			case "quest_rev":
				qs.takeAllItems(papper);
				htmltext = "2-4.htm";
				qs.getPlayer().addExpAndSp(95000, 22);
				qs.giveItems(57, 550);
				qs.giveItems(1060, 50);
				qs.giveItems(37, 1);
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
		
		switch (npc.getId())
		{
			case celin:
				if (qs.isCompleted())
				{
					htmltext = "0-c.htm";
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "start.htm";
				}
				else if (cond == 1)
				{
					htmltext = "0-4.htm";
				}
				else
				{
					htmltext = "0-nc.htm";
				}
				break;
			
			case walter:
				if (qs.isCompleted())
				{
					htmltext = "1-c.htm";
				}
				else if (cond == 1)
				{
					htmltext = "1-1.htm";
				}
				else if (cond == 2)
				{
					htmltext = "1-5.htm";
				}
				else if (cond == 3)
				{
					htmltext = "1-6.htm";
				}
				else
				{
					htmltext = "1-5.htm";
				}
				break;
			
			case dep:
				if (qs.isCompleted())
				{
					htmltext = "2-c.htm";
				}
				else if (cond == 0)
				{
					htmltext = "2-nc.htm";
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
		final int npcId = npc.getId();
		
		if (((qs.getCond() == 2) && (qs.getQuestItemsCount(papper) < 5) && (npcId == warper)) || (npcId == avian))
		{
			qs.rollAndGive(papper, 1, 35);
			qs.playSound(SOUND_ITEMGET);
		}
		
		if (qs.getQuestItemsCount(papper) >= 5)
		{
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
