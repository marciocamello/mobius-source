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
import java.sql.ResultSet;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;

public class Q00503_PursuitOfClanAmbition extends Quest implements ScriptFile
{
	private static final int G_Let_Martien = 3866;
	private static final int Th_Wyrm_Eggs = 3842;
	private static final int Drake_Eggs = 3841;
	private static final int Bl_Wyrm_Eggs = 3840;
	private static final int Mi_Drake_Eggs = 3839;
	private static final int Brooch = 3843;
	private static final int Bl_Anvil_Coin = 3871;
	private static final int G_Let_Balthazar = 3867;
	private static final int Recipe_Spiteful_Soul_Energy = 14854;
	private static final int Spiteful_Soul_Energy = 14855;
	private static final int Spiteful_Soul_Vengeance = 14856;
	private static final int G_Let_Rodemai = 3868;
	private static final int Imp_Keys = 3847;
	private static final int Scepter_Judgement = 3869;
	private static final int Proof_Aspiration = 3870;
	private static final int Gustaf = 30760;
	private static final int Martien = 30645;
	private static final int Athrea = 30758;
	private static final int Kalis = 30759;
	private static final int Fritz = 30761;
	private static final int Lutz = 30762;
	private static final int Kurtz = 30763;
	private static final int Kusto = 30512;
	private static final int Balthazar = 30764;
	private static final int Rodemai = 30868;
	private static final int Coffer = 30765;
	private static final int Cleo = 30766;
	private static final int ThunderWyrm1 = 20282;
	private static final int ThunderWyrm2 = 20243;
	private static final int Drake1 = 20137;
	private static final int Drake2 = 20285;
	private static final int BlitzWyrm = 27178;
	private static final int SpitefulSoulLeader = 20974;
	private static final int GraveGuard = 20668;
	private static final int GraveKeymaster = 27179;
	private static final int ImperialGravekeeper = 27181;
	private static final int[] EggList = new int[]
	{
		Mi_Drake_Eggs,
		Bl_Wyrm_Eggs,
		Drake_Eggs,
		Th_Wyrm_Eggs
	};
	
