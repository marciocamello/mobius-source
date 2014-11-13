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

public class Q00467_TheOppressorAndTheOppressed extends Quest implements ScriptFile
{
	// Npcs
	public static final int GUIDE = 33463;
	public static final int DASMOND = 30855;
	// Monsters
	private final int[] Mobs =
	{
		20650,
		20648,
		20647,
		20649
	};
	// Items
	public static final int CLEAR_CORE = 19488;
	
	public Q00467_TheOppressorAndTheOppressed()
	{
		super(true);
		addStartNpc(GUIDE);
		addTalkId(DASMOND);
		addKillId(Mobs);
		addQuestItem(CLEAR_CORE);
		addLevelCheck(60, 64);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		qs.getPlayer();
		
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
		else if ((npcId == DASMOND) && (state == 2))
		{
			if (cond == 1)
			{
				return "30855-1.htm";
			}
			else if (cond == 2)
			{
				qs.giveItems(57, 194000);
				qs.addExpAndSp(1879400, 1782000);
				qs.takeItems(CLEAR_CORE, -1);
				qs.unset("cond");
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				return "30855.htm";
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getCond() != 1) || (npc == null))
		{
			return null;
		}
		
		if (Util.contains(Mobs, npc.getId()) && Rnd.chance(50))
		{
			qs.giveItems(CLEAR_CORE, 1);
		}
		
		if (qs.getQuestItemsCount(CLEAR_CORE) >= 30)
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