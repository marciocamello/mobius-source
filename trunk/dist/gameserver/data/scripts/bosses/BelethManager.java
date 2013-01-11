/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package bosses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.RaidBossInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;
import lineage2.gameserver.utils.ReflectionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BelethManager extends Functions implements ScriptFile
{
	private static final Logger _log = LoggerFactory.getLogger(BelethManager.class);
	static Zone _zone = ReflectionUtils.getZone("[Beleth_room]");
	private static ZoneListener _zoneListener = new ZoneListener();
	static List<Player> _indexedPlayers = new ArrayList<>();
	static List<NpcInstance> _npcList = new ArrayList<>();
	private static final int _doorWaitTimeDuration = 60000;
	private static final int _spawnWaitTimeDuration = 120000;
	private static final int _closeDoorTimeDuration = 180000;
	private static final int _clonesRespawnTimeTimeDuration = 40000;
	private static final int _ringAvailableTime = 300000;
	private static final int _clearEntityTime = 600000;
	private static final long _belethRespawnTime = 2 * 24 * 60 * 60 * 1000;
	private static final long _entityInactivityTime = 2 * 60 * 60 * 1000;
	private static final int _ringSpawnTime = 300000;
	private static final int _lastSpawnTime = 600000;
	private static final int DOOR = 20240001;
	private static final int CORRDOOR = 20240002;
	private static final int COFFDOOR = 20240003;
	static boolean _taskStarted = false;
	static boolean _entryLocked = false;
	private static boolean _ringAvailable = false;
	static boolean _belethAlive = false;
	private static final int VORTEX = 29125;
	private static final int ELF = 29128;
	private static final int COFFIN = 32470;
	private static final int BELETH = 29118;
	private static final int CLONE = 29119;
	private static final int locZ = -9353;
	static final int[] VORTEXSPAWN =
	{
		16325,
		214983,
		-9353
	};
	static final int[] COFFSPAWN =
	{
		12471,
		215602,
		-9360,
		49152
	};
	static final int[] BELSPAWN =
	{
		16325,
		214614,
		-9353,
		49152
	};
	static RaidBossInstance _beleth = null;
	private static final int centerX = 16325;
	private static final int centerY = 213135;
	static Map<MonsterInstance, Location> _clones = new ConcurrentHashMap<>();
	static Location[] _cloneLoc = new Location[56];
	static ScheduledFuture<?> cloneRespawnTask;
	static ScheduledFuture<?> ringSpawnTask;
	static ScheduledFuture<?> lastSpawnTask;
	
	static NpcInstance spawn(int npcId, int x, int y, int z, int h)
	{
		Location loc = new Location(x, y, z);
		NpcTemplate template = NpcHolder.getInstance().getTemplate(npcId);
		NpcInstance npc = template.getNewInstance();
		npc.setSpawnedLoc(loc);
		npc.setLoc(loc);
		npc.setHeading(h);
		npc.spawnMe();
		return npc;
	}
	
	public static Zone getZone()
	{
		return _zone;
	}
	
	static boolean checkPlayer(Player player)
	{
		if (player.isDead() || (player.getLevel() < 80))
		{
			return false;
		}
		return true;
	}
	
	static boolean checkBossSpawnCond()
	{
		if ((_indexedPlayers.size() < 50) || _taskStarted)
		{
			return false;
		}
		if (ServerVariables.getLong("BelethKillTime", 0) > System.currentTimeMillis())
		{
			return false;
		}
		return true;
	}
	
	public static class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature actor)
		{
			if (!actor.isPlayer() || _entryLocked)
			{
				return;
			}
			Player player = actor.getPlayer();
			if (!_indexedPlayers.contains(player))
			{
				if (checkPlayer(player))
				{
					_indexedPlayers.add(player);
				}
			}
			if (checkBossSpawnCond())
			{
				ThreadPoolManager.getInstance().schedule(new BelethSpawnTask(), 10000L);
				_taskStarted = true;
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature actor)
		{
			if (!actor.isPlayer())
			{
				return;
			}
			Player player = actor.getPlayer();
			if (_indexedPlayers.contains(player))
			{
				_indexedPlayers.remove(player);
			}
		}
	}
	
	private static class CloneRespawnTask extends RunnableImpl
	{
		public CloneRespawnTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void runImpl()
		{
			if ((_clones == null) || _clones.isEmpty())
			{
				return;
			}
			MonsterInstance nextclone;
			for (MonsterInstance clone : _clones.keySet())
			{
				if (clone.isDead() || clone.isDeleted())
				{
					nextclone = (MonsterInstance) spawn(CLONE, _clones.get(clone).x, _clones.get(clone).y, locZ, 49152);
					_clones.put(nextclone, nextclone.getLoc());
					_clones.remove(clone);
				}
			}
		}
	}
	
	private static class BelethSpawnTask extends RunnableImpl
	{
		public BelethSpawnTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void runImpl()
		{
			_indexedPlayers.clear();
			ThreadPoolManager.getInstance().schedule(new eventExecutor(Event.start), 10000L);
			ThreadPoolManager.getInstance().schedule(new eventExecutor(Event.inactivity_check), _entityInactivityTime);
			initSpawnLocs();
		}
	}
	
	private enum Event
	{
		none,
		start,
		open_door,
		close_door,
		beleth_spawn,
		beleth_despawn,
		clone_despawn,
		clone_spawn,
		ring_unset,
		beleth_dead,
		entity_clear,
		inactivity_check,
		spawn_ring,
		spawn_extras
	}
	
	public static class eventExecutor extends RunnableImpl
	{
		Event _event;
		
		eventExecutor(Event event)
		{
			_event = event;
		}
		
		@Override
		public void runImpl()
		{
			switch (_event)
			{
				case start:
					ThreadPoolManager.getInstance().schedule(new eventExecutor(Event.open_door), _doorWaitTimeDuration);
					break;
				case open_door:
					ReflectionUtils.getDoor(DOOR).openMe();
					ThreadPoolManager.getInstance().schedule(new eventExecutor(Event.close_door), _closeDoorTimeDuration);
					break;
				case close_door:
					ReflectionUtils.getDoor(DOOR).closeMe();
					_entryLocked = true;
					ThreadPoolManager.getInstance().schedule(new eventExecutor(Event.beleth_spawn), _spawnWaitTimeDuration);
					break;
				case beleth_spawn:
					NpcInstance temp = spawn(VORTEX, VORTEXSPAWN[0], VORTEXSPAWN[1], VORTEXSPAWN[2], 16384);
					_npcList.add(temp);
					_beleth = (RaidBossInstance) spawn(BELETH, BELSPAWN[0], BELSPAWN[1], BELSPAWN[2], BELSPAWN[3]);
					_beleth.startImmobilized();
					_belethAlive = true;
					ThreadPoolManager.getInstance().schedule(new eventExecutor(Event.clone_spawn), 10);
					ringSpawnTask = ThreadPoolManager.getInstance().schedule(new eventExecutor(Event.spawn_ring), _ringSpawnTime);
					lastSpawnTask = ThreadPoolManager.getInstance().schedule(new eventExecutor(Event.spawn_extras), _lastSpawnTime);
					break;
				case clone_spawn:
					MonsterInstance clone;
					for (int i = 0; i < 32; i++)
					{
						clone = (MonsterInstance) spawn(CLONE, _cloneLoc[i].x, _cloneLoc[i].y, locZ, 49152);
						_clones.put(clone, clone.getLoc());
					}
					cloneRespawnTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new CloneRespawnTask(), _clonesRespawnTimeTimeDuration, _clonesRespawnTimeTimeDuration);
					break;
				case spawn_ring:
					for (int i = 32; i < 48; i++)
					{
						spawnClone(i);
					}
					break;
				case spawn_extras:
					for (int i = 48; i < 56; i++)
					{
						spawnClone(i);
					}
					break;
				case beleth_dead:
					if (cloneRespawnTask != null)
					{
						cloneRespawnTask.cancel(false);
						cloneRespawnTask = null;
					}
					if (ringSpawnTask != null)
					{
						ringSpawnTask.cancel(false);
						ringSpawnTask = null;
					}
					if (lastSpawnTask != null)
					{
						lastSpawnTask.cancel(false);
						lastSpawnTask = null;
					}
					temp = spawn(ELF, _beleth.getLoc().x, _beleth.getLoc().y, locZ, BELSPAWN[3]);
					_npcList.add(temp);
					temp = spawn(COFFIN, COFFSPAWN[0], COFFSPAWN[1], COFFSPAWN[2], COFFSPAWN[3]);
					_npcList.add(temp);
					ReflectionUtils.getDoor(CORRDOOR).openMe();
					ReflectionUtils.getDoor(COFFDOOR).openMe();
					setRingAvailable(true);
					_belethAlive = false;
					ServerVariables.set("BelethKillTime", System.currentTimeMillis() + _belethRespawnTime);
					for (Player i : _zone.getInsidePlayers())
					{
						i.sendMessage("Beleth's Lair will push you out in 10 minutes");
					}
					ThreadPoolManager.getInstance().schedule(new eventExecutor(Event.clone_despawn), 10);
					ThreadPoolManager.getInstance().schedule(new eventExecutor(Event.ring_unset), _ringAvailableTime);
					ThreadPoolManager.getInstance().schedule(new eventExecutor(Event.entity_clear), _clearEntityTime);
					break;
				case ring_unset:
					setRingAvailable(false);
					break;
				case entity_clear:
					for (NpcInstance n : _npcList)
					{
						if (n != null)
						{
							n.deleteMe();
						}
					}
					_npcList.clear();
					ReflectionUtils.getDoor(CORRDOOR).closeMe();
					ReflectionUtils.getDoor(COFFDOOR).closeMe();
					for (Player i : _zone.getInsidePlayers())
					{
						i.teleToLocation(new Location(-11802, 236360, -3271));
						i.sendMessage("Beleth's Lair has become unstable so you've been teleported out");
					}
					_entryLocked = false;
					_taskStarted = false;
					break;
				case clone_despawn:
					for (MonsterInstance clonetodelete : _clones.keySet())
					{
						clonetodelete.deleteMe();
					}
					_clones.clear();
					break;
				case inactivity_check:
					if (!_beleth.isDead())
					{
						_beleth.deleteMe();
						ThreadPoolManager.getInstance().schedule(new eventExecutor(Event.entity_clear), 10);
					}
					break;
			}
		}
	}
	
	public static boolean isRingAvailable()
	{
		return _ringAvailable;
	}
	
	public static void setRingAvailable(boolean value)
	{
		_ringAvailable = value;
	}
	
	public static void setBelethDead()
	{
		if (_entryLocked && _belethAlive)
		{
			ThreadPoolManager.getInstance().schedule(new eventExecutor(Event.beleth_dead), 10);
		}
	}
	
	static void spawnClone(int id)
	{
		MonsterInstance clone;
		clone = (MonsterInstance) spawn(CLONE, _cloneLoc[id].x, _cloneLoc[id].y, locZ, 49152);
		_clones.put(clone, clone.getLoc());
	}
	
	static void initSpawnLocs()
	{
		double angle = Math.toRadians(22.5);
		int radius = 700;
		for (int i = 0; i < 16; i++)
		{
			if ((i % 2) == 0)
			{
				radius -= 50;
			}
			else
			{
				radius += 50;
			}
			_cloneLoc[i] = new Location(centerX + (int) (radius * Math.sin(i * angle)), centerY + (int) (radius * Math.cos(i * angle)), PositionUtils.convertDegreeToClientHeading(270 - (i * 22.5)));
		}
		radius = 1340;
		angle = Math.asin(1 / Math.sqrt(3));
		int mulX = 1, mulY = 1, addH = 3;
		double decX = 1.0, decY = 1.0;
		for (int i = 0; i < 16; i++)
		{
			if ((i % 8) == 0)
			{
				mulX = 0;
			}
			else if (i < 8)
			{
				mulX = -1;
			}
			else
			{
				mulX = 1;
			}
			if ((i == 4) || (i == 12))
			{
				mulY = 0;
			}
			else if ((i > 4) && (i < 12))
			{
				mulY = -1;
			}
			else
			{
				mulY = 1;
			}
			if (((i % 8) == 1) || (i == 7) || (i == 15))
			{
				decX = 0.5;
			}
			else
			{
				decX = 1.0;
			}
			if (((i % 10) == 3) || (i == 5) || (i == 11))
			{
				decY = 0.5;
			}
			else
			{
				decY = 1.0;
			}
			if (((i + 2) % 4) == 0)
			{
				addH++;
			}
			_cloneLoc[i + 16] = new Location(centerX + (int) (radius * decX * mulX), centerY + (int) (radius * decY * mulY), PositionUtils.convertDegreeToClientHeading(180 + (addH * 90)));
		}
		angle = Math.toRadians(22.5);
		radius = 1000;
		for (int i = 0; i < 16; i++)
		{
			if ((i % 2) == 0)
			{
				radius -= 70;
			}
			else
			{
				radius += 70;
			}
			_cloneLoc[i + 32] = new Location(centerX + (int) (radius * Math.sin(i * angle)), centerY + (int) (radius * Math.cos(i * angle)), _cloneLoc[i].h);
		}
		int order = 48;
		radius = 650;
		for (int i = 1; i < 16; i += 2)
		{
			if ((i == 1) || (i == 15))
			{
				_cloneLoc[order] = new Location(_cloneLoc[i].x, _cloneLoc[i].y + radius, _cloneLoc[i + 16].h);
			}
			else if ((i == 3) || (i == 5))
			{
				_cloneLoc[order] = new Location(_cloneLoc[i].x + radius, _cloneLoc[i].y, _cloneLoc[i].h);
			}
			else if ((i == 7) || (i == 9))
			{
				_cloneLoc[order] = new Location(_cloneLoc[i].x, _cloneLoc[i].y - radius, _cloneLoc[i + 16].h);
			}
			else if ((i == 11) || (i == 13))
			{
				_cloneLoc[order] = new Location(_cloneLoc[i].x - radius, _cloneLoc[i].y, _cloneLoc[i].h);
			}
			order++;
		}
	}
	
	@Override
	public void onLoad()
	{
		getZone().addListener(_zoneListener);
		_log.info("Beleth Manager: Loaded successfuly");
	}
	
	@Override
	public void onReload()
	{
		getZone().removeListener(_zoneListener);
	}
	
	@Override
	public void onShutdown()
	{
	}
}
