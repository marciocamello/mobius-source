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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

import org.apache.commons.lang3.ArrayUtils;

public class Q00492_TombRaiders extends Quest implements ScriptFile
{
	// npc
	public static final int ZENIA = 32140;
	// mobs
	private static final int[] Mobs =
	{
		23193,
		23194,
		23195,
		23196
	};
	// q items
	public static final int ANCIENT_REL = 34769;
	
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
	
	public Q00492_TombRaiders()
	{
		super(true);
		addStartNpc(ZENIA);
		addKillId(Mobs);
		addQuestItem(ANCIENT_REL);
		addLevelCheck(80, 100);
		addClassLevelCheck(4);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("32140-5.htm"))
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
		Player player = st.getPlayer();
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		
		if (npcId == ZENIA)
		{
			if (player.getLevel() < 80)
			{
				return "32140-lvl.htm";
			}
			
			if (!player.getClassId().isOfLevel(ClassLevel.Second))
			{
				return "32140-class.htm";
			}
			
			if (state == 1)
			{
				if (!st.isNowAvailable())
				{
					return "32140-comp.htm";
				}
				
				return "32140.htm";
			}
			
			if (state == 2)
			{
				if (cond == 1)
				{
					return "32140-6.htm";
				}
				
				if (cond == 2)
				{
					st.addExpAndSp(9009000, 8997060); // Unknown!!!!!
					st.takeItems(ANCIENT_REL, -1);
					st.unset("cond");
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(this);
					return "32140-7.htm";
				}
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
		
		if (ArrayUtils.contains(Mobs, npc.getNpcId()) && Rnd.chance(25))
		{
			st.giveItems(ANCIENT_REL, 1);
		}
		
		if (st.getQuestItemsCount(ANCIENT_REL) >= 50)
		{
			st.setCond(2);
		}
		
		return null;
	}
}