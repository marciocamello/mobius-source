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
import lineage2.gameserver.instancemanager.WorldStatisticsManager;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.worldstatistics.CategoryType;
import lineage2.gameserver.network.serverpackets.ExSendUIEvent;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author KilRoy
 */
public class IsthinaNormal extends Reflection
{
	public int ballistaSeconds = 30;
	public long ballistaDamage = 0;
	private static final int Isthina = 29195;
	private static final int Ballista = 19021;
	private static final int Rumiese = 33293;
	private static final int Camera = 18919;
	private final ZoneListener _epicZoneListener = new ZoneListener();
	final DeathListener _deathListener = new DeathListener();
	final CurrentHpListener _currentHpListenerBallista = new CurrentHpListener();
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
	
	boolean checkstartCond(int raidplayers)
	{
		return !((raidplayers < getInstancedZone().getMinParty()) || _startLaunched);
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
				ThreadPoolManager.getInstance().schedule(new StartNormalIsthina(), 15000L);
				_startLaunched = true;
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
	
	private class StartNormalIsthina extends RunnableImpl
	{
		/**
		 *
		 */
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
			NpcInstance isthinaNormal = addSpawnWithoutRespawn(Isthina, new Location(-177125, 147856, -11384, 49140), 0);
			isthinaNormal.addListener(_deathListener);
			NpcInstance camera = addSpawnWithoutRespawn(Camera, new Location(-177325, 147856, -11384, 49140), 0);
			camera.setTargetable(false);
			
			for (Player player : getPlayers())
			{
				player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_ISTHINA_OPENING);
			}
		}
	}
	
	public class CurrentHpListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(final Creature actor, final double damage, final Creature attacker, Skill skill)
		{
			if (actor.getNpcId() == Ballista)
			{
				if (actor.isDead())
				{
					return;
				}
				
				ballistaDamage += damage;
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						if (actor.isDead())
						{
							return;
						}
						
						if (!_lockedTurn && (ballistaSeconds <= 0))
						{
							_lockedTurn = true;
							
							if (ballistaDamage < actor.getMaxHp())
							{
								double damagePercent = ballistaDamage / 4660000.0;
								int rewardId = 0;
								
								if (damagePercent > 0.5)
								{
									rewardId = 30374;
								}
								else if (damagePercent > 0.15)
								{
									rewardId = 30371;
								}
								
								Functions.addItem(attacker.getPlayer(), rewardId, 1);
								ThreadPoolManager.getInstance().schedule(new IsthinaDeathFinalA(), 10);
								actor.removeListener(_currentHpListenerBallista);
								WorldStatisticsManager.getInstance().updateStat(attacker.getPlayer(), CategoryType.EPIC_BOSS_KILLS_29195, 1);
							}
							else
							{
								ThreadPoolManager.getInstance().schedule(new IsthinaDeathFinalB(), 10);
								actor.removeListener(_currentHpListenerBallista);
								WorldStatisticsManager.getInstance().updateStat(attacker.getPlayer(), CategoryType.EPIC_BOSS_KILLS_29195, 1);
							}
						}
						
						int progress = (int) Math.min(6000, (ballistaDamage / 4660000) * 6000);
						progress -= progress % 60;
						
						for (Player player : getPlayers())
						{
							player.sendPacket(new ExSendUIEvent(player, 2, ballistaSeconds, progress, 122520, NpcString.NONE2));
						}
						
						ballistaSeconds -= 1;
					}
				}, 1000);
			}
		}
	}
	
	private class DeathListener implements OnDeathListener
	{
		/**
		 *
		 */
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
	
	private class IsthinaDeath extends RunnableImpl
	{
		/**
		 *
		 */
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
			
			ThreadPoolManager.getInstance().schedule(new SpawnBallista(), 7200L); // 7.2 secs for movie
		}
	}
	
	private class IsthinaDeathFinalA extends RunnableImpl
	{
		/**
		 *
		 */
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
			
			ThreadPoolManager.getInstance().schedule(new FinalAndCollapse(), 23300); // 23.3 secs for movie
			
			for (NpcInstance n : getNpcs())
			{
				n.deleteMe();
			}
			
			openDoor(14220100);
			openDoor(14220101);
		}
	}
	
	private class IsthinaDeathFinalB extends RunnableImpl
	{
		/**
		 *
		 */
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
			
			ThreadPoolManager.getInstance().schedule(new FinalAndCollapse(), 22200L); // 22.2 secs for movie
			
			for (NpcInstance n : getNpcs())
			{
				n.deleteMe();
			}
			
			openDoor(14220100);
			openDoor(14220101);
		}
	}
	
	private class FinalAndCollapse extends RunnableImpl
	{
		/**
		 *
		 */
		public FinalAndCollapse()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void runImpl()
		{
			clearReflection(5, true);
			addSpawnWithoutRespawn(Rumiese, new Location(-177125, 147856, -11384, 49140), 0);
		}
	}
	
	private class SpawnBallista extends RunnableImpl
	{
		/**
		 *
		 */
		public SpawnBallista()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void runImpl()
		{
			NpcInstance ballista = addSpawnWithoutRespawn(Ballista, new Location(-177125, 147856, -11384, 49140), 0);
			ballista.block();
			ballista.addListener(_currentHpListenerBallista);
			
			for (Player players : getPlayers())
			{
				players.sendPacket(new ExShowScreenMessage(NpcString.AFTER_$s1_SECONDS_THE_CHARGING_MAGIC_BALLISTAS_STARTS, 5000, ScreenMessageAlign.TOP_CENTER, false, String.valueOf(1)));
			}
			
			NpcInstance isthinaFinal = addSpawnWithoutRespawn(Isthina, new Location(-177128, 147224, -11414, 16383), 0);
			isthinaFinal.setTargetable(false);
			isthinaFinal.block();
		}
	}
}