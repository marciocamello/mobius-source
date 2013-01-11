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
package lineage2.gameserver.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.instancemanager.games.HandysBlockCheckerManager;
import lineage2.gameserver.instancemanager.games.HandysBlockCheckerManager.ArenaParticipantsHolder;
import lineage2.gameserver.listener.actor.player.OnPlayerExitListener;
import lineage2.gameserver.listener.actor.player.OnTeleportListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.instances.BlockInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.network.serverpackets.ExBasicActionList;
import lineage2.gameserver.network.serverpackets.ExCubeGameChangePoints;
import lineage2.gameserver.network.serverpackets.ExCubeGameCloseUI;
import lineage2.gameserver.network.serverpackets.ExCubeGameEnd;
import lineage2.gameserver.network.serverpackets.ExCubeGameExtendedChangePoints;
import lineage2.gameserver.network.serverpackets.RelationChanged;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BlockCheckerEngine
{
	private static final Logger _log = LoggerFactory.getLogger(BlockCheckerEngine.class);
	HandysBlockCheckerManager.ArenaParticipantsHolder _holder;
	final Map<Player, Integer> _redTeamPoints = new ConcurrentHashMap<>();
	final Map<Player, Integer> _blueTeamPoints = new ConcurrentHashMap<>();
	int _redPoints = 15;
	int _bluePoints = 15;
	int _arena = -1;
	final List<SimpleSpawner> _spawns = new CopyOnWriteArrayList<>();
	boolean _isRedWinner;
	long _startedTime;
	static final int[][] _arenaCoordinates =
	{
		{
			-58368,
			-62745,
			-57751,
			-62131,
			-58053,
			-62417
		},
		{
			-58350,
			-63853,
			-57756,
			-63266,
			-58053,
			-63551
		},
		{
			-57194,
			-63861,
			-56580,
			-63249,
			-56886,
			-63551
		},
		{
			-57200,
			-62727,
			-56584,
			-62115,
			-56850,
			-62391
		}
	};
	private static final int _zCoord = -2405;
	NpcInstance _girlNpc;
	final List<ItemInstance> _drops = new ArrayList<>();
	private static final byte DEFAULT_ARENA = -1;
	boolean _isStarted = false;
	ScheduledFuture<?> _task;
	boolean _abnormalEnd = false;
	final String[] zoneNames =
	{
		"[block_checker_1]",
		"[block_checker_2]",
		"[block_checker_3]",
		"[block_checker_4]"
	};
	
	public BlockCheckerEngine(HandysBlockCheckerManager.ArenaParticipantsHolder holder, int arena)
	{
		_holder = holder;
		if ((arena > -1) && (arena < 4))
		{
			_arena = arena;
		}
		for (Player player : holder.getRedPlayers())
		{
			_redTeamPoints.put(player, 0);
		}
		for (Player player : holder.getBluePlayers())
		{
			_blueTeamPoints.put(player, 0);
		}
	}
	
	public void updatePlayersOnStart(ArenaParticipantsHolder holder)
	{
		_holder = holder;
	}
	
	public ArenaParticipantsHolder getHolder()
	{
		return _holder;
	}
	
	public int getArena()
	{
		return _arena;
	}
	
	public long getStarterTime()
	{
		return _startedTime;
	}
	
	public int getRedPoints()
	{
		synchronized (this)
		{
			return _redPoints;
		}
	}
	
	public int getBluePoints()
	{
		synchronized (this)
		{
			return _bluePoints;
		}
	}
	
	public int getPlayerPoints(Player player, boolean isRed)
	{
		if (!_redTeamPoints.containsKey(player) && !_blueTeamPoints.containsKey(player))
		{
			return 0;
		}
		if (isRed)
		{
			return _redTeamPoints.get(player);
		}
		return _blueTeamPoints.get(player);
	}
	
	public synchronized void increasePlayerPoints(Player player, int team)
	{
		if (player == null)
		{
			return;
		}
		if (team == 0)
		{
			int points = getPlayerPoints(player, true) + 1;
			_redTeamPoints.put(player, points);
			_redPoints++;
			_bluePoints--;
		}
		else
		{
			int points = getPlayerPoints(player, false) + 1;
			_blueTeamPoints.put(player, points);
			_bluePoints++;
			_redPoints--;
		}
	}
	
	public void addNewDrop(ItemInstance item)
	{
		if (item != null)
		{
			_drops.add(item);
		}
	}
	
	public boolean isStarted()
	{
		return _isStarted;
	}
	
	void broadcastRelationChanged(Player plr)
	{
		for (Player p : _holder.getAllPlayers())
		{
			p.sendPacket(RelationChanged.update(plr, p, plr));
		}
	}
	
	public void endEventAbnormally()
	{
		try
		{
			synchronized (this)
			{
				_isStarted = false;
				if (_task != null)
				{
					_task.cancel(true);
				}
				_abnormalEnd = true;
				ThreadPoolManager.getInstance().execute(new EndEvent());
			}
		}
		catch (Exception e)
		{
			_log.error("Couldnt end Block Checker event at " + _arena + e);
		}
	}
	
	public void clearArena(String zoneName)
	{
		Zone zone = ReflectionUtils.getZone(zoneName);
		if (zone != null)
		{
			for (Creature cha : zone.getObjects())
			{
				if (cha.isPlayer() && (cha.getPlayer().getBlockCheckerArena() < 0))
				{
					cha.getPlayer().teleToClosestTown();
				}
				else if (cha.isNpc())
				{
					cha.deleteMe();
				}
			}
		}
	}
	
	public class StartEvent extends RunnableImpl
	{
		private final Skill _freeze, _transformationRed, _transformationBlue;
		private final ExCubeGameCloseUI _closeUserInterface = new ExCubeGameCloseUI();
		
		public StartEvent()
		{
			_freeze = SkillTable.getInstance().getInfo(6034, 1);
			_transformationRed = SkillTable.getInstance().getInfo(6035, 1);
			_transformationBlue = SkillTable.getInstance().getInfo(6036, 1);
		}
		
		private void setUpPlayers()
		{
			HandysBlockCheckerManager.getInstance().setArenaBeingUsed(_arena);
			_redPoints = _spawns.size() / 2;
			_bluePoints = _spawns.size() / 2;
			final ExCubeGameChangePoints initialPoints = new ExCubeGameChangePoints(300, _bluePoints, _redPoints);
			ExCubeGameExtendedChangePoints clientSetUp;
			for (Player player : _holder.getAllPlayers())
			{
				if (player == null)
				{
					continue;
				}
				player.addListener(_listener);
				boolean isRed = _holder.getRedPlayers().contains(player);
				clientSetUp = new ExCubeGameExtendedChangePoints(300, _bluePoints, _redPoints, isRed, player, 0);
				player.sendPacket(clientSetUp);
				player.sendActionFailed();
				int tc = _holder.getPlayerTeam(player) * 2;
				int x = _arenaCoordinates[_arena][tc];
				int y = _arenaCoordinates[_arena][tc + 1];
				player.teleToLocation(x, y, _zCoord);
				if (isRed)
				{
					_redTeamPoints.put(player, 0);
					player.setTeam(TeamType.RED);
				}
				else
				{
					_blueTeamPoints.put(player, 0);
					player.setTeam(TeamType.BLUE);
				}
				player.getEffectList().stopAllEffects();
				for (Summon summon : player.getSummonList())
				{
					summon.unSummon();
				}
				_freeze.getEffects(player, player, false, false);
				if (_holder.getPlayerTeam(player) == 0)
				{
					_transformationRed.getEffects(player, player, false, false);
				}
				else
				{
					_transformationBlue.getEffects(player, player, false, false);
				}
				player.setBlockCheckerArena((byte) _arena);
				player.sendPacket(initialPoints);
				player.sendPacket(_closeUserInterface);
				player.sendPacket(new ExBasicActionList(player));
				broadcastRelationChanged(player);
				player.broadcastCharInfo();
			}
		}
		
		@Override
		public void runImpl()
		{
			if (_arena == -1)
			{
				_log.error("Couldnt set up the arena Id for the Block Checker event, cancelling event...");
				return;
			}
			if (isStarted())
			{
				return;
			}
			clearArena(zoneNames[_arena]);
			_isStarted = true;
			ThreadPoolManager.getInstance().execute(new SpawnRound(16, 1));
			setUpPlayers();
			_startedTime = System.currentTimeMillis() + 300000;
		}
	}
	
	class SpawnRound extends RunnableImpl
	{
		int _numOfBoxes;
		int _round;
		
		SpawnRound(int numberOfBoxes, int round)
		{
			_numOfBoxes = numberOfBoxes;
			_round = round;
		}
		
		@Override
		public void runImpl()
		{
			if (!_isStarted)
			{
				return;
			}
			switch (_round)
			{
				case 1:
					_task = ThreadPoolManager.getInstance().schedule(new SpawnRound(20, 2), 60000);
					break;
				case 2:
					_task = ThreadPoolManager.getInstance().schedule(new SpawnRound(14, 3), 60000);
					break;
				case 3:
					_task = ThreadPoolManager.getInstance().schedule(new CountDown(), 175000);
					break;
			}
			byte random = 2;
			final NpcTemplate template = NpcHolder.getInstance().getTemplate(18672);
			try
			{
				for (int i = 0; i < _numOfBoxes; i++)
				{
					SimpleSpawner spawn = new SimpleSpawner(template);
					spawn.setLocx(_arenaCoordinates[_arena][4] + Rnd.get(-400, 400));
					spawn.setLocy(_arenaCoordinates[_arena][5] + Rnd.get(-400, 400));
					spawn.setLocz(_zCoord);
					spawn.setAmount(1);
					spawn.setHeading(1);
					spawn.setRespawnDelay(1);
					BlockInstance blockInstance = (BlockInstance) spawn.doSpawn(true);
					blockInstance.setRed((random % 2) == 0);
					_spawns.add(spawn);
					random++;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if ((_round == 1) || (_round == 2))
			{
				NpcTemplate girl = NpcHolder.getInstance().getTemplate(18676);
				try
				{
					final SimpleSpawner girlSpawn = new SimpleSpawner(girl);
					girlSpawn.setLocx(_arenaCoordinates[_arena][4] + Rnd.get(-400, 400));
					girlSpawn.setLocy(_arenaCoordinates[_arena][5] + Rnd.get(-400, 400));
					girlSpawn.setLocz(_zCoord);
					girlSpawn.setAmount(1);
					girlSpawn.setHeading(1);
					girlSpawn.setRespawnDelay(1);
					girlSpawn.doSpawn(true);
					girlSpawn.init();
					_girlNpc = girlSpawn.getLastSpawn();
					ThreadPoolManager.getInstance().schedule(new RunnableImpl()
					{
						@Override
						public void runImpl()
						{
							if (_girlNpc == null)
							{
								return;
							}
							_girlNpc.deleteMe();
						}
					}, 9000);
				}
				catch (Exception e)
				{
					_log.warn("Couldnt Spawn Block Checker NPCs! Wrong instance type at npc table?" + e);
				}
			}
			_redPoints += _numOfBoxes / 2;
			_bluePoints += _numOfBoxes / 2;
			int timeLeft = (int) ((getStarterTime() - System.currentTimeMillis()) / 1000);
			ExCubeGameChangePoints changePoints = new ExCubeGameChangePoints(timeLeft, getBluePoints(), getRedPoints());
			getHolder().broadCastPacketToTeam(changePoints);
		}
	}
	
	class CountDown extends RunnableImpl
	{
		private int seconds = 5;
		
		@Override
		public void runImpl()
		{
			switch (seconds)
			{
				case 5:
					_holder.broadCastPacketToTeam(new SystemMessage(SystemMessage.BLOCK_CHECKER_WILL_END_IN_5_SECONDS));
					break;
				case 4:
					_holder.broadCastPacketToTeam(new SystemMessage(SystemMessage.BLOCK_CHECKER_WILL_END_IN_4_SECONDS));
					break;
				case 3:
					_holder.broadCastPacketToTeam(new SystemMessage(SystemMessage.BLOCK_CHECKER_WILL_END_IN_3_SECONDS));
					break;
				case 2:
					_holder.broadCastPacketToTeam(new SystemMessage(SystemMessage.BLOCK_CHECKER_WILL_END_IN_2_SECONDS));
					break;
				case 1:
					_holder.broadCastPacketToTeam(new SystemMessage(SystemMessage.BLOCK_CHECKER_WILL_END_IN_1_SECOND));
					break;
			}
			if (--seconds > 0)
			{
				ThreadPoolManager.getInstance().schedule(this, 1000L);
			}
			else
			{
				ThreadPoolManager.getInstance().execute(new EndEvent());
			}
		}
	}
	
	class EndEvent extends RunnableImpl
	{
		private void clearMe()
		{
			HandysBlockCheckerManager.getInstance().clearPaticipantQueueByArenaId(_arena);
			for (Player player : _holder.getAllPlayers())
			{
				if (player == null)
				{
					continue;
				}
				player.removeListener(_listener);
			}
			_holder.clearPlayers();
			_blueTeamPoints.clear();
			_redTeamPoints.clear();
			HandysBlockCheckerManager.getInstance().setArenaFree(_arena);
			for (SimpleSpawner spawn : _spawns)
			{
				spawn.deleteAll();
			}
			_spawns.clear();
			for (ItemInstance item : _drops)
			{
				if (item == null)
				{
					continue;
				}
				if (!item.isVisible() || (item.getOwnerId() != 0))
				{
					continue;
				}
				item.deleteMe();
			}
			_drops.clear();
		}
		
		private void rewardPlayers()
		{
			if (_redPoints == _bluePoints)
			{
				return;
			}
			_isRedWinner = _redPoints > _bluePoints ? true : false;
			if (_isRedWinner)
			{
				rewardAsWinner(true);
				rewardAsLooser(false);
				SystemMessage msg = new SystemMessage(SystemMessage.THE_C1_TEAM_HAS_WON).addString("Red Team");
				_holder.broadCastPacketToTeam(msg);
			}
			else if (_bluePoints > _redPoints)
			{
				rewardAsWinner(false);
				rewardAsLooser(true);
				SystemMessage msg = new SystemMessage(SystemMessage.THE_C1_TEAM_HAS_WON).addString("Blue Team");
				_holder.broadCastPacketToTeam(msg);
			}
			else
			{
				rewardAsLooser(true);
				rewardAsLooser(false);
			}
		}
		
		private void addRewardItemWithMessage(int id, long count, Player player)
		{
			player.getInventory().addItem(id, (long) (count * Config.ALT_RATE_COINS_REWARD_BLOCK_CHECKER));
			player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_OBTAINED_S2_S1).addItemName(id).addNumber(count));
		}
		
		private void rewardAsWinner(boolean isRed)
		{
			Map<Player, Integer> tempPoints = isRed ? _redTeamPoints : _blueTeamPoints;
			for (Player pc : tempPoints.keySet())
			{
				if (pc == null)
				{
					continue;
				}
				if (tempPoints.get(pc) >= 10)
				{
					addRewardItemWithMessage(13067, 2, pc);
				}
				else
				{
					tempPoints.remove(pc);
				}
			}
			int first = 0, second = 0;
			Player winner1 = null, winner2 = null;
			for (Player pc : tempPoints.keySet())
			{
				int pcPoints = tempPoints.get(pc);
				if (pcPoints > first)
				{
					second = first;
					winner2 = winner1;
					first = pcPoints;
					winner1 = pc;
				}
				else if (pcPoints > second)
				{
					second = pcPoints;
					winner2 = pc;
				}
			}
			if (winner1 != null)
			{
				addRewardItemWithMessage(13067, 8, winner1);
			}
			if (winner2 != null)
			{
				addRewardItemWithMessage(13067, 5, winner2);
			}
		}
		
		private void rewardAsLooser(boolean isRed)
		{
			Map<Player, Integer> tempPoints = isRed ? _redTeamPoints : _blueTeamPoints;
			for (Player player : tempPoints.keySet())
			{
				if ((player != null) && (tempPoints.get(player) >= 10))
				{
					addRewardItemWithMessage(13067, 2, player);
				}
			}
		}
		
		private void setPlayersBack()
		{
			final ExCubeGameEnd end = new ExCubeGameEnd(_isRedWinner);
			for (Player player : _holder.getAllPlayers())
			{
				if (player == null)
				{
					continue;
				}
				player.getEffectList().stopAllEffects();
				player.setTeam(TeamType.NONE);
				player.setBlockCheckerArena(DEFAULT_ARENA);
				PcInventory inv = player.getInventory();
				inv.destroyItemByItemId(13787, inv.getCountOf(13787));
				inv.destroyItemByItemId(13788, inv.getCountOf(13788));
				broadcastRelationChanged(player);
				player.teleToLocation(-57478, -60367, -2370);
				player.sendPacket(end);
				player.broadcastCharInfo();
			}
		}
		
		@Override
		public void runImpl()
		{
			if (!_abnormalEnd)
			{
				rewardPlayers();
			}
			_isStarted = false;
			setPlayersBack();
			clearMe();
			_abnormalEnd = false;
		}
	}
	
	final OnExitPlayerListener _listener = new OnExitPlayerListener();
	
	private class OnExitPlayerListener implements OnTeleportListener, OnPlayerExitListener
	{
		private boolean _isExit = false;
		
		public OnExitPlayerListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onTeleport(Player player, int x, int y, int z, Reflection reflection)
		{
			if (_isExit)
			{
				return;
			}
			onPlayerExit(player);
		}
		
		@Override
		public void onPlayerExit(final Player player)
		{
			if (player.getBlockCheckerArena() < 0)
			{
				return;
			}
			_isExit = true;
			player.teleToLocation(-57478, -60367, -2370);
			player.setTransformation(0);
			player.getEffectList().stopAllEffects();
			int arena = player.getBlockCheckerArena();
			int team = HandysBlockCheckerManager.getInstance().getHolder(arena).getPlayerTeam(player);
			HandysBlockCheckerManager.getInstance().removePlayer(player, arena, team);
			player.setTeam(TeamType.NONE);
			player.broadcastCharInfo();
			PcInventory inv = player.getInventory();
			inv.destroyItemByItemId(13787, inv.getCountOf(13787));
			inv.destroyItemByItemId(13788, inv.getCountOf(13788));
		}
	}
}
