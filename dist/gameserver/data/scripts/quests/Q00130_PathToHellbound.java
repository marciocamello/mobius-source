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

public class Q00130_PathToHellbound extends Quest implements ScriptFile
{
	// Npcs
	private static final int CASIAN = 30612;
	private static final int GALATE = 32292;
	// Item
	private static final int CASIAN_BLUE_CRY = 12823;
	
	public Q00130_PathToHellbound()
	{
		super(false);
		addStartNpc(CASIAN);
		addTalkId(GALATE);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final int cond = qs.getCond();
		String htmltext = event;
		
		switch (event)
		{
			case "sage_kasian_q0130_05.htm":
				if (cond == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "galate_q0130_03.htm":
				if (cond == 1)
				{
					qs.setCond(2);
					qs.playSound(SOUND_MIDDLE);
				}
				break;
			
			case "sage_kasian_q0130_08.htm":
				if (cond == 2)
				{
					qs.setCond(3);
					qs.playSound(SOUND_MIDDLE);
					qs.giveItems(CASIAN_BLUE_CRY, 1);
				}
				break;
			
			case "galate_q0130_07.htm":
				if (cond == 3)
				{
					qs.playSound(SOUND_FINISH);
					qs.takeItems(CASIAN_BLUE_CRY, -1);
					qs.exitCurrentQuest(false);
				}
				break;
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
		final int npcId = npc.getId();
		final int id = qs.getState();
		
		if (npcId == CASIAN)
		{
			if (cond == 0)
			{
				if (qs.getPlayer().getLevel() >= 78)
				{
					htmltext = "sage_kasian_q0130_01.htm";
				}
				else
				{
					htmltext = "sage_kasian_q0130_02.htm";
					qs.exitCurrentQuest(true);
				}
			}
			else if (cond == 2)
			{
				htmltext = "sage_kasian_q0130_07.htm";
			}
		}
		else if (id == STARTED)
		{
			if (npcId == GALATE)
			{
				if (cond == 1)
				{
					htmltext = "galate_q0130_01.htm";
				}
				else if (cond == 3)
				{
					htmltext = "galate_q0130_05.htm";
				}
			}
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
