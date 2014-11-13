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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00551_OlympiadStarter extends Quest implements ScriptFile
{
	// Npc
	private static final int OLYMPIAD_MANAGER = 31688;
	// Items
	private static final int MEDAL_OF_GLORY = 21874;
	private static final int OLYMPIAD_CHEST = 32263;
	private static final int OLYMPIAD_CERT1 = 17238;
	private static final int OLYMPIAD_CERT2 = 17239;
	private static final int OLYMPIAD_CERT3 = 17240;
	
	public Q00551_OlympiadStarter()
	{
		super(false);
		addStartNpc(OLYMPIAD_MANAGER);
		addTalkId(OLYMPIAD_MANAGER);
		addQuestItem(OLYMPIAD_CERT1, OLYMPIAD_CERT2, OLYMPIAD_CERT3);
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
					return "olympiad_operator_q0551_08.htm";
				}
				else if (qs.isCreated())
				{
					if (qs.isNowAvailableByTime())
					{
						return "olympiad_operator_q0551_01.htm";
					}
					
					return "olympiad_operator_q0551_06.htm";
				}
				else if (qs.isStarted())
				{
					if (qs.getQuestItemsCount(OLYMPIAD_CERT1, OLYMPIAD_CERT2, OLYMPIAD_CERT3) == 0)
					{
						return "olympiad_operator_q0551_04.htm";
					}
					
					if (qs.getQuestItemsCount(OLYMPIAD_CERT3) > 0)
					{
						qs.giveItems(OLYMPIAD_CHEST, 2);
						qs.giveItems(MEDAL_OF_GLORY, 5);
						qs.takeItems(OLYMPIAD_CERT1, -1);
						qs.takeItems(OLYMPIAD_CERT2, -1);
						qs.takeItems(OLYMPIAD_CERT3, -1);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(this);
						return "olympiad_operator_q0551_07.htm";
					}
					
					return "olympiad_operator_q0551_05.htm";
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
			case "olympiad_operator_q0551_03.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "olympiad_operator_q0551_07.htm":
				if (qs.getQuestItemsCount(OLYMPIAD_CERT3) > 0)
				{
					qs.giveItems(OLYMPIAD_CHEST, 2);
					Player player = qs.getPlayer();
					player.setFame(player.getFame() + 10000, "quest olympiad");
					qs.takeItems(OLYMPIAD_CERT1, -1);
					qs.takeItems(OLYMPIAD_CERT2, -1);
					qs.takeItems(OLYMPIAD_CERT3, -1);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(this);
				}
				else if (qs.getQuestItemsCount(OLYMPIAD_CERT2) > 0)
				{
					qs.giveItems(OLYMPIAD_CHEST, 2);
					Player player = qs.getPlayer();
					player.setFame(player.getFame() + 6000, "quest olympiad");
					qs.takeItems(OLYMPIAD_CERT1, -1);
					qs.takeItems(OLYMPIAD_CERT2, -1);
					qs.takeItems(OLYMPIAD_CERT3, -1);
					qs.playSound(SOUND_FINISH);
					qs.exitCurrentQuest(this);
				}
				else if (qs.getQuestItemsCount(OLYMPIAD_CERT1) > 0)
				{
					qs.giveItems(OLYMPIAD_CHEST, 1);
					qs.takeItems(OLYMPIAD_CERT1, -1);
					qs.takeItems(OLYMPIAD_CERT2, -1);
					qs.takeItems(OLYMPIAD_CERT3, -1);
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
			final int count = qs.getInt("count") + 1;
			qs.set("count", count);
			
			if (count == 3)
			{
				qs.giveItems(OLYMPIAD_CERT1, 1);
				qs.playSound(SOUND_ITEMGET);
			}
			else if (count == 5)
			{
				qs.giveItems(OLYMPIAD_CERT2, 1);
				qs.playSound(SOUND_ITEMGET);
			}
			else if (count == 10)
			{
				qs.giveItems(OLYMPIAD_CERT3, 1);
				qs.setCond(2);
				qs.playSound(SOUND_MIDDLE);
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
