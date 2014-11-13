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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;

public class Q00501_ProofOfClanAlliance extends Quest implements ScriptFile
{
	private static final int SIR_KRISTOF_RODEMAI = 30756;
	private static final int STATUE_OF_OFFERING = 30757;
	private static final int WITCH_ATHREA = 30758;
	private static final int WITCH_KALIS = 30759;
	private static final int HERB_OF_HARIT = 3832;
	private static final int HERB_OF_VANOR = 3833;
	private static final int HERB_OF_OEL_MAHUM = 3834;
	private static final int BLOOD_OF_EVA = 3835;
	private static final int SYMBOL_OF_LOYALTY = 3837;
	private static final int PROOF_OF_ALLIANCE = 3874;
	private static final int VOUCHER_OF_FAITH = 3873;
	private static final int ANTIDOTE_RECIPE = 3872;
	private static final int POTION_OF_RECOVERY = 3889;
	private static final int[] CHESTS =
	{
		27173,
		27174,
		27175,
		27176,
		27177
	};
	private static final int[][] MOBS =
	{
		{
			20685,
			HERB_OF_VANOR
		},
		{
			20644,
			HERB_OF_HARIT
		},
		{
			20576,
			HERB_OF_OEL_MAHUM
		}
	};
	private static final int RATE = 35;
	private static final int RETRY_PRICE = 10000;
	
	public Q00501_ProofOfClanAlliance()
	{
		super(PARTY_NONE);
		addStartNpc(SIR_KRISTOF_RODEMAI, STATUE_OF_OFFERING, WITCH_ATHREA);
		addTalkId(WITCH_KALIS);
		addQuestItem(SYMBOL_OF_LOYALTY, ANTIDOTE_RECIPE);
		
		for (int[] i : MOBS)
		{
			addKillId(i[0]);
			addQuestItem(i[1]);
		}
		
		for (int i : CHESTS)
		{
			addKillId(i);
		}
	}
	
	public QuestState getLeader(QuestState qs)
	{
		final Clan clan = qs.getPlayer().getClan();
		QuestState leader = null;
		
		if ((clan != null) && (clan.getLeader() != null) && (clan.getLeader().getPlayer() != null))
		{
			leader = clan.getLeader().getPlayer().getQuestState(getName());
		}
		
		return leader;
	}
	
	public void removeQuestFromMembers(QuestState qs, boolean leader)
	{
		removeQuestFromOfflineMembers(qs);
		removeQuestFromOnlineMembers(qs, leader);
	}
	
