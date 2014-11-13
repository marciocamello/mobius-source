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

public class Q00490_DutyOfTheSurvivor extends Quest implements ScriptFile
{
	// Npc
	public static final int Volodos = 30137;
	// Items
	private static final int Zhelch = 34059;
	private static final int Blood = 34060;
	// Monsters
	public static final int[] Monsters =
	{
		23162,
		23163,
		23164,
		23165,
		23166,
		23167,
		23168,
		23169,
		23170,
		23171,
		23172,
		23173
	};
	
	public Q00490_DutyOfTheSurvivor()
	{
		super(true);
		addStartNpc(Volodos);
		addTalkId(Volodos);
		addKillId(Monsters);
		addQuestItem(Zhelch, Blood);
		addLevelCheck(85, 89);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (event.equals("30137-6.htm"))
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
		final int cond = qs.getCond();
		final Player player = qs.getPlayer();
		
		switch (qs.getState())
		{
			case 1:
				if ((player.getLevel() < 85) || (player.getLevel() > 89))
				{
					return "30137-lvl.htm";
				}
				if (!qs.isNowAvailable())
				{
					return "30137-comp.htm";
				}
				return "30137.htm";
				
			case 2:
				if (cond == 1)
				{
					return "30137-7.htm";
				}
				if (cond == 2)
				{
					qs.giveItems(57, 505062);
					qs.addExpAndSp(145557000, 58119840);
					qs.unset("cond");
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(this);
					return "30137-9.htm";
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
		
		if (Util.contains(Monsters, npc.getId()))
		{
			if (Rnd.chance(10))
			{
				if (Rnd.chance(50))
				{
					if (qs.getQuestItemsCount(Zhelch) < 20)
					{
						qs.giveItems(Zhelch, 1);
					}
				}
				else if (qs.getQuestItemsCount(Blood) < 20)
				{
					qs.giveItems(Blood, 1);
				}
				
				if ((qs.getQuestItemsCount(Zhelch) >= 20) && (qs.getQuestItemsCount(Blood) >= 20))
				{
					qs.setCond(2);
				}
			}
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