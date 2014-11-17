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
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

public class Q10307_TheCorruptedLeaderHisTruth extends Quest implements ScriptFile
{
	// Npcs
	private static final int NPC_NAOMI_KASHERON = 32896;
	private static final int NPC_MIMILEAD = 32895;
	// Monsters
	private static final int[] MOB_KIMERIAN =
	{
		25745,
		25747
	};
	// Item
	private static final int REWARD_ENCHANT_ARMOR_R = 17527;
	
	public Q10307_TheCorruptedLeaderHisTruth()
	{
		super(false);
		addStartNpc(NPC_NAOMI_KASHERON);
		addTalkId(NPC_MIMILEAD);
		addKillId(MOB_KIMERIAN);
		addQuestCompletedCheck(Q10306_TheCorruptedLeader.class);
		addLevelCheck(90, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if (qs == null)
		{
			return "noquest";
		}
		
		switch (event)
		{
			case "32896-05.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32896-08.htm":
				qs.setCond(3); // ?
				qs.playSound(SOUND_FINISH);
				qs.addExpAndSp(11779522, 5275253);
				qs.giveItems(REWARD_ENCHANT_ARMOR_R, 1);
				qs.exitCurrentQuest(false);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		
		if (qs == null)
		{
			return htmltext;
		}
		
		final Player player = qs.getPlayer();
		final QuestState prevst = player.getQuestState(Q10306_TheCorruptedLeader.class);
		
		if (npc.getId() == NPC_NAOMI_KASHERON)
		{
			switch (qs.getState())
			{
				case COMPLETED:
					htmltext = "32896-02.htm";
					break;
				
				case CREATED:
					if (player.getLevel() >= 90)
					{
						if ((prevst != null) && (prevst.isCompleted()))
						{
							htmltext = "32896-01.htm";
						}
						else
						{
							qs.exitCurrentQuest(true);
							htmltext = "32896-03.htm";
						}
					}
					else
					{
						qs.exitCurrentQuest(true);
						htmltext = "32896-03.htm";
					}
					break;
				
				case STARTED:
					if (qs.getCond() == 1)
					{
						htmltext = "32896-05.htm";
					}
					else
					{
						if (qs.getCond() != 2)
						{
							break;
						}
						
						htmltext = "32896-06.htm";
					}
					break;
			}
		}
		else if (npc.getId() == NPC_MIMILEAD)
		{
			if (qs.isStarted())
			{
				if (qs.getCond() == 3)
				{
					htmltext = "32895-01.htm";
				}
			}
			else if (qs.isCompleted())
			{
				htmltext = "32895-05.htm";
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((npc == null) || (qs == null) || (qs.getCond() != 1))
		{
			return null;
		}
		
		if (Util.contains(MOB_KIMERIAN, npc.getId()))
		{
			if (qs.getCond() == 1)
			{
				qs.playSound(SOUND_MIDDLE);
				qs.setCond(2);
			}
		}
		
		return null;
	}
	
	@Override
	public boolean isVisible(Player player)
	{
		final QuestState qs = player.getQuestState(Q10307_TheCorruptedLeaderHisTruth.class);
		return ((qs == null) && isAvailableFor(player)) || ((qs != null) && qs.isNowAvailableByTime());
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
