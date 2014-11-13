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

public class Q00015_SweetWhispers extends Quest implements ScriptFile
{
	// Npcs
	private static final int VLADIMIR = 31302;
	private static final int HIERARCH = 31517;
	private static final int NECROMANCER = 31518;
	
	public Q00015_SweetWhispers()
	{
		super(false);
		addStartNpc(VLADIMIR);
		addTalkId(HIERARCH, NECROMANCER);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "trader_vladimir_q0015_0104.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "dark_necromancer_q0015_0201.htm":
				qs.setCond(2);
				break;
			
			case "dark_presbyter_q0015_0301.htm":
				qs.addExpAndSp(714215, 650980);
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
		if (qs.isCompleted())
		{
			return "completed";
		}
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case VLADIMIR:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() >= 60)
					{
						htmltext = "trader_vladimir_q0015_0101.htm";
					}
					else
					{
						htmltext = "trader_vladimir_q0015_0103.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond >= 1)
				{
					htmltext = "trader_vladimir_q0015_0105.htm";
				}
				break;
			
			case NECROMANCER:
				if (cond == 1)
				{
					htmltext = "dark_necromancer_q0015_0101.htm";
				}
				else if (cond == 2)
				{
					htmltext = "dark_necromancer_q0015_0202.htm";
				}
				break;
			
			case HIERARCH:
				if (cond == 2)
				{
					htmltext = "dark_presbyter_q0015_0201.htm";
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
