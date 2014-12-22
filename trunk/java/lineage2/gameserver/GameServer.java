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
package lineage2.gameserver;

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;

import lineage2.commons.lang.StatsUtils;
import lineage2.commons.listener.Listener;
import lineage2.commons.listener.ListenerList;
import lineage2.commons.net.nio.impl.SelectorThread;
import lineage2.commons.versioning.Version;
import lineage2.gameserver.cache.CrestCache;
import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.dao.ItemsDAO;
import lineage2.gameserver.data.BoatHolder;
import lineage2.gameserver.data.xml.Parsers;
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.data.xml.holder.StaticObjectHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.handlers.AdminCommandHandler;
import lineage2.gameserver.handlers.ItemHandler;
import lineage2.gameserver.handlers.UserCommandHandler;
import lineage2.gameserver.handlers.VoicedCommandHandler;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.instancemanager.ArcanManager;
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.instancemanager.CoupleManager;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.instancemanager.DelusionChamberManager;
import lineage2.gameserver.instancemanager.FindPartyManager;
import lineage2.gameserver.instancemanager.HarnakUndegroundManager;
import lineage2.gameserver.instancemanager.L2TopManager;
import lineage2.gameserver.instancemanager.MMOTopManager;
import lineage2.gameserver.instancemanager.ParnassusManager;
import lineage2.gameserver.instancemanager.PetitionManager;
import lineage2.gameserver.instancemanager.PlayerMessageStack;
import lineage2.gameserver.instancemanager.RaidBossSpawnManager;
import lineage2.gameserver.instancemanager.SMSWayToPay;
import lineage2.gameserver.instancemanager.SoDManager;
import lineage2.gameserver.instancemanager.SoHManager;
import lineage2.gameserver.instancemanager.SoIManager;
import lineage2.gameserver.instancemanager.SpawnManager;
import lineage2.gameserver.instancemanager.commission.CommissionShopManager;
import lineage2.gameserver.instancemanager.games.FishingChampionShipManager;
import lineage2.gameserver.instancemanager.games.LotteryManager;
import lineage2.gameserver.instancemanager.games.MiniGameScoreManager;
import lineage2.gameserver.instancemanager.itemauction.ItemAuctionManager;
import lineage2.gameserver.instancemanager.naia.NaiaCoreManager;
import lineage2.gameserver.instancemanager.naia.NaiaTowerManager;
import lineage2.gameserver.listener.GameListener;
import lineage2.gameserver.listener.game.OnStartListener;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.model.entity.MonsterRace;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.items.etcitems.AttributeStoneManager;
import lineage2.gameserver.model.items.etcitems.CrystallizationManager;
import lineage2.gameserver.model.items.etcitems.EnchantScrollManager;
import lineage2.gameserver.model.items.etcitems.LifeStoneManager;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.GamePacketHandler;
import lineage2.gameserver.network.loginservercon.LoginServerCommunication;
import lineage2.gameserver.scripts.Scripts;
import lineage2.gameserver.tables.AugmentationData;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.tables.DualClassTable;
import lineage2.gameserver.tables.EnchantHPBonusTable;
import lineage2.gameserver.tables.FakePlayersTable;
import lineage2.gameserver.tables.PetSkillsTable;
import lineage2.gameserver.tables.SkillTreeTable;
import lineage2.gameserver.tables.SubClassTable;
import lineage2.gameserver.taskmanager.ItemsAutoDestroy;
import lineage2.gameserver.taskmanager.TaskManager;
import lineage2.gameserver.taskmanager.tasks.RestoreOfflineTraders;
import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GameServer
{
	public static final int LOGIN_SERVER_PROTOCOL = 2;
	private static final Logger _log = LoggerFactory.getLogger(GameServer.class);
	
	private class GameServerListenerList extends ListenerList<GameServer>
	{
		public GameServerListenerList()
		{
		}
		
		/**
		 * Method onStart.
		 */
		void onStart()
		{
			for (Listener<GameServer> listener : getListeners())
			{
				if (OnStartListener.class.isInstance(listener))
				{
					((OnStartListener) listener).onStart();
				}
			}
		}
	}
	
	private static GameServer _instance;
	private final SelectorThread<GameClient> _selectorThreads[];
	private final Version version;
	private final GameServerListenerList _listeners;
	private final int _serverStarted;
	
	/**
	 * Method getSelectorThreads.
	 * @return SelectorThread<GameClient>[]
	 */
	public SelectorThread<GameClient>[] getSelectorThreads()
	{
		return _selectorThreads;
	}
	
	/**
	 * Method time.
	 * @return int
	 */
	private int time()
	{
		return (int) (System.currentTimeMillis() / 1000);
	}
	
	/**
	 * Method uptime.
	 * @return int
	 */
	int uptime()
	{
		return time() - _serverStarted;
	}
	
	/**
	 * Constructor for GameServer.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public GameServer() throws Exception
	{
		_instance = this;
		_serverStarted = time();
		_listeners = new GameServerListenerList();
		new File("./log/").mkdir();
		version = new Version(GameServer.class);
		_log.info("==============================================================");
		// _log.info("Revision: ................ " + version.getRevisionNumber());
		_log.info("Build date: .............. " + version.getBuildDate());
		_log.info("Compiler version: ........ " + version.getBuildJdk());
		_log.info("==============================================================");
		Config.load();
		checkFreePorts();
		Class.forName(Config.DATABASE_DRIVER).newInstance();
		DatabaseFactory.getInstance().getConnection().close();
		IdFactory _idFactory = IdFactory.getInstance();
		if (!_idFactory.isInitialized())
		{
			_log.error("Could not read object IDs from DB. Please Check Your Data.");
			throw new Exception("Could not initialize the ID factory");
		}
		CacheManager.getInstance();
		ThreadPoolManager.getInstance();
		GameTimeController.getInstance();
		GeoEngine.load();
		World.init();
		Scripts.getInstance();
		Parsers.parseAll();
		ItemsDAO.getInstance();
		AttributeStoneManager.load();
		LifeStoneManager.load();
		EnchantScrollManager.load();
		CrystallizationManager.load();
		CrestCache.getInstance();
		CharacterDAO.getInstance();
		ClanTable.getInstance();
		FakePlayersTable.getInstance();
		EnchantHPBonusTable.getInstance();
		SkillTreeTable.getInstance();
		PetSkillsTable.getInstance();
		AugmentationData.getInstance();
		ItemAuctionManager.getInstance();
		CommissionShopManager.getInstance();
		SpawnManager.getInstance().spawnAll();
		StaticObjectHolder.getInstance().spawnAll();
		RaidBossSpawnManager.getInstance();
		Scripts.getInstance().init();
		L2TopManager.getInstance();
		MMOTopManager.getInstance();
		SMSWayToPay.getInstance();
		Announcements.getInstance();
		PetitionManager.getInstance();
		PlayerMessageStack.getInstance();
		if (Config.AUTODESTROY_ITEM_AFTER > 0)
		{
			ItemsAutoDestroy.getInstance();
		}
		if (Config.ENABLE_OLYMPIAD)
		{
			Olympiad.load();
			Hero.getInstance();
		}
		if (Config.ALT_FISH_CHAMPIONSHIP_ENABLED)
		{
			FishingChampionShipManager.getInstance();
		}
		if (!Config.ALLOW_WEDDING)
		{
			CoupleManager.getInstance();
			_log.info("CoupleManager initialized");
		}
		CoupleManager.getInstance();
		MonsterRace.getInstance();
		LotteryManager.getInstance();
		CursedWeaponsManager.getInstance();
		DelusionChamberManager.getInstance();
		ItemHandler.getInstance();
		AdminCommandHandler.getInstance();
		UserCommandHandler.getInstance();
		VoicedCommandHandler.getInstance();
		TaskManager.getInstance();
		ResidenceHolder.getInstance().callInit();
		EventHolder.getInstance().callInit();
		BoatHolder.getInstance().spawnAll();
		CastleManorManager.getInstance();
		NaiaTowerManager.getInstance();
		NaiaCoreManager.getInstance();
		SoDManager.getInstance();
		SoIManager.getInstance();
		SoHManager.getInstance();
		HarnakUndegroundManager.getInstance();
		MiniGameScoreManager.getInstance();
		AwakingManager.getInstance();
		FindPartyManager.getInstance().load();
		ArcanManager.getInstance();
		ParnassusManager.getInstance();
		SubClassTable.getInstance();
		DualClassTable.getInstance();
		if (Config.GARBAGE_COLLECTOR_INTERVAL > 0)
		{
			Class.forName(GarbageCollector.class.getName());
		}
		// Uncomment to check for double spawns
		/*
		 * for (NpcInstance npcInst : GameObjectsStorage.getAllNpcsForIterate()) { final List<NpcInstance> around = npcInst.getAroundNpc(10, 10); if ((around != null) && !around.isEmpty()) { for (NpcInstance npc : around) { if ((npcInst.getId() == npc.getId()) && !npcInst.isMonster() &&
		 * !npc.getTitle().equals("Double Spawn") && !npcInst.getName().contains("Star Stone") && !npcInst.getName().contains("Wisp")) { npcInst.setTitle("Double Spawn"); npc.setTitle("Double Spawn"); _log.info("Probable double spawn: NpcId " + npc.getId() + " Location " + npc.getSpawnedLoc().getX()
		 * + " " + npc.getSpawnedLoc().getY() + " " + npc.getSpawnedLoc().getZ() + " " + npc.getSpawnedLoc().getHeading()); } } } }
		 */
		_log.info("==============================================================");
		_log.info("GameServer Started");
		_log.info("==============================================================");
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		Shutdown.getInstance().schedule(Config.RESTART_AT_TIME, Shutdown.RESTART);
		_log.info("Maximum Numbers of Connected Players: " + Config.MAXIMUM_ONLINE_USERS);
		_log.info("IdFactory: Free Object IDs remaining: " + IdFactory.getInstance().size());
		getListeners().onStart();
		_log.info("==============================================================");
		String memUsage = new StringBuilder().append(StatsUtils.getMemUsage()).toString();
		for (String line : memUsage.split("\n"))
		{
			_log.info(line);
		}
		_log.info("==============================================================");
		_log.info("Server loaded in " + uptime() + " seconds.");
		_log.info("==============================================================");
		
		InetAddress serverAddr = Config.GAMESERVER_HOSTNAME.equals("*") ? null : InetAddress.getByName(Config.GAMESERVER_HOSTNAME);
		_selectorThreads = new SelectorThread[Config.PORTS_GAME.length];
		GamePacketHandler gph = new GamePacketHandler();
		for (int i = 0; i < Config.PORTS_GAME.length; i++)
		{
			_selectorThreads[i] = new SelectorThread<>(Config.SELECTOR_CONFIG, gph, gph, gph, null);
			_selectorThreads[i].openServerSocket(serverAddr, Config.PORTS_GAME[i]);
			_selectorThreads[i].start();
		}
		LoginServerCommunication.getInstance().start();
		
		if (Config.SERVICES_OFFLINE_TRADE_RESTORE_AFTER_RESTART)
		{
			ThreadPoolManager.getInstance().schedule(new RestoreOfflineTraders(), 30000L);
		}
	}
	
	/**
	 * Method getListeners.
	 * @return GameServerListenerList
	 */
	public GameServerListenerList getListeners()
	{
		return _listeners;
	}
	
	/**
	 * Method getInstance.
	 * @return GameServer
	 */
	public static GameServer getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method addListener.
	 * @param <T>
	 * @param listener T
	 * @return boolean
	 */
	<T extends GameListener> boolean addListener(T listener)
	{
		return _listeners.add(listener);
	}
	
	/**
	 * Method removeListener.
	 * @param <T>
	 * @param listener T
	 * @return boolean
	 */
	<T extends GameListener> boolean removeListener(T listener)
	{
		return _listeners.remove(listener);
	}
	
	/**
	 * Method checkFreePorts.
	 */
	private static void checkFreePorts()
	{
		boolean binded = false;
		
		while (!binded)
		{
			for (int PORT_GAME : Config.PORTS_GAME)
			{
				try
				{
					ServerSocket ss;
					
					if (Config.GAMESERVER_HOSTNAME.equals("*"))
					{
						ss = new ServerSocket(PORT_GAME);
					}
					else
					{
						ss = new ServerSocket(PORT_GAME, 50, InetAddress.getByName(Config.GAMESERVER_HOSTNAME));
					}
					
					ss.close();
					binded = true;
				}
				catch (Exception e)
				{
					_log.warn("Port " + PORT_GAME + " is allready binded. Please free it and restart server.");
					binded = false;
					
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e2)
					{
						// empty catch clause
					}
				}
			}
		}
	}
	
	/**
	 * Method main.
	 * @param args String[]
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		new GameServer();
	}
	
	/**
	 * Method getVersion.
	 * @return Version
	 */
	public Version getVersion()
	{
		return version;
	}
}
