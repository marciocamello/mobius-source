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
package events.TeamVsTeam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerExitListener;
import lineage2.gameserver.listener.actor.player.OnTeleportListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.impl.DuelEvent;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.network.serverpackets.ExCubeGameAddPlayer;
import lineage2.gameserver.network.serverpackets.ExCubeGameChangePoints;
import lineage2.gameserver.network.serverpackets.ExCubeGameCloseUI;
import lineage2.gameserver.network.serverpackets.ExCubeGameEnd;
import lineage2.gameserver.network.serverpackets.ExCubeGameExtendedChangePoints;
import lineage2.gameserver.network.serverpackets.ExCubeGameRemovePlayer;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.Revive;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.DoorTemplate;
import lineage2.gameserver.templates.ZoneTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;
import lineage2.gameserver.utils.ReflectionUtils;

import org.apache.commons.lang3.mutable.MutableInt;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.trove.map.hash.TIntObjectHashMap;

public class TeamVsTeam extends Functions implements ScriptFile, OnDeathListener, OnTeleportListener, OnPlayerExitListener
{
	
	private static final Logger _log = LoggerFactory.getLogger(TeamVsTeam.class);
	
	private static ScheduledFuture<?> _startTask;
	
	private static final int[] doors = new int[]
	{
		24190001,
		24190002,
		24190003,
		24190004
	};
	
	private static List<Long> players_list1 = new CopyOnWriteArrayList<>();
	private static List<Long> players_list2 = new CopyOnWriteArrayList<>();
	static List<Long> live_list1 = new CopyOnWriteArrayList<>();
	static List<Long> live_list2 = new CopyOnWriteArrayList<>();
	
	private static int[][] mage_buffs = new int[Config.EVENT_TvTMageBuffs.length][2];
	private static int[][] fighter_buffs = new int[Config.EVENT_TvTFighterBuffs.length][2];
	private static long _startedTime = 0;
	
	private static int[][] rewards = new int[Config.EVENT_TvTRewards.length][2];
	
	private static Map<Long, Location> playerRestoreCoord = new LinkedHashMap<>();
	
	private static Map<Long, String> boxes = new LinkedHashMap<>();
	
	private static boolean _isRegistrationActive = false;
	static int _status = 0;
	private static int _time_to_start;
	private static int _category;
	private static int _minLevel;
	private static int _maxLevel;
	private static int _autoContinue = 0;
	static boolean _active = false;
	private static Skill buff;
	
	static Reflection reflection = ReflectionManager.TVT_EVENT;
	
	private static ScheduledFuture<?> _endTask;
	
	private static Zone _zone;
	private static Zone _zone1;
	private static Zone _myZone = null;
	private static Territory territory = null;
	private static Map<Integer, Integer> _pScore = new HashMap<>();
	private static Map<String, ZoneTemplate> _zones = new HashMap<>();
	private static IntObjectMap<DoorTemplate> _doors = new HashIntObjectMap<>();
	private static ZoneListener _zoneListener = new ZoneListener();
	
	private static int bluePoints = 0;
	private static int redPoints = 0;
	private static TIntObjectHashMap<MutableInt> score = new TIntObjectHashMap<>();
	
	private static long getStarterTime()
	{
		return _startedTime;
	}
	
	private static boolean isActive()
	{
		return _active;
	}
	
	public void activateEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		
		if (!isActive())
		{
			if (_startTask == null)
			{
				scheduleEventStart();
			}
			
			ServerVariables.set("TvT", "on");
			_log.info("Event 'TvT' activated.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.TvT.AnnounceEventStarted", null);
		}
		else
		{
			player.sendMessage("Event 'TvT' already active.");
		}
		
		_active = true;
		
