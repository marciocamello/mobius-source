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
package instances;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import lineage2.commons.geometry.Polygon;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.utils.Location;

/**
 * @author KilRoy Skills: 14027, 14035, 14279-14282, 14285, 14474(5),
 */
public final class OctavisNormal extends Reflection
{
	private static final int Octavis1 = 29191;
	private static final int Octavis2 = 29193;
	private static final int Octavis3 = 29194;
	private static final int OctavisRider = 29192;
	private static final int guardOctavisGladiator = 22928; // Two Stage Guard TODO[K]: 23086, 23143, 23145,
	private static final int guardOctavisHighAcademic = 22930; // Three Stage Guard TODO[K]: 23088,
	private final ZoneListener _epicZoneListener = new ZoneListener();
	final DeathListener _deathListener = new DeathListener();
	final CurrentHpListener _currentHpListenerFistsStage = new CurrentHpListener();
	final CurrentHpListener _currentHpListenerOctavisRide = new CurrentHpListener();
	final CurrentHpListener _currentHpListenerTwoStage = new CurrentHpListener();
	ScheduledFuture<?> twoStageGuardSpawn;
	ScheduledFuture<?> threeStageGuardSpawn;
	boolean _entryLocked = false;
	boolean _startLaunched = false;
	boolean _lockedTurn = false;
	NpcInstance octavisMassive;
	private static final int Door = 26210001;
	private static final int Door2 = 26210002;
	static final Territory centralRoomPoint = new Territory().add(new Polygon().add(210548, -118712).add(210548, -118800).add(210348, -118612).add(210748, -118312).add(210548, -118912).add(210648, -118812).add(210548, -118712).add(210448, -118512).setZmax(-10020).setZmin(-9020));
	final AtomicInteger raidplayers = new AtomicInteger();
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		getZone("[Octavis_epic]").addListener(_epicZoneListener);
	}
	
	@Override
	protected void onCollapse()
	{
		super.onCollapse();
	}
	
	public final class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (_entryLocked)
			{
				return;
			}
			
			Player player = cha.getPlayer();
			
			if ((player == null) || !cha.isPlayer())
			{
				return;
			}
			
			if (checkstartCond(raidplayers.incrementAndGet()))
			{
				ThreadPoolManager.getInstance().schedule(new FirstStage(), 30000L);
				_startLaunched = true;
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
			Player player = cha.getPlayer();
			
			if ((player == null) || !cha.isPlayer())
			{
				return;
			}
			
			raidplayers.decrementAndGet();
		}
	}
	
	boolean checkstartCond(int raidplayers)
	{
		return !((raidplayers < getInstancedZone().getMinParty()) || _startLaunched);
	}
	
	public final class CurrentHpListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			if (actor.getNpcId() == Octavis1)
			{
				if (actor.isDead())
				{
					return;
				}
				
				if (!_lockedTurn && (actor.getCurrentHp() <= 250000))
				{
					actor.setIsInvul(true);
					_lockedTurn = true;
					ThreadPoolManager.getInstance().schedule(new TwoStage(), 10);
					actor.removeListener(_currentHpListenerFistsStage);
					octavisMassive.removeListener(_currentHpListenerOctavisRide);
					
					for (NpcInstance n : getNpcs())
					{
						n.deleteMe();
					}
				}
			}
			
			if (actor.getNpcId() == Octavis2)
			{
				if (actor.isDead())
				{
					return;
				}
				
				if (!_lockedTurn && (actor.getCurrentHp() <= 250000))
				{
					_lockedTurn = true;
					ThreadPoolManager.getInstance().schedule(new ThreeStage(), 10);
					actor.removeListener(_currentHpListenerTwoStage);
					
					for (NpcInstance n : getNpcs())
					{
						n.deleteMe();
					}
				}
			}
			
			if (actor.getNpcId() == OctavisRider)
			{
				if (actor.isDead())
				{
					return;
				}
				
				double newHp = actor.getCurrentHp();
				double maxHp = actor.getMaxHp();
				
				if (newHp <= (maxHp / 2))
				{
					actor.setIsInvul(true);
					octavisMassive.setLockedTarget(false);
				}
				else
				{
					actor.setIsInvul(false);
					octavisMassive.setLockedTarget(true);
				}
			}
		}
	}
	
	private class FirstStage extends RunnableImpl
	{
		public FirstStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			_entryLocked = true;
			closeDoor(Door);
			closeDoor(Door2);
			
			for (Player player : getPlayers())
			{
				player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_OCTABIS_OPENING);
			}
			
			ThreadPoolManager.getInstance().schedule(() ->
			{
				NpcInstance octavisFirstStage = addSpawnWithoutRespawn(Octavis1, new Location(207192, 120568, -10032, 49151), 0);
				octavisFirstStage.addListener(_currentHpListenerFistsStage);
				octavisFirstStage.setLockedTarget(true);
				octavisMassive = octavisFirstStage;
				NpcInstance octavisRider = addSpawnWithoutRespawn(OctavisRider, new Location(207192, 120588, -10032, 49151), 0);
				octavisRider.addListener(_currentHpListenerOctavisRide);
			}, 26700);
		}
	}
	
	private class TwoStage extends RunnableImpl
	{
		public TwoStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_OCTABIS_PHASECH_A);
			}
			
			twoStageGuardSpawn = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SpawnGuardForStage(1), 10000L, 120000L); // 10 secs for movie
			NpcInstance octavisTwoStage = addSpawnWithoutRespawn(Octavis2, new Location(207192, 120568, -10032, 49151), 0);
			octavisTwoStage.addListener(_currentHpListenerTwoStage);
			_lockedTurn = false;
		}
	}
	
	private class SpawnGuardForStage extends RunnableImpl
	{
		int _stage, _guard1MinCount, _guard1MaxCount, _guard2MinCount, _guard2MaxCount;
		
		public SpawnGuardForStage(int stage) // 1 - two, 2 - three
		{
			_stage = stage;
		}
		
		@Override
		public void runImpl()
		{
			switch (_stage)
			{
				case 1:
					_guard1MinCount = 5;
					_guard1MaxCount = 10;
					break;
				
				case 2:
					_guard2MinCount = 5;
					_guard2MaxCount = 10;
					_guard1MinCount = 5;
					_guard1MaxCount = 10;
					break;
				
				default:
					break;
			}
			
			for (int i = 0; i < Rnd.get(_guard1MinCount, _guard1MaxCount); i++)
			{
				addSpawnWithoutRespawn(guardOctavisGladiator, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 0);
			}
			
			for (int i = 0; i < Rnd.get(_guard2MinCount, _guard2MaxCount); i++)
			{
				addSpawnWithoutRespawn(guardOctavisHighAcademic, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 0);
			}
		}
	}
	
	private class ThreeStage extends RunnableImpl
	{
		public ThreeStage()
		{
		}
		
		@Override
		public void runImpl()
		{
			twoStageGuardSpawn.cancel(true);
			
			for (Player player : getPlayers())
			{
				player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_OCTABIS_PHASECH_B);
			}
			
			threeStageGuardSpawn = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SpawnGuardForStage(2), 14000L, 120000L); // 14 secs for movie
			NpcInstance octavisThreeStage = addSpawnWithoutRespawn(Octavis3, new Location(207192, 120568, -10032, 49151), 0);
			octavisThreeStage.addListener(_deathListener);
		}
	}
	
	private class DeathListener implements OnDeathListener
	{
		public DeathListener()
		{
		}
		
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (self.isNpc() && (self.getNpcId() == Octavis3))
			{
				threeStageGuardSpawn.cancel(true);
				ThreadPoolManager.getInstance().schedule(new EndingMovie(), 10);
				openDoor(Door);
				openDoor(Door2);
			}
		}
	}
	
	private class EndingMovie extends RunnableImpl
	{
		public EndingMovie()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player p : getPlayers())
			{
				p.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_OCTABIS_ENDING);
			}
			
			for (NpcInstance n : getNpcs())
			{
				n.deleteMe();
			}
			
			ThreadPoolManager.getInstance().schedule(new CollapseInstance(), 38000L); // 38 secs for movie
		}
	}
	
	private class CollapseInstance extends RunnableImpl
	{
		public CollapseInstance()
		{
		}
		
		@Override
		public void runImpl()
		{
			startCollapseTimer(5 * 60 * 1000L);
			doCleanup();
			
			for (Player p : getPlayers())
			{
				p.sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(5));
			}
		}
	}
	
	void doCleanup()
	{
		if (twoStageGuardSpawn != null)
		{
			twoStageGuardSpawn.cancel(true);
		}
		
		if (threeStageGuardSpawn != null)
		{
			threeStageGuardSpawn.cancel(true);
		}
	}
}