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
package bosses;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.CommandChannel;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.instances.BossInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.utils.TimeUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bosses.EpicBossState.State;

/**
 * @author Mobius
 */
public final class LindviorManager extends Functions implements ScriptFile, OnDeathListener
{
	private static final Logger _log = LoggerFactory.getLogger(LindviorManager.class);
	static BossInstance LINDVIOR_BOSS_INSTANCE;
	private static final int LINDVIOR = 25899;
	private static final Location TELEPORT_POSITION = new Location(42424, -24248, -1417);
	static final Location LINDVIOR_LOCATION = new Location(44248, -26120, -1430, 24575);
	private static NpcInstance TELEPORT_CUBE_INSTANCE;
	private static final int TELEPORT_CUBE = 31759;
	private static final Location TELEPORT_CUBE_LOCATION = new Location(44248, -26120, -1430, 24575);
	static EpicBossState EPIC_BOSS_STATE;
	private static Zone ZONE;
	static long LAST_ATTACK_TIME = 0;
	private static final int FIXED_INTERVAL_OF_LINDVIOR = 11 * 24 * 60 * 60000;
	private static final int APPTIME_OF_LINDVIOR = 5 * 60000;
	private static boolean DYING = false;
	private static boolean ENTRY_LOCKED = false;
	private static boolean DISABLED = true; // disable boss
	private static boolean DEBUG = false; // gm test mode
	
	private static ScheduledFuture<?> monsterSpawnTask;
	private static ScheduledFuture<?> intervalEndTask;
	static ScheduledFuture<?> socialTask;
	private static ScheduledFuture<?> moveAtRandomTask;
	static ScheduledFuture<?> sleepCheckTask;
	private static ScheduledFuture<?> onAnnihilatedTask;
	
	private static class LindviorSpawn extends RunnableImpl
	{
		private int _taskId = 0;
		
		/**
		 * Constructor for LindviorSpawn.
		 * @param taskId int
		 */
		LindviorSpawn(int taskId)
		{
			_taskId = taskId;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			switch (_taskId)
			{
				case 1:
					LINDVIOR_BOSS_INSTANCE = (BossInstance) Functions.spawn(LINDVIOR_LOCATION, LINDVIOR);
					LINDVIOR_BOSS_INSTANCE.setAggroRange(0);
					EPIC_BOSS_STATE.setRespawnDate(Rnd.get(FIXED_INTERVAL_OF_LINDVIOR, FIXED_INTERVAL_OF_LINDVIOR));
					EPIC_BOSS_STATE.setState(EpicBossState.State.ALIVE);
					EPIC_BOSS_STATE.update();
					socialTask = ThreadPoolManager.getInstance().schedule(new LindviorSpawn(2), 10000);
					break;
				
				case 2:
					LINDVIOR_BOSS_INSTANCE.stopRandomAnimation();
					break;
				
				case 3:
					onLindviorDie();
					break;
			}
		}
	}
	
	private static class IntervalEnd extends RunnableImpl
	{
		/**
		 * Constructor for IntervalEnd.
		 */
		IntervalEnd()
		{
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			EPIC_BOSS_STATE.setState(EpicBossState.State.NOTSPAWN);
			EPIC_BOSS_STATE.update();
		}
	}
	
	private static class onAnnihilated extends RunnableImpl
	{
		/**
		 * Constructor for onAnnihilated.
		 */
		onAnnihilated()
		{
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			sleep();
		}
	}
	
	/**
	 * Method banishForeigners.
	 */
	private static void banishForeigners()
	{
		for (Player player : getPlayersInside())
		{
			player.teleToClosestTown();
		}
	}
	
	/**
	 * Method checkAnnihilated.
	 */
	private synchronized static void checkAnnihilated()
	{
		if ((onAnnihilatedTask == null) && isPlayersAnnihilated())
		{
			onAnnihilatedTask = ThreadPoolManager.getInstance().schedule(new onAnnihilated(), 5000);
		}
	}
	
	/**
	 * Method getPlayersInside.
	 * @return List<Player>
	 */
	static List<Player> getPlayersInside()
	{
		return getZone().getInsidePlayers();
	}
	
