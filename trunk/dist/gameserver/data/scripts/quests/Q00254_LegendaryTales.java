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

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author pchayka
 */
public class Q00254_LegendaryTales extends Quest implements ScriptFile
{
	// Npc
	private static final int Gilmore = 30754;
	// Item
	private static final int LargeBone = 17249;
	// Monsters
	private static final int[] raids =
	{
		25718,
		25719,
		25720,
		25721,
		25722,
		25723,
		25724
	};
	
	public Q00254_LegendaryTales()
	{
		super(PARTY_ALL);
		addStartNpc(Gilmore);
		addKillId(raids);
		addQuestItem(LargeBone);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equals("gilmore_q254_05.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.startsWith("gilmore_q254_20.htm"))
		{
			qs.takeAllItems(LargeBone);
			StringTokenizer tokenizer = new StringTokenizer(event);
			tokenizer.nextToken();
			
			switch (Integer.parseInt(tokenizer.nextToken()))
			{
				case 1:
					qs.giveItems(13467, 1);
					break;
				
				case 2:
					qs.giveItems(13462, 1);
					break;
				
				case 3:
					qs.giveItems(13464, 1);
					break;
				
				case 4:
					qs.giveItems(13461, 1);
					break;
				
				case 5:
					qs.giveItems(13465, 1);
					break;
				
				case 6:
					qs.giveItems(13463, 1);
					break;
				
				case 7:
					qs.giveItems(13460, 1);
					break;
				
				case 8:
					qs.giveItems(13466, 1);
					break;
				
				case 9:
					qs.giveItems(13459, 1);
					break;
				
				case 10:
					qs.giveItems(13457, 1);
					break;
				
				case 11:
					qs.giveItems(13458, 1);
					break;
				
				default:
					break;
			}
			
			qs.playSound(SOUND_FINISH);
			qs.setState(COMPLETED);
			qs.exitCurrentQuest(false);
			htmltext = "gilmore_q254_20.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (cond)
		{
			case 0:
				if (qs.getPlayer().getLevel() >= 80)
				{
					htmltext = "gilmore_q254_01.htm";
				}
				else
				{
					htmltext = "gilmore_q254_00.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				if (qs.getQuestItemsCount(LargeBone) >= 1)
				{
					htmltext = "gilmore_q254_10.htm";
				}
				else
				{
					htmltext = "gilmore_q254_05.htm";
				}
				break;
			
			case 2:
				htmltext = "gilmore_q254_18.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			int mask = 1;
			int var = npc.getId();
			
			for (int raid : raids)
			{
				if (raid == var)
				{
					break;
				}
				
				mask = mask << 1;
			}
			
			var = qs.getInt("RaidsKilled");
			
			if ((var & mask) == 0)
			{
				var |= mask;
				qs.set("RaidsKilled", var);
				qs.giveItems(LargeBone, 1);
				
				if (qs.getQuestItemsCount(LargeBone) >= 7)
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