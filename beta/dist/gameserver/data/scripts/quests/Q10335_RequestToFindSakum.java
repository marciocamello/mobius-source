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

public class Q10335_RequestToFindSakum extends Quest implements ScriptFile
{
	private static final int Batis = 30332;
	private static final int Kalesin = 33177;
	private static final int Jena = 33509;
	private static final int Sledopyt = 20035;
	private static final int Strelec = 20051;
	private static final int Skelet = 20054;
	private static final int Zombie = 20026;
	private static final String sledopyt_item = "sledopyt";
	private static final String strelec_item = "strelec";
	private static final String skelet_item = "skelet";
	private static final String zombie_item = "zombie";
	
	public Q10335_RequestToFindSakum()
	{
		super(false);
		addStartNpc(Batis);
		addTalkId(Batis);
		addTalkId(Kalesin);
		addTalkId(Jena);
		addKillNpcWithLog(2, sledopyt_item, 10, Sledopyt);
		addKillNpcWithLog(2, strelec_item, 10, Strelec);
		addKillNpcWithLog(2, skelet_item, 15, Skelet);
		addKillNpcWithLog(2, zombie_item, 15, Zombie);
		addLevelCheck(23, 40);
		addQuestCompletedCheck(Q10334_WindmillHillStatusReport.class);
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
				htmltext = "2-3.htm";
				qs.getPlayer().addExpAndSp(350000, 10000);
				qs.giveItems(57, 899);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "1-2.htm":
				qs.setCond(2);
				htmltext = "1-2.htm";
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
					htmltext = "0-1.htm";
				}
				else if ((cond == 1) || (cond == 2) || (cond == 3))
				{
					htmltext = "0-3.htm";
				}
				else
				{
					htmltext = "0-nc.htm";
				}
				break;
			
			case Kalesin:
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
					htmltext = "1-3.htm";
				}
				break;
			
			case Jena:
				if (qs.isCompleted())
				{
					htmltext = "2-c.htm";
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
		if (updateKill(npc, qs))
		{
			qs.unset(sledopyt_item);
			qs.unset(strelec_item);
			qs.unset(skelet_item);
			qs.unset(zombie_item);
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
