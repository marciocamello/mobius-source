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
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.utils.Util;

public class Q00129_PailakaDevilsLegacy extends Quest implements ScriptFile
{
	// Npcs
	private static final int DISURVIVOR = 32498;
	private static final int SUPPORTER = 32501;
	private static final int DADVENTURER = 32508;
	private static final int DADVENTURER2 = 32511;
	private static final int CHEST = 32495;
	private static final int[] Pailaka2nd = new int[]
	{
		18623,
		18624,
		18625,
		18626,
		18627
	};
	private static final int KAMS = 18629;
	private static final int ALKASO = 18631;
	private static final int LEMATAN = 18633;
	// Items
	private static final int ScrollOfEscape = 736;
	private static final int SWORD = 13042;
	private static final int ENCHSWORD = 13043;
	private static final int LASTSWORD = 13044;
	private static final int KDROP = 13046;
	private static final int ADROP = 13047;
	private static final int KEY = 13150;
	private static final int[] HERBS = new int[]
	{
		8601,
		8602,
		8604,
		8605
	};
	private static final int[] CHESTDROP = new int[]
	{
		13033,
		13048,
		13049
	};
	private static final int PBRACELET = 13295;
	private static final int izId = 44;
	
	public Q00129_PailakaDevilsLegacy()
	{
		super(false);
		addStartNpc(DISURVIVOR);
		addTalkId(SUPPORTER, DADVENTURER, DADVENTURER2);
		addKillId(KAMS, ALKASO, LEMATAN, CHEST);
		addKillId(Pailaka2nd);
		addQuestItem(SWORD, ENCHSWORD, LASTSWORD, KDROP, ADROP, KEY);
		addQuestItem(CHESTDROP);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final Player player = qs.getPlayer();
		String htmltext = event;
		
		switch (event)
		{
			case "Enter":
				enterInstance(player);
				return null;
				
			case "32498-02.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32498-05.htm":
				qs.setCond(2);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "32501-03.htm":
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
				qs.giveItems(SWORD, 1);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (qs.isCompleted())
		{
			return "completed";
		}
		String htmltext = "noquest";
		final int cond = qs.getCond();
		final int npcId = npc.getId();
		final int id = qs.getState();
		final Player player = qs.getPlayer();
		
		switch (npcId)
		{
			case DISURVIVOR:
				if (cond == 0)
				{
					if ((player.getLevel() < 61) || (player.getLevel() > 67))
					{
						htmltext = "32498-no.htm";
						qs.exitCurrentQuest(true);
					}
					else
					{
						return "32498-01.htm";
					}
				}
				else if (id == COMPLETED)
				{
					htmltext = "32498-no.htm";
				}
				else if ((cond == 1) || (cond == 2))
				{
					htmltext = "32498-06.htm";
				}
				else
				{
					htmltext = "32498-07.htm";
				}
				break;
			
			case SUPPORTER:
				if ((cond == 1) || (cond == 2))
				{
					htmltext = "32501-01.htm";
				}
				else
				{
					htmltext = "32501-04.htm";
				}
				break;
			
			case DADVENTURER:
				if ((qs.getQuestItemsCount(SWORD) > 0) && (qs.getQuestItemsCount(KDROP) == 0))
				{
					htmltext = "32508-01.htm";
				}
				if ((qs.getQuestItemsCount(ENCHSWORD) > 0) && (qs.getQuestItemsCount(ADROP) == 0))
				{
					htmltext = "32508-01.htm";
				}
				if ((qs.getQuestItemsCount(SWORD) == 0) && (qs.getQuestItemsCount(KDROP) > 0))
				{
					htmltext = "32508-05.htm";
				}
				if ((qs.getQuestItemsCount(ENCHSWORD) == 0) && (qs.getQuestItemsCount(ADROP) > 0))
				{
					htmltext = "32508-05.htm";
				}
				if ((qs.getQuestItemsCount(SWORD) == 0) && (qs.getQuestItemsCount(ENCHSWORD) == 0))
				{
					htmltext = "32508-05.htm";
				}
				if ((qs.getQuestItemsCount(KDROP) == 0) && (qs.getQuestItemsCount(ADROP) == 0))
				{
					htmltext = "32508-01.htm";
				}
				if (player.getSummonList().size() > 0)
				{
					htmltext = "32508-04.htm";
				}
				if ((qs.getQuestItemsCount(SWORD) > 0) && (qs.getQuestItemsCount(KDROP) > 0))
				{
					qs.takeItems(SWORD, 1);
					qs.takeItems(KDROP, 1);
					qs.giveItems(ENCHSWORD, 1);
					htmltext = "32508-02.htm";
				}
				if ((qs.getQuestItemsCount(ENCHSWORD) > 0) && (qs.getQuestItemsCount(ADROP) > 0))
				{
					qs.takeItems(ENCHSWORD, 1);
					qs.takeItems(ADROP, 1);
					qs.giveItems(LASTSWORD, 1);
					htmltext = "32508-03.htm";
				}
				if (qs.getQuestItemsCount(LASTSWORD) > 0)
				{
					htmltext = "32508-03.htm";
				}
				break;
			
			case DADVENTURER2:
				if (cond == 4)
				{
					if (player.getSummonList().size() > 0)
					{
						htmltext = "32511-03.htm";
					}
					else
					{
						qs.giveItems(ScrollOfEscape, 1);
						qs.giveItems(PBRACELET, 1);
						qs.addExpAndSp(4010000, 1235000);
						qs.giveItems(ADENA_ID, 411500);
						qs.setCond(5);
						qs.setState(COMPLETED);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(false);
						player.getReflection().startCollapseTimer(60000);
						player.setVitality(Config.MAX_VITALITY);
						htmltext = "32511-01.htm";
					}
				}
				else if (id == COMPLETED)
				{
					htmltext = "32511-02.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		final int npcId = npc.getId();
		final int refId = player.getReflectionId();
		
		if (Util.contains(Pailaka2nd, npcId))
		{
			if (Rnd.get(100) < 80)
			{
				qs.dropItem(npc, HERBS[Rnd.get(HERBS.length)], Rnd.get(1, 2));
			}
		}
		
		switch (npcId)
		{
			case KAMS:
				if (qs.getQuestItemsCount(KDROP) == 0)
				{
					qs.giveItems(KDROP, 1);
				}
				break;
			
			case ALKASO:
				if (qs.getQuestItemsCount(ADROP) == 0)
				{
					qs.giveItems(ADROP, 1);
				}
				break;
			
			case LEMATAN:
				qs.setCond(4);
				qs.playSound(SOUND_MIDDLE);
				addSpawnToInstance(DADVENTURER2, new Location(84990, -208376, -3342, 55000), 0, refId);
				break;
			
			case CHEST:
				if (Rnd.get(100) < 80)
				{
					qs.dropItem(npc, CHESTDROP[Rnd.get(CHESTDROP.length)], Rnd.get(1, 10));
				}
				break;
		}
		
		return null;
	}
	
	private void enterInstance(Player player)
	{
		final Reflection r = player.getActiveReflection();
		
		if (r != null)
		{
			if (player.canReenterInstance(izId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(izId))
		{
			ReflectionUtils.enterReflection(player, izId);
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
