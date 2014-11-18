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
import lineage2.gameserver.model.entity.olympiad.OlympiadGame;
import lineage2.gameserver.model.entity.olympiad.OlympiadTeam;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00553_OlympiadUndefeated extends Quest implements ScriptFile
{
	private static final int OLYMPIAD_MANAGER = 31688;
	private static final int MEDAL_OF_GLORY = 21874;
	private static final int OLYMPIAD_CHEST = 32263;
	private static final int WINS_CONFIRMATION1 = 17244;
	private static final int WINS_CONFIRMATION2 = 17245;
	private static final int WINS_CONFIRMATION3 = 17246;
	
	public Q00553_OlympiadUndefeated()
	{
		super(false);
		addStartNpc(OLYMPIAD_MANAGER);
		addTalkId(OLYMPIAD_MANAGER);
		addQuestItem(WINS_CONFIRMATION1, WINS_CONFIRMATION2, WINS_CONFIRMATION3);
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		switch (npc.getId())
		{
			case OLYMPIAD_MANAGER:
				final Player player = qs.getPlayer();
				
				if (!player.isNoble() || (player.getLevel() < 75) || (player.getClassLevel() < 4))
				{
					return "olympiad_operator_q0553_08.htm";
				}
				else if (qs.isCreated())
				{
					if (qs.isNowAvailableByTime())
					{
						return "olympiad_operator_q0553_01.htm";
					}
					
					return "olympiad_operator_q0553_06.htm";
				}
				else if (qs.isStarted())
				{
					if (qs.getQuestItemsCount(WINS_CONFIRMATION1, WINS_CONFIRMATION2, WINS_CONFIRMATION3) == 0)
					{
						return "olympiad_operator_q0553_04.htm";
					}
					else if (qs.getQuestItemsCount(WINS_CONFIRMATION3) > 0)
					{
						qs.giveItems(OLYMPIAD_CHEST, 6);
						qs.giveItems(MEDAL_OF_GLORY, 5);
						qs.takeItems(WINS_CONFIRMATION1, -1);
						qs.takeItems(WINS_CONFIRMATION2, -1);
						qs.takeItems(WINS_CONFIRMATION3, -1);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(this);
						return "olympiad_operator_q0553_07.htm";
					}
					
					return "olympiad_operator_q0553_05.htm";
				}
				break;
		}
		
		return null;
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "olympiad_operator_q0553_03.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "olympiad_operator_q0553_07.htm":
				if (qs.getQuestItemsCount(WINS_CONFIRMATION3) > 0)
				{
					qs.giveItems(OLYMPIAD_CHEST, 6);
					Player player = qs.getPlayer();
					player.setFame(player.getFame() + 20000, "quest olympiad");
					qs.takeItems(WINS_CONFIRMATION1, -1);
					qs.takeItems(WINS_CONFIRMATION2, -1);
					qs.takeItems(WINS_CONFIRMATION3, -1);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(this);
				}
				else if (qs.getQuestItemsCount(WINS_CONFIRMATION2) > 0)
				{
					qs.giveItems(OLYMPIAD_CHEST, 3);
					Player player = qs.getPlayer();
					player.setFame(player.getFame() + 10000, "quest olympiad");
					qs.takeItems(WINS_CONFIRMATION1, -1);
					qs.takeItems(WINS_CONFIRMATION2, -1);
					qs.takeItems(WINS_CONFIRMATION3, -1);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(this);
				}
				else if (qs.getQuestItemsCount(WINS_CONFIRMATION1) > 0)
				{
					qs.giveItems(OLYMPIAD_CHEST, 1);
					qs.takeItems(WINS_CONFIRMATION1, -1);
					qs.takeItems(WINS_CONFIRMATION2, -1);
					qs.takeItems(WINS_CONFIRMATION3, -1);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(this);
				}
				break;
		}
		
		return event;
	}
	
	@Override
	public void onOlympiadEnd(OlympiadGame og, QuestState qs)
	{
		if (qs.getCond() == 1)
		{
			int count = qs.getInt("count");
			final OlympiadTeam winner = og.getWinnerTeam();
			
			if ((winner != null) && winner.contains(qs.getPlayer().getObjectId()))
			{
				count++;
			}
			else
			{
				count = 0;
			}
			
			qs.set("count", count);
			
			if ((count == 2) && (qs.getQuestItemsCount(WINS_CONFIRMATION1) == 0))
			{
				qs.giveItems(WINS_CONFIRMATION1, 1);
				qs.playSound(SOUND_ITEMGET);
			}
			else if ((count == 5) && (qs.getQuestItemsCount(WINS_CONFIRMATION2) == 0))
			{
				qs.giveItems(WINS_CONFIRMATION2, 1);
				qs.playSound(SOUND_ITEMGET);
			}
			else if ((count == 10) && (qs.getQuestItemsCount(WINS_CONFIRMATION3) == 0))
			{
				qs.giveItems(WINS_CONFIRMATION3, 2);
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
			}
			
			if ((count < 10) && (qs.getQuestItemsCount(WINS_CONFIRMATION3) > 0))
			{
				qs.takeItems(WINS_CONFIRMATION3, -1);
			}
			
			if ((count < 5) && (qs.getQuestItemsCount(WINS_CONFIRMATION2) > 0))
			{
				qs.takeItems(WINS_CONFIRMATION2, -1);
			}
			
			if ((count < 2) && (qs.getQuestItemsCount(WINS_CONFIRMATION1) > 0))
			{
				qs.takeItems(WINS_CONFIRMATION1, -1);
			}
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
