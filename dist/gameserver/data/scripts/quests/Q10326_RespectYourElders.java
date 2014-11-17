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

public class Q10326_RespectYourElders extends Quest implements ScriptFile
{
	// Npcs
	private static final int GALLINT = 32980;
	private static final int PANTEON = 32972;
	
	public Q10326_RespectYourElders()
	{
		super(false);
		addStartNpc(GALLINT);
		addTalkId(PANTEON);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(Q10325_SearchingForNewPower.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "3.htm":
				qs.set("cond", "1", true);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "5.htm":
				qs.giveItems(57, 14000);
				qs.addExpAndSp(5300, 2800);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
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
			case GALLINT:
				if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "1.htm";
				}
				else if (cond >= 8)
				{
					htmltext = "3.htm";
					qs.giveItems(57, 12000);
					
					if (qs.getPlayer().isMageClass())
					{
						qs.giveItems(2509, 1000);
					}
					else
					{
						qs.giveItems(1835, 1000);
					}
					
					qs.addExpAndSp(3254, 2400);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(false);
				}
				break;
			
			case PANTEON:
				if (cond == 1)
				{
					htmltext = "4.htm";
				}
				break;
		}
		
		return htmltext;
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
