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

import java.util.StringTokenizer;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10289_FadeToBlack extends Quest implements ScriptFile
{
	// Npc
	private static final int Greymore = 32757;
	// Monster
	private static final int Anays = 25701;
	// Items
	private static final int MarkofSplendor = 15527;
	private static final int MarkofDarkness = 15528;
	
	public Q10289_FadeToBlack()
	{
		super(PARTY_ALL);
		addStartNpc(Greymore);
		addKillId(Anays);
		addQuestItem(MarkofSplendor, MarkofDarkness);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("greymore_q10289_03.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("showmark"))
		{
			if ((qs.getCond() == 2) && (qs.getQuestItemsCount(MarkofDarkness) > 0))
			{
				htmltext = "greymore_q10289_06.htm";
			}
			else if ((qs.getCond() == 3) && (qs.getQuestItemsCount(MarkofSplendor) > 0))
			{
				htmltext = "greymore_q10289_07.htm";
			}
			else
			{
				htmltext = "greymore_q10289_08.htm";
			}
		}
		else if (event.startsWith("exchange"))
		{
			StringTokenizer str = new StringTokenizer(event);
			str.nextToken();
			int id = Integer.parseInt(str.nextToken());
			
			switch (id)
			{
				case 1:
					qs.giveItems(15775, 1, false);
					qs.giveItems(ADENA_ID, 420920);
					break;
				
				case 2:
					qs.giveItems(15776, 1, false);
					qs.giveItems(ADENA_ID, 420920);
					break;
				
				case 3:
					qs.giveItems(15777, 1, false);
					qs.giveItems(ADENA_ID, 420920);
					break;
				
				case 4:
					qs.giveItems(15778, 1, false);
					break;
				
				case 5:
					qs.giveItems(15779, 1, false);
					qs.giveItems(ADENA_ID, 168360);
					break;
				
				case 6:
					qs.giveItems(15780, 1, false);
					qs.giveItems(ADENA_ID, 168360);
					break;
				
				case 7:
					qs.giveItems(15781, 1, false);
					qs.giveItems(ADENA_ID, 252540);
					break;
				
				case 8:
					qs.giveItems(15782, 1, false);
					qs.giveItems(ADENA_ID, 357780);
					break;
				
				case 9:
					qs.giveItems(15783, 1, false);
					qs.giveItems(ADENA_ID, 357780);
					break;
				
				case 10:
					qs.giveItems(15784, 1, false);
					qs.giveItems(ADENA_ID, 505100);
					break;
				
				case 11:
					qs.giveItems(15785, 1, false);
					qs.giveItems(ADENA_ID, 505100);
					break;
				
				case 12:
					qs.giveItems(15786, 1, false);
					qs.giveItems(ADENA_ID, 505100);
					break;
				
				case 13:
					qs.giveItems(15787, 1, false);
					qs.giveItems(ADENA_ID, 505100);
					break;
				
				case 14:
					qs.giveItems(15788, 1, false);
					qs.giveItems(ADENA_ID, 505100);
					break;
				
				case 15:
					qs.giveItems(15789, 1, false);
					qs.giveItems(ADENA_ID, 505100);
					break;
				
				case 16:
					qs.giveItems(15790, 1, false);
					qs.giveItems(ADENA_ID, 496680);
					break;
				
				case 17:
					qs.giveItems(15791, 1, false);
					qs.giveItems(ADENA_ID, 496680);
					break;
				
				case 18:
					qs.giveItems(15812, 1, false);
					qs.giveItems(ADENA_ID, 563860);
					break;
				
				case 19:
					qs.giveItems(15813, 1, false);
					qs.giveItems(ADENA_ID, 509040);
					break;
				
				case 20:
					qs.giveItems(15814, 1, false);
					qs.giveItems(ADENA_ID, 454240);
					break;
			}
			
			htmltext = "greymore_q10289_09.htm";
			qs.takeAllItems(MarkofSplendor, MarkofDarkness);
			qs.exitCurrentQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		switch (qs.getCond())
		{
			case 0:
				final QuestState state = qs.getPlayer().getQuestState(Q10288_SecretMission.class);
				if ((qs.getPlayer().getLevel() >= 82) && (state != null) && state.isCompleted())
				{
					htmltext = "greymore_q10289_01.htm";
				}
				else
				{
					htmltext = "greymore_q10289_00.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				htmltext = "greymore_q10289_04.htm";
				break;
			
			case 2:
			case 3:
				htmltext = "greymore_q10289_05.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			if (Rnd.chance(30))
			{
				qs.giveItems(MarkofSplendor, 1);
				qs.setCond(3);
			}
			else
			{
				qs.giveItems(MarkofDarkness, 1);
				qs.setCond(2);
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
