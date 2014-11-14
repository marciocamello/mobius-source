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

public class Q00647_InfluxOfMachines extends Quest implements ScriptFile
{
	// Npc
	private static final int GUTENHAGEN = 32069;
	// Items
	private static final int BROKEN_GOLEM_FRAGMENT = 15521;
	private static final int[] RECIPES =
	{
		6887,
		6881,
		6897,
		7580,
		6883,
		6899,
		6891,
		6885,
		6893,
		6895
	};
	
	public Q00647_InfluxOfMachines()
	{
		super(true);
		addStartNpc(GUTENHAGEN);
		addTalkId(GUTENHAGEN);
		addQuestItem(BROKEN_GOLEM_FRAGMENT);
		
		for (int i = 22801; i < 22812; i++)
		{
			addKillId(i);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_accept":
				htmltext = "collecter_gutenhagen_q0647_0103.htm";
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "647_3":
				if (qs.getQuestItemsCount(BROKEN_GOLEM_FRAGMENT) >= 500)
				{
					qs.takeItems(BROKEN_GOLEM_FRAGMENT, -1);
					qs.giveItems(RECIPES[Rnd.get(RECIPES.length)], 1);
					htmltext = "collecter_gutenhagen_q0647_0201.htm";
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "collecter_gutenhagen_q0647_0106.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final long count = qs.getQuestItemsCount(BROKEN_GOLEM_FRAGMENT);
		
		switch (qs.getCond())
		{
			case 0:
				if (qs.getPlayer().getLevel() >= 70)
				{
					htmltext = "collecter_gutenhagen_q0647_0101.htm";
				}
				else
				{
					htmltext = "collecter_gutenhagen_q0647_0102.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case 1:
				if (count < 500)
				{
					htmltext = "collecter_gutenhagen_q0647_0106.htm";
				}
				break;
			
			case 2:
				if (count >= 500)
				{
					htmltext = "collecter_gutenhagen_q0647_0105.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() == 1) && qs.rollAndGive(BROKEN_GOLEM_FRAGMENT, 1, 1, 500, GUTENHAGEN * npc.getTemplate().rateHp))
		{
			qs.setCond(2);
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
