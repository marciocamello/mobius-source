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

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

public class Q10329_BackupSeekers extends Quest implements ScriptFile
{
	// Npcs
	private static final int Atran = 33448;
	private static final int Kakai = 30565;
	private static final int Soldier = 33204;
	// Others
	private NpcInstance soldierg = null;
	private static final int[] SOLDIER_START_POINT =
	{
		-117880,
		255864,
		-1352
	};
	
	public Q10329_BackupSeekers()
	{
		super(false);
		addStartNpc(Kakai);
		addTalkId(Atran, Kakai);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(Q10328_RequestToSealTheEvilFragment.class);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "quest_ac":
				qs.setState(STARTED);
				qs.setCond(1);
				qs.playSound(SOUND_ACCEPT);
				htmltext = "0-3.htm";
				spawnsolider(qs);
				break;
			
			case "qet_rev":
				qs.getPlayer().sendPacket(new ExShowScreenMessage(NpcString.GOING_INTO_REAL_WAR_SOULSHOTS_ADDED, 4500, ScreenMessageAlign.TOP_CENTER));
				htmltext = "1-2.htm";
				qs.getPlayer().addExpAndSp(16900, 500);
				qs.giveItems(57, 249);
				qs.giveItems(875, 2);
				qs.giveItems(906, 1);
				despawnsoldier();
				qs.exitCurrentQuest(false);
				qs.playSound(SOUND_FINISH);
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
			case Kakai:
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
				else
				{
					htmltext = "0-nc.htm";
				}
				break;
			
			case Atran:
				if (qs.isCompleted())
				{
					htmltext = "1-c.htm";
				}
				else if (cond == 0)
				{
					htmltext = "1-nc.htm";
				}
				else if (cond == 1)
				{
					htmltext = "1-1.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	private void spawnsolider(QuestState qs)
	{
		soldierg = NpcUtils.spawnSingle(Soldier, Location.findPointToStay(SOLDIER_START_POINT[0], SOLDIER_START_POINT[1], SOLDIER_START_POINT[2], 50, 100, qs.getPlayer().getGeoIndex()));
		soldierg.setFollowTarget(qs.getPlayer());
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
