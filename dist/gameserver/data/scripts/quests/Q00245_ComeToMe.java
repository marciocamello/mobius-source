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

/*
 * @author JustForFun
 * @date 14/04/13
 */
public class Q00245_ComeToMe extends Quest implements ScriptFile
{
	// Npc
	private static final int FERRIS = 30847;
	// Items
	private static final int FLAME_ASHES = 30322;
	private static final int CRYSTAL_OF_EXPERIENCE = 30323;
	private static final int CRYSTAL_A = 1461;
	private static final int REWARD_RING = 30383;
	// Monsters
	private static final int[] monstersSwamp1 = new int[]
	{
		21110,
		21111
	};
	private static final int[] monstersSwamp2 = new int[]
	{
		21112,
		21113,
		21115,
		21116
	};
	private Player sponsoredPlayer;
	private Player sponsorPlayer;
	private int sponsorObjId;
	
	public Q00245_ComeToMe()
	{
		super(PARTY_NONE);
		addStartNpc(FERRIS);
		addKillId(monstersSwamp1);
		addKillId(monstersSwamp2);
		addFirstTalkId(FERRIS);
		addQuestItem(CRYSTAL_OF_EXPERIENCE, FLAME_ASHES);
		addLevelCheck(70, 75);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final Player player = qs.getPlayer();
		
		if ((player.getObjectId() == sponsorObjId) && (event.equals("30847-13.htm")))
		{
			if ((sponsoredPlayer != null) && sponsoredPlayer.isOnline())
			{
				if (sponsoredPlayer.getInventory().destroyItemByItemId(CRYSTAL_A, 100))
				{
					sponsorPlayer.getQuestState(getName()).setCond(3);
					return event;
				}
				
				return "30847-14.htm";
			}
			
			return "30847-12.htm";
		}
		else if (event.equals("30847-04.htm"))
		{
			qs.setState(STARTED);
			qs.setCond(1);
			qs.playSound(SOUND_ACCEPT);
		}
		else if (event.equals("30847-07.htm"))
		{
			qs.set("talk", "1");
			qs.takeAllItems(FLAME_ASHES);
			qs.playSound("ItemSound.quest_middle");
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final Player player = qs.getPlayer();
		
		switch (qs.getState())
		{
			case COMPLETED:
				htmltext = "30847-03.htm";
				break;
			
			case CREATED:
				if ((player.getLevel() > 70) && (player.getLevel() < 75) && (player.getPledgeType() == Clan.SUBUNIT_ACADEMY) && (player.getSponsor() != 0))
				{
					htmltext = "30847-01.htm";
				}
				else
				{
					htmltext = "30847-02.htm";
					
					qs.exitCurrentQuest(true);
				}
				
				break;
			
			case STARTED:
				switch (qs.getCond())
				{
					case 1:
						htmltext = "30847-05.htm";
						break;
					
					case 2:
						if (qs.getInt("talk") == 1)
						{
							htmltext = "30847-06.htm";
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
								htmltext = "30847-10.htm";
							}
							else
							{
								htmltext = "30847-08.htm";
							}
						}
						else
						{
							htmltext = "30847-09.htm";
						}
						
						break;
					
					case 3:
						htmltext = "30847-17.htm";
						
						qs.setCond(4);
						qs.playSound("ItemSound.quest_middle");
						break;
					
					case 4:
						htmltext = "30847-18.htm";
						break;
					
					case 5:
						htmltext = "30847-19.htm";
						
						qs.takeAllItems(CRYSTAL_OF_EXPERIENCE);
						qs.addExpAndSp(2018733, 200158);
						qs.giveItems(REWARD_RING, 1);
						
						if (player.getPledgeType() == Clan.SUBUNIT_ACADEMY)
						{
							player.getClan().incReputation(1000, true, "quest245");
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
		if ((player.getObjectId() == sponsorObjId) && (npc.getId() == FERRIS))
		{
			if ((sponsoredPlayer != null) && sponsoredPlayer.isOnline())
			{
				return "30847-11.htm";
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
				if ((Util.contains(monstersSwamp1, npc.getId())) && (Rnd.chance(50)))
				{
					qs.giveItems(FLAME_ASHES, 1, true);
					qs.playSound("ItemSound.quest_itemget");
					
					if (qs.getQuestItemsCount(FLAME_ASHES) >= 15)
					{
						qs.setCond(2);
						qs.playSound("ItemSound.quest_middle");
					}
				}
				break;
			
			case 4:
				if (Util.contains(monstersSwamp2, npc.getId()))
				{
					if ((player.getPledgeType() == Clan.SUBUNIT_ACADEMY) && (player.getSponsor() != 0))
					{
						if (player.getDistance(sponsorPlayer) <= 400)
						{
							qs.giveItems(CRYSTAL_OF_EXPERIENCE, 1, true);
							qs.playSound("ItemSound.quest_itemget");
							
							if (qs.getQuestItemsCount(CRYSTAL_OF_EXPERIENCE) >= 12)
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