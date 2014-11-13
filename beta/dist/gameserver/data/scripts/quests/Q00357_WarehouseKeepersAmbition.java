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

public class Q00357_WarehouseKeepersAmbition extends Quest implements ScriptFile
{
	// Npc
	private static final int SILVA = 30686;
	// Monsters
	private static final int MOB1 = 20594;
	private static final int MOB2 = 20595;
	private static final int MOB3 = 20596;
	private static final int MOB4 = 20597;
	private static final int MOB5 = 20598;
	// Items
	private static final int JADE_CRYSTAL = 5867;
	// Other
	private static final int REWARD1 = 900;
	private static final int REWARD2 = 10000;
	private static final int DROPRATE = 50;
	
	public Q00357_WarehouseKeepersAmbition()
	{
		super(false);
		addStartNpc(SILVA);
		addKillId(MOB1, MOB2, MOB3, MOB4, MOB5);
		addQuestItem(JADE_CRYSTAL);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "warehouse_keeper_silva_q0357_04.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "warehouse_keeper_silva_q0357_08.htm":
				final long count = qs.getQuestItemsCount(JADE_CRYSTAL);
				if (count > 0)
				{
					long reward = count * REWARD1;
					
					if (count >= 100)
					{
						reward = reward + REWARD2;
					}
					
					qs.takeItems(JADE_CRYSTAL, -1);
					qs.giveItems(ADENA_ID, reward);
				}
				else
				{
					htmltext = "warehouse_keeper_silva_q0357_06.htm";
				}
				break;
			
			case "warehouse_keeper_silva_q0357_11.htm":
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
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
		final int id = qs.getState();
		final long jade = qs.getQuestItemsCount(JADE_CRYSTAL);
		
		if ((cond == 0) || (id == CREATED))
		{
			if (qs.getPlayer().getLevel() >= 47)
			{
				htmltext = "warehouse_keeper_silva_q0357_02.htm";
			}
			else
			{
				htmltext = "warehouse_keeper_silva_q0357_01.htm";
				qs.exitCurrentQuest(true);
			}
		}
		else if (jade == 0)
		{
			htmltext = "warehouse_keeper_silva_q0357_06.htm";
		}
		else if (jade > 0)
		{
			htmltext = "warehouse_keeper_silva_q0357_07.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (Rnd.chance(DROPRATE))
		{
			qs.giveItems(JADE_CRYSTAL, 1);
			qs.playSound(SOUND_ITEMGET);
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