	public void removeQuestFromOfflineMembers(QuestState qs)
	{
		if ((qs.getPlayer() == null) || (qs.getPlayer().getClan() == null))
		{
			qs.exitCurrentQuest(true);
			return;
		}
		
		final int clan = qs.getPlayer().getClan().getClanId();
		
		try (Connection con = DatabaseFactory.getInstance().getConnection();)
		{
			
			PreparedStatement offline = con.prepareStatement("DELETE FROM character_quests WHERE name = ? AND char_id IN (SELECT obj_id FROM characters WHERE clanId = ? AND online = 0)");
			offline.setString(1, getName());
			offline.setInt(2, clan);
			offline.executeUpdate();
			offline.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void removeQuestFromOnlineMembers(QuestState qs, boolean leader)
	{
		if ((qs.getPlayer() == null) || (qs.getPlayer().getClan() == null))
		{
			qs.exitCurrentQuest(true);
			return;
		}
		
		QuestState leaderState;
		Player pleader = null;
		
		if (leader)
		{
			leaderState = getLeader(qs);
			
			if (leaderState != null)
			{
				pleader = leaderState.getPlayer();
			}
		}
		
		if (pleader != null)
		{
			pleader.stopImmobilized();
			pleader.getEffectList().stopEffect(4082);
		}
		
		for (Player pl : qs.getPlayer().getClan().getOnlineMembers(qs.getPlayer().getClan().getLeaderId()))
		{
			if ((pl != null) && (pl.getQuestState(getName()) != null))
			{
				pl.getQuestState(getName()).exitCurrentQuest(true);
			}
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		if ((qs.getPlayer() == null) || (qs.getPlayer().getClan() == null))
		{
			qs.exitCurrentQuest(true);
			return "noquest";
		}
		
		final QuestState leader = getLeader(qs);
		
		if (leader == null)
		{
			removeQuestFromMembers(qs, true);
			return "Quest Failed";
		}
		
		String htmltext = event;
		
		if (qs.getPlayer().isClanLeader())
		{
			switch (event)
			{
				case "30756-03.htm":
					qs.setCond(1);
					qs.setState(STARTED);
					qs.playSound(SOUND_ACCEPT);
					break;
				
				case "30759-03.htm":
					qs.setCond(2);
					qs.set("dead_list", " ");
					break;
				
				case "30759-07.htm":
					qs.takeItems(SYMBOL_OF_LOYALTY, -1);
					qs.giveItems(ANTIDOTE_RECIPE, 1);
					qs.addNotifyOfDeath(qs.getPlayer(), false);
					qs.setCond(3);
					qs.set("chest_count", "0");
					qs.set("chest_game", "0");
					qs.set("chest_try", "0");
					qs.startQuestTimer("poison_timer", 3600000);
					qs.getPlayer().altUseSkill(SkillTable.getInstance().getInfo(4082, 1), qs.getPlayer());
					qs.getPlayer().startImmobilized();
					htmltext = "30759-07.htm";
					break;
			}
		}
		
		switch (event)
		{
			case "poison_timer":
				removeQuestFromMembers(qs, true);
				htmltext = "30759-09.htm";
				break;
			
			case "chest_timer":
				htmltext = "";
				if (leader.getInt("chest_game") < 2)
				{
					stop_chest_game(qs);
				}
				break;
			
			case "30757-04.htm":
				List<String> deadlist = new ArrayList<>();
				deadlist.addAll(Arrays.asList(leader.get("dead_list").split(" ")));
				deadlist.add(qs.getPlayer().getName());
				String deadstr = "";
				for (String s : deadlist)
				{
					deadstr += s + " ";
				}
				leader.set("dead_list", deadstr);
				qs.addNotifyOfDeath(leader.getPlayer(), false);
				if (Rnd.chance(50))
				{
					qs.getPlayer().reduceCurrentHp(qs.getPlayer().getCurrentHp() * 8, 0, qs.getPlayer(), null, true, true, false, false, false, false, false);
				}
				qs.giveItems(SYMBOL_OF_LOYALTY, 1);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "30757-05.htm":
				qs.exitCurrentQuest(true);
				break;
			
			case "30758-03.htm":
				start_chest_game(qs);
				break;
			
			case "30758-07.htm":
				if (qs.getQuestItemsCount(ADENA_ID) < RETRY_PRICE)
				{
					htmltext = "30758-06.htm";
				}
				else
				{
					qs.takeItems(ADENA_ID, RETRY_PRICE);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int cond = qs.getCond();
		
		if ((qs.getPlayer() == null) || (qs.getPlayer().getClan() == null))
		{
			qs.exitCurrentQuest(true);
			return htmltext;
		}
		
		final QuestState leader = getLeader(qs);
		
		if (leader == null)
		{
			removeQuestFromMembers(qs, true);
			return "Quest Failed";
		}
		
		final int npcId = npc.getId();
		
		switch (npcId)
		{
			case SIR_KRISTOF_RODEMAI:
				if (!qs.getPlayer().isClanLeader())
				{
					qs.exitCurrentQuest(true);
					return "30756-10.htm";
				}
				else if (qs.getPlayer().getClan().getLevel() <= 2)
				{
					qs.exitCurrentQuest(true);
					return "30756-08.htm";
				}
				else if (qs.getPlayer().getClan().getLevel() >= 4)
				{
					qs.exitCurrentQuest(true);
					return "30756-09.htm";
				}
				else if (qs.getQuestItemsCount(VOUCHER_OF_FAITH) > 0)
				{
					qs.playSound(SOUND_FANFARE2);
					qs.takeItems(VOUCHER_OF_FAITH, -1);
					qs.giveItems(PROOF_OF_ALLIANCE, 1);
					qs.addExpAndSp(0, 120000);
					htmltext = "30756-07.htm";
					qs.exitCurrentQuest(true);
				}
				else if ((cond == 1) || (cond == 2))
				{
					return "30756-06.htm";
				}
				else if (qs.getQuestItemsCount(PROOF_OF_ALLIANCE) == 0)
				{
					qs.setCond(0);
					return "30756-01.htm";
				}
				else
				{
					qs.exitCurrentQuest(true);
					return htmltext;
				}
				break;
			
			case WITCH_KALIS:
				if (qs.getPlayer().isClanLeader())
				{
					if (cond == 1)
					{
						return "30759-01.htm";
					}
					else if (cond == 2)
					{
						htmltext = "30759-05.htm";
						
						if (qs.getQuestItemsCount(SYMBOL_OF_LOYALTY) == 3)
						{
							int deads = 0;
							
							try
							{
								deads = qs.get("dead_list").split(" ").length;
							}
							finally
							{
								if (deads == 3)
								{
									htmltext = "30759-06.htm";
								}
							}
						}
					}
					else if (cond == 3)
					{
						if ((qs.getQuestItemsCount(HERB_OF_HARIT) > 0) && (qs.getQuestItemsCount(HERB_OF_VANOR) > 0) && (qs.getQuestItemsCount(HERB_OF_OEL_MAHUM) > 0) && (qs.getQuestItemsCount(BLOOD_OF_EVA) > 0) && (qs.getQuestItemsCount(ANTIDOTE_RECIPE) > 0))
						{
							qs.takeItems(ANTIDOTE_RECIPE, 1);
							qs.takeItems(HERB_OF_HARIT, 1);
							qs.takeItems(HERB_OF_VANOR, 1);
							qs.takeItems(HERB_OF_OEL_MAHUM, 1);
							qs.takeItems(BLOOD_OF_EVA, 1);
							qs.giveItems(POTION_OF_RECOVERY, 1);
							qs.giveItems(VOUCHER_OF_FAITH, 1);
							qs.cancelQuestTimer("poison_timer");
							removeQuestFromMembers(qs, false);
							qs.getPlayer().stopImmobilized();
							qs.getPlayer().getEffectList().stopEffect(4082);
							qs.setCond(4);
							qs.playSound(SOUND_FINISH);
							return "30759-08.htm";
						}
						else if (qs.getQuestItemsCount(VOUCHER_OF_FAITH) == 0)
						{
							return "30759-10.htm";
						}
					}
				}
				else if (leader.getCond() == 3)
				{
					return "30759-11.htm";
				}
				break;
			
			case STATUE_OF_OFFERING:
				if (qs.getPlayer().isClanLeader())
				{
					return "30757-03.htm";
				}
				else if (qs.getPlayer().getLevel() <= 39)
				{
					qs.exitCurrentQuest(true);
					return "30757-02.htm";
				}
				else
				{
					String[] dlist;
					int deads;
					
					try
					{
						dlist = leader.get("dead_list").split(" ");
						deads = dlist.length;
					}
					catch (Exception e)
					{
						removeQuestFromMembers(qs, true);
						return "Who are you?";
					}
					
					if (deads < 3)
					{
						for (String str : dlist)
						{
							if (qs.getPlayer().getName().equals(str))
							{
								return "you cannot die again!";
							}
						}
						
						return "30757-01.htm";
					}
				}
				break;
			
			case WITCH_ATHREA:
				if (qs.getPlayer().isClanLeader())
				{
					return "30757-03.htm";
				}
				String[] dlist;
				try
				{
					dlist = leader.get("dead_list").split(" ");
				}
				catch (Exception e)
				{
					qs.exitCurrentQuest(true);
					return "Who are you?";
				}
				Boolean flag = false;
				if (dlist != null)
				{
					for (String str : dlist)
					{
						if (qs.getPlayer().getName().equals(str))
						{
							flag = true;
						}
					}
				}
				if (!flag)
				{
					qs.exitCurrentQuest(true);
					return "Who are you?";
				}
				int game_state = leader.getInt("chest_game");
				if (game_state == 0)
				{
					if (leader.getInt("chest_try") == 0)
					{
						return "30758-01.htm";
					}
					
					return "30758-05.htm";
				}
				else if (game_state == 1)
				{
					return "30758-09.htm";
				}
				else if (game_state == 2)
				{
					qs.playSound(SOUND_FINISH);
					qs.giveItems(BLOOD_OF_EVA, 1);
					qs.cancelQuestTimer("chest_timer");
					stop_chest_game(qs);
					leader.set("chest_game", "3");
					return "30758-08.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if ((qs.getPlayer() == null) || (qs.getPlayer().getClan() == null))
		{
			qs.exitCurrentQuest(true);
			return "noquest";
		}
		
		final QuestState leader = getLeader(qs);
		
		if (leader == null)
		{
			removeQuestFromMembers(qs, true);
			return "Quest Failed";
		}
		
		final int npcId = npc.getId();
		
		if (!leader.isRunningQuestTimer("poison_timer"))
		{
			stop_chest_game(qs);
			return "Quest Failed";
		}
		
		for (int[] m : MOBS)
		{
			if ((npcId == m[0]) && (qs.getInt(String.valueOf(m[1])) == 0))
			{
				if (Rnd.chance(RATE))
				{
					qs.giveItems(m[1], 1);
					leader.set(String.valueOf(m[1]), "1");
					qs.playSound(SOUND_MIDDLE);
					return null;
				}
			}
		}
		
		for (int i : CHESTS)
		{
			if (npcId == i)
			{
				if (!leader.isRunningQuestTimer("chest_timer"))
				{
					stop_chest_game(qs);
					return "Time is up!";
				}
				
				if (Rnd.chance(25))
				{
					Functions.npcSay(npc, "###### BINGO! ######");
					int count = leader.getInt("chest_count");
					
					if (count < 4)
					{
						count += 1;
						leader.set("chest_count", String.valueOf(count));
					}
					
					if (count >= 4)
					{
						stop_chest_game(qs);
						leader.set("chest_game", "2");
						leader.cancelQuestTimer("chest_timer");
						qs.playSound(SOUND_MIDDLE);
					}
					else
					{
						qs.playSound(SOUND_ITEMGET);
					}
				}
				
				return null;
			}
		}
		
		return null;
	}
	
	public void start_chest_game(QuestState qs)
	{
		if ((qs.getPlayer() == null) || (qs.getPlayer().getClan() == null))
		{
			qs.exitCurrentQuest(true);
			return;
		}
		
		final QuestState leader = getLeader(qs);
		
		if (leader == null)
		{
			removeQuestFromMembers(qs, true);
			return;
		}
		
		leader.set("chest_game", "1");
		leader.set("chest_count", "0");
		int attempts = leader.getInt("chest_try");
		leader.set("chest_try", String.valueOf(attempts + 1));
		
		for (NpcInstance npc : GameObjectsStorage.getAllByNpcId(CHESTS, false))
		{
			npc.deleteMe();
		}
		
		for (int n = 1; n <= 5; n++)
		{
			for (int i : CHESTS)
			{
				leader.addSpawn(i, 102100, 103450, -3400, 0, 100, 60000);
			}
		}
		
		leader.startQuestTimer("chest_timer", 60000);
	}
	
	public void stop_chest_game(QuestState qs)
	{
		QuestState leader = getLeader(qs);
		
		if (leader == null)
		{
			removeQuestFromMembers(qs, true);
			return;
		}
		
		for (NpcInstance npc : GameObjectsStorage.getAllByNpcId(CHESTS, false))
		{
			npc.deleteMe();
		}
		
		leader.set("chest_game", "0");
	}
	
	@Override
	public String onDeath(Creature npc, Creature pc, QuestState qs)
	{
		if ((qs.getPlayer() == null) || (qs.getPlayer().getClan() == null))
		{
			qs.exitCurrentQuest(true);
			return null;
		}
		
		final QuestState leader = getLeader(qs);
		
		if (leader == null)
		{
			removeQuestFromMembers(qs, true);
			return null;
		}
		
		if (qs.getPlayer() == pc)
		{
			leader.cancelQuestTimer("poison_timer");
			leader.cancelQuestTimer("chest_timer");
			removeQuestFromMembers(qs, true);
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
