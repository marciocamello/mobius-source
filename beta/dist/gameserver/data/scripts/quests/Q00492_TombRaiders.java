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
import lineage2.gameserver.enums.ClassLevel;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

public class Q00492_TombRaiders extends Quest implements ScriptFile
{
	// Npc
	public static final int ZENIA = 32140;
	// Item
	public static final int ANCIENT_REL = 34769;
	// Monsters
	private static final int[] MONSTERS =
	{
		23193,
		23194,
		23195,
		23196
	};
	
	public Q00492_TombRaiders()
	{
		super(true);
		addStartNpc(ZENIA);
		addKillId(MONSTERS);
		addQuestItem(ANCIENT_REL);
		addLevelCheck(80, 100);
		addClassLevelCheck(4);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("32140-5.htm"))
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
		final Player player = qs.getPlayer();
		final int state = qs.getState();
		final int cond = qs.getCond();
		
		if (player.getLevel() < 80)
		{
			return "32140-lvl.htm";
		}
		else if (!player.getClassId().isOfLevel(ClassLevel.Second))
		{
			return "32140-class.htm";
		}
		
		if (state == 1)
		{
			if (!qs.isNowAvailable())
			{
				return "32140-comp.htm";
			}
			
			return "32140.htm";
		}
		else if (state == 2)
		{
			if (cond == 1)
			{
				return "32140-6.htm";
			}
			else if (cond == 2)
			{
				qs.addExpAndSp(9009000, 8997060);
				qs.takeItems(ANCIENT_REL, -1);
				qs.unset("cond");
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				return "32140-7.htm";
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
		
		if (Util.contains(MONSTERS, npc.getId()) && Rnd.chance(25))
		{
			qs.giveItems(ANCIENT_REL, 1);
		}
		
		if (qs.getQuestItemsCount(ANCIENT_REL) >= 50)
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