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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

public class Q00244_FollowMe extends Quest implements ScriptFile
{
	// Npc
	private static final int ISAEL = 30655;
	// Items
	private static final int TAMLIN_ORC_MARK = 30320;
	private static final int MEMORIAL_CRYSTAL = 30321;
	private static final int CRYSTAL_C = 1459;
	private static final int REWARD_ACADEMY_CIRCLET = 8181;
	// Monsters
	private static final int[] monstersOrcs = new int[]
	{
		20601,
		20602
	};
	private static final int[] monstersValley = new int[]
	{
		20603,
		20604,
		20605
	};
	Player sponsoredPlayer;
	Player sponsorPlayer;
	int sponsorObjId;
	
	public Q00244_FollowMe()
	{
		super(false);
		addStartNpc(ISAEL);
		addKillId(monstersOrcs);
		addKillId(monstersValley);
		addFirstTalkId(ISAEL);
		addQuestItem(TAMLIN_ORC_MARK, MEMORIAL_CRYSTAL);
		addLevelCheck(40, 50);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		final Player player = qs.getPlayer();
		
		if ((player.getObjectId() == sponsorObjId) && (event.equals("30655-13.htm")))
		{
			if ((sponsoredPlayer != null) && sponsoredPlayer.isOnline())
			{
				if (sponsoredPlayer.getInventory().destroyItemByItemId(CRYSTAL_C, 55))
				{
					sponsorPlayer.getQuestState(getName()).setCond(3);
					return event;
				}
				
				return "30655-14.htm";
			}
			
			return "30655-12.htm";
		}
		else if (event.equals("30655-04.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("30655-07.htm"))
		{
			qs.set("talk", "1");
			qs.takeAllItems(TAMLIN_ORC_MARK);
			qs.playSound("ItemSound.quest_middle");
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final Player player = qs.getPlayer();
		
		switch (qs.getState())
		{
			case COMPLETED:
				htmltext = "30655-03.htm";
				break;
			
			case CREATED:
				if ((player.getLevel() > 40) && (player.getLevel() < 50) && (player.getPledgeType() == Clan.SUBUNIT_ACADEMY) && (player.getSponsor() != 0))
				{
					htmltext = "30655-01.htm";
				}
				else
				{
					htmltext = "30655-02.htm";
					qs.exitCurrentQuest(true);
				}
				break;
			
			case STARTED:
				switch (qs.getCond())
				{
					case 1:
						htmltext = "30655-05.htm";
						break;
					
					case 2:
						if (qs.getInt("talk") == 1)
						{
							htmltext = "30655-06.htm";
						}
						else if ((player.getPledgeType() == Clan.SUBUNIT_ACADEMY) && (player.getSponsor() != 0))
						{
							sponsorObjId = player.getSponsor();
							sponsoredPlayer = qs.getPlayer();
							
							for (UnitMember unitMember : player.getClan().getAllMembers())
							{
								if (unitMember.getObjectId() == sponsorObjId)
								{
									sponsorPlayer = unitMember.getPlayer();
								}
							}
							
							if ((sponsorPlayer != null) && (sponsorPlayer.isOnline()) && (player.getDistance(sponsorPlayer) < 200))
							{
								htmltext = "30655-10.htm";
							}
							else
							{
								htmltext = "30655-08.htm";
							}
						}
						else
						{
							htmltext = "30655-09.htm";
						}
						break;
					
					case 3:
						htmltext = "30655-17.htm";
						qs.setCond(4);
						qs.playSound("ItemSound.quest_middle");
						break;
					
					case 4:
						htmltext = "30655-18.htm";
						break;
					
					case 5:
						htmltext = "30655-19.htm";
						qs.takeAllItems(MEMORIAL_CRYSTAL);
						qs.addExpAndSp(606680, 39200);
						qs.giveItems(REWARD_ACADEMY_CIRCLET, 1);
						
						if (player.getPledgeType() == Clan.SUBUNIT_ACADEMY)
						{
							player.getClan().incReputation(100, true, "quest244");
						}
						
						qs.playSound("ItemSound.quest_finish");
						qs.exitCurrentQuest(false);
				}
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		if ((player.getObjectId() == sponsorObjId) && (npc.getId() == ISAEL))
		{
			if ((sponsoredPlayer != null) && sponsoredPlayer.isOnline())
			{
				return "30655-11.htm";
			}
		}
		
		return "";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if (qs.getState() != STARTED)
		{
			return null;
		}
		
		final Player player = qs.getPlayer();
		
		switch (qs.getCond())
		{
			case 1:
				if ((Util.contains(monstersValley, npc.getId())) && (Rnd.chance(50)))
				{
					qs.giveItems(TAMLIN_ORC_MARK, 1, true);
					qs.playSound("ItemSound.quest_itemget");
					
					if (qs.getQuestItemsCount(TAMLIN_ORC_MARK) >= 10)
					{
						qs.setCond(2);
						qs.playSound("ItemSound.quest_middle");
					}
				}
				break;
			
			case 4:
				if (Util.contains(monstersValley, npc.getId()))
				{
					if ((player.getPledgeType() == Clan.SUBUNIT_ACADEMY) && (player.getSponsor() != 0))
					{
						if (player.getDistance(sponsorPlayer) <= 400)
						{
							qs.giveItems(MEMORIAL_CRYSTAL, 1, true);
							qs.playSound("ItemSound.quest_itemget");
							
							if (qs.getQuestItemsCount(MEMORIAL_CRYSTAL) >= 8)
							{
								qs.setCond(5);
								qs.playSound("ItemSound.quest_middle");
							}
						}
					}
				}
				break;
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
