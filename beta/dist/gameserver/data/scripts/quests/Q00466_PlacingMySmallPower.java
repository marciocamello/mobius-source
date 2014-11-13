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
	// Npcs
	public static final int ASTERIOS = 30154;
	public static final int NOEM_MILID = 32895;
	// Monsters
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
	// Items
	public static final int WingI = 17597;
	public static final int CoconI = 17598;
	public static final int BreathI = 17599;
	public static final int ProofWord = 30384;
	public static final int NavozItem = 17596;
	public static final int NavozRecipe = 17603;
	
	public Q00466_PlacingMySmallPower()
	{
		super(true);
		addStartNpc(ASTERIOS);
		addTalkId(NOEM_MILID);
		addKillId(WingMobs);
		addKillId(CoconMobs);
		addKillId(BreathMobs);
		addQuestItem(WingI, CoconI, BreathI, NavozItem, NavozRecipe);
		addLevelCheck(90, 100);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "30154-4.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32895-4.htm":
				qs.setCond(2);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		final int npcId = npc.getId();
		final int state = qs.getState();
		final int cond = qs.getCond();
		
		if (npcId == ASTERIOS)
		{
			if (state == 1)
			{
				if (player.getLevel() < 90)
				{
					return "30154-lvl.htm";
				}
				else if (!qs.isNowAvailable())
				{
					return "30154-comp.htm";
				}
				else if (qs.getPlayer().getLevel() < 90)
				{
					return "30154-lvl.htm";
				}
				
				return "30154.htm";
			}
			else if (state == 2)
			{
				if (cond == 1)
				{
					return "32921-5.htm";
				}
				else if (cond == 5)
				{
					qs.giveItems(ProofWord, 1);
					qs.takeItems(NavozItem, 1);
					qs.unset("cond");
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(this);
					return "32921-6.htm"; // no further html do here
				}
			}
		}
		else if ((npcId == NOEM_MILID) && (state == 2))
		{
			switch (cond)
			{
				case 1:
					return "32895.htm";
					
				case 2:
					return "32895-5.htm";
					
				case 3:
					qs.setCond(4);
					qs.giveItems(NavozRecipe, 1);
					return "32895-6.htm";
					
				case 4:
					if (qs.getQuestItemsCount(NavozItem) == 0)
					{
						return "32895-7.htm";
					}
					qs.setCond(5);
					return "32895-8.htm";
					
				case 5:
					return "32895-10.htm";
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		if ((cond != 2) || (npc == null))
		{
			return null;
		}
		
		if (Util.contains(WingMobs, npc.getId()) && Rnd.chance(7))
		{
			qs.giveItems(WingI, 1);
		}
		else if (Util.contains(CoconMobs, npc.getId()) && Rnd.chance(10))
		{
			qs.giveItems(CoconI, 1);
		}
		else if (Util.contains(BreathMobs, npc.getId()) && Rnd.chance(12))
		{
			qs.giveItems(BreathI, 1);
		}
		
		checkCond(qs);
		return null;
	}
	
	private static void checkCond(QuestState qs)
	{
		if ((qs.getQuestItemsCount(WingI) >= 5) && (qs.getQuestItemsCount(CoconI) >= 5) && (qs.getQuestItemsCount(BreathI) >= 5))
		{
			qs.setCond(3);
		}
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