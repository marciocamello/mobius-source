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

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00382_KailsMagicCoin extends Quest implements ScriptFile
{
	// Npc
	private static final int VERGARA = 30687;
	// Item
	private static final int ROYAL_MEMBERSHIP = 5898;
	// Monsters
	private static final Map<Integer, int[]> MOBS = new HashMap<>();
	static
	{
		MOBS.put(21017, new int[]
		{
			5961
		});
		MOBS.put(21019, new int[]
		{
			5962
		});
		MOBS.put(21020, new int[]
		{
			5963
		});
		MOBS.put(21022, new int[]
		{
			5961,
			5962,
			5963
		});
	}
	
	public Q00382_KailsMagicCoin()
	{
		super(false);
		addStartNpc(VERGARA);
		
		for (int mobId : MOBS.keySet())
		{
			addKillId(mobId);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "head_blacksmith_vergara_q0382_03.htm":
				if ((qs.getPlayer().getLevel() >= 55) && (qs.getQuestItemsCount(ROYAL_MEMBERSHIP) > 0))
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
				}
				else
				{
					htmltext = "head_blacksmith_vergara_q0382_01.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case "list":
				MultiSellHolder.getInstance().SeparateAndSend(382, qs.getPlayer(), 0);
				htmltext = null;
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
		
		if ((qs.getQuestItemsCount(ROYAL_MEMBERSHIP) == 0) || (qs.getPlayer().getLevel() < 55))
		{
			htmltext = "head_blacksmith_vergara_q0382_01.htm";
			qs.exitCurrentQuest(true);
		}
		else if (cond == 0)
		{
			htmltext = "head_blacksmith_vergara_q0382_02.htm";
		}
		else
		{
			htmltext = "head_blacksmith_vergara_q0382_04.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getState() != STARTED) || (qs.getQuestItemsCount(ROYAL_MEMBERSHIP) == 0))
		{
			return null;
		}
		
		final int[] droplist = MOBS.get(npc.getId());
		qs.rollAndGive(droplist[Rnd.get(droplist.length)], 1, 10);
		
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
