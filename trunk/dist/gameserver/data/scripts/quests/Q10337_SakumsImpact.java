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

public class Q10337_SakumsImpact extends Quest implements ScriptFile
{
	private static final int Guild = 31795;
	private static final int Silvana = 33178;
	private static final int Lef = 33510;
	private static final int Bes = 20506;
	private static final int Skelet = 23022;
	private static final int Batt = 27458;
	private static final int Sc_bat = 20411;
	private static final int Ruin_bat = 20505;
	private static final String bes_item = "bes";
	private static final String bat_item = "bat";
	private static final String skelet_item = "skelet";
	
	public Q10337_SakumsImpact()
	{
		super(false);
		addStartNpc(Guild);
		addTalkId(Guild, Silvana, Lef);
		addKillNpcWithLog(2, bes_item, 20, Bes);
		addKillNpcWithLog(2, bat_item, 25, Batt, Sc_bat, Ruin_bat);
		addKillNpcWithLog(2, skelet_item, 15, Skelet);
		addLevelCheck(28, 40);
		addQuestCompletedCheck(Q10336_DividedSakumKanilov.class);
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
				htmltext = "2-2.htm";
				qs.getPlayer().addExpAndSp(650000, 16000);
				qs.giveItems(57, 1030);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "1-3.htm":
				htmltext = "1-3.htm";
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
			case Guild:
				if (qs.isCompleted())
				{
					htmltext = TODO_FIND_HTML;
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "0-1.htm";
				}
				else if ((cond == 1) || (cond == 2) || (cond == 3))
				{
					htmltext = "0-4.htm";
				}
				break;
			
			case Silvana:
				if (qs.isCompleted())
				{
					htmltext = TODO_FIND_HTML;
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
					htmltext = "1-4.htm";
				}
				break;
			
			case Lef:
				if (qs.isCompleted())
				{
					htmltext = TODO_FIND_HTML;
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
			qs.unset(bes_item);
			qs.unset(skelet_item);
			qs.unset(bat_item);
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
