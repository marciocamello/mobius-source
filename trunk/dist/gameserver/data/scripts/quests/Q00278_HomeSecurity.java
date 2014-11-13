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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00278_HomeSecurity extends Quest implements ScriptFile
{
	// Npc
	private static final int Tunatun = 31537;
	// Monsters
	private static final int[] FarmMonsters =
	{
		18905,
		18906
	};
	// Item
	private static final int SelMahumMane = 15531;
	
	public Q00278_HomeSecurity()
	{
		super(false);
		addStartNpc(Tunatun);
		addKillId(FarmMonsters);
		addQuestItem(SelMahumMane);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("tunatun_q278_03.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		if (qs.isCompleted())
		{
			return "completed";
		}
		final int cond = qs.getCond();
		
		switch (cond)
		{
			case 0:
				if (qs.getPlayer().getLevel() >= 82)
				{
					htmltext = "tunatun_q278_01.htm";
				}
				else
				{
					htmltext = "tunatun_q278_00.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				htmltext = "tunatun_q278_04.htm";
				break;
			
			case 2:
				if (qs.getQuestItemsCount(SelMahumMane) >= 300)
				{
					htmltext = "tunatun_q278_05.htm";
					qs.takeAllItems(SelMahumMane);
					
					switch (Rnd.get(1, 13))
					{
						case 1:
							qs.giveItems(960, 1);
							break;
						
						case 2:
							qs.giveItems(960, 2);
							break;
						
						case 3:
							qs.giveItems(960, 3);
							break;
						
						case 4:
							qs.giveItems(960, 4);
							break;
						
						case 5:
							qs.giveItems(960, 5);
							break;
						
						case 6:
							qs.giveItems(960, 6);
							break;
						
						case 7:
							qs.giveItems(960, 7);
							break;
						
						case 8:
							qs.giveItems(960, 8);
							break;
						
						case 9:
							qs.giveItems(960, 9);
							break;
						
						case 10:
							qs.giveItems(960, 10);
							break;
						
						case 11:
							qs.giveItems(9553, 1);
							break;
						
						case 12:
							qs.giveItems(9553, 2);
							break;
						
						case 13:
							qs.giveItems(959, 1);
							break;
					}
					
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "tunatun_q278_04.htm";
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
			if (qs.getQuestItemsCount(SelMahumMane) < 300)
			{
				qs.giveItems(SelMahumMane, 1, true);
				
				if (qs.getQuestItemsCount(SelMahumMane) >= 300)
				{
					qs.setCond(2);
				}
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
