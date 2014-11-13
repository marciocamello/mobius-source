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

public class Q00132_MatrasCuriosity extends Quest implements ScriptFile
{
	// Npc
	private static final int Matras = 32245;
	// Monsters
	private static final int Ranku = 25542;
	private static final int Demon_Prince = 25540;
	// Items
	private static final int Rankus_Blueprint = 9800;
	private static final int Demon_Princes_Blueprint = 9801;
	private static final int Rough_Ore_of_Fire = 10521;
	private static final int Rough_Ore_of_Water = 10522;
	private static final int Rough_Ore_of_Earth = 10523;
	private static final int Rough_Ore_of_Wind = 10524;
	private static final int Rough_Ore_of_Darkness = 10525;
	private static final int Rough_Ore_of_Divinity = 10526;
	
	public Q00132_MatrasCuriosity()
	{
		super(PARTY_ALL);
		addStartNpc(Matras);
		addKillId(Ranku, Demon_Prince);
		addQuestItem(Rankus_Blueprint, Demon_Princes_Blueprint);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "32245-02.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				String is_given = qs.getPlayer().getVar("q132_Rough_Ore_is_given");
				if (is_given != null)
				{
					htmltext = "32245-02a.htm";
				}
				else
				{
					qs.giveItems(Rough_Ore_of_Fire, 1, false);
					qs.giveItems(Rough_Ore_of_Water, 1, false);
					qs.giveItems(Rough_Ore_of_Earth, 1, false);
					qs.giveItems(Rough_Ore_of_Wind, 1, false);
					qs.giveItems(Rough_Ore_of_Darkness, 1, false);
					qs.giveItems(Rough_Ore_of_Divinity, 1, false);
					qs.getPlayer().setVar("q132_Rough_Ore_is_given", "1", -1);
				}
				break;
			
			case "32245-04.htm":
				qs.setCond(3);
				qs.setState(STARTED);
				qs.startQuestTimer("talk_timer", 10000);
				break;
			
			case "talk_timer":
				htmltext = "Matras wishes to talk to you.";
				break;
			
			case "get_reward":
				qs.playSound(SOUND_FINISH);
				qs.addExpAndSp(5388330, 6048600);
				qs.giveItems(ADENA_ID, 575545);
				qs.exitCurrentQuest(false);
				return null;
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
		
		switch (cond)
		{
			case 0:
				if (qs.getPlayer().getLevel() >= 78)
				{
					htmltext = "32245-01.htm";
				}
				break;
			
			case 1:
				htmltext = "32245-02a.htm";
				break;
			
			case 2:
				if ((qs.getQuestItemsCount(Rankus_Blueprint) > 0) && (qs.getQuestItemsCount(Demon_Princes_Blueprint) > 0))
				{
					htmltext = "32245-03.htm";
				}
				break;
			
			case 3:
				if (qs.isRunningQuestTimer("talk_timer"))
				{
					htmltext = "32245-04.htm";
				}
				else
				{
					htmltext = "32245-04a.htm";
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
			if ((npc.getId() == Ranku) && (qs.getQuestItemsCount(Rankus_Blueprint) < 1))
			{
				qs.playSound(SOUND_ITEMGET);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(Rankus_Blueprint, 1, false);
			}
			else if ((npc.getId() == Demon_Prince) && (qs.getQuestItemsCount(Demon_Princes_Blueprint) < 1))
			{
				qs.playSound(SOUND_ITEMGET);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(Demon_Princes_Blueprint, 1, false);
			}
			
			if ((qs.getQuestItemsCount(Rankus_Blueprint) > 0) && (qs.getQuestItemsCount(Demon_Princes_Blueprint) > 0))
			{
				qs.setCond(2);
				qs.setState(STARTED);
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