	public Q00503_PursuitOfClanAmbition()
	{
		super(PARTY_ALL);
		addStartNpc(Gustaf);
		addTalkId(Martien, Athrea, Kalis, Fritz, Lutz, Kurtz, Kusto, Balthazar, Rodemai, Coffer, Cleo);
		addKillId(ThunderWyrm1, ThunderWyrm2, Drake1, Drake2, BlitzWyrm, SpitefulSoulLeader, GraveGuard, GraveKeymaster, ImperialGravekeeper);
		addAttackId(ImperialGravekeeper);
		addQuestItem(Recipe_Spiteful_Soul_Energy, Spiteful_Soul_Energy, Spiteful_Soul_Vengeance);
		for (int i = 3839; i <= 3848; i++)
		{
			addQuestItem(i);
		}
		for (int i = 3866; i <= 3869; i++)
		{
			addQuestItem(i);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		String htmltext = event;
		
		switch (event)
		{
			case "30760-08.htm":
				qs.giveItems(G_Let_Martien, 1);
				qs.setCond(1);
				qs.set("Fritz", "1");
				qs.set("Lutz", "1");
				qs.set("Kurtz", "1");
				qs.set("ImpGraveKeeper", "1");
				qs.setState(STARTED);
				break;
			
			case "30760-12.htm":
				qs.giveItems(G_Let_Balthazar, 1);
				qs.setCond(4);
				break;
			
			case "30760-16.htm":
				qs.giveItems(G_Let_Rodemai, 1);
				qs.setCond(7);
				break;
			
			case "30760-20.htm":
				exit503(true, qs);
				break;
			
			case "30760-22.htm":
				qs.setCond(13);
				break;
			
			case "30760-23.htm":
				exit503(true, qs);
				break;
			
			case "30645-03.htm":
				qs.takeItems(G_Let_Martien, -1);
				qs.setCond(2);
				suscribe_members(qs);
				List<Player> members = qs.getPlayer().getClan().getOnlineMembers(qs.getPlayer().getObjectId());
				for (Player player : members)
				{
					newQuestState(player, STARTED);
				}
				break;
			
			case "30763-03.htm":
				if (qs.getInt("Kurtz") == 1)
				{
					htmltext = "30763-02.htm";
					qs.giveItems(Mi_Drake_Eggs, 6);
					qs.giveItems(Brooch, 1);
					qs.set("Kurtz", "2");
				}
				break;
			
			case "30762-03.htm":
				int lutz = qs.getInt("Lutz");
				if (lutz == 1)
				{
					htmltext = "30762-02.htm";
					qs.giveItems(Mi_Drake_Eggs, 4);
					qs.giveItems(Bl_Wyrm_Eggs, 3);
					qs.set("Lutz", "2");
				}
				qs.addSpawn(BlitzWyrm, npc.getLoc().getX(), npc.getLoc().getY(), npc.getLoc().getZ(), Location.getRandomHeading(), 300, 120000);
				qs.addSpawn(BlitzWyrm, npc.getLoc().getX(), npc.getLoc().getY(), npc.getLoc().getZ(), Location.getRandomHeading(), 300, 120000);
				break;
			
			case "30761-03.htm":
				int fritz = qs.getInt("Fritz");
				if (fritz == 1)
				{
					htmltext = "30761-02.htm";
					qs.giveItems(Bl_Wyrm_Eggs, 3);
					qs.set("Fritz", "2");
				}
				qs.addSpawn(BlitzWyrm, npc.getLoc().getX(), npc.getLoc().getY(), npc.getLoc().getZ(), Location.getRandomHeading(), 300, 120000);
				qs.addSpawn(BlitzWyrm, npc.getLoc().getX(), npc.getLoc().getY(), npc.getLoc().getZ(), Location.getRandomHeading(), 300, 120000);
				break;
			
			case "30512-03.htm":
				qs.takeItems(Brooch, -1);
				qs.giveItems(Bl_Anvil_Coin, 1);
				qs.set("Kurtz", "3");
				break;
			
			case "30764-03.htm":
				qs.takeItems(G_Let_Balthazar, -1);
				qs.setCond(5);
				qs.set("Kurtz", "3");
				break;
			
			case "30764-05.htm":
				qs.takeItems(G_Let_Balthazar, -1);
				qs.setCond(5);
				break;
			
			case "30764-06.htm":
				qs.takeItems(Bl_Anvil_Coin, -1);
				qs.set("Kurtz", "4");
				qs.giveItems(Recipe_Spiteful_Soul_Energy, 1);
				break;
			
			case "30868-04.htm":
				qs.takeItems(G_Let_Rodemai, -1);
				qs.setCond(8);
				break;
			
			case "30868-06a.htm":
				qs.setCond(10);
				break;
			
			case "30868-10.htm":
				qs.setCond(12);
				break;
			
			case "30766-04.htm":
				qs.setCond(9);
				NpcInstance n = qs.findTemplate(Cleo);
				if (n != null)
				{
					Functions.npcSay(n, "Blood and Honour");
				}
				n = qs.findTemplate(Kalis);
				if (n != null)
				{
					Functions.npcSay(n, "Ambition and Power");
				}
				n = qs.findTemplate(Athrea);
				if (n != null)
				{
					Functions.npcSay(n, "War and Death");
				}
				break;
			
			case "30766-08.htm":
				qs.takeItems(Scepter_Judgement, -1);
				exit503(false, qs);
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int npcId = npc.getId();
		final int id = qs.getState();
		final boolean isLeader = qs.getPlayer().isClanLeader();
		
		if ((id == CREATED) && (npcId == Gustaf))
		{
			if (qs.getPlayer().getClan() != null)
			{
				if (isLeader)
				{
					int clanLevel = qs.getPlayer().getClan().getLevel();
					
					if (qs.getQuestItemsCount(Proof_Aspiration) > 0)
					{
						htmltext = "30760-03.htm";
						qs.exitCurrentQuest(true);
					}
					else if (clanLevel > 3)
					{
						htmltext = "30760-04.htm";
					}
					else
					{
						htmltext = "30760-02.htm";
						qs.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "30760-04t.htm";
					qs.exitCurrentQuest(true);
				}
			}
			else
			{
				htmltext = "30760-01.htm";
				qs.exitCurrentQuest(true);
			}
			
			return htmltext;
		}
		else if ((qs.getPlayer().getClan() != null) && (qs.getPlayer().getClan().getLevel() == 5))
		{
			return "completed";
		}
		else if (isLeader)
		{
			if (qs.getCond() == 0)
			{
				qs.setCond(1);
			}
			
			if (qs.get("Kurtz") == null)
			{
				qs.set("Kurtz", "1");
			}
			
			if (qs.get("Lutz") == null)
			{
				qs.set("Lutz", "1");
			}
			
			if (qs.get("Fritz") == null)
			{
				qs.set("Fritz", "1");
			}
			
			final int cond = qs.getCond();
			final int kurtz = qs.getInt("Kurtz");
			final int lutz = qs.getInt("Lutz");
			final int fritz = qs.getInt("Fritz");
			
			switch (npcId)
			{
				case Gustaf:
					switch (cond)
					{
						case 1:
							htmltext = "30760-09.htm";
							break;
						
						case 2:
							htmltext = "30760-10.htm";
							break;
						
						case 3:
							htmltext = "30760-11.htm";
							break;
						
						case 4:
							htmltext = "30760-13.htm";
							break;
						
						case 5:
							htmltext = "30760-14.htm";
							break;
						
						case 6:
							htmltext = "30760-15.htm";
							break;
						
						case 7:
							htmltext = "30760-17.htm";
							break;
						
						case 12:
							htmltext = "30760-19.htm";
							break;
						
						case 13:
							htmltext = "30760-24.htm";
							break;
						
						default:
							htmltext = "30760-18.htm";
							break;
					}
					break;
				
				case Martien:
					if (cond == 1)
					{
						htmltext = "30645-02.htm";
					}
					else if (cond == 2)
					{
						if (checkEggs(qs) && (kurtz > 1) && (lutz > 1) && (fritz > 1))
						{
							htmltext = "30645-05.htm";
							qs.setCond(3);
							
							for (int item : EggList)
							{
								qs.takeItems(item, -1);
							}
						}
						else
						{
							htmltext = "30645-04.htm";
						}
					}
					else if (cond == 3)
					{
						htmltext = "30645-07.htm";
					}
					else
					{
						htmltext = "30645-08.htm";
					}
					break;
				
				case Lutz:
					if (cond == 2)
					{
						htmltext = "30762-01.htm";
					}
					break;
				
				case Kurtz:
					if (cond == 2)
					{
						htmltext = "30763-01.htm";
					}
					break;
				
				case Fritz:
					if (cond == 2)
					{
						htmltext = "30761-01.htm";
					}
					break;
				
				case Kusto:
					if (kurtz == 1)
					{
						htmltext = "30512-01.htm";
					}
					else if (kurtz == 2)
					{
						htmltext = "30512-02.htm";
					}
					else
					{
						htmltext = "30512-04.htm";
					}
					break;
				
				case Balthazar:
					if (cond == 4)
					{
						if (kurtz > 2)
						{
							htmltext = "30764-04.htm";
						}
						else
						{
							htmltext = "30764-02.htm";
						}
					}
					else if (cond == 5)
					{
						if (qs.getQuestItemsCount(Spiteful_Soul_Energy) > 9)
						{
							htmltext = "30764-08.htm";
							qs.takeItems(Spiteful_Soul_Energy, -1);
							qs.takeItems(Brooch, -1);
							qs.setCond(6);
						}
						else
						{
							htmltext = "30764-07.htm";
						}
					}
					else if (cond == 6)
					{
						htmltext = "30764-09.htm";
					}
					break;
				
				case Rodemai:
					switch (cond)
					{
						case 7:
							htmltext = "30868-02.htm";
							break;
						
						case 8:
							htmltext = "30868-05.htm";
							break;
						
						case 9:
							htmltext = "30868-06.htm";
							break;
						
						case 10:
							htmltext = "30868-08.htm";
							break;
						
						case 11:
							htmltext = "30868-09.htm";
							break;
						
						case 12:
							htmltext = "30868-11.htm";
							break;
					}
					break;
				
				case Cleo:
					switch (cond)
					{
						case 8:
							htmltext = "30766-02.htm";
							break;
						
						case 9:
							htmltext = "30766-05.htm";
							break;
						
						case 10:
							htmltext = "30766-06.htm";
							break;
						
						case 11:
						case 12:
						case 13:
							htmltext = "30766-07.htm";
							break;
					}
					break;
				
				case Coffer:
					if (qs.getCond() == 10)
					{
						if (qs.getQuestItemsCount(Imp_Keys) < 6)
						{
							htmltext = "30765-03a.htm";
						}
						else if (qs.getInt("ImpGraveKeeper") == 3)
						{
							htmltext = "30765-02.htm";
							qs.setCond(11);
							qs.takeItems(Imp_Keys, 6);
							qs.giveItems(Scepter_Judgement, 1);
						}
						else
						{
							htmltext = "<html><head><body>(You and your Clan didn't kill the Imperial Gravekeeper by your own, do it try again.)</body></html>";
						}
					}
					else
					{
						htmltext = "<html><head><body>(You already have the Scepter of Judgement.)</body></html>";
					}
					break;
				
				case Kalis:
					htmltext = "30759-01.htm";
					break;
				
				case Athrea:
					htmltext = "30758-01.htm";
					break;
			}
			
			return htmltext;
		}
		else
		{
			final int cond = getLeaderVar(qs, "cond");
			
			switch (npcId)
			{
				case Martien:
					if ((cond == 1) || (cond == 2) || (cond == 3))
					{
						htmltext = "30645-01.htm";
					}
					break;
				
				case Rodemai:
					if ((cond == 9) || (cond == 10))
					{
						htmltext = "30868-07.htm";
					}
					else if (cond == 7)
					{
						htmltext = "30868-01.htm";
					}
					break;
				
				case Balthazar:
					if (cond == 4)
					{
						htmltext = "30764-01.htm";
					}
					break;
				
				case Cleo:
					if (cond == 8)
					{
						htmltext = "30766-01.htm";
					}
					break;
				
				case Kusto:
					if ((cond > 2) && (cond < 6))
					{
						htmltext = "30512-01a.htm";
					}
					break;
				
				case Coffer:
					if (cond == 10)
					{
						htmltext = "30765-01.htm";
					}
					break;
				
				case Gustaf:
					switch (cond)
					{
						case 3:
							htmltext = "30760-11t.htm";
							break;
						
						case 4:
							htmltext = "30760-15t.htm";
							break;
						
						case 12:
							htmltext = "30760-19t.htm";
							break;
						
						case 13:
							htmltext = "30766-24t.htm";
							break;
					}
					break;
			}
			
			return htmltext;
		}
	}
	
	@Override
	public String onAttack(NpcInstance npc, QuestState qs)
	{
		if (((npc.getMaxHp() / 2) > npc.getCurrentHp()) && Rnd.chance(4))
		{
			final int ImpGraveKepperStat = getLeaderVar(qs, "ImpGraveKeeper");
			
			if (ImpGraveKepperStat == 1)
			{
				for (int i = 1; i <= 4; i++)
				{
					qs.addSpawn(27180, 120000);
				}
				
				setLeaderVar(qs, "ImpGraveKeeper", "2");
			}
			else
			{
				List<Player> players = World.getAroundPlayers(npc, 900, 200);
				
				if (players.size() > 0)
				{
					Player player = players.get(Rnd.get(players.size()));
					
					if (player != null)
					{
						player.teleToLocation(185462, 20342, -3250);
					}
				}
			}
		}
		
		return null;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final int npcId = npc.getId();
		
		switch (getLeaderVar(qs, "cond"))
		{
			case 2:
				switch (npcId)
				{
					case ThunderWyrm1:
						if (Rnd.chance(20))
						{
							giveItem(Th_Wyrm_Eggs, 10, qs);
						}
						break;
					
					case ThunderWyrm2:
						if (Rnd.chance(15))
						{
							giveItem(Th_Wyrm_Eggs, 10, qs);
						}
						break;
					
					case Drake1:
						if (Rnd.chance(20))
						{
							giveItem(Drake_Eggs, 10, qs);
						}
						break;
					
					case Drake2:
						if (Rnd.chance(25))
						{
							giveItem(Drake_Eggs, 10, qs);
						}
						break;
					
					case BlitzWyrm:
						giveItem(Bl_Wyrm_Eggs, 10, qs);
						break;
				}
				break;
			
			case 5:
				if ((npcId == SpitefulSoulLeader) && Rnd.chance(25))
				{
					if (Rnd.chance(50))
					{
						if (getLeaderVar(qs, "Kurtz") < 4)
						{
							return null;
						}
						
						giveItem(Spiteful_Soul_Vengeance, 40, qs);
					}
					else
					{
						giveItem(Spiteful_Soul_Energy, 10, qs);
					}
				}
				break;
			
			case 10:
				switch (npcId)
				{
					case GraveGuard:
						if (Rnd.chance(15))
						{
							qs.addSpawn(GraveKeymaster, 120000);
						}
						break;
					
					case GraveKeymaster:
						if (Rnd.chance(80))
						{
							giveItem(Imp_Keys, 6, qs);
						}
						break;
					
					case ImperialGravekeeper:
						NpcInstance spawnedNpc = qs.addSpawn(Coffer, 120000);
						Functions.npcSay(spawnedNpc, "Curse of the gods on the one that defiles the property of the empire!");
						setLeaderVar(qs, "ImpGraveKeeper", "3");
						break;
				}
				break;
		}
		
		return null;
	}
	
	public void suscribe_members(QuestState qs)
	{
		final int clan = qs.getPlayer().getClan().getClanId();
		
		try (Connection con = DatabaseFactory.getInstance().getConnection();)
		{
			
			PreparedStatement offline = con.prepareStatement("SELECT obj_Id FROM characters WHERE clanid=? AND online=0");
			PreparedStatement insertion = con.prepareStatement("REPLACE INTO character_quests (char_id,name,var,value) VALUES (?,?,?,?)");
			offline.setInt(1, clan);
			ResultSet rs = offline.executeQuery();
			
			while (rs.next())
			{
				int char_id = rs.getInt("obj_Id");
				
				try
				{
					insertion.setInt(1, char_id);
					insertion.setString(2, getName());
					insertion.setString(3, "<state>");
					insertion.setString(4, "Started");
					insertion.executeUpdate();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			offline.close();
			insertion.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void offlineMemberExit(QuestState qs)
	{
		final int clan = qs.getPlayer().getClan().getClanId();
		
		try (Connection con = DatabaseFactory.getInstance().getConnection();)
		{
			
			PreparedStatement offline = con.prepareStatement("DELETE FROM character_quests WHERE name=? AND char_id IN (SELECT obj_id FROM characters WHERE clanId=? AND online=0)");
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
	
	public Player getLeader(QuestState qs)
	{
		final Player player = qs.getPlayer();
		
		if (player == null)
		{
			return null;
		}
		
		final Clan clan = player.getClan();
		
		if (clan == null)
		{
			return null;
		}
		
		return clan.getLeader().getPlayer();
	}
	
	public int getLeaderVar(QuestState qs, String var)
	{
		final boolean cond = "cond".equals(var);
		
		try
		{
			Player leader = getLeader(qs);
			
			if (leader != null)
			{
				if (cond)
				{
					return leader.getQuestState(getName()).getCond();
				}
				
				return leader.getQuestState(getName()).getInt(var);
			}
		}
		catch (Exception e)
		{
			return -1;
		}
		
		final Clan clan = qs.getPlayer().getClan();
		
		if (clan == null)
		{
			return -1;
		}
		
		final int leaderId = clan.getLeaderId();
		
		try (Connection con = DatabaseFactory.getInstance().getConnection();)
		{
			PreparedStatement offline = con.prepareStatement("SELECT value FROM character_quests WHERE char_id=? AND var=? AND name=?");
			offline.setInt(1, leaderId);
			offline.setString(2, var);
			offline.setString(3, getName());
			int val = -1;
			ResultSet rs = offline.executeQuery();
			
			if (rs.next())
			{
				val = rs.getInt("value");
				
				if (cond && ((val & 0x80000000) != 0))
				{
					val &= 0x7fffffff;
					
					for (int i = 1; i < 32; i++)
					{
						val = (val >> 1);
						
						if (val == 0)
						{
							return i;
						}
					}
				}
			}
			rs.close();
			offline.close();
			
			return val;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public void setLeaderVar(QuestState qs, String var, String value)
	{
		final Clan clan = qs.getPlayer().getClan();
		
		if (clan == null)
		{
			return;
		}
		
		final Player leader = clan.getLeader().getPlayer();
		
		if (leader != null)
		{
			if ("cond".equals(var))
			{
				leader.getQuestState(getName()).setCond(Integer.parseInt(value));
			}
			else
			{
				leader.getQuestState(getName()).set(var, value);
			}
		}
		else
		{
			int leaderId = qs.getPlayer().getClan().getLeaderId();
			
			try (Connection con = DatabaseFactory.getInstance().getConnection();)
			{
				PreparedStatement offline = con.prepareStatement("UPDATE character_quests SET value=? WHERE char_id=? AND var=? AND name=?");
				offline.setString(1, value);
				offline.setInt(2, leaderId);
				offline.setString(3, var);
				offline.setString(4, getName());
				offline.executeUpdate();
				offline.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
	
	public boolean checkEggs(QuestState qs)
	{
		int count = 0;
		
		for (int item : EggList)
		{
			if (qs.getQuestItemsCount(item) > 9)
			{
				count += 1;
			}
		}
		
		return count > 3;
	}
	
	public void giveItem(int item, long maxcount, QuestState qs)
	{
		final Player player = qs.getPlayer();
		
		if (player == null)
		{
			return;
		}
		
		final Player leader = getLeader(qs);
		
		if (leader == null)
		{
			return;
		}
		
		if (player.getDistance(leader) > Config.ALT_PARTY_DISTRIBUTION_RANGE)
		{
			return;
		}
		
		final QuestState state = leader.getQuestState(getClass());
		
		if (state == null)
		{
			return;
		}
		
		final long count = state.getQuestItemsCount(item);
		
		if (count < maxcount)
		{
			state.giveItems(item, 1);
			
			if (count == (maxcount - 1))
			{
				state.playSound(SOUND_MIDDLE);
			}
			else
			{
				state.playSound(SOUND_ITEMGET);
			}
		}
	}
	
	public String exit503(boolean completed, QuestState qs)
	{
		if (completed)
		{
			qs.giveItems(Proof_Aspiration, 1);
			qs.addExpAndSp(0, 250000);
			qs.unset("cond");
			qs.unset("Fritz");
			qs.unset("Lutz");
			qs.unset("Kurtz");
			qs.unset("ImpGraveKeeper");
			qs.exitCurrentQuest(false);
		}
		else
		{
			qs.exitCurrentQuest(true);
		}
		
		qs.takeItems(Scepter_Judgement, -1);
		
		try
		{
			final List<Player> members = qs.getPlayer().getClan().getOnlineMembers(0);
			
			for (Player player : members)
			{
				if (player == null)
				{
					continue;
				}
				
				final QuestState state = player.getQuestState(getName());
				
				if (state != null)
				{
					state.exitCurrentQuest(true);
				}
			}
			
			offlineMemberExit(qs);
		}
		catch (Exception e)
		{
			return "You dont have any members in your Clan, so you can't finish the Pursuit of Aspiration";
		}
		
		return "Congratulations, you have finished the Pursuit of Clan Ambition";
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
