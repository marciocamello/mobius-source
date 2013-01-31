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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OctavisNormal extends Reflection
{
	/**
	 * Field Octavis1.
	 */
	private final int Octavis1 = 29191;
	/**
	 * Field Octavis2.
	 */
	private final int Octavis2 = 29193;
	/**
	 * Field Octavis3.
	 */
	private final int Octavis3 = 29194;
	/**
	 * Field OctavisRider.
	 */
	private final int OctavisRider = 29210;
	/**
	 * Field guardOctavisGladiator.
	 */
	private final int guardOctavisGladiator = 22928;
	/**
	 * Field guardOctavisHighAcademic.
	 */
	private final int guardOctavisHighAcademic = 22930;
	/**
	 * Field _epicZoneListener.
	 */
	private final ZoneListener _epicZoneListener = new ZoneListener();
	/**
	 * Field _deathListener.
	 */
	final DeathListener _deathListener = new DeathListener();
	/**
	 * Field _currentHpListenerFistsStage.
	 */
	final CurrentHpListener _currentHpListenerFistsStage = new CurrentHpListener();
	/**
	 * Field _currentHpListenerTwoStage.
	 */
	final CurrentHpListener _currentHpListenerTwoStage = new CurrentHpListener();
	/**
	 * Field twoStageGuardSpawn.
	 */
	ScheduledFuture<?> twoStageGuardSpawn;
	/**
	 * Field threeStageGuardSpawn.
	 */
	ScheduledFuture<?> threeStageGuardSpawn;
	/**
	 * Field _entryLocked.
	 */
	boolean _entryLocked = false;
	/**
	 * Field _startLaunched.
	 */
	boolean _startLaunched = false;
	/**
	 * Field _lockedTurn.
	 */
	boolean _lockedTurn = false;
	/**
	 * Field Door.
	 */
	private final int Door = 26210001;
	/**
	 * Field Door2.
	 */
	private final int Door2 = 26210002;
	/**
	 * Field centralRoomPoint.
	 */
	static Territory centralRoomPoint = new Territory().add(new Polygon().add(206232, 120184).add(206792, 119624).add(207592, 119624).add(208152, 120184).add(208152, 120968).add(207592, 121528).add(206792, 121528).add(206232, 120968).setZmax(-10040).setZmin(-9020));
	/**
	 * Field raidplayers.
	 */
	final AtomicInteger raidplayers = new AtomicInteger();
	
	/**
	 * Method onCreate.
	 */
	@Override
	protected void onCreate()
	{
		super.onCreate();
		getZone("[Octavis_epic]").addListener(_epicZoneListener);
	}
	
	/**
	 * Method onCollapse.
	 */
	@Override
	protected void onCollapse()
	{
		super.onCollapse();
	}
	
	/**
	 * @author Mobius
	 */
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
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
		
		/**
		 * Method onZoneLeave.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
		 */
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
	
	/**
	 * Method checkstartCond.
	 * @param raidplayers int
	 * @return boolean
	 */
	boolean checkstartCond(int raidplayers)
	{
		return !((raidplayers < getInstancedZone().getMinParty()) || _startLaunched);
	}
	
	/**
	 * @author Mobius
	 */
	public class CurrentHpListener implements OnCurrentHpDamageListener
	{
		/**
		 * Method onCurrentHpDamage.
		 * @param actor Creature
		 * @param damage double
		 * @param attacker Creature
		 * @param skill Skill
		 * @see lineage2.gameserver.listener.actor.OnCurrentHpDamageListener#onCurrentHpDamage(Creature, double, Creature, Skill)
		 */
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			if (actor.getNpcId() == Octavis1)
			{
				if ((actor == null) || actor.isDead())
				{
					return;
				}
				if (!_lockedTurn && (actor.getCurrentHp() < 25000))
				{
					actor.setIsInvul(true);
					_lockedTurn = true;
					ThreadPoolManager.getInstance().schedule(new TwoStage(), 10);
					actor.removeListener(_currentHpListenerFistsStage);
					for (NpcInstance n : getNpcs())
					{
						n.deleteMe();
					}
				}
			}
			if (actor.getNpcId() == Octavis2)
			{
				if ((actor == null) || actor.isDead())
				{
					return;
				}
				if (!_lockedTurn && (actor.getCurrentHp() <= 25000))
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
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class FirstStage extends RunnableImpl
	{
		/**
		 * Constructor for FirstStage.
		 */
		public FirstStage()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
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
			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					NpcInstance octavisFirstStage = addSpawnWithoutRespawn(Octavis1, new Location(207192, 120568, -10032, 49151), 0);
					octavisFirstStage.addListener(_currentHpListenerFistsStage);
					addSpawnWithoutRespawn(OctavisRider, new Location(207192, 120588, -10032, 49151), 0);
				}
			}, 26700);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class TwoStage extends RunnableImpl
	{
		/**
		 * Constructor for TwoStage.
		 */
		public TwoStage()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_OCTABIS_PHASECH_A);
			}
			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					NpcInstance octavisTwoStage = addSpawnWithoutRespawn(Octavis2, new Location(207192, 120568, -10032, 49151), 0);
					octavisTwoStage.addListener(_currentHpListenerTwoStage);
					twoStageGuardSpawn = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SpawnGuardForStage(1), 1000L, 50000L);
					_lockedTurn = false;
				}
			}, 10000L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class ThreeStage extends RunnableImpl
	{
		/**
		 * Constructor for ThreeStage.
		 */
		public ThreeStage()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			twoStageGuardSpawn.cancel(true);
			for (Player player : getPlayers())
			{
				player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_OCTABIS_PHASECH_B);
			}
			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					NpcInstance octavisThreeStage = addSpawnWithoutRespawn(Octavis3, new Location(207192, 120568, -10032, 49151), 0);
					octavisThreeStage.addListener(_deathListener);
					threeStageGuardSpawn = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SpawnGuardForStage(2), 1000L, 50000L);
				}
			}, 14000L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class SpawnGuardForStage extends RunnableImpl
	{
		/**
		 * Field _guard2MaxCount.
		 */
		/**
		 * Field _guard2MinCount.
		 */
		/**
		 * Field _guard1MaxCount.
		 */
		/**
		 * Field _guard1MinCount.
		 */
		/**
		 * Field _stage.
		 */
		int _stage, _guard1MinCount, _guard1MaxCount, _guard2MinCount, _guard2MaxCount;
		
		/**
		 * Constructor for SpawnGuardForStage.
		 * @param stage int
		 */
		public SpawnGuardForStage(int stage)
		{
			_stage = stage;
			if ((_stage < 1) || (_stage > 2))
			{
				_stage = 1;
			}
		}
		
		/**
		 * Method runImpl.
		 */
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
					_guard2MinCount = 10;
					_guard2MaxCount = 20;
					break;
				default:
					break;
			}
			switch (_stage)
			{
				case 1:
					for (int i = 0; i < Rnd.get(_guard1MinCount, _guard1MaxCount); i++)
					{
						addSpawnWithoutRespawn(guardOctavisGladiator, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 0);
					}
					break;
				case 2:
					for (int i = 0; i < Rnd.get(_guard2MinCount, _guard2MaxCount); i++)
					{
						addSpawnWithoutRespawn(guardOctavisHighAcademic, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 0);
					}
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class DeathListener implements OnDeathListener
	{
		/**
		 * Constructor for DeathListener.
		 */
		public DeathListener()
		{
			// TODO Auto-generated constructor stub
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
			if (self.isNpc() && (self.getNpcId() == Octavis3))
			{
				threeStageGuardSpawn.cancel(true);
				ThreadPoolManager.getInstance().schedule(new EndingMovie(), 10);
				openDoor(Door);
				openDoor(Door2);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class EndingMovie extends RunnableImpl
	{
		/**
		 * Constructor for EndingMovie.
		 */
		public EndingMovie()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
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
			ThreadPoolManager.getInstance().schedule(new CollapseInstance(), 38000L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class CollapseInstance extends RunnableImpl
	{
		/**
		 * Constructor for CollapseInstance.
		 */
		public CollapseInstance()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
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
	
	/**
	 * Method doCleanup.
	 */
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
