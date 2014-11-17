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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

public class Q10322_SearchingForTheMysteriousPower extends Quest implements ScriptFile
{
	private static final int Evan = 33464;
	private static final int Shenon = 32974;
	private static final int Helper = 32981;
	private static final int Soldier = 33016;
	private static final int[] SOLDIER_START_POINT =
	{
		-111430,
		255720,
		-1440
	};
	private static final int Crow = 27457;
	private NpcInstance soldierg = null;
	
	public Q10322_SearchingForTheMysteriousPower()
	{
		super(false);
		addStartNpc(Shenon);
		addTalkId(Shenon);
		addTalkId(Helper);
		addTalkId(Evan);
		addKillId(Crow);
		addKillId(Soldier);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(Q10321_QualificationsOfTheSeeker.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				spawnsoldier(qs);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "0-3.htm";
				break;
			
			case "bufs":
				SkillTable.getInstance().getInfo(4322, 1).getEffects(player, player, false, false);
				SkillTable.getInstance().getInfo(4323, 1).getEffects(player, player, false, false);
				SkillTable.getInstance().getInfo(5637, 1).getEffects(player, player, false, false);
				if (!player.isMageClass() || (player.getTemplate().getRace() == Race.orc))
				{
					SkillTable.getInstance().getInfo(4324, 1).getEffects(player, player, false, false);
					SkillTable.getInstance().getInfo(4327, 1).getEffects(player, player, false, false);
					SkillTable.getInstance().getInfo(4325, 1).getEffects(player, player, false, false);
					SkillTable.getInstance().getInfo(4326, 1).getEffects(player, player, false, false);
				}
				else
				{
					SkillTable.getInstance().getInfo(4328, 1).getEffects(player, player, false, false);
					SkillTable.getInstance().getInfo(4329, 1).getEffects(player, player, false, false);
					SkillTable.getInstance().getInfo(4330, 1).getEffects(player, player, false, false);
					SkillTable.getInstance().getInfo(4331, 1).getEffects(player, player, false, false);
				}
				qs.setCond(5);
				qs.playSound(SOUND_MIDDLE);
				htmltext = "2-2.htm";
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
			case Shenon:
				if (qs.isCompleted())
				{
					htmltext = "0-c.htm";
				}
				else if ((cond == 0) && isAvailableFor(qs.getPlayer()))
				{
					htmltext = "start.htm";
				}
				else if (cond >= 1)
				{
					htmltext = "0-4.htm";
				}
				else
				{
					htmltext = "noqu.htm";
				}
				break;
			
			case Evan:
				if (qs.isCompleted())
				{
					htmltext = "1-c.htm";
				}
				else if (cond == 0)
				{
					htmltext = TODO_FIND_HTML;
				}
				else if (cond == 1)
				{
					htmltext = "1-1.htm";
					despawnsoldier();
					qs.setCond(2);
					qs.playSound(SOUND_MIDDLE);
				}
				else if (cond == 2)
				{
					htmltext = "1-3.htm";
				}
				else if (cond == 3)
				{
					htmltext = "1-2.htm";
					qs.setCond(4);
					qs.playSound(SOUND_MIDDLE);
				}
				else if (cond == 4)
				{
					htmltext = "1-4.htm";
				}
				else if (cond == 5)
				{
					htmltext = "1-6.htm";
				}
				else if (cond == 6)
				{
					htmltext = "1-5.htm";
					Functions.npcSayToPlayer(npc, qs.getPlayer(), NpcString.WEAPONS_HAVE_BEEN_ADDED_TO_YOUR_INVENTORY, ChatType.NPC_SAY);
					qs.getPlayer().addExpAndSp(300, 800);
					qs.giveItems(57, 7000);
					qs.giveItems(17, 500);
					qs.giveItems(7816, 1);
					qs.giveItems(7817, 1);
					qs.giveItems(7818, 1);
					qs.giveItems(7819, 1);
					qs.giveItems(7820, 1);
					qs.giveItems(7821, 1);
					qs.giveItems(1060, 50);
					qs.exitCurrentQuest(false);
					qs.playSound(SOUND_FINISH);
				}
				break;
			
			case Helper:
				if (qs.isCompleted() || (cond == 0) || (cond == 1) || (cond == 2) || (cond == 3) || (cond == 6))
				{
					htmltext = "2-nc.htm";
				}
				else if (cond == 4)
				{
					htmltext = "2-1.htm";
					qs.showTutorialHTML(TutorialShowHtml.QT_002, TutorialShowHtml.TYPE_WINDOW);
				}
				else if (cond == 5)
				{
					htmltext = "2-3.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (npc.getId() == Crow)
		{
			if ((qs.getCond() == 2))
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(3);
			}
			else if ((qs.getCond() == 5))
			{
				qs.setCond(6);
				qs.playSound(SOUND_MIDDLE);
			}
		}
		
		return null;
	}
	
	private void spawnsoldier(QuestState qs)
	{
		soldierg = NpcUtils.spawnSingle(Soldier, Location.findPointToStay(SOLDIER_START_POINT[0], SOLDIER_START_POINT[1], SOLDIER_START_POINT[2], 50, 100, qs.getPlayer().getGeoIndex()));
		soldierg.setFollowTarget(qs.getPlayer());
		Functions.npcSay(soldierg, NpcString.S1_COME_WITH_ME_I_WILL_LEAD_YOU_TO_IBANE, ChatType.NPC_SAY, 800, qs.getPlayer().getName());
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
