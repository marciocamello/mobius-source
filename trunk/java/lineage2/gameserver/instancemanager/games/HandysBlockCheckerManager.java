/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.instancemanager.games;

import gnu.trove.map.hash.TIntIntHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.BlockCheckerEngine;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.impl.KrateisCubeRunnerEvent;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.network.serverpackets.ExCubeGameAddPlayer;
import lineage2.gameserver.network.serverpackets.ExCubeGameChangeTeam;
import lineage2.gameserver.network.serverpackets.ExCubeGameRemovePlayer;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage;

public final class HandysBlockCheckerManager
{
	private static ArenaParticipantsHolder[] _arenaPlayers;
	private static TIntIntHashMap _arenaVotes = new TIntIntHashMap();
	private static Map<Integer, Boolean> _arenaStatus;
	static List<Integer> _registrationPenalty = new ArrayList<>();
	
	public synchronized int getArenaVotes(int arenaId)
	{
		return _arenaVotes.get(arenaId);
	}
	
	public synchronized void increaseArenaVotes(int arena)
	{
		int newVotes = _arenaVotes.get(arena) + 1;
		ArenaParticipantsHolder holder = _arenaPlayers[arena];
		if ((newVotes > (holder.getAllPlayers().size() / 2)) && !holder.getEvent().isStarted())
		{
			clearArenaVotes(arena);
			if ((holder.getBlueTeamSize() == 0) || (holder.getRedTeamSize() == 0))
			{
				return;
			}
			if (Config.ALT_HBCE_FAIR_PLAY)
			{
				holder.checkAndShuffle();
			}
			ThreadPoolManager.getInstance().execute(holder.getEvent().new StartEvent());
		}
		else
		{
			_arenaVotes.put(arena, newVotes);
		}
	}
	
	public synchronized void clearArenaVotes(int arena)
	{
		_arenaVotes.put(arena, 0);
	}
	
	HandysBlockCheckerManager()
	{
		if (_arenaStatus == null)
		{
			_arenaStatus = new HashMap<>();
			_arenaStatus.put(0, false);
			_arenaStatus.put(1, false);
			_arenaStatus.put(2, false);
			_arenaStatus.put(3, false);
		}
	}
	
	public ArenaParticipantsHolder getHolder(int arena)
	{
		return _arenaPlayers[arena];
	}
	
	public void startUpParticipantsQueue()
	{
		_arenaPlayers = new ArenaParticipantsHolder[4];
		for (int i = 0; i < 4; ++i)
		{
			_arenaPlayers[i] = new ArenaParticipantsHolder(i);
		}
	}
	
