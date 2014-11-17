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

public class Q10357_AltarOfBloodThatAwakensDestruction extends Quest implements ScriptFile
{
	// Npcs
	private static final int JORJINO = 33515;
	private static final int ELKARDIA = 32798;
	// Others
	private static final String A_LIST = "a_list";
	private static final String B_LIST = "b_list";
	
	public Q10357_AltarOfBloodThatAwakensDestruction()
	{
		super(true);
		addStartNpc(JORJINO);
		addTalkId(JORJINO, ELKARDIA);
		addKillNpcWithLog(2, A_LIST, 1, 25876);
		addKillNpcWithLog(2, B_LIST, 1, 25877);
		addLevelCheck(95, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("33515-4.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("32798-1.htm"))
		{
			qs.setCond(2);
			qs.playSound(SOUND_MIDDLE);
		}
		
		if (event.startsWith("give"))
		{
			if (event.equals("givematerials"))
			{
				qs.giveItems(19305, 1);
				qs.giveItems(19306, 1);
				qs.giveItems(19307, 1);
				qs.giveItems(19308, 1);
			}
			else if (event.equals("giveenchants"))
			{
				qs.giveItems(22561, 2);
			}
			else if (event.equals("givesacks"))
			{
				qs.giveItems(34861, 2);
			}
			
			qs.addExpAndSp(11000000, 5000000);
			qs.playSound(SOUND_FINISH);
			qs.exitCurrentQuest(false);
			return "33515-7.htm";
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		if (qs.getState() == COMPLETED)
		{
			return "33515-comp.htm";
		}
		
		if (qs.getPlayer().getLevel() < 95)
		{
			return "33515-lvl.htm";
		}
		
		switch (npc.getId())
		{
			case JORJINO:
				if (cond == 0)
				{
					return "33515.htm";
				}
				else if (cond == 1)
				{
					return "33515-5.htm";
				}
				else if (cond == 2)
				{
					return "33515-5.htm";
				}
				else if (cond == 3)
				{
					return "33515-6.htm";
				}
				break;
			
			case ELKARDIA:
				if (cond == 1)
				{
					return "32798.htm";
				}
				else if (cond == 2)
				{
					return "32798-2.htm";
				}
				else if (cond == 3)
				{
					return "32798-5.htm";
				}
				break;
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 2) && updateKill(npc, qs))
		{
			qs.unset(A_LIST);
			qs.unset(B_LIST);
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