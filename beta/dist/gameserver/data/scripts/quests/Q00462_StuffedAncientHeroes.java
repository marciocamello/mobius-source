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

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00462_StuffedAncientHeroes extends Quest implements ScriptFile
{
	// Npc
	public static final int Tipia = 32892;
	// Monsters
	private final int[] Bosses =
	{
		25760,
		25761,
		25762,
		25763,
		25764,
		25766,
		25767,
		25768,
		25769,
		25770
	};
	
	public Q00462_StuffedAncientHeroes()
	{
		super(true);
		addStartNpc(Tipia);
		addKillId(Bosses);
		addLevelCheck(95, 100);
		addQuestCompletedCheck(Q10317_OrbisWitch.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "32892-6.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "reward":
				qs.giveItems(30386, 3);
				qs.unset("cond");
				qs.unset("1bk");
				qs.unset("2bk");
				qs.set("1bk", "0");
				qs.set("2bk", "0");
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(this);
				return "32892-11.htm";
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		final Player player = st.getPlayer();
		final int state = st.getState();
		final int cond = st.getCond();
		
		if (state == 1)
		{
			if (player.getLevel() < 95)
			{
				return "32892-lvl.htm";
			}
			else if (!st.isNowAvailable())
			{
				return "32892-comp.htm";
			}
			else if (st.getPlayer().getLevel() < 95)
			{
				return "32892-lvl.htm";
			}
			
			String htmltext = HtmCache.getInstance().getNotNull("quests/Q00462_StuffedAncientHeroes/32892.htm", st.getPlayer());
			htmltext.replace("%name%", player.getName());
			return htmltext;
		}
		else if (state == 2)
		{
			if (cond == 1)
			{
				return "32892-7.htm";
			}
			else if (cond == 3)
			{
				return "32892-8.htm";
			}
			else if (cond == 2)
			{
				return "reward";
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		if ((cond != 1) || (cond != 3))
		{
			return null;
		}
		
		if (npc != null)
		{
			if (qs.getInt("1bk") == 1)
			{
				qs.set("2bk", "1");
				qs.setCond(2);
			}
			else
			{
				qs.set("1bk", "1");
				qs.setCond(3);
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