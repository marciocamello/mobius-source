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

import lineage2.commons.geometry.Polygon;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SceneMovie;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Location;

/**
 * @author Awakeninger + Nache
 */
public class Balok extends Reflection
{
	// Npcs
	private static final int BALOK = 29218;
	private static final int BALOK_SLAVE = 29219;
	private static final int ROOM_WARDEN = 23123;
	// Locations
	public Player player;
	public Location Balokspawn = new Location(153576, 142088, -12762, 16383);
	public DeathListener _deathListener = new DeathListener();
	public CurrentHpListener _currentHpListener = new CurrentHpListener();
	private static final long BeforeDelay = 42 * 1000L;
	public static Territory centralRoomPoint = new Territory().add(new Polygon().add(152712, 142936).add(154360, 142936).add(154360, 141288).add(152712, 141288).setZmax(-12862).setZmin(-12700));
	public static final int[][] WARDEN_SPAWNS =
	{
		{
			153571,
			140838,
			52800,
			16616
		},
		{
			154190,
			141001,
			52800,
			20928
		},
		{
			154190,
			141001,
			52800,
			20928
		},
		{
			154847,
			142076,
			52800,
			32640
		},
		{
			154672,
			141454,
			52800,
			25528
		},
		{
			152468,
			141458,
			52800,
			7976
		},
		{
			152311,
			142070,
			52800,
			0
		},
		{
			154203,
			143156,
			52800,
			40720
		},
		{
			152944,
			143160,
			52800,
			54400
		}
	};
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		ThreadPoolManager.getInstance().schedule(new FirstStage(), BeforeDelay);
	}
	
	private class DeathListener implements OnDeathListener
	{
		public DeathListener()
		{
		}
		
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (self.isNpc() && (self.getId() == BALOK))
			{
				for (Player p : getPlayers())
				{
					p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5));
				}
				startCollapseTimer(5 * 60 * 1000L);
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
			for (Player player : getPlayers())
			{
				player.showQuestMovie(SceneMovie.si_barlog_opening);
			}
			closeDoor(24220008);
			ThreadPoolManager.getInstance().schedule(new BalokSpawn(), 19300);
			ThreadPoolManager.getInstance().schedule(new RoomWardenSpawn(), 180000);
		}
	}
	
	public class CurrentHpListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			if (actor.getId() == BALOK)
			{
				if (actor.isDead())
				{
					return;
				}
				
				if (actor.getCurrentHp() <= 7850000)
				{
					ThreadPoolManager.getInstance().schedule(new CaveStage(), 0);
					actor.removeListener(_currentHpListener);
				}
			}
		}
		
	}
	
	public class CaveStage extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			NpcInstance Slave1 = addSpawnWithoutRespawn(BALOK_SLAVE, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
			NpcInstance Slave2 = addSpawnWithoutRespawn(BALOK_SLAVE, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
			NpcInstance Slave3 = addSpawnWithoutRespawn(BALOK_SLAVE, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
			NpcInstance Slave4 = addSpawnWithoutRespawn(BALOK_SLAVE, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
			NpcInstance Slave5 = addSpawnWithoutRespawn(BALOK_SLAVE, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
			NpcInstance Slave6 = addSpawnWithoutRespawn(BALOK_SLAVE, Territory.getRandomLoc(centralRoomPoint, getGeoIndex()), 150);
			for (Player p : Slave1.getReflection().getPlayers())
			{
				Slave1.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 1000);
				Slave2.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 1000);
				Slave3.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 1000);
				Slave4.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 1000);
				Slave5.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 1000);
				Slave6.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 1000);
			}
			
		}
	}
	
	public class RoomWardenSpawn extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			for (int[] loc : WARDEN_SPAWNS)
			{
				addSpawnWithoutRespawn(ROOM_WARDEN, new Location(loc[0], loc[1], loc[2], loc[3]), 0);
			}
		}
	}
	
	public class BalokSpawn extends RunnableImpl
	{
		public BalokSpawn()
		{
		}
		
		@Override
		public void runImpl()
		{
			NpcInstance BalokStay = addSpawnWithoutRespawn(BALOK, Balokspawn, 0);
			BalokStay.addListener(_deathListener);
			BalokStay.addListener(_currentHpListener);
			
		}
	}
}