		show("admin/events.htm", player);
	}
	
	public void deactivateEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		
		if (isActive())
		{
			if (_startTask != null)
			{
				_startTask.cancel(false);
				_startTask = null;
			}
			ServerVariables.unset("TvT");
			_log.info("Event 'TvT' deactivated.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.TvT.AnnounceEventStoped", null);
		}
		else
		{
			player.sendMessage("Event 'TvT' not active.");
		}
		
		_active = false;
		
		show("admin/events.htm", player);
	}
	
	public static boolean isRunned()
	{
		return _isRegistrationActive || (_status > 0);
	}
	
	public static int getMinLevelForCategory(int category)
	{
		switch (category)
		{
			case 1:
				return 20;
			case 2:
				return 30;
			case 3:
				return 40;
			case 4:
				return 52;
			case 5:
				return 62;
			case 6:
				return 76;
		}
		return 0;
	}
	
	public static int getMaxLevelForCategory(int category)
	{
		switch (category)
		{
			case 1:
				return 29;
			case 2:
				return 39;
			case 3:
				return 51;
			case 4:
				return 61;
			case 5:
				return 75;
			case 6:
				return 99;
		}
		return 0;
	}
	
	public static int getCategory(int level)
	{
		if ((level >= 20) && (level <= 29))
		{
			return 1;
		}
		else if ((level >= 30) && (level <= 39))
		{
			return 2;
		}
		else if ((level >= 40) && (level <= 51))
		{
			return 3;
		}
		else if ((level >= 52) && (level <= 61))
		{
			return 4;
		}
		else if ((level >= 62) && (level <= 75))
		{
			return 5;
		}
		else if (level >= 76)
		{
			return 6;
		}
		return 0;
	}
	
	public void start(String[] var)
	{
		Player player = getSelf();
		if (var.length != 2)
		{
			show(new CustomMessage("common.Error", player), player);
			return;
		}
		
		Integer category;
		Integer autoContinue;
		try
		{
			category = Integer.valueOf(var[0]);
			autoContinue = Integer.valueOf(var[1]);
		}
		catch (Exception e)
		{
			show(new CustomMessage("common.Error", player), player);
			return;
		}
		
		_category = category;
		_autoContinue = autoContinue;
		
		if (_category == -1)
		{
			_minLevel = 1;
			_maxLevel = 99;
		}
		else
		{
			_minLevel = getMinLevelForCategory(_category);
			_maxLevel = getMaxLevelForCategory(_category);
		}
		
		if (_endTask != null)
		{
			show(new CustomMessage("common.TryLater", player), player);
			return;
		}
		
		_status = 0;
		_isRegistrationActive = true;
		_time_to_start = Config.EVENT_TvTTime;
		
		players_list1 = new CopyOnWriteArrayList<>();
		players_list2 = new CopyOnWriteArrayList<>();
		live_list1 = new CopyOnWriteArrayList<>();
		live_list2 = new CopyOnWriteArrayList<>();
		
		playerRestoreCoord = new LinkedHashMap<>();
		
		String[] param =
		{
			String.valueOf(_time_to_start),
			String.valueOf(_minLevel),
			String.valueOf(_maxLevel)
		};
		sayToAll("scripts.events.TvT.AnnouncePreStart", param);
		
		executeTask("events.TeamVsTeam.TeamVsTeam", "question", new Object[0], 10000);
		executeTask("events.TeamVsTeam.TeamVsTeam", "announce", new Object[0], 60000);
	}
	
	public static void sayToAll(String address, String[] replacements)
	{
		Announcements.getInstance().announceByCustomMessage(address, replacements, ChatType.CRITICAL_ANNOUNCE);
	}
	
	public static void question()
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if ((player != null) && !player.isDead() && (player.getLevel() >= _minLevel) && (player.getLevel() <= _maxLevel) && player.getReflection().isDefault() && !player.isInOlympiadMode() && !player.isInObserverMode())
			{
				player.scriptRequest(new CustomMessage("scripts.events.TvT.AskPlayer", player).toString(), "events.TeamVsTeam.TeamVsTeam:addPlayer", new Object[0]);
			}
		}
	}
	
	public static void announce()
	{
		if (_time_to_start > 1)
		{
			_time_to_start--;
			String[] param =
			{
				String.valueOf(_time_to_start),
				String.valueOf(_minLevel),
				String.valueOf(_maxLevel)
			};
			sayToAll("scripts.events.TvT.AnnouncePreStart", param);
			executeTask("events.TeamVsTeam.TeamVsTeam", "announce", new Object[0], 60000);
		}
		else
		{
			if (players_list1.isEmpty() || players_list2.isEmpty() || (players_list1.size() < Config.EVENT_TvTMinPlayerInTeam) || (players_list2.size() < Config.EVENT_TvTMinPlayerInTeam))
			{
				sayToAll("scripts.events.TvT.AnnounceEventCancelled", null);
				_isRegistrationActive = false;
				_status = 0;
				executeTask("events.TeamVsTeam.TeamVsTeam", "autoContinue", new Object[0], 10000);
				boxes.clear();
				return;
			}
			_status = 1;
			_isRegistrationActive = false;
			sayToAll("scripts.events.TvT.AnnounceEventStarting", null);
			executeTask("events.TeamVsTeam.TeamVsTeam", "prepare", new Object[0], 5000);
		}
	}
	
	public void addPlayer()
	{
		Player player = getSelf();
		if ((player == null) || !checkPlayer(player, true) || !checkDualBox(player))
		{
			return;
		}
		
		int team = 0, size1 = players_list1.size(), size2 = players_list2.size();
		
		if ((size1 == Config.EVENT_TvTMaxPlayerInTeam) && (size2 == Config.EVENT_TvTMaxPlayerInTeam))
		{
			show(new CustomMessage("scripts.events.TvT.CancelledCount", player), player);
			_isRegistrationActive = false;
			return;
		}
		
		if (!Config.EVENT_TvTAllowMultiReg)
		{
			if ("IP".equals(Config.EVENT_TvTCheckWindowMethod))
			{
				boxes.put(player.getStoredId(), player.getIP());
				// if("HWid".equals(Config.EVENT_TvTCheckWindowMethod))
				// boxes.put(player.getStoredId(), player.getNetConnection().getHWID());
			}
		}
		
		if (size1 > size2)
		{
			team = 2;
		}
		else if (size1 < size2)
		{
			team = 1;
		}
		else
		{
			team = Rnd.get(1, 2);
		}
		
		if (team == 1)
		{
			players_list1.add(player.getStoredId());
			live_list1.add(player.getStoredId());
			show(new CustomMessage("scripts.events.TvT.Registered", player), player);
		}
		else if (team == 2)
		{
			players_list2.add(player.getStoredId());
			live_list2.add(player.getStoredId());
			show(new CustomMessage("scripts.events.TvT.Registered", player), player);
		}
		else
		{
			_log.info("WTF??? Command id 0 in TvT...");
		}
		player.setRegisteredInEvent(true);
	}
	
	public static boolean checkPlayer(Player player, boolean first)
	{
		
		if (first && (!_isRegistrationActive || player.isDead()))
		{
			show(new CustomMessage("scripts.events.Late", player), player);
			return false;
		}
		
		if (first && (players_list1.contains(player.getStoredId()) || players_list2.contains(player.getStoredId())))
		{
			player.setRegisteredInEvent(false);
			show(new CustomMessage("scripts.events.TvT.Cancelled", player), player);
			if (players_list1.contains(player.getStoredId()))
			{
				players_list1.remove(player.getStoredId());
			}
			if (players_list2.contains(player.getStoredId()))
			{
				players_list2.remove(player.getStoredId());
			}
			if (live_list1.contains(player.getStoredId()))
			{
				live_list1.remove(player.getStoredId());
			}
			if (live_list2.contains(player.getStoredId()))
			{
				live_list2.remove(player.getStoredId());
			}
			if (boxes.containsKey(player.getStoredId()))
			{
				boxes.remove(player.getStoredId());
			}
			return false;
		}
		
		if ((player.getLevel() < _minLevel) || (player.getLevel() > _maxLevel))
		{
			show(new CustomMessage("scripts.events.TvT.CancelledLevel", player), player);
			return false;
		}
		
		if (player.isMounted())
		{
			show(new CustomMessage("scripts.events.TvT.Cancelled", player), player);
			return false;
		}
		
		if (player.isCursedWeaponEquipped())
		{
			show(new CustomMessage("scripts.events.CtF.Cancelled", player), player);
			return false;
		}
		
		if (player.isInDuel())
		{
			show(new CustomMessage("scripts.events.TvT.CancelledDuel", player), player);
			return false;
		}
		
		if (player.getTeam() != TeamType.NONE)
		{
			show(new CustomMessage("scripts.events.TvT.CancelledOtherEvent", player), player);
			return false;
		}
		
		if ((player.getOlympiadGame() != null) || (first && Olympiad.isRegistered(player)))
		{
			show(new CustomMessage("scripts.events.TvT.CancelledOlympiad", player), player);
			return false;
		}
		
		if (player.isInObserverMode())
		{
			show(new CustomMessage("scripts.event.TvT.CancelledObserver", player), player);
			return false;
		}
		
		if (player.isTeleporting())
		{
			show(new CustomMessage("scripts.events.TvT.CancelledTeleport", player), player);
			return false;
		}
		return true;
	}
	
	public static void prepare()
	{
		for (DoorInstance door : reflection.getDoors())
		{
			door.openMe();
		}
		
		for (Zone z : reflection.getZones())
		{
			z.setType(ZoneType.Peace);
		}
		
		cleanPlayers();
		executeTask("events.TeamVsTeam.TeamVsTeam", "ressurectPlayers", new Object[0], 1000);
		executeTask("events.TeamVsTeam.TeamVsTeam", "healPlayers", new Object[0], 2000);
		executeTask("events.TeamVsTeam.TeamVsTeam", "teleportPlayersToColiseum", new Object[0], 3000);
		executeTask("events.TeamVsTeam.TeamVsTeam", "paralyzePlayers", new Object[0], 4000);
		executeTask("events.TeamVsTeam.TeamVsTeam", "buffPlayers", new Object[0], 5000);
		executeTask("events.TeamVsTeam.TeamVsTeam", "go", new Object[0], 60000);
		
		sayToAll("scripts.events.TvT.AnnounceFinalCountdown", null);
	}
	
	public static void go()
	{
		_status = 2;
		upParalyzePlayers();
		checkLive();
		sayToAll("scripts.events.TvT.AnnounceFight", null);
		for (Zone z : reflection.getZones())
		{
			z.setType(ZoneType.Battle);
		}
		_endTask = executeTask("events.TeamVsTeam.TeamVsTeam", "endBattle", new Object[0], 600000);
		_startedTime = System.currentTimeMillis() + 600000;
		
		final ExCubeGameChangePoints initialPoints = new ExCubeGameChangePoints(600, bluePoints, redPoints);
		final ExCubeGameCloseUI cui = new ExCubeGameCloseUI();
		ExCubeGameExtendedChangePoints clientSetUp;
		
		for (Player player : getPlayers(players_list1))
		{
			if (player == null)
			{
				continue;
			}
			
			score.put(player.getObjectId(), new MutableInt());
			
			player.setCurrentCp(player.getMaxCp());
			player.setCurrentHp(player.getMaxHp(), false);
			player.setCurrentMp(player.getMaxMp());
			
			clientSetUp = new ExCubeGameExtendedChangePoints(600, bluePoints, redPoints, isRedTeam(player), player, 0);
			player.sendPacket(clientSetUp);
			player.sendActionFailed();
			player.sendPacket(initialPoints);
			player.sendPacket(cui);
			player.broadcastCharInfo();
			for (Player player1 : getPlayers(players_list1))
			{
				player1.sendPacket(new ExCubeGameAddPlayer(player, isRedTeam(player)));
			}
			for (Player player2 : getPlayers(players_list2))
			{
				player2.sendPacket(new ExCubeGameAddPlayer(player, isRedTeam(player)));
			}
		}
		
		for (Player player2 : getPlayers(players_list2))
		{
			if (player2 == null)
			{
				continue;
			}
			
			score.put(player2.getObjectId(), new MutableInt());
			
			player2.setCurrentCp(player2.getMaxCp());
			player2.setCurrentHp(player2.getMaxHp(), false);
			player2.setCurrentMp(player2.getMaxMp());
			
			clientSetUp = new ExCubeGameExtendedChangePoints(600, bluePoints, redPoints, isRedTeam(player2), player2, 0);
			player2.sendPacket(clientSetUp);
			player2.sendActionFailed();
			player2.sendPacket(initialPoints);
			player2.sendPacket(cui);
			player2.broadcastCharInfo();
			player2.sendPacket(new ExCubeGameAddPlayer(player2, isRedTeam(player2)));
			for (@SuppressWarnings("unused")
			Player player : getPlayers(players_list1))
			{
				player2.sendPacket(new ExCubeGameAddPlayer(player2, isRedTeam(player2)));
			}
		}
	}
	
	public static void endBattle()
	{
		_status = 0;
		removeAura();
		for (Zone z : reflection.getZones())
		{
			z.setType(ZoneType.Peace);
		}
		boxes.clear();
		
		if (bluePoints > redPoints)
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedBlueWins", null);
			giveItemsToWinner(false, true, 1);
		}
		else if (bluePoints < redPoints)
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedRedWins", null);
			giveItemsToWinner(true, false, 1);
		}
		else if (bluePoints == redPoints)
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedDraw", null);
			giveItemsToWinner(true, true, 0.5);
		}
		
		sayToAll("scripts.events.TvT.AnnounceEnd", null);
		executeTask("events.TeamVsTeam.TeamVsTeam", "end", new Object[0], 30000);
		_isRegistrationActive = false;
		if (_endTask != null)
		{
			_endTask.cancel(false);
			_endTask = null;
		}
		boolean _isRedWinner = bluePoints < redPoints ? true : false;
		final ExCubeGameEnd end = new ExCubeGameEnd(_isRedWinner);
		
		for (Player player : getPlayers(players_list1))
		{
			player.sendPacket(end);
		}
		
		for (Player player : getPlayers(players_list2))
		{
			player.sendPacket(end);
		}
		bluePoints = 0;
		redPoints = 0;
		_startedTime = 0;
		_myZone = null;
		territory = null;
		score.clear();
	}
	
	public static void end()
	{
		executeTask("events.TeamVsTeam.TeamVsTeam", "ressurectPlayers", new Object[0], 1000);
		executeTask("events.TeamVsTeam.TeamVsTeam", "healPlayers", new Object[0], 2000);
		executeTask("events.TeamVsTeam.TeamVsTeam", "teleportPlayers", new Object[0], 3000);
		executeTask("events.TeamVsTeam.TeamVsTeam", "autoContinue", new Object[0], 10000);
	}
	
	public void autoContinue()
	{
		live_list1.clear();
		live_list2.clear();
		players_list1.clear();
		players_list2.clear();
		
		if (_autoContinue > 0)
		{
			if (_autoContinue >= 6)
			{
				_autoContinue = 0;
				return;
			}
			start(new String[]
			{
				"" + (_autoContinue + 1),
				"" + (_autoContinue + 1)
			});
		}
		else
		{
			scheduleEventStart();
		}
	}
	
	public static void giveItemsToWinner(boolean team1, boolean team2, double rate)
	{
		if (team1)
		{
			for (Player player : getPlayers(players_list1))
			{
				for (int[] reward : rewards)
				{
					addItem(player, reward[0], Math.round((Config.EVENT_TvTrate ? player.getLevel() : 1) * reward[1] * rate));
				}
			}
		}
		if (team2)
		{
			for (Player player : getPlayers(players_list2))
			{
				for (int[] reward : rewards)
				{
					addItem(player, reward[0], Math.round((Config.EVENT_TvTrate ? player.getLevel() : 1) * reward[1] * rate));
				}
			}
		}
	}
	
	public static void teleportPlayersToColiseum()
	{
		switch (2)
		{
			case 1:
				_myZone = _zone;
				break;
			case 2:
				_myZone = _zone1;
				break;
			default:
				_myZone = _zone;
		}
		territory = _myZone.getTerritory();
		
		for (Player player : getPlayers(players_list1))
		{
			unRide(player);
			
			if (!Config.EVENT_TvTAllowSummons)
			{
				unSummonPet(player, true);
			}
			
			DuelEvent duel = player.getEvent(DuelEvent.class);
			if (duel != null)
			{
				duel.abortDuel(player);
			}
			
			playerRestoreCoord.put(player.getStoredId(), new Location(player.getX(), player.getY(), player.getZ()));
			
			player.teleToLocation(Territory.getRandomLoc(territory), reflection);
			player.setIsInTvT(true);
			
			if (!Config.EVENT_TvTAllowBuffs)
			{
				player.getEffectList().stopAllEffects();
			}
		}
		
		for (Player player : getPlayers(players_list2))
		{
			unRide(player);
			
			if (!Config.EVENT_TvTAllowSummons)
			{
				unSummonPet(player, true);
			}
			
			playerRestoreCoord.put(player.getStoredId(), new Location(player.getX(), player.getY(), player.getZ()));
			
			player.teleToLocation(Territory.getRandomLoc(territory), reflection);
			player.setIsInTvT(true);
			
			if (!Config.EVENT_TvTAllowBuffs)
			{
				player.getEffectList().stopAllEffects();
			}
		}
	}
	
	public static void teleportPlayers()
	{
		for (Player player : getPlayers(players_list1))
		{
			if ((player == null) || !playerRestoreCoord.containsKey(player.getStoredId()))
			{
				continue;
			}
			player.teleToLocation(playerRestoreCoord.get(player.getStoredId()), ReflectionManager.DEFAULT);
			player.setRegisteredInEvent(false);
		}
		
		for (Player player : getPlayers(players_list2))
		{
			if ((player == null) || !playerRestoreCoord.containsKey(player.getStoredId()))
			{
				continue;
			}
			player.teleToLocation(playerRestoreCoord.get(player.getStoredId()), ReflectionManager.DEFAULT);
			player.setRegisteredInEvent(false);
		}
		
		playerRestoreCoord.clear();
	}
	
	public static void paralyzePlayers()
	{
		Skill revengeSkill = SkillTable.getInstance().getInfo(Skill.SKILL_RAID_CURSE, 1);
		
		for (Player player : getPlayers(players_list1))
		{
			player.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
			revengeSkill.getEffects(player, player, false, false);
		}
		
		for (Player player : getPlayers(players_list2))
		{
			player.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
			revengeSkill.getEffects(player, player, false, false);
		}
	}
	
	public static void upParalyzePlayers()
	{
		for (Player player : getPlayers(players_list1))
		{
			player.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
			player.leaveParty();
		}
		
		for (Player player : getPlayers(players_list2))
		{
			player.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
			player.leaveParty();
		}
	}
	
	public static void ressurectPlayers()
	{
		for (Player player : getPlayers(players_list1))
		{
			if (player.isDead())
			{
				player.restoreExp();
				player.setCurrentCp(player.getMaxCp());
				player.setCurrentHp(player.getMaxHp(), true);
				player.setCurrentMp(player.getMaxMp());
				player.broadcastPacket(new Revive(player));
			}
		}
		for (Player player : getPlayers(players_list2))
		{
			if (player.isDead())
			{
				player.restoreExp();
				player.setCurrentCp(player.getMaxCp());
				player.setCurrentHp(player.getMaxHp(), true);
				player.setCurrentMp(player.getMaxMp());
				player.broadcastPacket(new Revive(player));
			}
		}
	}
	
	public static void healPlayers()
	{
		for (Player player : getPlayers(players_list1))
		{
			player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
			player.setCurrentCp(player.getMaxCp());
		}
		for (Player player : getPlayers(players_list2))
		{
			player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
			player.setCurrentCp(player.getMaxCp());
		}
	}
	
	public static void cleanPlayers()
	{
		for (Player player : getPlayers(players_list1))
		{
			if (!checkPlayer(player, false))
			{
				removePlayer(player);
			}
		}
		for (Player player : getPlayers(players_list2))
		{
			if (!checkPlayer(player, false))
			{
				removePlayer(player);
			}
		}
	}
	
	public static void checkLive()
	{
		List<Long> new_live_list1 = new CopyOnWriteArrayList<>();
		List<Long> new_live_list2 = new CopyOnWriteArrayList<>();
		
		for (Long storeId : live_list1)
		{
			Player player = GameObjectsStorage.getAsPlayer(storeId);
			if (player != null)
			{
				new_live_list1.add(storeId);
			}
		}
		
		for (Long storeId : live_list2)
		{
			Player player = GameObjectsStorage.getAsPlayer(storeId);
			if (player != null)
			{
				new_live_list2.add(storeId);
			}
		}
		
		live_list1 = new_live_list1;
		live_list2 = new_live_list2;
		
		for (Player player : getPlayers(live_list1))
		{
			if (!player.isDead() && !player.isLogoutStarted())
			{
				player.setTeam(TeamType.RED);
			}
			else
			{
				removePlayer(player);
			}
		}
		
		for (Player player : getPlayers(live_list2))
		{
			if (!player.isDead() && !player.isLogoutStarted())
			{
				player.setTeam(TeamType.BLUE);
			}
			else
			{
				removePlayer(player);
			}
		}
		
		if ((live_list1.size() < 1) || (live_list2.size() < 1))
		{
			endBattle();
		}
	}
	
	public static void removeAura()
	{
		for (Player player : getPlayers(live_list1))
		{
			player.setTeam(TeamType.NONE);
			player.setIsInTvT(false);
		}
		for (Player player : getPlayers(live_list2))
		{
			player.setTeam(TeamType.NONE);
			player.setIsInTvT(false);
		}
	}
	
	@Override
	public void onDeath(Creature self, Creature killer)
	{
		if ((_status > 1) && self.isPlayer() && (self.getTeam() != TeamType.NONE) && (live_list1.contains(self.getStoredId()) || live_list2.contains(self.getStoredId())))
		{
			checkKillsAndAnnounce(killer.getPlayer());
			increasePoints(killer);
			resurrectAtBase(self);
			_pScore.remove(self.getPlayer().getObjectId());
		}
		
	}
	
	private static void checkKillsAndAnnounce(Player player)
	{
		if ((player == null) || (_pScore == null))
		{
			return;
		}
		int score1 = 0;
		if (_pScore.get(player.getObjectId()) != null)
		{
			score1 = _pScore.get(player.getObjectId());
		}
		_pScore.put(player.getObjectId(), score1 + 1);
		
		MutableInt points = score.get(player.getObjectId());
		points.increment();
		
		String text = "";
		
		switch (_pScore.get(player.getObjectId()))
		{
			case 0:
			case 1:
				return;
			case 10:
				text = "" + player.getName() + ": Killing Spree";
				break;
			case 20:
				text = "" + player.getName() + ": Rampage";
				break;
			case 30:
				text = "" + player.getName() + ": Unstoppable";
				break;
			case 40:
				text = "" + player.getName() + ": Dominating";
				break;
			case 50:
				text = "" + player.getName() + ": Godlike";
				break;
			case 60:
				text = "" + player.getName() + ": Legendary";
				break;
			case 70:
				text = "" + player.getName() + ": Arena Master";
				break;
			case 80:
				text = "" + player.getName() + ": Best Player";
				break;
			default:
				return;
		}
		for (Player player1 : getPlayers(players_list1))
		{
			player1.sendPacket(new ExShowScreenMessage(text, 3000, ScreenMessageAlign.TOP_CENTER, true));
		}
		
		for (Player player2 : getPlayers(players_list2))
		{
			player2.sendPacket(new ExShowScreenMessage(text, 3000, ScreenMessageAlign.TOP_CENTER, true));
		}
	}
	
	public static void resurrectAtBase(Creature self)
	{
		Player player = self.getPlayer();
		if (player == null)
		{
			return;
		}
		if (player.getTeam() == TeamType.NONE)
		{
			return;
		}
		if (player.isDead())
		{
			player.setCurrentCp(player.getMaxCp());
			player.setCurrentHp(player.getMaxHp(), true);
			player.setCurrentMp(player.getMaxMp());
			player.broadcastPacket(new Revive(player));
			buffPlayer(player);
		}
		player.teleToLocation(Territory.getRandomLoc(territory), reflection);
	}
	
	public static void buffPlayer(Player player)
	{
		if (player.isMageClass())
		{
			mageBuff(player);
		}
		else
		{
			fighterBuff(player);
		}
	}
	
	private static void increasePoints(Creature killer)
	{
		Player player = killer.getPlayer();
		if (player == null)
		{
			return;
		}
		if (player.getTeam() == TeamType.BLUE)
		{
			bluePoints++;
		}
		else
		{
			redPoints++;
		}
		
		int timeLeft = (int) ((getStarterTime() - System.currentTimeMillis()) / 1000);
		
		if (player.getTeam() == TeamType.RED)
		{
			for (Player player1 : getPlayers(players_list1))
			{
				player1.sendPacket(new ExCubeGameExtendedChangePoints(timeLeft, bluePoints, redPoints, true, player, getPlayerScore(player)));
			}
			for (Player player2 : getPlayers(players_list2))
			{
				player2.sendPacket(new ExCubeGameExtendedChangePoints(timeLeft, bluePoints, redPoints, true, player, getPlayerScore(player)));
			}
			for (Player player1 : getPlayers(players_list1))
			{
				player1.sendPacket(new ExCubeGameExtendedChangePoints(timeLeft, bluePoints, redPoints, false, player, getPlayerScore(player)));
			}
			for (Player player2 : getPlayers(players_list2))
			{
				player2.sendPacket(new ExCubeGameExtendedChangePoints(timeLeft, bluePoints, redPoints, false, player, getPlayerScore(player)));
			}
		}
		else
		{
			for (Player player1 : getPlayers(players_list1))
			{
				player1.sendPacket(new ExCubeGameExtendedChangePoints(timeLeft, bluePoints, redPoints, false, player, getPlayerScore(player)));
			}
			for (Player player2 : getPlayers(players_list2))
			{
				player2.sendPacket(new ExCubeGameExtendedChangePoints(timeLeft, bluePoints, redPoints, false, player, getPlayerScore(player)));
			}
			for (Player player1 : getPlayers(players_list1))
			{
				player1.sendPacket(new ExCubeGameExtendedChangePoints(timeLeft, bluePoints, redPoints, true, player, getPlayerScore(player)));
			}
			for (Player player2 : getPlayers(players_list2))
			{
				player2.sendPacket(new ExCubeGameExtendedChangePoints(timeLeft, bluePoints, redPoints, true, player, getPlayerScore(player)));
			}
		}
	}
	
	@Override
	public void onTeleport(Player player, int x, int y, int z, Reflection reflection)
	{
	}
	
	@Override
	public void onPlayerExit(Player player)
	{
		if (player.getTeam() == TeamType.NONE)
		{
			return;
		}
		
		if ((_status == 0) && _isRegistrationActive && (player.getTeam() != TeamType.NONE) && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId())))
		{
			removePlayer(player);
			return;
		}
		
		if ((_status == 1) && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId())))
		{
			player.teleToLocation(playerRestoreCoord.get(player.getStoredId()), ReflectionManager.DEFAULT);
			removePlayer(player);
			return;
		}
		
		if ((_status > 1) && (player.getTeam() != TeamType.NONE) && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId())))
		{
			removePlayer(player);
			checkLive();
		}
	}
	
	private static class ZoneListener implements OnZoneEnterLeaveListener
	{
		public ZoneListener()
		{
		}
		
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
			if (cha == null)
			{
				return;
			}
			Player player = cha.getPlayer();
			if ((_status > 1) && (player != null) && (player.getTeam() != TeamType.NONE) && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId())))
			{
				double angle = PositionUtils.convertHeadingToDegree(cha.getHeading());
				double radian = Math.toRadians(angle - 90);
				int x = (int) (cha.getX() + (250 * Math.sin(radian)));
				int y = (int) (cha.getY() - (250 * Math.cos(radian)));
				int z = cha.getZ();
				player.teleToLocation(x, y, z, reflection);
			}
		}
	}
	
	private static void removePlayer(Player player)
	{
		if (player != null)
		{
			live_list1.remove(player.getStoredId());
			live_list2.remove(player.getStoredId());
			players_list1.remove(player.getStoredId());
			players_list2.remove(player.getStoredId());
			// playerRestoreCoord.remove(player.getStoredId());
			player.setIsInTvT(false);
			
			if (!Config.EVENT_TvTAllowMultiReg)
			{
				boxes.remove(player.getStoredId());
			}
			
			for (Player player1 : getPlayers(players_list1))
			{
				player1.sendPacket(new ExCubeGameRemovePlayer(player, player.getTeam() == TeamType.RED ? true : false));
			}
			
			for (Player player2 : getPlayers(players_list2))
			{
				player2.sendPacket(new ExCubeGameRemovePlayer(player, player.getTeam() == TeamType.RED ? true : false));
			}
			
			player.sendPacket(new ExCubeGameEnd(false));
			player.setTeam(TeamType.NONE);
			player.teleToLocation(147451, 46728, -3410, ReflectionManager.DEFAULT);
		}
	}
	
	private static List<Player> getPlayers(List<Long> list)
	{
		List<Player> result = new ArrayList<>();
		for (Long storeId : list)
		{
			Player player = GameObjectsStorage.getAsPlayer(storeId);
			if (player != null)
			{
				result.add(player);
			}
		}
		return result;
	}
	
	public static void buffPlayers()
	{
		
		for (Player player : getPlayers(players_list1))
		{
			if (player.isMageClass())
			{
				mageBuff(player);
			}
			else
			{
				fighterBuff(player);
			}
		}
		
		for (Player player : getPlayers(players_list2))
		{
			if (player.isMageClass())
			{
				mageBuff(player);
			}
			else
			{
				fighterBuff(player);
			}
		}
	}
	
	public void scheduleEventStart()
	{
		try
		{
			Calendar currentTime = Calendar.getInstance();
			Calendar nextStartTime = null;
			Calendar testStartTime = null;
			
			for (String timeOfDay : Config.EVENT_TvTStartTime)
			{
				testStartTime = Calendar.getInstance();
				testStartTime.setLenient(true);
				
				String[] splitTimeOfDay = timeOfDay.split(":");
				
				testStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTimeOfDay[0]));
				testStartTime.set(Calendar.MINUTE, Integer.parseInt(splitTimeOfDay[1]));
				
				if (testStartTime.getTimeInMillis() < currentTime.getTimeInMillis())
				{
					testStartTime.add(Calendar.DAY_OF_MONTH, 1);
				}
				
				if ((nextStartTime == null) || (testStartTime.getTimeInMillis() < nextStartTime.getTimeInMillis()))
				{
					nextStartTime = testStartTime;
				}
				
				if (_startTask != null)
				{
					_startTask.cancel(false);
					_startTask = null;
				}
				_startTask = ThreadPoolManager.getInstance().schedule(new StartTask(), nextStartTime.getTimeInMillis() - System.currentTimeMillis());
				
			}
			
			currentTime = null;
			nextStartTime = null;
			testStartTime = null;
			
		}
		catch (Exception e)
		{
			_log.warn("TvT: Error figuring out a start time. Check TvT_StartTime in config file.");
		}
	}
	
	public static void mageBuff(Player player)
	{
		for (int[] mage_buff : mage_buffs)
		{
			buff = SkillTable.getInstance().getInfo(mage_buff[0], mage_buff[1]);
			if (buff == null)
			{
				return;
			}
			buff.getEffects(player, player, false, false);
		}
	}
	
	public static void fighterBuff(Player player)
	{
		for (int[] fighter_buff : fighter_buffs)
		{
			buff = SkillTable.getInstance().getInfo(fighter_buff[0], fighter_buff[1]);
			if (buff == null)
			{
				return;
			}
			buff.getEffects(player, player, false, false);
		}
	}
	
	private static boolean checkDualBox(Player player)
	{
		if (!Config.EVENT_TvTAllowMultiReg)
		{
			if ("IP".equals(Config.EVENT_TvTCheckWindowMethod))
			{
				if (boxes.containsValue(player.getIP()))
				{
					show(new CustomMessage("scripts.events.TvT.CancelledBox", player), player);
					return false;
				}
			}
			// else if ("HWid".equals(Config.EVENT_TvTCheckWindowMethod))
			// {
			// if (boxes.containsValue(player.getNetConnection().getHWID()))
			// {
			// show(new CustomMessage("scripts.events.TvT.CancelledBox", player), player);
			// return false;
			// }
			// }
		}
		return true;
	}
	
	public class StartTask extends RunnableImpl
	{
		
		@Override
		public void runImpl()
		{
			if (!_active)
			{
				return;
			}
			
			if (isPvPEventStarted())
			{
				_log.info("TvT not started: another event is already running");
				return;
			}
			
			for (Residence c : ResidenceHolder.getInstance().getResidenceList(Castle.class))
			{
				if ((c.getSiegeEvent() != null) && c.getSiegeEvent().isInProgress())
				{
					_log.debug("TvT not started: CastleSiege in progress");
					return;
				}
			}
			
			if (Config.EVENT_TvTCategories)
			{
				start(new String[]
				{
					"1",
					"1"
				});
			}
			else
			{
				start(new String[]
				{
					"-1",
					"-1"
				});
			}
		}
	}
	
	/**
	 * @param player
	 * @return Returns personal player score
	 */
	public static int getPlayerScore(Player player)
	{
		MutableInt points = score.get(player.getObjectId());
		return points.intValue();
	}
	
	private static boolean isRedTeam(Player player)
	{
		if (player.getTeam() == TeamType.RED)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
		
		_zones.put("[colosseum_battle]", ReflectionUtils.getZone("[colosseum_battle]").getTemplate());
		_zones.put("[cleft_tvt]", ReflectionUtils.getZone("[cleft_tvt]").getTemplate());
		_zones.put("[cleft_tvt]", ReflectionUtils.getZone("[cleft_tvt]").getTemplate());
		
		for (final int doorId : doors)
		{
			_doors.put(doorId, ReflectionUtils.getDoor(doorId).getTemplate());
		}
		
		int geoIndex = GeoEngine.NextGeoIndex(24, 19, reflection.getId());
		reflection.setGeoIndex(geoIndex);
		reflection.init(_doors, _zones);
		
		_zone = reflection.getZone("[cleft_tvt]");
		_zone1 = reflection.getZone("[cleft_tvt]");
		_active = ServerVariables.getString("TvT", "off").equals("on");
		if (isActive())
		{
			scheduleEventStart();
		}
		
		_zone.addListener(_zoneListener);
		_zone1.addListener(_zoneListener);
		
		int i = 0;
		
		if (Config.EVENT_TvTBuffPlayers && (Config.EVENT_TvTMageBuffs.length != 0))
		{
			for (String skill : Config.EVENT_TvTMageBuffs)
			{
				String[] splitSkill = skill.split(",");
				mage_buffs[i][0] = Integer.parseInt(splitSkill[0]);
				mage_buffs[i][1] = Integer.parseInt(splitSkill[1]);
				i++;
			}
		}
		
		i = 0;
		
		if (Config.EVENT_TvTBuffPlayers && (Config.EVENT_TvTMageBuffs.length != 0))
		{
			for (String skill : Config.EVENT_TvTFighterBuffs)
			{
				String[] splitSkill = skill.split(",");
				fighter_buffs[i][0] = Integer.parseInt(splitSkill[0]);
				fighter_buffs[i][1] = Integer.parseInt(splitSkill[1]);
				i++;
			}
		}
		
		i = 0;
		if (Config.EVENT_TvTRewards.length != 0)
		{
			for (String reward : Config.EVENT_TvTRewards)
			{
				String[] splitReward = reward.split(",");
				rewards[i][0] = Integer.parseInt(splitReward[0]);
				rewards[i][1] = Integer.parseInt(splitReward[1]);
				i++;
			}
		}
		
		_log.info("Loaded Event: TvT");
	}
	
	@Override
	public void onReload()
	{
		if (_startTask != null)
		{
			_startTask.cancel(false);
			_startTask = null;
		}
	}
	
	@Override
	public void onShutdown()
	{
		onReload();
	}
}