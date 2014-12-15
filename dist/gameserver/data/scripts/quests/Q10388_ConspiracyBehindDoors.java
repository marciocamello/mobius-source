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

public class Q10388_ConspiracyBehindDoors extends Quest implements ScriptFile
{
	// Npcs
	private static final int ELIA = 31329;
	private static final int KARGOS = 33821;
	private static final int HICHEN = 33820;
	private static final int RAZDEN = 33803;
	
	public Q10388_ConspiracyBehindDoors()
	{
		super(true);
		addStartNpc(ELIA);
		addTalkId(KARGOS, HICHEN, RAZDEN);
		addLevelCheck(97, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "go.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "toCond2.htm":
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "toCond3.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				break;
			
			case "final.htm":
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				qs.giveItems(ADENA_ID, 65136);
				qs.addExpAndSp(29638350, 7113);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		
		if (qs.getState() == CREATED)
		{
			if (qs.getPlayer().getLevel() >= 97)
			{
				htmltext = "start.htm";
			}
			else
			{
				htmltext = "nolvl.htm";
			}
		}
		else if (npcId == KARGOS)
		{
			if (cond == 1)
			{
				return "cond1.htm";
			}
		}
		else if (npcId == HICHEN)
		{
			if (cond == 2)
			{
				return "cond2.htm";
			}
		}
		else if (npcId == RAZDEN)
		{
			if (cond == 3)
			{
				return "cond3.htm";
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