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

public class Q00146_TheZeroHour extends Quest implements ScriptFile
{
	// Npc
	private static final int KAHMAN = 31554;
	// Items
	private static final int STAKATO_QUEENS_FANG = 14859;
	private static final int KAHMANS_SUPPLY_BOX = 14849;
	// Monster
	private static final int QUEEN_SHYEED_ID = 25671;
	
	public Q00146_TheZeroHour()
	{
		super(true);
		addStartNpc(KAHMAN);
		addTalkId(KAHMAN);
		addKillId(QUEEN_SHYEED_ID);
		addQuestItem(STAKATO_QUEENS_FANG);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final int cond = qs.getCond();
		String htmltext = event;
		
		switch (event)
		{
			case "merc_kahmun_q0146_0103.htm":
				if (cond == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "reward":
				if (cond == 2)
				{
					htmltext = "merc_kahmun_q0146_0107.htm";
					qs.takeItems(STAKATO_QUEENS_FANG, -1);
					qs.giveItems(KAHMANS_SUPPLY_BOX, 1);
					qs.addExpAndSp(2850000, 3315000);
					qs.exitCurrentQuest(false);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final QuestState InSearchOfTheNest = qs.getPlayer().getQuestState(Q00109_InSearchOfTheNest.class);
		
		switch (cond)
		{
			case 0:
				if (qs.getPlayer().getLevel() >= 81)
				{
					if ((InSearchOfTheNest != null) && InSearchOfTheNest.isCompleted())
					{
						htmltext = "merc_kahmun_q0146_0101.htm";
					}
					else
					{
						htmltext = "merc_kahmun_q0146_0104.htm";
					}
				}
				else
				{
					htmltext = "merc_kahmun_q0146_0102.htm";
				}
				break;
			
			case 1:
				if (qs.getQuestItemsCount(STAKATO_QUEENS_FANG) < 1)
				{
					htmltext = "merc_kahmun_q0146_0105.htm";
				}
				break;
			
			case 2:
				htmltext = "merc_kahmun_q0146_0106.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() == STARTED)
		{
			qs.setCond(2);
			qs.giveItems(STAKATO_QUEENS_FANG, 1);
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