	/**
	 * Method getRespawnInterval.
	 * @return int
	 */
	private static int getRespawnInterval()
	{
		return (int) (Config.ALT_RAID_RESPAWN_MULTIPLIER * FIXED_INTERVAL_OF_LINDVIOR);
	}
	
	/**
	 * Method getZone.
	 * @return Zone
	 */
	public static Zone getZone()
	{
		return ZONE;
	}
	
	/**
	 * Method isPlayersAnnihilated.
	 * @return boolean
	 */
	private static boolean isPlayersAnnihilated()
	{
		for (Player pc : getPlayersInside())
		{
			if (!pc.isDead())
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Method onLindviorDie.
	 */
	static void onLindviorDie()
	{
		if (DYING)
		{
			return;
		}
		
		DYING = true;
		EPIC_BOSS_STATE.setRespawnDate(getRespawnInterval());
		EPIC_BOSS_STATE.setState(EpicBossState.State.INTERVAL);
		EPIC_BOSS_STATE.update();
		ENTRY_LOCKED = false;
		TELEPORT_CUBE_INSTANCE = Functions.spawn(TELEPORT_CUBE_LOCATION, TELEPORT_CUBE);
		Log.add("Lindvior died", "bosses");
	}
	
	/**
	 * Method onDeath.
	 * @param self Creature
	 * @param killer Creature
	 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
	 */
	@Override
	public void onDeath(Creature self, Creature killer)
	{
		if (self.isPlayer() && (EPIC_BOSS_STATE != null) && (EPIC_BOSS_STATE.getState() == State.ALIVE) && (ZONE != null) && ZONE.checkIfInZone(self.getX(), self.getY()))
		{
			checkAnnihilated();
		}
		else if (self.isNpc() && (self.getId() == LINDVIOR))
		{
			ThreadPoolManager.getInstance().schedule(new LindviorSpawn(3), 10);
		}
	}
	
	/**
	 * Method setIntervalEndTask.
	 */
	private static void setIntervalEndTask()
	{
		setUnspawn();
		
		if (EPIC_BOSS_STATE.getState().equals(EpicBossState.State.ALIVE))
		{
			EPIC_BOSS_STATE.setState(EpicBossState.State.NOTSPAWN);
			EPIC_BOSS_STATE.update();
			return;
		}
		
		if (!EPIC_BOSS_STATE.getState().equals(EpicBossState.State.INTERVAL))
		{
			EPIC_BOSS_STATE.setRespawnDate(getRespawnInterval());
			EPIC_BOSS_STATE.setState(EpicBossState.State.INTERVAL);
			EPIC_BOSS_STATE.update();
		}
		
		intervalEndTask = ThreadPoolManager.getInstance().schedule(new IntervalEnd(), EPIC_BOSS_STATE.getInterval());
	}
	
	/**
	 * Method setUnspawn.
	 */
	private static void setUnspawn()
	{
		banishForeigners();
		
		if (LINDVIOR_BOSS_INSTANCE != null)
		{
			LINDVIOR_BOSS_INSTANCE.deleteMe();
		}
		
		if (TELEPORT_CUBE_INSTANCE != null)
		{
			TELEPORT_CUBE_INSTANCE.deleteMe();
		}
		
		ENTRY_LOCKED = false;
		
		if (monsterSpawnTask != null)
		{
			monsterSpawnTask.cancel(false);
			monsterSpawnTask = null;
		}
		
		if (intervalEndTask != null)
		{
			intervalEndTask.cancel(false);
			intervalEndTask = null;
		}
		
		if (socialTask != null)
		{
			socialTask.cancel(false);
			socialTask = null;
		}
		
		if (moveAtRandomTask != null)
		{
			moveAtRandomTask.cancel(false);
			moveAtRandomTask = null;
		}
		
		if (sleepCheckTask != null)
		{
			sleepCheckTask.cancel(false);
			sleepCheckTask = null;
		}
		
		if (onAnnihilatedTask != null)
		{
			onAnnihilatedTask.cancel(false);
			onAnnihilatedTask = null;
		}
	}
	
	/**
	 * Method init.
	 */
	private void init()
	{
		EPIC_BOSS_STATE = new EpicBossState(LINDVIOR);
		ZONE = ReflectionUtils.getZone("[lindvior_epic]");
		CharListenerList.addGlobal(this);
		_log.info("LindviorManager: State of Lindvior is " + EPIC_BOSS_STATE.getState() + ".");
		
		if (!EPIC_BOSS_STATE.getState().equals(EpicBossState.State.NOTSPAWN))
		{
			setIntervalEndTask();
		}
		
		_log.info("LindviorManager: Next spawn date of Lindvior is " + TimeUtils.toSimpleFormat(EPIC_BOSS_STATE.getRespawnDate()) + ".");
	}
	
	/**
	 * Method sleep.
	 */
	static void sleep()
	{
		setUnspawn();
		
		if (EPIC_BOSS_STATE.getState().equals(EpicBossState.State.ALIVE))
		{
			EPIC_BOSS_STATE.setState(EpicBossState.State.NOTSPAWN);
			EPIC_BOSS_STATE.update();
		}
	}
	
	/**
	 * Method setLastAttackTime.
	 */
	public static void setLastAttackTime()
	{
		LAST_ATTACK_TIME = System.currentTimeMillis();
	}
	
	/**
	 * Method setLindviorSpawnTask.
	 */
	public synchronized static void setLindviorSpawnTask()
	{
		if (monsterSpawnTask == null)
		{
			monsterSpawnTask = ThreadPoolManager.getInstance().schedule(new LindviorSpawn(1), APPTIME_OF_LINDVIOR);
		}
		
		ENTRY_LOCKED = true;
	}
	
	/**
	 * Method broadcastScreenMessage.
	 * @param npcs NpcString
	 */
	public static void broadcastScreenMessage(NpcString npcs)
	{
		for (Player p : getPlayersInside())
		{
			p.sendPacket(new ExShowScreenMessage(npcs, 8000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
		}
	}
	
	/**
	 * Method enterTheLair.
	 * @param player Player
	 */
	public static void enterTheLair(Player player)
	{
		if (player == null)
		{
			return;
		}
		
		if (DISABLED)
		{
			player.sendMessage("This boss is not accessible.");
			return;
		}
		
		if ((!DEBUG && !player.isGM()) && ((player.getParty() == null) || !player.getParty().isInCommandChannel()))
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_ENTER_BECAUSE_YOU_ARE_NOT_IN_A_CURRENT_COMMAND_CHANNEL));
			return;
		}
		
		CommandChannel cc = null;
		if ((player.getParty() != null) && (player.getParty().getCommandChannel() != null))
		{
			cc = player.getParty().getCommandChannel();
		}
		
		if ((!DEBUG && !player.isGM()) && (((cc != null) && !cc.getChannelLeader().equals(player)) || (cc == null)))
		{
			player.sendPacket(new SystemMessage(SystemMessage.ONLY_THE_ALLIANCE_CHANNEL_LEADER_CAN_ATTEMPT_ENTRY));
			return;
		}
		
		if ((!DEBUG && !player.isGM()) && ((cc != null) && (cc.getMemberCount() > 144)))
		{
			player.sendMessage("The maximum of players that can invade Lindvior is 144.");
			return;
		}
		
		if (EPIC_BOSS_STATE.getState() != EpicBossState.State.NOTSPAWN)
		{
			player.sendMessage("Lindvior is still reborning. You cannot invade him now.");
			return;
		}
		
		if (ENTRY_LOCKED || (EPIC_BOSS_STATE.getState() == EpicBossState.State.ALIVE))
		{
			player.sendMessage("Lindvior has already been reborned and is being attacked. The entrance is sealed.");
			return;
		}
		
		if (cc != null)
		{
			for (Player p : cc)
			{
				if (p.isDead() || p.isFlying() || p.isCursedWeaponEquipped() || !p.isInRange(player, 1500))
				{
					player.sendMessage("Command Channel member " + p.getName() + " doesn't meet the requirements to enter.");
					return;
				}
			}
			
			for (Player p : cc)
			{
				p.teleToLocation(TELEPORT_POSITION);
			}
		}
		else
		{
			player.teleToLocation(TELEPORT_POSITION);
		}
		
		setLindviorSpawnTask();
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		init();
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		sleep();
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
		// empty method
	}
}