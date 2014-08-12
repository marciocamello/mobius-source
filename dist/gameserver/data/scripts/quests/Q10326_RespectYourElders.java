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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q10326_RespectYourElders extends Quest implements ScriptFile
{
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
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		// Player player = st.getPlayer();
		if (event.equalsIgnoreCase("3.htm"))
		{
			st.set("cond", "1", true);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("5.htm"))
		{
			st.giveItems(57, 14000);
			st.addExpAndSp(5300, 2800);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		Player player = st.getPlayer();
		
		if (npcId == GALLINT)
		{
			if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "1.htm";
				// else TODO
			}
			else if (cond >= 8)
			{
				htmltext = "3.htm";
				st.giveItems(57, 12000);
				
				if (player.isMageClass())
				{
					st.giveItems(2509, 1000);
				}
				else
				{
					st.giveItems(1835, 1000);
				}
				
				st.addExpAndSp(3254, 2400);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		else if (npcId == PANTEON)
		{
			if (cond == 1)
			{
				htmltext = "4.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public void onLoad()
	{
		//
	}
	
	@Override
	public void onReload()
	{
		//
	}
	
	@Override
	public void onShutdown()
	{
		//
	}
}
