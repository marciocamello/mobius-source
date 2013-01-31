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
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.ExChangeClientEffectInfo;
import lineage2.gameserver.network.serverpackets.ExSendUIEvent;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.Location;
import quests._10286_ReunionWithSirra;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FreyaNormal extends Reflection
{
	/**
	 * Field FreyaThrone. (value is 29177)
	 */
	private static final int FreyaThrone = 29177;
	/**
	 * Field FreyaStandNormal. (value is 29179)
	 */
	private static final int FreyaStandNormal = 29179;
	/**
	 * Field IceKnightNormal. (value is 18855)
	 */
	private static final int IceKnightNormal = 18855;
	/**
	 * Field IceKnightLeaderNormal. (value is 25699)
	 */
	private static final int IceKnightLeaderNormal = 25699;
	/**
	 * Field IceCastleBreath. (value is 18854)
	 */
	private static final int IceCastleBreath = 18854;
	/**
	 * Field Glacier. (value is 18853)
	 */
	private static final int Glacier = 18853;
	/**
	 * Field IceCastleController. (value is 18932)
	 */
	private static final int IceCastleController = 18932;
	/**
	 * Field Sirra. (value is 32762)
	 */
	private static final int Sirra = 32762;
	/**
	 * Field Jinia. (value is 18850)
	 */
	private static final int Jinia = 18850;
	/**
	 * Field Kegor. (value is 18851)
	 */
	private static final int Kegor = 18851;
	/**
	 * Field _eventTriggers.
	 */
	private static final int[] _eventTriggers =
	{
		23140202,
		23140204,
		23140206,
		23140208,
		23140212,
		23140214,
		23140216
	};
	/**
	 * Field pcbuff2. Field pcbuff. Field attackUp. Field damagezone.
	 */
	private Zone damagezone, attackUp, pcbuff, pcbuff2;
	/**
	 * Field firstStageGuardSpawn.
	 */
	ScheduledFuture<?> firstStageGuardSpawn;
	/**
	 * Field secondStageGuardSpawn.
	 */
	ScheduledFuture<?> secondStageGuardSpawn;
	/**
	 * Field thirdStageGuardSpawn.
	 */
	ScheduledFuture<?> thirdStageGuardSpawn;
	/**
	 * Field _epicZoneListener.
	 */
	private final ZoneListener _epicZoneListener = new ZoneListener();
	/**
	 * Field _landingZoneListener.
	 */
	private final ZoneListenerL _landingZoneListener = new ZoneListenerL();
	/**
	 * Field _deathListener.
	 */
	final DeathListener _deathListener = new DeathListener();
	/**
	 * Field _currentHpListener.
	 */
	final CurrentHpListener _currentHpListener = new CurrentHpListener();
	/**
	 * Field _entryLocked.
	 */
	boolean _entryLocked = false;
	/**
	 * Field _startLaunched.
	 */
	boolean _startLaunched = false;
	/**
	 * Field _freyaSlayed.
	 */
	boolean _freyaSlayed = false;
	/**
	 * Field raidplayers.
	 */
	final AtomicInteger raidplayers = new AtomicInteger();
	/**
	 * Field centralRoom.
	 */
	static Territory centralRoom = new Territory().add(new Polygon().add(114264, -113672).add(113640, -114344).add(113640, -115240).add(114264, -115912).add(115176, -115912).add(115800, -115272).add(115800, -114328).add(115192, -113672).setZmax(-11225).setZmin(-11225));
	
	/**
	 * Method onCreate.
	 */
	@Override
	protected void onCreate()
	{
		super.onCreate();
		attackUp = getZone("[freya_attack_up]");
		pcbuff = getZone("[freya_pc_buff1]");
		pcbuff2 = getZone("[freya_pc_buff2]");
		getZone("[freya_normal_epic]").addListener(_epicZoneListener);
		getZone("[freya_landing_room_epic]").addListener(_landingZoneListener);
	}
	
	/**
	 * Method manageDamageZone.
	 * @param level int
	 * @param disable boolean
	 */
	void manageDamageZone(int level, boolean disable)
	{
		if (disable)
		{
			damagezone.setActive(false);
			return;
		}
		if (damagezone != null)
		{
			damagezone.setActive(false);
		}
		switch (level)
		{
			case 1:
				damagezone = getZone("[freya_normal_freezing_01]");
				break;
			case 2:
				damagezone = getZone("[freya_normal_freezing_02]");
				break;
			case 3:
				damagezone = getZone("[freya_normal_freezing_03]");
				break;
			case 4:
				damagezone = getZone("[freya_normal_freezing_04]");
				break;
			case 5:
				damagezone = getZone("[freya_normal_freezing_05]");
				break;
			case 6:
				damagezone = getZone("[freya_normal_freezing_06]");
				break;
			case 7:
				damagezone = getZone("[freya_normal_freezing_07]");
				break;
			default:
				break;
		}
		if (damagezone != null)
		{
			damagezone.setActive(true);
		}
	}
	
	/**
	 * Method manageAttackUpZone.
	 * @param disable boolean
	 */
	void manageAttackUpZone(boolean disable)
	{
		if ((attackUp != null) && disable)
		{
			attackUp.setActive(false);
			return;
		}
		if (attackUp != null)
		{
			attackUp.setActive(true);
		}
	}
	
	/**
	 * Method managePcBuffZone.
	 * @param disable boolean
	 */
	void managePcBuffZone(boolean disable)
	{
		if ((pcbuff != null) && (pcbuff2 != null) && disable)
		{
			pcbuff.setActive(false);
			pcbuff2.setActive(false);
			return;
		}
		if (pcbuff != null)
		{
			pcbuff.setActive(true);
		}
		if (pcbuff2 != null)
		{
			pcbuff2.setActive(true);
		}
	}
	
	/**
	 * Method manageCastleController.
	 * @param state int
	 */
	void manageCastleController(int state)
	{
		for (NpcInstance n : getNpcs())
		{
			if (n.getNpcId() == IceCastleController)
			{
				n.setNpcState(state);
			}
		}
	}
	
	/**
	 * Method manageStorm.
	 * @param active boolean
	 */
	void manageStorm(boolean active)
	{
		for (Player p : getPlayers())
		{
			for (int _eventTrigger : _eventTriggers)
			{
				p.sendPacket(new EventTrigger(_eventTrigger, active));
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class StartNormalFreya extends RunnableImpl
	{
		/**
		 * Constructor for StartNormalFreya.
		 */
		public StartNormalFreya()
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
			closeDoor(23140101);
			for (Player player : getPlayers())
			{
				QuestState qs = player.getQuestState(_10286_ReunionWithSirra.class);
				if ((qs != null) && (qs.getCond() == 5))
				{
					qs.setCond(6);
				}
				player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_FREYA_OPENING);
			}
			ThreadPoolManager.getInstance().schedule(new PreStage(), 55000L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class PreStage extends RunnableImpl
	{
		/**
		 * Constructor for PreStage.
		 */
		public PreStage()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			manageDamageZone(1, false);
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.BEGIN_STAGE_1_FREYA, 6000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));
			}
			for (int i = 0; i < 10; i++)
			{
				addSpawnWithoutRespawn(IceKnightNormal, Territory.getRandomLoc(centralRoom, getGeoIndex()), 0);
			}
			ThreadPoolManager.getInstance().schedule(new FirstStage(), 40000L);
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
			manageCastleController(1);
			manageDamageZone(2, false);
			for (Player player : getPlayers())
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.FREYA_HAS_STARTED_TO_MOVE, 4000, ScreenMessageAlign.MIDDLE_CENTER, true));
			}
			NpcInstance freyaTrhone = addSpawnWithoutRespawn(FreyaThrone, new Location(114720, -117085, -11088, 15956), 0);
			freyaTrhone.addListener(_deathListener);
			firstStageGuardSpawn = ThreadPoolManager.getInstance().scheduleAtFixedRate(new GuardSpawnTask(1), 2000L, 30000L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class GuardSpawnTask extends RunnableImpl
	{
		/**
		 * Field _breathMax.
		 */
		/**
		 * Field _breathMin.
		 */
		/**
		 * Field _knightsMax.
		 */
		/**
		 * Field _knightsMin.
		 */
		/**
		 * Field _mode.
		 */
		int _mode, _knightsMin, _knightsMax, _breathMin, _breathMax;
		
		/**
		 * Constructor for GuardSpawnTask.
		 * @param mode int
		 */
		public GuardSpawnTask(int mode)
		{
			_mode = mode;
			if ((_mode < 1) || (_mode > 4))
			{
				_mode = 1;
			}
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			switch (_mode)
			{
				case 1:
					_knightsMin = 2;
					_knightsMax = 3;
					_breathMin = 1;
					_breathMax = 2;
					break;
				case 2:
					_knightsMin = 2;
					_knightsMax = 4;
					_breathMin = 1;
					_breathMax = 3;
					break;
				case 3:
					_knightsMin = 3;
					_knightsMax = 8;
					_breathMin = 2;
					_breathMax = 4;
					break;
				case 4:
					_knightsMin = 6;
					_knightsMax = 10;
					_breathMin = 3;
					_breathMax = 7;
					break;
				default:
					break;
			}
			for (int i = 0; i < Rnd.get(_knightsMin, _knightsMax); i++)
			{
				addSpawnWithoutRespawn(IceKnightNormal, Territory.getRandomLoc(centralRoom, getGeoIndex()), 0);
			}
			for (int i = 0; i < Rnd.get(_breathMin, _breathMax); i++)
			{
				addSpawnWithoutRespawn(IceCastleBreath, Territory.getRandomLoc(centralRoom, getGeoIndex()), 0);
			}
			if (Rnd.chance(60))
			{
				for (int i = 0; i < Rnd.get(1, 3); i++)
				{
					addSpawnWithoutRespawn(Glacier, Territory.getRandomLoc(centralRoom, getGeoIndex()), 0);
				}
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class PreSecondStage extends RunnableImpl
	{
		/**
		 * Constructor for PreSecondStage.
		 */
		public PreSecondStage()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			firstStageGuardSpawn.cancel(true);
			for (NpcInstance n : getNpcs())
			{
				if ((n.getNpcId() != Sirra) && (n.getNpcId() != IceCastleController))
				{
					n.deleteMe();
				}
			}
			for (Player p : getPlayers())
			{
				p.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_FREYA_PHASE_A);
			}
			ThreadPoolManager.getInstance().schedule(new TimerToSecondStage(), 22000L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class TimerToSecondStage extends RunnableImpl
	{
		/**
		 * Constructor for TimerToSecondStage.
		 */
		public TimerToSecondStage()
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
				p.sendPacket(new ExSendUIEvent(p, false, false, 60, 0, NpcString.TIME_REMAINING_UNTIL_NEXT_BATTLE));
			}
			ThreadPoolManager.getInstance().schedule(new SecondStage(), 60000L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class SecondStage extends RunnableImpl
	{
		/**
		 * Constructor for SecondStage.
		 */
		public SecondStage()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			manageCastleController(3);
			manageDamageZone(3, false);
			for (Player p : getPlayers())
			{
				p.sendPacket(new ExShowScreenMessage(NpcString.BEGIN_STAGE_2_FREYA, 6000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));
			}
			secondStageGuardSpawn = ThreadPoolManager.getInstance().scheduleAtFixedRate(new GuardSpawnTask(2), 2000L, 30000L);
			ThreadPoolManager.getInstance().schedule(new KnightCaptainSpawnMovie(), 60000L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class KnightCaptainSpawnMovie extends RunnableImpl
	{
		/**
		 * Constructor for KnightCaptainSpawnMovie.
		 */
		public KnightCaptainSpawnMovie()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (NpcInstance n : getNpcs())
			{
				n.block();
			}
			for (Player p : getPlayers())
			{
				p.showQuestMovie(ExStartScenePlayer.SCENE_ICE_HEAVYKNIGHT_SPAWN);
			}
			ThreadPoolManager.getInstance().schedule(new KnightCaptainSpawn(), 7500L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class KnightCaptainSpawn extends RunnableImpl
	{
		/**
		 * Constructor for KnightCaptainSpawn.
		 */
		public KnightCaptainSpawn()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			manageDamageZone(4, false);
			for (NpcInstance n : getNpcs())
			{
				n.unblock();
			}
			NpcInstance knightLeader = addSpawnWithoutRespawn(IceKnightLeaderNormal, new Location(114707, -114799, -11199, 15956), 0);
			knightLeader.addListener(_deathListener);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class PreThirdStage extends RunnableImpl
	{
		/**
		 * Constructor for PreThirdStage.
		 */
		public PreThirdStage()
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
				p.sendPacket(new ExSendUIEvent(p, false, false, 60, 0, NpcString.TIME_REMAINING_UNTIL_NEXT_BATTLE));
			}
			secondStageGuardSpawn.cancel(true);
			for (NpcInstance n : getNpcs())
			{
				if ((n.getNpcId() != Sirra) && (n.getNpcId() != IceCastleController))
				{
					n.deleteMe();
				}
			}
			ThreadPoolManager.getInstance().schedule(new PreThirdStageM(), 60000L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class PreThirdStageM extends RunnableImpl
	{
		/**
		 * Constructor for PreThirdStageM.
		 */
		public PreThirdStageM()
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
				p.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_FREYA_PHASE_B);
			}
			ThreadPoolManager.getInstance().schedule(new ThirdStage(), 22000L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class ThirdStage extends RunnableImpl
	{
		/**
		 * Constructor for ThirdStage.
		 */
		public ThirdStage()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			manageCastleController(4);
			manageAttackUpZone(false);
			manageDamageZone(5, false);
			manageStorm(true);
			for (Player p : getPlayers())
			{
				p.sendPacket(new ExShowScreenMessage(NpcString.BEGIN_STAGE_3_FREYA, 6000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));
				p.sendPacket(new ExChangeClientEffectInfo(2));
			}
			thirdStageGuardSpawn = ThreadPoolManager.getInstance().scheduleAtFixedRate(new GuardSpawnTask(3), 2000L, 30000L);
			NpcInstance freyaStand = addSpawnWithoutRespawn(FreyaStandNormal, new Location(114720, -117085, -11088, 15956), 0);
			freyaStand.addListener(_currentHpListener);
			freyaStand.addListener(_deathListener);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class PreForthStage extends RunnableImpl
	{
		/**
		 * Constructor for PreForthStage.
		 */
		public PreForthStage()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (NpcInstance n : getNpcs())
			{
				n.block();
			}
			for (Player p : getPlayers())
			{
				p.block();
				p.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_KEGOR_INTRUSION);
			}
			ThreadPoolManager.getInstance().schedule(new ForthStage(), 28000L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class ForthStage extends RunnableImpl
	{
		/**
		 * Constructor for ForthStage.
		 */
		public ForthStage()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (NpcInstance n : getNpcs())
			{
				n.unblock();
			}
			for (Player p : getPlayers())
			{
				p.unblock();
				p.sendPacket(new ExShowScreenMessage(NpcString.BEGIN_STAGE_4_FREYA, 6000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));
			}
			addSpawnWithoutRespawn(Jinia, new Location(114727, -114700, -11200, -16260), 0);
			addSpawnWithoutRespawn(Kegor, new Location(114690, -114700, -11200, -16260), 0);
			managePcBuffZone(false);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class FreyaDeathStage extends RunnableImpl
	{
		/**
		 * Constructor for FreyaDeathStage.
		 */
		public FreyaDeathStage()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			setReenterTime(System.currentTimeMillis());
			thirdStageGuardSpawn.cancel(true);
			manageDamageZone(1, true);
			manageAttackUpZone(true);
			managePcBuffZone(true);
			for (NpcInstance n : getNpcs())
			{
				n.deleteMe();
			}
			for (Player p : getPlayers())
			{
				QuestState qs = p.getQuestState(_10286_ReunionWithSirra.class);
				if ((qs != null) && (qs.getCond() == 6))
				{
					qs.setCond(7);
				}
				p.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_FREYA_ENDING_A);
			}
			ThreadPoolManager.getInstance().schedule(new ConclusionMovie(), 16200L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class ConclusionMovie extends RunnableImpl
	{
		/**
		 * Constructor for ConclusionMovie.
		 */
		public ConclusionMovie()
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
				p.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_FREYA_ENDING_B);
			}
			ThreadPoolManager.getInstance().schedule(new InstanceConclusion(), 57000L);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class InstanceConclusion extends RunnableImpl
	{
		/**
		 * Constructor for InstanceConclusion.
		 */
		public InstanceConclusion()
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
			if (self.isNpc() && (self.getNpcId() == FreyaThrone))
			{
				ThreadPoolManager.getInstance().schedule(new PreSecondStage(), 10);
				self.deleteMe();
			}
			else if (self.isNpc() && (self.getNpcId() == IceKnightLeaderNormal))
			{
				ThreadPoolManager.getInstance().schedule(new PreThirdStage(), 10);
			}
			else if (self.isNpc() && (self.getNpcId() == FreyaStandNormal))
			{
				ThreadPoolManager.getInstance().schedule(new FreyaDeathStage(), 10);
			}
		}
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
			if ((actor == null) || actor.isDead() || (actor.getNpcId() != FreyaStandNormal))
			{
				return;
			}
			double newHp = actor.getCurrentHp() - damage;
			double maxHp = actor.getMaxHp();
			if (!_freyaSlayed && (newHp <= (0.2 * maxHp)))
			{
				_freyaSlayed = true;
				ThreadPoolManager.getInstance().schedule(new PreForthStage(), 10);
				actor.removeListener(_currentHpListener);
			}
		}
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
				ThreadPoolManager.getInstance().schedule(new StartNormalFreya(), 30000L);
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
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class ZoneListenerL implements OnZoneEnterLeaveListener
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
			if (cha.isPlayer())
			{
				cha.sendPacket(new ExChangeClientEffectInfo(1));
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
	 * Method doCleanup.
	 */
	void doCleanup()
	{
		if (firstStageGuardSpawn != null)
		{
			firstStageGuardSpawn.cancel(true);
		}
		if (secondStageGuardSpawn != null)
		{
			secondStageGuardSpawn.cancel(true);
		}
		if (thirdStageGuardSpawn != null)
		{
			thirdStageGuardSpawn.cancel(true);
		}
	}
	
	/**
	 * Method onCollapse.
	 */
	@Override
	protected void onCollapse()
	{
		super.onCollapse();
		doCleanup();
	}
}
