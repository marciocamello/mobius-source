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

/**
 * @author Lacost
 */
public class Q00210_ObtainAWolfPet extends Quest implements ScriptFile
{
	// Npcs
	private static final int LUNDY = 30827;
	private static final int BELLA = 30256;
	private static final int BRYNN = 30335;
	private static final int SYDNIA = 30321;
	// Reward
	private static final int WOLF_COLLAR = 2375;
	
	public Q00210_ObtainAWolfPet()
	{
		super(false);
		addStartNpc(LUNDY);
		addTalkId(LUNDY);
		addTalkId(BELLA);
		addTalkId(BRYNN);
		addTalkId(SYDNIA);
		addLevelCheck(15, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_ac":
				st.setState(STARTED);
				st.setCond(1);
				st.playSound(SOUND_ACCEPT);
				htmltext = "0-3.htm";
				break;
			
			case "1":
				st.setState(STARTED);
				st.setCond(2);
				htmltext = "1-3.htm";
				break;
			
			case "2":
				st.setState(STARTED);
				st.setCond(3);
				htmltext = "2-2.htm";
				break;
			
			case "3":
				st.setState(STARTED);
				st.setCond(4);
				htmltext = "3-2.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getId();
		String htmltext = "noquest";
		
		switch (npcId)
		{
			case LUNDY:
				if (st.isCompleted())
				{
					htmltext = "Completed.htm";
				}
				else if ((cond == 0) && isAvailableFor(st.getPlayer()))
				{
					htmltext = "0-1.htm";
				}
				else if (cond == 1)
				{
					htmltext = "0-4.htm";
				}
				else if (cond == 4)
				{
					htmltext = "4.htm";
					st.giveItems(WOLF_COLLAR, 1);
					st.exitCurrentQuest(false);
					st.playSound(SOUND_FINISH);
				}
				break;
			
			case BELLA:
				if (st.isCompleted())
				{
					htmltext = "Completed.htm";
				}
				else if (cond == 1)
				{
					htmltext = "1-1.htm";
				}
				break;
			
			case BRYNN:
				if (st.isCompleted())
				{
					htmltext = "Completed.htm";
				}
				else if (cond == 2)
				{
					htmltext = "2-1.htm";
				}
				break;
			
			case SYDNIA:
				if (st.isCompleted())
				{
					htmltext = "Completed.htm";
				}
				else if (cond == 3)
				{
					htmltext = "3-1.htm";
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