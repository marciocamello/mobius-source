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

import org.apache.commons.lang3.ArrayUtils;

public class _467_TheOppressorAndTheOppressed extends Quest implements ScriptFile
{
	// npc
	public static final int GUIDE = 33463;
	public static final int DASMOND = 30855;
	// mobs
	private final int[] Mobs =
	{
		20650,
		20648,
		20647,
		20649
	};
	// q items
	public static final int CLEAR_CORE = 19488;
	
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
	
	public _467_TheOppressorAndTheOppressed()
	{
		super(true);
		addStartNpc(GUIDE);
		addTalkId(DASMOND);
		addKillId(Mobs);
		addQuestItem(CLEAR_CORE);
		addLevelCheck(60, 64);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		st.getPlayer();
		
		if (event.equalsIgnoreCase("33463-3.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		st.getPlayer();
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		
		if (npcId == GUIDE)
		{
			if (state == 1)
			{
				if (!st.isNowAvailableByTime())
				{
					return "33463-comp.htm";
				}
				
				return "33463.htm";
			}
			
			if (state == 2)
			{
				if (cond == 1)
				{
					return "33463-4.htm";
				}
			}
		}
		
		if ((npcId == DASMOND) && (state == 2))
		{
			if (cond == 1)
			{
				return "30855-1.htm";
			}
			
			if (cond == 2)
			{
				st.giveItems(57, 194000);
				st.addExpAndSp(1879400, 1782000);
				st.takeItems(CLEAR_CORE, -1);
				st.unset("cond");
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(this);
				return "30855.htm"; // no further html do here
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		
		if ((cond != 1) || (npc == null))
		{
			return null;
		}
		
		if (ArrayUtils.contains(Mobs, npc.getNpcId()) && Rnd.chance(50))
		{
			st.giveItems(CLEAR_CORE, 1);
		}
		
		if (st.getQuestItemsCount(CLEAR_CORE) >= 30)
		{
			st.setCond(2);
		}
		
		return null;
	}
}