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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

public class Q00466_PlacingMySmallPower extends Quest implements ScriptFile
{
	// npc
	public static final int ASTERIOS = 30154;
	public static final int NOEM_MILID = 32895;
	// mobs
	private final int[] WingMobs =
	{
		22863,
		22864,
		22907,
		22899,
		22891,
		22875
	};
	private final int[] CoconMobs =
	{
		32920
	};
	private final int[] BreathMobs =
	{
		22870,
		22886,
		22902,
		22894,
		22878
	};
	// q items
	public static final int WingI = 17597;
	public static final int CoconI = 17598;
	public static final int BreathI = 17599;
	public static final int ProofWord = 30384;
	public static final int NavozItem = 17596;
	public static final int NavozRecipe = 17603;
	
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
	
	public Q00466_PlacingMySmallPower()
	{
		super(true);
		addStartNpc(ASTERIOS);
		addTalkId(NOEM_MILID);
		addKillId(WingMobs);
		addKillId(CoconMobs);
		addKillId(BreathMobs);
		addQuestItem(WingI);
		addQuestItem(CoconI);
		addQuestItem(BreathI);
		addQuestItem(NavozItem);
		addQuestItem(NavozRecipe);
		addLevelCheck(90, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("30154-4.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		
		if (event.equalsIgnoreCase("32895-4.htm"))
		{
			st.setCond(2);
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		int npcId = npc.getId();
		int state = st.getState();
		int cond = st.getCond();
		
		if (npcId == ASTERIOS)
		{
			if (state == 1)
			{
				if (player.getLevel() < 90)
				{
					return "30154-lvl.htm";
				}
				
				if (!st.isNowAvailable())
				{
					return "30154-comp.htm";
				}
				
				if (st.getPlayer().getLevel() < 90)
				{
					return "30154-lvl.htm";
				}
				
				return "30154.htm";
			}
			
			if (state == 2)
			{
				if (cond == 1)
				{
					return "32921-5.htm";
				}
				
				if (cond == 5)
				{
					st.giveItems(ProofWord, 1);
					st.takeItems(NavozItem, 1);
					st.unset("cond");
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(this);
					return "32921-6.htm"; // no further html do here
				}
			}
		}
		
		if ((npcId == NOEM_MILID) && (state == 2))
		{
			if (cond == 1)
			{
				return "32895.htm";
			}
			
			if (cond == 2)
			{
				return "32895-5.htm";
			}
			
			if (cond == 3)
			{
				st.setCond(4);
				st.giveItems(NavozRecipe, 1);
				return "32895-6.htm";
			}
			
			if ((cond == 4) && (st.getQuestItemsCount(NavozItem) == 0))
			{
				return "32895-7.htm";
			}
			
			if ((cond == 4) && (st.getQuestItemsCount(NavozItem) != 0))
			{
				st.setCond(5);
				return "32895-8.htm";
			}
			
			if (cond == 5)
			{
				return "32895-10.htm";
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		
		if ((cond != 2) || (npc == null))
		{
			return null;
		}
		
		if (Util.contains(WingMobs, npc.getId()) && Rnd.chance(7))
		{
			st.giveItems(WingI, 1);
		}
		
		if (Util.contains(CoconMobs, npc.getId()) && Rnd.chance(10))
		{
			st.giveItems(CoconI, 1);
		}
		
		if (Util.contains(BreathMobs, npc.getId()) && Rnd.chance(12))
		{
			st.giveItems(BreathI, 1);
		}
		
		checkCond(st);
		return null;
	}
	
	private static void checkCond(QuestState st)
	{
		if ((st.getQuestItemsCount(WingI) >= 5) && (st.getQuestItemsCount(CoconI) >= 5) && (st.getQuestItemsCount(BreathI) >= 5))
		{
			st.setCond(3);
		}
	}
}