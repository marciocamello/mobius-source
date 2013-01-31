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

import java.util.concurrent.atomic.AtomicInteger;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class IsthinaNormal extends Reflection
{
	/**
	 * Field Isthina.
	 */
	private final int Isthina = 29195;
	/**
	 * Field Ballista.
	 */
	private final int Ballista = 19021;
	/**
	 * Field _epicZoneListener.
	 */
	private final ZoneListener _epicZoneListener = new ZoneListener();
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
	 * Field _lockedTurn.
	 */
	boolean _lockedTurn = false;
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
		getZone("[Isthina_epic]").addListener(_epicZoneListener);
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
				ThreadPoolManager.getInstance().schedule(new StartNormalIsthina(), 5000);
				_startLaunched = true;
			}
			NpcInstance isthinaNormal = addSpawnWithoutRespawn(Isthina, new Location(-177125, 147856, -11384, 49140), 0);
			isthinaNormal.addListener(_deathListener);
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
	 * @author Mobius
	 */
	private class StartNormalIsthina extends RunnableImpl
	{
		/**
		 * Constructor for StartNormalIsthina.
		 */
		public StartNormalIsthina()
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
			closeDoor(14220100);
			closeDoor(14220101);
			for (Player player : getPlayers())
			{
				player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_ISTHINA_OPENING);
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
			if (self.isNpc() && (self.getNpcId() == Isthina))
			{
				ThreadPoolManager.getInstance().schedule(new IsthinaDeath(), 10);
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
			if ((actor == null) || actor.isDead() || (actor.getNpcId() != Ballista))
			{
				return;
			}
			double newHp = actor.getCurrentHp() - damage;
			double maxHp = actor.getMaxHp();
			if (!_lockedTurn && (newHp <= (0.2 * maxHp)))
			{
				_lockedTurn = true;
				ThreadPoolManager.getInstance().schedule(new IsthinaDeathFinalA(), 10);
				actor.removeListener(_currentHpListener);
			}
			else
			{
				_lockedTurn = true;
				ThreadPoolManager.getInstance().schedule(new IsthinaDeathFinalB(), 10);
				actor.removeListener(_currentHpListener);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class IsthinaDeath extends RunnableImpl
	{
		/**
		 * Constructor for IsthinaDeath.
		 */
		public IsthinaDeath()
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
				player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_ISTHINA_BRIDGE);
			}
			ThreadPoolManager.getInstance().schedule(new SpawnBallista(), 23300L);
			for (NpcInstance n : getNpcs())
			{
				n.deleteMe();
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class IsthinaDeathFinalA extends RunnableImpl
	{
		/**
		 * Constructor for IsthinaDeathFinalA.
		 */
		public IsthinaDeathFinalA()
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
			for (Player player : getPlayers())
			{
				player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_ISTHINA_ENDING_A);
			}
			ThreadPoolManager.getInstance().schedule(new FinalAndCollapse(), 22200L);
			for (NpcInstance n : getNpcs())
			{
				n.deleteMe();
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class IsthinaDeathFinalB extends RunnableImpl
	{
		/**
		 * Constructor for IsthinaDeathFinalB.
		 */
		public IsthinaDeathFinalB()
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
			for (Player player : getPlayers())
			{
				player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_ISTHINA_ENDING_B);
			}
			ThreadPoolManager.getInstance().schedule(new FinalAndCollapse(), 22200L);
			for (NpcInstance n : getNpcs())
			{
				n.deleteMe();
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class FinalAndCollapse extends RunnableImpl
	{
		/**
		 * Constructor for FinalAndCollapse.
		 */
		public FinalAndCollapse()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			clearReflection(5, true);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class SpawnBallista extends RunnableImpl
	{
		/**
		 * Constructor for SpawnBallista.
		 */
		public SpawnBallista()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			NpcInstance ballista = addSpawnWithoutRespawn(Ballista, new Location(-177125, 147856, -11384, 49140), 0);
			ballista.addListener(_currentHpListener);
		}
	}
}
