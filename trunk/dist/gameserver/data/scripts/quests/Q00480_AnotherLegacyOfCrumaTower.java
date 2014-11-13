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

import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import services.SupportMagic;

public class Q00480_AnotherLegacyOfCrumaTower extends Quest implements ScriptFile
{
	// Npcs
	public static final int LILEJ = 33155;
	public static final int LINKENS = 33163;
	public static final int STRANGE_MECHANIC_CR = 33158;
	public static final int MARTES_NPC = 33292;
	public static final int MARTES_RB = 25829;
	// Items
	public static final int TRESURE_TOOL = 17619;
	public static final int MARTES_CORE = 17728;
	
	public Q00480_AnotherLegacyOfCrumaTower()
	{
		super(true);
		addStartNpc(LILEJ);
		addTalkId(LINKENS, MARTES_NPC);
		addKillId(MARTES_RB);
		addQuestItem(TRESURE_TOOL, MARTES_CORE);
		addLevelCheck(38, 100);
		addQuestCompletedCheck(Q10352_LegacyOfCrumaTower.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "33155-9.htm":
				SupportMagic.getSupportMagic(npc, player);
				break;
			
			case "33155-10.htm":
				SupportMagic.getSupportServitorMagic(npc, player);
				break;
			
			case "advanceCond3":
				if (qs.getCond() != 3)
				{
					qs.setCond(3);
				}
				return null;
				
			case "teleportCruma":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				player.teleToLocation(17192, 114173, -3439);
				return null;
				
			case "33163-8.htm":
				if (qs.getQuestItemsCount(TRESURE_TOOL) == 0)
				{
					qs.giveItems(TRESURE_TOOL, 30);
					qs.setCond(2);
				}
				else
				{
					return "33163-12.htm";
				}
				break;
			
			case "EnterInstance":
				if (player.getParty() == null)
				{
					player.sendMessage("You cannot enter without party!"); // pts message?
					return null;
				}
				for (Player member : player.getParty().getPartyMembers())
				{
					final QuestState state = member.getQuestState(Q00480_AnotherLegacyOfCrumaTower.class);
					
					if ((state == null) || (state.getCond() != 3))
					{
						// nothing as I've seen everybody can enter this instance
					}
					else if (state.getCond() == 3)
					{
						state.setCond(4);
					}
				}
				ReflectionUtils.enterReflection(player, 198);
				return null;
				
			case "LeaveInstance":
				player.teleToLocation(17192, 114173, -3439, ReflectionManager.DEFAULT);
				return null;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		final int cond = qs.getCond();
		
		if (qs.getState() == 1)
		{
			if (qs.getPlayer().getLevel() < 38)
			{
				return "33155-lvl.htm";
			}
			else if (!qs.isNowAvailable())
			{
				return "33155-comp.htm";
			}
		}
		
		switch (npc.getId())
		{
			case LILEJ:
				if (cond < 5)
				{
					return "33155.htm";
				}
				break;
			
			case LINKENS:
				if (cond == 1)
				{
					return "33163.htm";
				}
				else if (cond == 2)
				{
					return "33163-5.htm";
				}
				else if (cond == 5)
				{
					if (qs.getQuestItemsCount(MARTES_CORE) == 0)
					{
						return "33163-14.htm";
					}
					else if (qs.getQuestItemsCount(MARTES_CORE) != 0)
					{
						qs.takeItems(MARTES_CORE, -1);
						qs.takeItems(TRESURE_TOOL, -1);
						qs.addExpAndSp(240000, 156000);
						qs.unset("cond");
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(this);
						return "33163-15.htm";
					}
				}
				break;
			
			case MARTES_NPC:
				if (cond == 3)
				{
					return "33292.htm";
				}
				else if (cond == 5)
				{
					return "33292-1.htm";
				}
				break;
		}
		
		return "noquest";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		
		if (player.getParty() == null)
		{
			qs.setCond(5);
		}
		else
		{
			for (Player member : player.getParty().getPartyMembers())
			{
				final QuestState stats = member.getQuestState(Q00480_AnotherLegacyOfCrumaTower.class);
				
				if ((stats == null) || (stats.getCond() != 4))
				{
					continue;
				}
				
				stats.setCond(5);
			}
		}
		
		qs.getPlayer().getReflection().addSpawnWithoutRespawn(MARTES_NPC, Location.findPointToStay(qs.getPlayer(), 50, 100), qs.getPlayer().getGeoIndex());
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