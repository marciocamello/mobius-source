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

import org.apache.commons.lang3.ArrayUtils;

public class _490_DutyOfTheSurvivor extends Quest implements ScriptFile
{
	// npc
	public static final int VOLODOS = 30137;
	
	// mobs
	public static final int[] mobs =
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
	private static final int Zhelch = 34059;
	private static final int Blood = 34060;
	
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
	
	public _490_DutyOfTheSurvivor()
	{
		super(true);
		addStartNpc(VOLODOS);
		addTalkId(VOLODOS);
		addKillId(mobs);
		addLevelCheck(85, 89);
		addQuestItem(Zhelch);
		addQuestItem(Blood);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("30137-6.htm"))
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
		
		if (npcId == VOLODOS)
		{
			if (state == 1)
			{
				if ((player.getLevel() < 85) || (player.getLevel() > 89))
				{
					return "30137-lvl.htm";
				}
				
				if (!st.isNowAvailable())
				{
					return "30137-comp.htm";
				}
				
				return "30137.htm";
			}
			
			if (state == 2)
			{
				if (cond == 1)
				{
					return "30137-7.htm";
				}
				
				if (cond == 2)
				{
					st.giveItems(57, 505062);
					st.addExpAndSp(145557000, 58119840);
					st.unset("cond");
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(this);
					return "30137-9.htm";
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
		
		if (ArrayUtils.contains(mobs, npc.getNpcId()))
		{
			if (Rnd.chance(10))
			{
				if (Rnd.chance(50))
				{
					if (st.getQuestItemsCount(Zhelch) < 20)
					{
						st.giveItems(Zhelch, 1);
					}
				}
				else if (st.getQuestItemsCount(Blood) < 20)
				{
					st.giveItems(Blood, 1);
				}
				
				if ((st.getQuestItemsCount(Zhelch) >= 20) && (st.getQuestItemsCount(Blood) >= 20))
				{
					st.setCond(2);
				}
			}
		}
		
		return null;
	}
}