	public boolean addPlayerToArena(Player player, int arenaId)
	{
		ArenaParticipantsHolder holder = _arenaPlayers[arenaId];
		synchronized (holder)
		{
			boolean isRed;
			if (isRegistered(player))
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_ALREADY_BEEN_REGISTERED_IN_A_WAITING_LIST_OF_AN_EVENT).addName(player));
				return false;
			}
			if (player.isCursedWeaponEquipped())
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_REGISTER_WHILE_POSSESSING_A_CURSED_WEAPON));
				return false;
			}
			KrateisCubeRunnerEvent krateis = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, 2);
			if (krateis.isRegistered(player))
			{
				player.sendPacket(Msg.APPLICANTS_FOR_THE_OLYMPIAD_UNDERGROUND_COLISEUM_OR_KRATEI_S_CUBE_MATCHES_CANNOT_REGISTER);
				return false;
			}
			if (Olympiad.isRegistered(player))
			{
				player.sendPacket(Msg.APPLICANTS_FOR_THE_OLYMPIAD_UNDERGROUND_COLISEUM_OR_KRATEI_S_CUBE_MATCHES_CANNOT_REGISTER);
				return false;
			}
			if (_registrationPenalty.contains(player.getObjectId()))
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_MAKE_ANOTHER_REQUEST_FOR_10_SECONDS_AFTER_CANCELLING_A_MATCH_REGISTRATION));
				return false;
			}
			if (holder.getBlueTeamSize() < holder.getRedTeamSize())
			{
				holder.addPlayer(player, 1);
				isRed = false;
			}
			else
			{
				holder.addPlayer(player, 0);
				isRed = true;
			}
			holder.broadCastPacketToTeam(new ExCubeGameAddPlayer(player, isRed));
			return true;
		}
	}
	
	public void removePlayer(Player player, int arenaId, int team)
	{
		ArenaParticipantsHolder holder = _arenaPlayers[arenaId];
		synchronized (holder)
		{
			boolean isRed = team == 0 ? true : false;
			holder.removePlayer(player, team);
			holder.broadCastPacketToTeam(new ExCubeGameRemovePlayer(player, isRed));
			int teamSize = isRed ? holder.getRedTeamSize() : holder.getBlueTeamSize();
			if (teamSize == 0)
			{
				holder.getEvent().endEventAbnormally();
			}
			Integer objId = player.getObjectId();
			if (!_registrationPenalty.contains(objId))
			{
				_registrationPenalty.add(objId);
			}
			schedulePenaltyRemoval(objId);
		}
	}
	
	public void changePlayerToTeam(Player player, int arena, int team)
	{
		ArenaParticipantsHolder holder = _arenaPlayers[arena];
		synchronized (holder)
		{
			boolean isFromRed = holder._redPlayers.contains(player);
			if (isFromRed && (holder.getBlueTeamSize() == 6))
			{
				player.sendMessage("The team is full");
				return;
			}
			else if (!isFromRed && (holder.getRedTeamSize() == 6))
			{
				player.sendMessage("The team is full");
				return;
			}
			int futureTeam = isFromRed ? 1 : 0;
			holder.addPlayer(player, futureTeam);
			if (isFromRed)
			{
				holder.removePlayer(player, 0);
			}
			else
			{
				holder.removePlayer(player, 1);
			}
			holder.broadCastPacketToTeam(new ExCubeGameChangeTeam(player, isFromRed));
		}
	}
	
	public synchronized void clearPaticipantQueueByArenaId(int arenaId)
	{
		_arenaPlayers[arenaId].clearPlayers();
	}
	
	public static boolean isRegistered(Player player)
	{
		for (int i = 0; i < 4; i++)
		{
			if (_arenaPlayers[i].getAllPlayers().contains(player))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean arenaIsBeingUsed(int arenaId)
	{
		if ((arenaId < 0) || (arenaId > 3))
		{
			return false;
		}
		return _arenaStatus.get(arenaId);
	}
	
	public void setArenaBeingUsed(int arenaId)
	{
		_arenaStatus.put(arenaId, true);
	}
	
	public void setArenaFree(int arenaId)
	{
		_arenaStatus.put(arenaId, false);
	}
	
	public static HandysBlockCheckerManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		static HandysBlockCheckerManager _instance = new HandysBlockCheckerManager();
	}
	
	public class ArenaParticipantsHolder
	{
		int _arena;
		List<Player> _redPlayers;
		List<Player> _bluePlayers;
		BlockCheckerEngine _engine;
		
		public ArenaParticipantsHolder(int arena)
		{
			_arena = arena;
			_redPlayers = new ArrayList<>(6);
			_bluePlayers = new ArrayList<>(6);
			_engine = new BlockCheckerEngine(this, _arena);
		}
		
		public List<Player> getRedPlayers()
		{
			return _redPlayers;
		}
		
		public List<Player> getBluePlayers()
		{
			return _bluePlayers;
		}
		
		public ArrayList<Player> getAllPlayers()
		{
			ArrayList<Player> all = new ArrayList<>(12);
			all.addAll(_redPlayers);
			all.addAll(_bluePlayers);
			return all;
		}
		
		public void addPlayer(Player player, int team)
		{
			if (team == 0)
			{
				_redPlayers.add(player);
			}
			else
			{
				_bluePlayers.add(player);
			}
		}
		
		public void removePlayer(Player player, int team)
		{
			if (team == 0)
			{
				_redPlayers.remove(player);
			}
			else
			{
				_bluePlayers.remove(player);
			}
		}
		
		public int getPlayerTeam(Player player)
		{
			if (_redPlayers.contains(player))
			{
				return 0;
			}
			else if (_bluePlayers.contains(player))
			{
				return 1;
			}
			else
			{
				return -1;
			}
		}
		
		public int getRedTeamSize()
		{
			return _redPlayers.size();
		}
		
		public int getBlueTeamSize()
		{
			return _bluePlayers.size();
		}
		
		public void broadCastPacketToTeam(L2GameServerPacket packet)
		{
			ArrayList<Player> team = new ArrayList<>(12);
			team.addAll(_redPlayers);
			team.addAll(_bluePlayers);
			for (Player p : team)
			{
				p.sendPacket(packet);
			}
		}
		
		public void clearPlayers()
		{
			_redPlayers.clear();
			_bluePlayers.clear();
		}
		
		public BlockCheckerEngine getEvent()
		{
			return _engine;
		}
		
		public void updateEvent()
		{
			_engine.updatePlayersOnStart(this);
		}
		
		void checkAndShuffle()
		{
			int redSize = _redPlayers.size();
			int blueSize = _bluePlayers.size();
			if (redSize > (blueSize + 1))
			{
				broadCastPacketToTeam(new SystemMessage(SystemMessage.THE_TEAM_WAS_ADJUSTED_BECAUSE_THE_POPULATION_RATIO_WAS_NOT_CORRECT));
				int needed = redSize - (blueSize + 1);
				for (int i = 0; i < (needed + 1); i++)
				{
					Player plr = _redPlayers.get(i);
					if (plr == null)
					{
						continue;
					}
					changePlayerToTeam(plr, _arena, 1);
				}
			}
			else if (blueSize > (redSize + 1))
			{
				broadCastPacketToTeam(new SystemMessage(SystemMessage.THE_TEAM_WAS_ADJUSTED_BECAUSE_THE_POPULATION_RATIO_WAS_NOT_CORRECT));
				int needed = blueSize - (redSize + 1);
				for (int i = 0; i < (needed + 1); i++)
				{
					Player plr = _bluePlayers.get(i);
					if (plr == null)
					{
						continue;
					}
					changePlayerToTeam(plr, _arena, 0);
				}
			}
		}
	}
	
	private void schedulePenaltyRemoval(int objId)
	{
		ThreadPoolManager.getInstance().schedule(new PenaltyRemove(objId), 10000);
	}
	
	private class PenaltyRemove extends RunnableImpl
	{
		Integer objectId;
		
		public PenaltyRemove(Integer id)
		{
			objectId = id;
		}
		
		@Override
		public void runImpl()
		{
			_registrationPenalty.remove(objectId);
		}
	}
}
