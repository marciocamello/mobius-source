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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.InstantZoneHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.templates.InstantZone;
import lineage2.gameserver.utils.Location;

public class Q00512_BladeUnderFoot extends Quest implements ScriptFile
{
	private final static int INSTANCE_ZONE_ID = 13;
	private final static int FragmentOfTheDungeonLeaderMark = 9798;
	private final static int RewardMarksCount = 1500;
	private final static int KnightsEpaulette = 9912;
	private static final Map<Integer, Prison> _prisons = new ConcurrentHashMap<>();
	private static final int RhiannaTheTraitor = 25546;
	private static final int TeslaTheDeceiver = 25549;
	private static final int SoulHunterChakundel = 25552;
	private static final int DurangoTheCrusher = 25553;
	private static final int BrutusTheObstinate = 25554;
	private static final int RangerKarankawa = 25557;
	private static final int SargonTheMad = 25560;
	private static final int BeautifulAtrielle = 25563;
	private static final int NagenTheTomboy = 25566;
	private static final int JaxTheDestroyer = 25569;
	private static final int[] type1 = new int[]
	{
		RhiannaTheTraitor,
		TeslaTheDeceiver,
		SoulHunterChakundel
	};
	private static final int[] type2 = new int[]
	{
		DurangoTheCrusher,
		BrutusTheObstinate,
		RangerKarankawa,
		SargonTheMad
	};
	private static final int[] type3 = new int[]
	{
		BeautifulAtrielle,
		NagenTheTomboy,
		JaxTheDestroyer
	};
	
