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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExSendUIEvent;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.utils.Location;

/**
 * @author Iqman
 * @author GW
 * @author Nache
 */
public class IstinaInstance extends Reflection
{
	// Npcs
	public static final int ISTINA_LIGHT = 29195;
	public static final int ISTINA_HARD = 29196;
	public static final int ACID_ERUPTION_CAMERA = 18920;
	public static final int BALLISTA = 19021;
	private static final int ISTINAS_CREATION = 23125;
	private static final int SEALING_ENERGY = 19036;
	public static final int RUMIESE_OUTSIDE = 33293;
	public static final int RUMIESE_INSIDE = 33151;
	
	// Instances Ids
	public static final int INSTANCE_ID_LIGHT = 169;
	public static final int INSTANCE_ID_HARD = 170;
	
	// Locations
	public static final Location OUTSIDE = new Location(-178470, 147111, 2132);
	public static final Location ENTRANCE = new Location(-177120, 142293, -11274);
	public static final Location LAIR_ENTRANCE = new Location(-177104, 146452, -11389);
	public static final Location CENTER = new Location(-177125, 147856, -11384);
	
	public static final int BOX_CONTAINING_MATK = 30371;
	public static final int MAGIC_FILLED_BOX = 30374;
	public static final int BALLISTA_MAX_DAMAGE = 4660000;
	
	public final ZoneListener _epicZoneListener = new ZoneListener();
	public final CurrentHpListener _currentHpListener = new CurrentHpListener();
	public ScheduledFuture<?> _ballistaTimer = null;
	public short _ballistaReadySeconds = 5;
	public boolean _startLaunched = false;
	public boolean _lockedTurn = false;
	public boolean _entryLocked = false;
	public boolean _isHardInstance = false;
	public boolean _instanceDone = false;
	public int _ballistaSeconds = 30;
	public long _ballistaDamage = 0;
	public NpcInstance _ballista = null;
	
	public List<NpcInstance> acidEruptionCameras = new ArrayList<>(3);
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		getZone("[Isthina_epic]").addListener(_epicZoneListener);
		_isHardInstance = getInstancedZoneId() == INSTANCE_ID_HARD;
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
			
			if (!cha.isPlayer())
			{
				return;
			}
			
