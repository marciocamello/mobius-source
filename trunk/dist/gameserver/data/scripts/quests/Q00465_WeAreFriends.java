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
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;

public class Q00465_WeAreFriends extends Quest implements ScriptFile
{
	public static final int FEYA_STARTER = 32921;
	public static final int FAIRY_CITIZEN = 32922;
	public static final int COCON = 32919;
	public static final int HUGE_COCON = 32920;
	public static final int SIGN_OF_GRATITUDE = 17377;
	private static NpcInstance npcFeya = null;
	
	public Q00465_WeAreFriends()
	{
		super(true);
		addStartNpc(FEYA_STARTER);
		addTalkId(FAIRY_CITIZEN);
		addFirstTalkId(FAIRY_CITIZEN);
		addKillId(COCON, HUGE_COCON);
		addQuestItem(SIGN_OF_GRATITUDE);
		addLevelCheck(90, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		qs.getPlayer();
		
		switch (event)
		{
			case "32921-4.htm":
				qs.setCond(1);
				qs.setState(2);
				qs.playSound("ItemSound.quest_accept");
				break;
			
			case "32922-4.htm":
				qs.setCond(2);
				qs.giveItems(17377, 2L);
				return "despawn_task";
				
			case "despawn_task":
				if (npcFeya == null)
				{
					return null;
				}
				qs.unset("q465feya");
				npcFeya.deleteMe();
				npcFeya = null;
				return null;
				
			case "32921-8.htm":
				qs.takeItems(17377, 2L);
				return "reward";
				
			case "32921-10.htm":
				return "reward";
				
			case "reward":
				qs.giveItems(17378, 1L);
				qs.unset("cond");
				qs.playSound("ItemSound.quest_finish");
				qs.exitCurrentQuest(this);
				if (qs.getQuestItemsCount(17377) > 0L)
				{
					qs.giveItems(30384, 2L);
					return "32921-10.htm";
				}
				qs.giveItems(30384, 4L);
				return "32921-8.htm";
		}
		
		return event;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs = player.getQuestState(getClass());
		
		if (qs == null)
		{
			return "32922.htm";
		}
		else if ((qs.get("q465feya") != null) && (Integer.parseInt(qs.get("q465feya")) != npc.getObjectId()))
		{
			return "32922-1.htm";
		}
		else if (qs.get("q465feya") == null)
		{
			return "32922-1.htm";
		}
		
		return "32922-3.htm";
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		final int npcId = npc.getId();
		final int state = qs.getState();
		final int cond = qs.getCond();
		
		if (npcId == FEYA_STARTER)
		{
			if (state == 1)
			{
				if (player.getLevel() < 90)
				{
					return "32921-lvl.htm";
				}
				else if (!qs.isNowAvailableByTime())
				{
					return "32921-comp.htm";
				}
				else if (qs.getPlayer().getLevel() < 90)
				{
					return "32921-lvl.htm";
				}
				
				return "32921.htm";
			}
			else if (state == 2)
			{
				if (cond == 1)
				{
					return "32921-5.htm";
				}
				else if (cond == 2)
				{
					return "32921-6.htm";
				}
			}
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getPlayer() != null)
		{
			qs.getPlayer().sendMessage("We Are Friend... ok");
		}
		
		if (Rnd.chance(5))
		{
			npcFeya = Functions.spawn(Location.findPointToStay(qs.getPlayer(), 50, 100), 32922);
			qs.set("q465feya", "" + npcFeya.getObjectId() + "");
			qs.startQuestTimer("despawn_task", 180000L);
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