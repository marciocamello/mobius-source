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

public class IsthinaNormal extends Reflection
{
	private final int Isthina = 29195;
	private final int Ballista = 19021;
	private final ZoneListener _epicZoneListener = new ZoneListener();
	final DeathListener _deathListener = new DeathListener();
	final CurrentHpListener _currentHpListener = new CurrentHpListener();
	boolean _entryLocked = false;
	boolean _startLaunched = false;
	boolean _lockedTurn = false;
	final AtomicInteger raidplayers = new AtomicInteger();
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		getZone("[Isthina_epic]").addListener(_epicZoneListener);
	}
	
	@Override
	protected void onCollapse()
	{
		super.onCollapse();
	}
	
	public class ZoneListener implements OnZoneEnterLeaveListener
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
				ThreadPoolManager.getInstance().schedule(new StartNormalIsthina(), 5000);
				_startLaunched = true;
			}
			NpcInstance isthinaNormal = addSpawnWithoutRespawn(Isthina, new Location(-177125, 147856, -11384, 49140), 0);
			isthinaNormal.addListener(_deathListener);
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
	
	boolean checkstartCond(int raidplayers)
	{
		return !((raidplayers < getInstancedZone().getMinParty()) || _startLaunched);
	}
	
	private class StartNormalIsthina extends RunnableImpl
	{
		public StartNormalIsthina()
		{
			// TODO Auto-generated constructor stub
		}
		
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
	
	private class DeathListener implements OnDeathListener
	{
		public DeathListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (self.isNpc() && (self.getNpcId() == Isthina))
			{
				ThreadPoolManager.getInstance().schedule(new IsthinaDeath(), 10);
			}
		}
	}
	
	public class CurrentHpListener implements OnCurrentHpDamageListener
	{
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
	
	private class IsthinaDeath extends RunnableImpl
	{
		public IsthinaDeath()
		{
			// TODO Auto-generated constructor stub
		}
		
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
	
	private class IsthinaDeathFinalA extends RunnableImpl
	{
		public IsthinaDeathFinalA()
		{
			// TODO Auto-generated constructor stub
		}
		
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
	
	private class IsthinaDeathFinalB extends RunnableImpl
	{
		public IsthinaDeathFinalB()
		{
			// TODO Auto-generated constructor stub
		}
		
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
	
	private class FinalAndCollapse extends RunnableImpl
	{
		public FinalAndCollapse()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void runImpl()
		{
			clearReflection(5, true);
		}
	}
	
	private class SpawnBallista extends RunnableImpl
	{
		public SpawnBallista()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void runImpl()
		{
			NpcInstance ballista = addSpawnWithoutRespawn(Ballista, new Location(-177125, 147856, -11384, 49140), 0);
			ballista.addListener(_currentHpListener);
		}
	}
}