			if (zone.getInsidePlayers().size() >= getPlayers().size())
			{
				ThreadPoolManager.getInstance().schedule(new StartIsthinaOpenMovie(), 15000L);
				_startLaunched = true;
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
			//
		}
	}
	
	public class StartIsthinaOpenMovie extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			for (Player player : getPlayers())
			{
				player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_ISTHINA_OPENING);
			}
			
			ThreadPoolManager.getInstance().schedule(new SpawnIsthina(), 36700L);
		}
	}
	
	public class SpawnIsthina extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			_entryLocked = true;
			int npcId = _isHardInstance ? ISTINA_HARD : ISTINA_LIGHT;
			addSpawnWithoutRespawn(npcId, new Location(-177125, 147856, -11384, 49140), 0);
			for (byte i = 0; i < 4; i = (byte) (i + 1))
			{
				acidEruptionCameras.add(addSpawnWithoutRespawn(ACID_ERUPTION_CAMERA, new Location(CENTER.getX(), CENTER.getY(), CENTER.getZ(), 49140), 0));
			}
			closeDoor(14220100);
			closeDoor(14220101);
		}
	}
	
	public void presentBallista(NpcInstance npc)
	{
		for (Player player : getPlayers())
		{
			player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_ISTHINA_BRIDGE);
		}
		if ((npc.getId() == ACID_ERUPTION_CAMERA) && (npc.getId() == ISTINAS_CREATION) && (npc.getId() == SEALING_ENERGY))
		{
			npc.deleteMe();
		}
		ThreadPoolManager.getInstance().schedule(new SpawnBallista(npc), 7200L); // 7.2 secs for movie
	}
	
	public class CurrentHpListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			if (actor.getId() == BALLISTA)
			{
				if (actor.isDead())
				{
					return;
				}
				_ballistaDamage += damage;
			}
		}
	}
	
	private class SpawnBallista extends RunnableImpl
	{
		public final NpcInstance _boss;
		
		SpawnBallista(NpcInstance boss)
		{
			_boss = boss;
		}
		
		@Override
		public void runImpl()
		{
			_ballista = addSpawnWithoutRespawn(BALLISTA, new Location(-177125, 147856, -11384, 49140), 0);
			_ballista.addListener(_currentHpListener);
			_ballista.addDeathImmunity();
			_ballista.startParalyzed();
			_ballista.setRandomWalk(false);
			_ballista.setTargetable(false);
			ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					if (_ballistaReadySeconds > 0)
					{
						for (Player player : getPlayers())
						{
							player.sendPacket(new ExShowScreenMessage(NpcStringId.AFTER_S1_SECONDS_THE_CHARGING_MAGIC_BALLISTAS_STARTS, 500, ScreenMessageAlign.MIDDLE_CENTER, String.valueOf(_ballistaReadySeconds)));
						}
					}
					else
					{
						for (Player player : getPlayers())
						{
							player.sendPacket(new ExShowScreenMessage(NpcStringId.START_CHARGING_MANA_BALLISTA, 3000, ScreenMessageAlign.MIDDLE_CENTER, String.valueOf(_ballistaReadySeconds)));
						}
						startBallista(_boss); // need to include mob
						_ballista.setTargetable(true);
						return;
					}
					_ballistaReadySeconds -= 1;
					ThreadPoolManager.getInstance().schedule(this, 1000L);
				}
			}, 8000L);
		}
	}
	
	public NpcInstance getBallista()
	{
		if (_ballista == null)
		{
			System.out.println("_ballista is null!");
		}
		return _ballista;
	}
	
	public void startBallista(final NpcInstance istina)
	{
		_ballistaTimer = ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if ((_ballistaSeconds <= 0) && !_instanceDone)
				{
					_ballistaTimer.cancel(true);
					
					if (_ballistaDamage < getBallista().getMaxHp())
					{
						if (getPlayers().size() < 1)
						{
							return;
						}
						
						istina.stopParalyzed();
						istina.setIsInvul(false);
						istina.getAggroList().addDamageHate(getPlayers().get(0), 1, 999);
						istina.doDie(getPlayers().get(0));
						istina.decayMe();
						
						for (Player player : getPlayers())
						{
							player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_ISTHINA_ENDING_B);
						}
					}
					else
					{
						if (getPlayers().size() < 1)
						{
							return;
						}
						
						istina.stopParalyzed();
						istina.setIsInvul(false);
						istina.getAggroList().addDamageHate(getPlayers().get(0), 1, 999);
						istina.doDie(getPlayers().get(0));
						istina.decayMe();
						
						double damagePercent = _ballistaDamage / BALLISTA_MAX_DAMAGE;
						int rewardId = 0;
						if (damagePercent > 0.5D)
						{
							rewardId = MAGIC_FILLED_BOX;
						}
						else if (damagePercent > 0.15D)
						{
							rewardId = BOX_CONTAINING_MATK;
						}
						for (Player player : getPlayers())
						{
							if (player != null)
							{
								player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_ISTHINA_ENDING_A);
								if (rewardId > 0)
								{
									player.getInventory().addItem(rewardId, 1);
								}
							}
						}
						_instanceDone = true;
					}
					
					for (NpcInstance npc : getNpcs())
					{
						if (npc.getId() == RUMIESE_OUTSIDE)
						{
							npc.teleToLocation(new Location(-177125, 147856, -11384, 49140), npc.getReflection());
							break;
						}
					}
					
					_ballista.deleteMe();
					startCollapseTimer(5 * 60 * 1000L);
					setReenterTime(System.currentTimeMillis());
				}
				
				int progress = (int) Math.min(6000, (_ballistaDamage / BALLISTA_MAX_DAMAGE) * 6000);
				progress -= progress % 60;
				
				for (Player player : getPlayers())
				{
					player.sendPacket(new ExSendUIEvent(player, 2, _ballistaSeconds, progress, 122520, NpcStringId.STRING_NONE));
				}
				
				if (_instanceDone)
				{
					_ballistaTimer.cancel(true);
					_ballistaTimer = null;
					
					for (NpcInstance npc : getNpcs())
					{
						if (npc.isMonster() && !npc.isRaid())
						{
							npc.deleteMe();
						}
					}
				}
				_ballistaSeconds -= 1;
			}
		}, 5000L, 1000L);
	}
}