	public Q00512_BladeUnderFoot()
	{
		super(false);
		addStartNpc(36403, 36404, 36405, 36406, 36407, 36408, 36409, 36410, 36411);
		addQuestItem(FragmentOfTheDungeonLeaderMark);
		addKillId(RhiannaTheTraitor, TeslaTheDeceiver, SoulHunterChakundel, DurangoTheCrusher, BrutusTheObstinate, RangerKarankawa, SargonTheMad, BeautifulAtrielle, NagenTheTomboy, JaxTheDestroyer);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "gludio_prison_keeper_q0512_03.htm":
			case "gludio_prison_keeper_q0512_05.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "exit":
				qs.exitCurrentQuest(true);
				return null;
				
			case "enter":
				if ((qs.getState() == CREATED) || !check(qs.getPlayer()))
				{
					return "gludio_prison_keeper_q0512_01a.htm";
				}
				return enterPrison(qs.getPlayer());
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		if (!check(qs.getPlayer()))
		{
			return "gludio_prison_keeper_q0512_01a.htm";
		}
		else if (qs.getState() == CREATED)
		{
			return "gludio_prison_keeper_q0512_01.htm";
		}
		else if (qs.getQuestItemsCount(FragmentOfTheDungeonLeaderMark) > 0)
		{
			qs.giveItems(KnightsEpaulette, qs.getQuestItemsCount(FragmentOfTheDungeonLeaderMark));
			qs.takeItems(FragmentOfTheDungeonLeaderMark, -1);
			qs.playSound(SOUND_FINISH);
			return "gludio_prison_keeper_q0512_08.htm";
		}
		
		return "gludio_prison_keeper_q0512_09.htm";
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		for (Prison prison : _prisons.values())
		{
			if (prison.getReflectionId() == npc.getReflectionId())
			{
				switch (npc.getId())
				{
					case RhiannaTheTraitor:
					case TeslaTheDeceiver:
					case SoulHunterChakundel:
						prison.initSpawn(type2[Rnd.get(type2.length)], false);
						break;
					
					case DurangoTheCrusher:
					case BrutusTheObstinate:
					case RangerKarankawa:
					case SargonTheMad:
						prison.initSpawn(type3[Rnd.get(type3.length)], false);
						break;
					
					case BeautifulAtrielle:
					case NagenTheTomboy:
					case JaxTheDestroyer:
						Party party = qs.getPlayer().getParty();
						
						if (party != null)
						{
							for (Player member : party.getPartyMembers())
							{
								final QuestState state = member.getQuestState(getClass());
								
								if ((state != null) && state.isStarted())
								{
									state.giveItems(FragmentOfTheDungeonLeaderMark, RewardMarksCount / party.getMemberCount());
									state.playSound(SOUND_ITEMGET);
									state.getPlayer().sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(5));
								}
							}
						}
						else
						{
							qs.giveItems(FragmentOfTheDungeonLeaderMark, RewardMarksCount);
							qs.playSound(SOUND_ITEMGET);
							qs.getPlayer().sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(5));
						}
						
						final Reflection r = ReflectionManager.getInstance().get(prison.getReflectionId());
						
						if (r != null)
						{
							r.startCollapseTimer(300000);
						}
						break;
				}
				break;
			}
		}
		
		return null;
	}
	
	private boolean check(Player player)
	{
		final Castle castle = ResidenceHolder.getInstance().getResidenceByObject(Castle.class, player);
		
		if (castle == null)
		{
			return false;
		}
		
		final Clan clan = player.getClan();
		
		if (clan == null)
		{
			return false;
		}
		
		if (clan.getClanId() != castle.getOwnerId())
		{
			return false;
		}
		
		return true;
	}
	
	private String enterPrison(Player player)
	{
		final Castle castle = ResidenceHolder.getInstance().getResidenceByObject(Castle.class, player);
		
		if ((castle == null) || (castle.getOwner() != player.getClan()))
		{
			return "gludio_prison_keeper_q0512_01a.htm";
		}
		else if (!areMembersSameClan(player))
		{
			return "gludio_prison_keeper_q0512_01a.htm";
		}
		
		if (player.canEnterInstance(INSTANCE_ZONE_ID))
		{
			final InstantZone iz = InstantZoneHolder.getInstance().getInstantZone(INSTANCE_ZONE_ID);
			Prison prison = null;
			
			if (!_prisons.isEmpty())
			{
				prison = _prisons.get(castle.getId());
				
				if ((prison != null) && prison.isLocked())
				{
					player.sendPacket(new SystemMessage(SystemMessage.C1_MAY_NOT_RE_ENTER_YET).addName(player));
					return null;
				}
			}
			
			prison = new Prison(castle.getId(), iz);
			_prisons.put(prison.getCastleId(), prison);
			Reflection r = ReflectionManager.getInstance().get(prison.getReflectionId());
			r.setReturnLoc(player.getLoc());
			
			for (Player member : player.getParty().getPartyMembers())
			{
				if (member != player)
				{
					newQuestState(member, STARTED);
				}
				
				member.setReflection(r);
				member.teleToLocation(iz.getTeleportCoord());
				member.setVar("backCoords", r.getReturnLoc().toXYZString(), -1);
				member.setInstanceReuse(iz.getId(), System.currentTimeMillis());
			}
			
			player.getParty().setReflection(r);
			r.setParty(player.getParty());
			r.startCollapseTimer(iz.getTimelimit() * 60 * 1000L);
			player.getParty().broadCast(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(iz.getTimelimit()));
			prison.initSpawn(type1[Rnd.get(type1.length)], true);
		}
		
		return null;
	}
	
	private boolean areMembersSameClan(Player player)
	{
		if (player.getParty() == null)
		{
			return true;
		}
		
		for (Player p : player.getParty().getPartyMembers())
		{
			if (p.getClan() != player.getClan())
			{
				return false;
			}
		}
		
		return true;
	}
	
	private class Prison
	{
		private int _castleId;
		int _reflectionId;
		private long _lastEnter;
		
		private class PrisonSpawnTask extends RunnableImpl
		{
			int _npcId;
			
			public PrisonSpawnTask(int npcId)
			{
				_npcId = npcId;
			}
			
			@Override
			public void runImpl()
			{
				addSpawnToInstance(_npcId, new Location(12152, -49272, -3008, 25958), 0, _reflectionId);
			}
		}
		
		public Prison(int id, InstantZone iz)
		{
			try
			{
				Reflection r = new Reflection();
				r.init(iz);
				_reflectionId = r.getId();
				_castleId = id;
				_lastEnter = System.currentTimeMillis();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public void initSpawn(int npcId, boolean first)
		{
			ThreadPoolManager.getInstance().schedule(new PrisonSpawnTask(npcId), first ? 60000 : 180000);
		}
		
		public int getReflectionId()
		{
			return _reflectionId;
		}
		
		public int getCastleId()
		{
			return _castleId;
		}
		
		public boolean isLocked()
		{
			return (System.currentTimeMillis() - _lastEnter) < (4 * 60 * 60 * 1000L);
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
