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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class Q00381_LetsBecomeARoyalMember extends Quest implements ScriptFile
{
	// Npcs
	private static final int SORINT = 30232;
	private static final int SANDRA = 30090;
	// Items
	private static final int KAILS_COIN = 5899;
	private static final int COIN_ALBUM = 5900;
	private static final int MEMBERSHIP_1 = 3813;
	private static final int CLOVER_COIN = 7569;
	private static final int ROYAL_MEMBERSHIP = 5898;
	// Monsters
	private static final int ANCIENT_GARGOYLE = 21018;
	private static final int VEGUS = 27316;
	// Others
	private static final int GARGOYLE_CHANCE = 5;
	private static final int VEGUS_CHANCE = 100;
	
	public Q00381_LetsBecomeARoyalMember()
	{
		super(false);
		addStartNpc(SORINT);
		addTalkId(SANDRA);
		addKillId(ANCIENT_GARGOYLE, VEGUS);
		addQuestItem(KAILS_COIN, COIN_ALBUM, CLOVER_COIN);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "warehouse_keeper_sorint_q0381_02.htm":
				if ((qs.getPlayer().getLevel() >= 55) && (qs.getQuestItemsCount(MEMBERSHIP_1) > 0))
				{
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
					htmltext = "warehouse_keeper_sorint_q0381_03.htm";
				}
				else
				{
					htmltext = "warehouse_keeper_sorint_q0381_02.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case "sandra_q0381_02.htm":
				if (qs.getCond() == 1)
				{
					qs.set("id", "1");
					qs.playSound(SOUND_ACCEPT);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = qs.isCompleted() ? "completed" : "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		final long album = qs.getQuestItemsCount(COIN_ALBUM);
		
		switch (npcId)
		{
			case SORINT:
				if (cond == 0)
				{
					htmltext = "warehouse_keeper_sorint_q0381_01.htm";
				}
				else if (cond == 1)
				{
					final long coin = qs.getQuestItemsCount(KAILS_COIN);
					
					if ((coin > 0) && (album > 0))
					{
						qs.takeItems(KAILS_COIN, -1);
						qs.takeItems(COIN_ALBUM, -1);
						qs.giveItems(ROYAL_MEMBERSHIP, 1);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(true);
						htmltext = "warehouse_keeper_sorint_q0381_06.htm";
					}
					else if (album == 0)
					{
						htmltext = "warehouse_keeper_sorint_q0381_05.htm";
					}
					else if (coin == 0)
					{
						htmltext = "warehouse_keeper_sorint_q0381_04.htm";
					}
				}
				break;
			
			case SANDRA:
				final long clover = qs.getQuestItemsCount(CLOVER_COIN);
				if (album > 0)
				{
					htmltext = "sandra_q0381_05.htm";
				}
				else if (clover > 0)
				{
					qs.takeItems(CLOVER_COIN, -1);
					qs.giveItems(COIN_ALBUM, 1);
					qs.playSound(SOUND_ITEMGET);
					htmltext = "sandra_q0381_04.htm";
				}
				else if (qs.getInt("id") == 0)
				{
					htmltext = "sandra_q0381_01.htm";
				}
				else
				{
					htmltext = "sandra_q0381_03.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() != STARTED)
		{
			return null;
		}
		
		final int npcId = npc.getId();
		final long album = qs.getQuestItemsCount(COIN_ALBUM);
		final long coin = qs.getQuestItemsCount(KAILS_COIN);
		final long clover = qs.getQuestItemsCount(CLOVER_COIN);
		
		if ((npcId == ANCIENT_GARGOYLE) && (coin == 0))
		{
			if (Rnd.chance(GARGOYLE_CHANCE))
			{
				qs.giveItems(KAILS_COIN, 1);
				
				if ((album > 0) || (clover > 0))
				{
					qs.playSound(SOUND_MIDDLE);
				}
				else
				{
					qs.playSound(SOUND_ITEMGET);
				}
			}
		}
		else if ((npcId == VEGUS) && ((clover + album) == 0) && (qs.getInt("id") != 0))
		{
			if (Rnd.chance(VEGUS_CHANCE))
			{
				qs.giveItems(CLOVER_COIN, 1);
				
				if (coin > 0)
				{
					qs.playSound(SOUND_MIDDLE);
				}
				else
				{
					qs.playSound(SOUND_ITEMGET);
				}
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
