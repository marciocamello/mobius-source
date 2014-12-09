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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;
import lineage2.gameserver.utils.ReflectionUtils;

public class Q10323_GoingIntoARealWarLetsGoToTheTrainingGround extends Quest implements ScriptFile
{
	private final static int Key = 17574;
	private NpcInstance soldierg = null;
	private final static int Shenon = 32974;
	private final static int Evain = 33464;
	private final static int Holden = 33194;
	private final static int Guard = 33021;
	private final static int Husk = 23113;
	private final static int Soldier = 33006;
	private static final int[] SOLDIER_START_POINT =
	{
		-110808,
		253896,
		-1817
	};
	
	public Q10323_GoingIntoARealWarLetsGoToTheTrainingGround()
	{
		super(false);
		addStartNpc(Evain);
		addTalkId(Shenon, Holden);
		addFirstTalkId(Guard);
		addKillId(Husk);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(Q10322_SearchingForTheMysteriousPower.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "quest_ac":
				qs.giveItems(17574, 1);
				qs.setState(STARTED);
				qs.setCond(1);
				spawnsoldier(qs);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "0-3.htm";
				break;
			
			case "qet_rev":
				htmltext = "3-2.htm";
				qs.getPlayer().addExpAndSp(300, 150);
				qs.giveItems(57, 89);
				qs.takeAllItems(17574);
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
				break;
			
			case "enter_instance":
				if (qs.getQuestItemsCount(Key) >= 1)
				{
					despawnsoldier();
					qs.setCond(2);
					qs.playSound(SOUND_MIDDLE);
					enterInstance(player, 176);
					return null;
				}
				htmltext = "1-1.htm";
				break;
			
			case "getshots":
				qs.playSound(SOUND_MIDDLE);
				qs.showTutorialHTML(TutorialShowHtml.QT_003, TutorialShowHtml.TYPE_WINDOW);
				if (!player.isMageClass() || (player.getTemplate().getRace() == Race.orc))
				{
					player.sendPacket(new ExShowScreenMessage(NpcString.GOING_INTO_REAL_WAR_SOULSHOTS_ADDED, 4500, ScreenMessageAlign.TOP_CENTER));
					qs.startQuestTimer("soul_timer", 4000);
					qs.giveItems(5789, 500);
					qs.setCond(5);
				}
				else
				{
					player.sendPacket(new ExShowScreenMessage(NpcString.GOING_INTO_REAL_WAR_SPIRITSHOTS_ADDED, 4500, ScreenMessageAlign.TOP_CENTER));
					qs.startQuestTimer("spirit_timer", 4000);
					qs.giveItems(5790, 500);
					qs.setCond(4);
				}
				return null;
				
			case "soul_timer":
				htmltext = "2-3.htm";
				player.sendPacket(new ExShowScreenMessage(NpcString.GOING_INTO_REAL_WAR_AUTOMATE_SOULSHOTS, 4500, ScreenMessageAlign.TOP_CENTER));
				break;
			
			case "spirit_timer":
				htmltext = "2-3m.htm";
				player.sendPacket(new ExShowScreenMessage(NpcString.GOING_INTO_REAL_WAR_AUTOMATE_SPIRITSHOTS, 4500, ScreenMessageAlign.TOP_CENTER));
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		switch (npc.getId())
		{
			case Evain:
				if (qs.isCompleted())
				{
					htmltext = "0-c.htm";
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "start.htm";
				}
				else if (cond == 1)
				{
					htmltext = "0-4.htm";
				}
				else if ((cond >= 1) && (qs.getQuestItemsCount(Key) < 1))
				{
					qs.giveItems(17574, 1);
					htmltext = "0-5.htm";
				}
				else if (cond == 8)
				{
					htmltext = "0-6.htm";
				}
				else
				{
					htmltext = "noqu.htm";
				}
				break;
			
			case Shenon:
				if (qs.isCompleted())
				{
					htmltext = "3-c.htm";
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 8)
				{
					htmltext = "3-1.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		final QuestState qs = player.getQuestState(getClass());
		String htmltext = "";
		
		if (qs.getCond() == 3)
		{
			if (!player.isMageClass() || (player.getTemplate().getRace() == Race.orc))
			{
				htmltext = "2-2.htm";
			}
			else
			{
				htmltext = "2-2m.htm";
			}
		}
		else if ((qs.getCond() == 4) || (qs.getCond() == 5))
		{
			qs.setCond(7);
			qs.playSound(SOUND_MIDDLE);
			qs.getPlayer().getReflection().addSpawnWithoutRespawn(Husk, new Location(-115029, 247884, -7872, 0), 0);
			qs.getPlayer().getReflection().addSpawnWithoutRespawn(Husk, new Location(-114921, 248281, -7872, 0), 0);
			qs.getPlayer().getReflection().addSpawnWithoutRespawn(Husk, new Location(-114559, 248661, -7872, 0), 0);
			qs.getPlayer().getReflection().addSpawnWithoutRespawn(Husk, new Location(-114148, 248416, -7872, 0), 0);
			
			if (!player.isMageClass() || (player.getTemplate().getRace() == Race.orc))
			{
				htmltext = "2-4.htm";
			}
			else
			{
				htmltext = "2-4m.htm";
			}
		}
		else if (qs.getCond() == 8)
		{
			htmltext = "2-5.htm";
		}
		else if (qs.isCompleted())
		{
			htmltext = TODO_FIND_HTML;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		int npcId = npc.getId();
		int killedhusk = qs.getInt("killedhusk");
		
		if ((npcId == Husk) && ((qs.getCond() == 2) || (qs.getCond() == 7)))
		{
			if (killedhusk >= 3)
			{
				qs.setCond(qs.getCond() + 1);
				qs.unset("killedhusk");
				qs.playSound(SOUND_MIDDLE);
			}
			else
			{
				qs.set("killedhusk", ++killedhusk);
			}
		}
		
		return null;
	}
	
	private void enterInstance(Player player, int instancedZoneId)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(instancedZoneId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(instancedZoneId))
		{
			ReflectionUtils.enterReflection(player, instancedZoneId);
		}
	}
	
	private void spawnsoldier(QuestState qs)
	{
		soldierg = NpcUtils.spawnSingle(Soldier, Location.findPointToStay(SOLDIER_START_POINT[0], SOLDIER_START_POINT[1], SOLDIER_START_POINT[2], 50, 100, qs.getPlayer().getGeoIndex()));
		soldierg.setFollowTarget(qs.getPlayer());
		Functions.npcSay(soldierg, NpcString.S1_COME_WITH_ME_I_WILL_LEAD_YOU_TO_HOLDEN, ChatType.NPC_SAY, 800, qs.getPlayer().getName());
	}
	
	private void despawnsoldier()
	{
		if (soldierg != null)
		{
			soldierg.deleteMe();
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
