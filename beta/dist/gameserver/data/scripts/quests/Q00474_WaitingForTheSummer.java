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
import lineage2.gameserver.utils.Util;

public class Q00474_WaitingForTheSummer extends Quest implements ScriptFile
{
	// Npcs
	private static final int GUIDE = 33463;
	private static final int VISHOTSKY = 31981;
	// Items
	private static final int BUFFALO_MEAT = 19490;
	private static final int URSUS_MEAT = 19491;
	private static final int YETI_MIAT = 19492;
	// Monsters
	private static final int[] BUFFALO =
	{
		22093,
		22094
	};
	private static final int[] URSUS =
	{
		22095,
		22096
	};
	private static final int[] YETI =
	{
		22097,
		22098
	};
	
	public Q00474_WaitingForTheSummer()
	{
		super(false);
		addStartNpc(GUIDE);
		addTalkId(VISHOTSKY);
		addKillId(BUFFALO);
		addKillId(URSUS);
		addKillId(YETI);
		addQuestItem(BUFFALO_MEAT, YETI_MIAT, URSUS_MEAT);
		addLevelCheck(60, 64);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("33463-3.htm"))
		{
			qs.setCond(1);
			qs.setState(STARTED);
			qs.playSound(SOUND_ACCEPT);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int state = qs.getState();
		final int cond = qs.getCond();
		
		if (npcId == GUIDE)
		{
			if (state == 1)
			{
				if (!qs.isNowAvailableByTime())
				{
					return "33463-comp.htm";
				}
				
				return "33463.htm";
			}
			else if (state == 2)
			{
				if (cond == 1)
				{
					return "33463-4.htm";
				}
			}
		}
		else if ((npcId == VISHOTSKY) && (state == 2))
		{
			if (cond == 1)
			{
				return "31981-1.htm";
			}
			else if (cond == 2)
			{
				qs.giveItems(57, 194000);
				qs.addExpAndSp(1879400, 1782000);
				qs.takeItems(BUFFALO_MEAT, -1);
				qs.takeItems(URSUS_MEAT, -1);
				qs.takeItems(YETI_MIAT, -1);
				qs.unset("cond");
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				return "31981.htm";
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		final int cond = qs.getCond();
		
		if ((cond == 1) && (Rnd.chance(50)))
		{
			if (Util.contains(YETI, npcId))
			{
				if (qs.getQuestItemsCount(YETI_MIAT) < 30)
				{
					qs.giveItems(YETI_MIAT, 1);
					qs.playSound("ItemSound.quest_itemget");
				}
			}
			else if (Util.contains(URSUS, npcId))
			{
				if (qs.getQuestItemsCount(URSUS_MEAT) < 30)
				{
					qs.giveItems(URSUS_MEAT, 1);
					qs.playSound("ItemSound.quest_itemget");
				}
			}
			else if (Util.contains(BUFFALO, npcId))
			{
				if (qs.getQuestItemsCount(BUFFALO_MEAT) < 30)
				{
					qs.giveItems(BUFFALO_MEAT, 1);
					qs.playSound("ItemSound.quest_itemget");
				}
			}
			
			if ((qs.getQuestItemsCount(YETI_MIAT) >= 30) && (qs.getQuestItemsCount(URSUS_MEAT) >= 30) && (qs.getQuestItemsCount(BUFFALO_MEAT) >= 30))
			{
				qs.setCond(2);
				qs.playSound("ItemSound.quest_middle");
			}
		}
		
		return null;
	}
	
	public boolean isDailyQuest()
	{
		return true;
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
