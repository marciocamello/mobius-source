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

public class Q00013_ParcelDelivery extends Quest implements ScriptFile
{
	// Npcs
	private static final int FUNDIN = 31274;
	private static final int VULCAN = 31539;
	// Item
	private static final int PACKAGE = 7263;
	
	public Q00013_ParcelDelivery()
	{
		super(false);
		addStartNpc(FUNDIN);
		addTalkId(FUNDIN, VULCAN);
		addQuestItem(PACKAGE);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "mineral_trader_fundin_q0013_0104.htm":
				qs.setCond(1);
				qs.giveItems(PACKAGE, 1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "warsmith_vulcan_q0013_0201.htm":
				qs.takeItems(PACKAGE, -1);
				qs.giveItems(ADENA_ID, 157834, true);
				qs.addExpAndSp(589092, 58794);
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case FUNDIN:
				if (cond == 0)
				{
					if (qs.getPlayer().getLevel() >= 74)
					{
						htmltext = "mineral_trader_fundin_q0013_0101.htm";
					}
					else
					{
						htmltext = "mineral_trader_fundin_q0013_0103.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else if (cond == 1)
				{
					htmltext = "mineral_trader_fundin_q0013_0105.htm";
				}
				break;
			
			case VULCAN:
				if ((cond == 1) && (qs.getQuestItemsCount(PACKAGE) == 1))
				{
					htmltext = "warsmith_vulcan_q0013_0101.htm";
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
