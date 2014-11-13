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

public class Q00121_PavelTheGiant extends Quest implements ScriptFile
{
	// Npcs
	private static final int NEWYEAR = 31961;
	private static final int YUMI = 32041;
	
	public Q00121_PavelTheGiant()
	{
		super(false);
		addStartNpc(NEWYEAR);
		addTalkId(NEWYEAR, YUMI);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("collecter_yumi_q0121_0201.htm"))
		{
			qs.playSound(SOUND_FINISH);
			qs.addExpAndSp(1959460, 2039940);
			qs.exitCurrentQuest(false);
		}
		
		return event;
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
		
		if ((qs.getState() == CREATED) && (npcId == NEWYEAR))
		{
			if (qs.getPlayer().getLevel() >= 70)
			{
				htmltext = "head_blacksmith_newyear_q0121_0101.htm";
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
			}
			else
			{
				htmltext = "head_blacksmith_newyear_q0121_0103.htm";
				qs.exitCurrentQuest(false);
			}
		}
		else if (qs.getState() == STARTED)
		{
			if ((npcId == YUMI) && (cond == 1))
			{
				htmltext = "collecter_yumi_q0121_0101.htm";
			}
			else
			{
				htmltext = "head_blacksmith_newyear_q0121_0105.htm";
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
