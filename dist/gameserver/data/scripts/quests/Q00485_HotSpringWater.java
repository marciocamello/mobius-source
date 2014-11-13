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

public class Q00485_HotSpringWater extends Quest implements ScriptFile
{
	// Npcs
	public static final int GUIDE = 33463;
	public static final int VALDEMOR = 30844;
	// Items
	public static final int WATER = 19497;
	// Monsters
	private final static int[] MONSTERS =
	{
		21314,
		21315,
		21316,
		21317,
		21318,
		21319,
		21320,
		21321,
		21322,
		21323
	};
	
	public Q00485_HotSpringWater()
	{
		super(true);
		addStartNpc(GUIDE);
		addTalkId(VALDEMOR);
		addKillId(MONSTERS);
		addQuestItem(WATER);
		addLevelCheck(70, 74);
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
		int state = qs.getState();
		int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case GUIDE:
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
					
					if (cond == 2)
					{
						return "33463-5.htm";
					}
				}
				break;
			
			case VALDEMOR:
				if (state == 2)
				{
					if (cond == 1)
					{
						return "30844-1.htm";
					}
					else if (cond == 2)
					{
						qs.addExpAndSp(9483000, 9470430);
						qs.takeItems(WATER, -1);
						qs.giveItems(57, 371745);
						qs.unset("cond");
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(this);
						return "30844.htm";
					}
				}
				break;
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
		
		if (Util.contains(MONSTERS, npc.getId()) && Rnd.chance(50))
		{
			qs.giveItems(WATER, 1);
			qs.playSound(SOUND_MIDDLE);
		}
		
		if (qs.getQuestItemsCount(WATER) >= 40)
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