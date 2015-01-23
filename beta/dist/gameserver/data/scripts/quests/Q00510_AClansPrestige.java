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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00510_AClansPrestige extends Quest implements ScriptFile
{
	// Npc
	private static final int VALDIS = 31331;
	// Items
	private static final int CLAW = 8767;
	private static final int CLAN_POINTS_REWARD = 30;
	
	public Q00510_AClansPrestige()
	{
		super(PARTY_ALL);
		addStartNpc(VALDIS);
		
		for (int npc = 22215; npc <= 22217; npc++)
		{
			addKillId(npc);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "31331-3.htm":
				if (qs.getCond() == 0)
				{
					qs.setCond(1);
					qs.setState(STARTED);
				}
				break;
			
			case "31331-6.htm":
				qs.playSound(SOUND_FINISH);
				qs.exitCurrentQuest(true);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final Player player = qs.getPlayer();
		
		if ((player.getClan() == null) || !player.isClanLeader())
		{
			qs.exitCurrentQuest(true);
			htmltext = "31331-0.htm";
		}
		else if (player.getClan().getLevel() < 5)
		{
			qs.exitCurrentQuest(true);
			htmltext = "31331-0.htm";
		}
		else
		{
			final int cond = qs.getCond();
			final int id = qs.getState();
			
			if ((id == CREATED) && (cond == 0))
			{
				htmltext = "31331-1.htm";
			}
			else if ((id == STARTED) && (cond == 1))
			{
				long count = qs.getQuestItemsCount(CLAW);
				
				if (count == 0)
				{
					htmltext = "31331-4.htm";
				}
				else if (count >= 1)
				{
					htmltext = "31331-7.htm";
					qs.takeItems(CLAW, -1);
					int pointsCount = CLAN_POINTS_REWARD * (int) count;
					
					if (count > 10)
					{
						pointsCount += (count % 10) * 118;
					}
					
					int increasedPoints = player.getClan().incReputation(pointsCount, true, "Q00510_AClansPrestige");
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_SUCCESSFULLY_COMPLETED_A_CLAN_QUEST_S1_POINT_S_HAVE_BEEN_ADDED_TO_YOUR_CLAN_REPUTATION).addInt(increasedPoints));
				}
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (!qs.getPlayer().isClanLeader())
		{
			qs.exitCurrentQuest(true);
		}
		else if (qs.getState() == STARTED)
		{
			final int npcId = npc.getId();
			
			if ((npcId >= 22215) && (npcId <= 22218))
			{
				qs.giveItems(CLAW, 1);
				qs.playSound(SOUND_ITEMGET);
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
