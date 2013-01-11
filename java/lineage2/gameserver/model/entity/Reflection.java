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
package lineage2.gameserver.model.entity;

import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.commons.listener.Listener;
import lineage2.commons.listener.ListenerList;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.actor.door.impl.MasterOnOpenCloseListenerImpl;
import lineage2.gameserver.listener.reflection.OnReflectionCollapseListener;
import lineage2.gameserver.listener.zone.impl.AirshipControllerZoneListener;
import lineage2.gameserver.listener.zone.impl.NoLandingZoneListener;
import lineage2.gameserver.listener.zone.impl.ResidenceEnterLeaveListenerImpl;
import lineage2.gameserver.model.CommandChannel;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.HardSpawner;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.DoorTemplate;
import lineage2.gameserver.templates.InstantZone;
import lineage2.gameserver.templates.ZoneTemplate;
import lineage2.gameserver.templates.spawn.SpawnTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

import org.apache.commons.lang3.StringUtils;
import org.napile.primitive.Containers;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reflection
{
	public class ReflectionListenerList extends ListenerList<Reflection>
	{
		public void onCollapse()
		{
			if (!getListeners().isEmpty())
			{
				for (Listener<Reflection> listener : getListeners())
				{
					((OnReflectionCollapseListener) listener).onReflectionCollapse(Reflection.this);
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(Reflection.class);
	private final static AtomicInteger _nextId = new AtomicInteger();
	private final int _id;
	private String _name = StringUtils.EMPTY;
	private InstantZone _instance;
	private int _geoIndex;
	private Location _resetLoc;
	private Location _returnLoc;
	private Location _teleportLoc;
	protected List<Spawner> _spawns = new ArrayList<>();
	public List<GameObject> _objects = new ArrayList<>();
	protected IntObjectMap<DoorInstance> _doors = Containers.emptyIntObjectMap();
	protected Map<String, Zone> _zones = Collections.emptyMap();
	protected Map<String, List<Spawner>> _spawners = Collections.emptyMap();
	protected TIntHashSet _visitors = new TIntHashSet();
	public final Lock lock = new ReentrantLock();
	protected int _playerCount;
	protected Party _party;
	protected CommandChannel _commandChannel;
	private int _collapseIfEmptyTime;
	private boolean _isCollapseStarted;
	private Future<?> _collapseTask;
	private Future<?> _collapse1minTask;
	private Future<?> _hiddencollapseTask;
	private final ReflectionListenerList listeners = new ReflectionListenerList();
	
	public Reflection()
	{
		this(_nextId.incrementAndGet());
	}
	
	private Reflection(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getInstancedZoneId()
	{
		return _instance == null ? 0 : _instance.getId();
	}
	
	public void setParty(Party party)
	{
		_party = party;
	}
	
	public Party getParty()
	{
		return _party;
	}
	
	public void setCommandChannel(CommandChannel commandChannel)
	{
		_commandChannel = commandChannel;
	}
	
	public void setCollapseIfEmptyTime(int value)
	{
		_collapseIfEmptyTime = value;
	}
	
	public String getName()
	{
		return _name;
	}
	
	protected void setName(String name)
	{
		_name = name;
	}
	
	public InstantZone getInstancedZone()
	{
		return _instance;
	}
	
	protected void setInstancedZone(InstantZone iz)
	{
		_instance = iz;
	}
	
	public int getGeoIndex()
	{
		return _geoIndex;
	}
	
	protected void setGeoIndex(int geoIndex)
	{
		_geoIndex = geoIndex;
	}
	
	public void setCoreLoc(Location l)
	{
		_resetLoc = l;
	}
	
	public Location getCoreLoc()
	{
		return _resetLoc;
	}
	
	public void setReturnLoc(Location l)
	{
		_returnLoc = l;
	}
	
	public Location getReturnLoc()
	{
		return _returnLoc;
	}
	
	public void setTeleportLoc(Location l)
	{
		_teleportLoc = l;
	}
	
	public Location getTeleportLoc()
	{
		return _teleportLoc;
	}
	
	public List<Spawner> getSpawns()
	{
		return _spawns;
	}
	
	public Collection<DoorInstance> getDoors()
	{
		return _doors.values();
	}
	
	public DoorInstance getDoor(int id)
	{
		return _doors.get(id);
	}
	
	public Zone getZone(String name)
	{
		return _zones.get(name);
	}
	
	public void startCollapseTimer(long timeInMillis)
	{
		if (isDefault())
		{
			new Exception("Basic reflection " + _id + " could not be collapsed!").printStackTrace();
			return;
		}
		lock.lock();
		try
		{
			if (_collapseTask != null)
			{
				_collapseTask.cancel(false);
				_collapseTask = null;
			}
			if (_collapse1minTask != null)
			{
				_collapse1minTask.cancel(false);
				_collapse1minTask = null;
			}
			_collapseTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					collapse();
				}
			}, timeInMillis);
			if (timeInMillis >= (60 * 1000L))
			{
				_collapse1minTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						minuteBeforeCollapse();
					}
				}, timeInMillis - (60 * 1000L));
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public void stopCollapseTimer()
	{
		lock.lock();
		try
		{
			if (_collapseTask != null)
			{
				_collapseTask.cancel(false);
				_collapseTask = null;
			}
			if (_collapse1minTask != null)
			{
				_collapse1minTask.cancel(false);
				_collapse1minTask = null;
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public void minuteBeforeCollapse()
	{
		if (_isCollapseStarted)
		{
			return;
		}
		lock.lock();
		try
		{
			for (GameObject o : _objects)
			{
				if (o.isPlayer())
				{
					((Player) o).sendPacket(new SystemMessage(SystemMessage.THIS_INSTANCE_ZONE_WILL_BE_TERMINATED_IN_S1_MINUTES_YOU_WILL_BE_FORCED_OUT_OF_THE_DANGEON_THEN_TIME_EXPIRES).addNumber(1));
				}
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public void collapse()
	{
		if (_id <= 0)
		{
			new Exception("Basic reflection " + _id + " could not be collapsed!").printStackTrace();
			return;
		}
		lock.lock();
		try
		{
			if (_isCollapseStarted)
			{
				return;
			}
			_isCollapseStarted = true;
		}
		finally
		{
			lock.unlock();
		}
		listeners.onCollapse();
		try
		{
			stopCollapseTimer();
			if (_hiddencollapseTask != null)
			{
				_hiddencollapseTask.cancel(false);
				_hiddencollapseTask = null;
			}
			for (Spawner s : _spawns)
			{
				s.deleteAll();
			}
			for (String group : _spawners.keySet())
			{
				despawnByGroup(group);
			}
			for (DoorInstance d : _doors.values())
			{
				d.deleteMe();
			}
			_doors.clear();
			for (Zone zone : _zones.values())
			{
				zone.setActive(false);
			}
			_zones.clear();
			List<Player> teleport = new ArrayList<>();
			List<GameObject> delete = new ArrayList<>();
			lock.lock();
			try
			{
				for (GameObject o : _objects)
				{
					if (o.isPlayer())
					{
						teleport.add((Player) o);
					}
					else if (!o.isPlayable())
					{
						delete.add(o);
					}
				}
			}
			finally
			{
				lock.unlock();
			}
			for (Player player : teleport)
			{
				if (player.getParty() != null)
				{
					if (equals(player.getParty().getReflection()))
					{
						player.getParty().setReflection(null);
					}
					if ((player.getParty().getCommandChannel() != null) && equals(player.getParty().getCommandChannel().getReflection()))
					{
						player.getParty().getCommandChannel().setReflection(null);
					}
				}
				if (equals(player.getReflection()))
				{
					if (getReturnLoc() != null)
					{
						player.teleToLocation(getReturnLoc(), ReflectionManager.DEFAULT);
					}
					else
					{
						player.setReflection(ReflectionManager.DEFAULT);
					}
				}
				onPlayerExit(player);
			}
			if (_commandChannel != null)
			{
				_commandChannel.setReflection(null);
				_commandChannel = null;
			}
			if (_party != null)
			{
				_party.setReflection(null);
				_party = null;
			}
			for (GameObject o : delete)
			{
				o.deleteMe();
			}
			_spawns.clear();
			_objects.clear();
			_visitors.clear();
			_doors.clear();
			_playerCount = 0;
			onCollapse();
		}
		finally
		{
			ReflectionManager.getInstance().remove(this);
			GeoEngine.FreeGeoIndex(getGeoIndex());
		}
	}
	
	protected void onCollapse()
	{
	}
	
	public void addObject(GameObject o)
	{
		if (_isCollapseStarted)
		{
			return;
		}
		lock.lock();
		try
		{
			_objects.add(o);
			if (o.isPlayer())
			{
				_playerCount++;
				_visitors.add(o.getObjectId());
				onPlayerEnter(o.getPlayer());
			}
		}
		finally
		{
			lock.unlock();
		}
		if ((_collapseIfEmptyTime > 0) && (_hiddencollapseTask != null))
		{
			_hiddencollapseTask.cancel(false);
			_hiddencollapseTask = null;
		}
	}
	
	public void removeObject(GameObject o)
	{
		if (_isCollapseStarted)
		{
			return;
		}
		lock.lock();
		try
		{
			if (!_objects.remove(o))
			{
				return;
			}
			if (o.isPlayer())
			{
				_playerCount--;
				onPlayerExit(o.getPlayer());
			}
		}
		finally
		{
			lock.unlock();
		}
		if ((_playerCount <= 0) && !isDefault() && (_hiddencollapseTask == null))
		{
			if (_collapseIfEmptyTime <= 0)
			{
				collapse();
			}
			else
			{
				_hiddencollapseTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						collapse();
					}
				}, _collapseIfEmptyTime * 60 * 1000L);
			}
		}
	}
	
	public void onPlayerEnter(Player player)
	{
		player.getInventory().validateItems();
	}
	
	public void onPlayerExit(Player player)
	{
		player.getInventory().validateItems();
	}
	
	public List<Player> getPlayers()
	{
		List<Player> result = new ArrayList<>();
		lock.lock();
		try
		{
			for (GameObject o : _objects)
			{
				if (o.isPlayer())
				{
					result.add((Player) o);
				}
			}
		}
		finally
		{
			lock.unlock();
		}
		return result;
	}
	
	public List<NpcInstance> getNpcs()
	{
		List<NpcInstance> result = new ArrayList<>();
		lock.lock();
		try
		{
			for (GameObject o : _objects)
			{
				if (o.isNpc())
				{
					result.add((NpcInstance) o);
				}
			}
		}
		finally
		{
			lock.unlock();
		}
		return result;
	}
	
	public List<NpcInstance> getAllByNpcId(int npcId, boolean onlyAlive)
	{
		List<NpcInstance> result = new ArrayList<>();
		lock.lock();
		try
		{
			for (GameObject o : _objects)
			{
				if (o.isNpc())
				{
					NpcInstance npc = (NpcInstance) o;
					if ((npcId == npc.getNpcId()) && (!onlyAlive || !npc.isDead()))
					{
						result.add(npc);
					}
				}
			}
		}
		finally
		{
			lock.unlock();
		}
		return result;
	}
	
	public boolean canChampions()
	{
		return _id <= 0;
	}
	
	public boolean isAutolootForced()
	{
		return false;
	}
	
	public boolean isCollapseStarted()
	{
		return _isCollapseStarted;
	}
	
	public void addSpawn(SimpleSpawner spawn)
	{
		if (spawn != null)
		{
			_spawns.add(spawn);
		}
	}
	
	public void fillSpawns(List<InstantZone.SpawnInfo> si)
	{
		if (si == null)
		{
			return;
		}
		for (InstantZone.SpawnInfo s : si)
		{
			SimpleSpawner c;
			switch (s.getSpawnType())
			{
				case 0:
					for (Location loc : s.getCoords())
					{
						c = new SimpleSpawner(s.getNpcId());
						c.setReflection(this);
						c.setRespawnDelay(s.getRespawnDelay(), s.getRespawnRnd());
						c.setAmount(s.getCount());
						c.setLoc(loc);
						c.doSpawn(true);
						if (s.getRespawnDelay() == 0)
						{
							c.stopRespawn();
						}
						else
						{
							c.startRespawn();
						}
						addSpawn(c);
					}
					break;
				case 1:
					c = new SimpleSpawner(s.getNpcId());
					c.setReflection(this);
					c.setRespawnDelay(s.getRespawnDelay(), s.getRespawnRnd());
					c.setAmount(1);
					c.setLoc(s.getCoords().get(Rnd.get(s.getCoords().size())));
					c.doSpawn(true);
					if (s.getRespawnDelay() == 0)
					{
						c.stopRespawn();
					}
					else
					{
						c.startRespawn();
					}
					addSpawn(c);
					break;
				case 2:
					c = new SimpleSpawner(s.getNpcId());
					c.setReflection(this);
					c.setRespawnDelay(s.getRespawnDelay(), s.getRespawnRnd());
					c.setAmount(s.getCount());
					c.setTerritory(s.getLoc());
					for (int j = 0; j < s.getCount(); j++)
					{
						c.doSpawn(true);
					}
					if (s.getRespawnDelay() == 0)
					{
						c.stopRespawn();
					}
					else
					{
						c.startRespawn();
					}
					addSpawn(c);
			}
		}
	}
	
	public void init(IntObjectMap<DoorTemplate> doors, Map<String, ZoneTemplate> zones)
	{
		if (!doors.isEmpty())
		{
			_doors = new HashIntObjectMap<>(doors.size());
		}
		for (DoorTemplate template : doors.values())
		{
			DoorInstance door = new DoorInstance(IdFactory.getInstance().getNextId(), template);
			door.setReflection(this);
			door.setIsInvul(true);
			door.spawnMe(template.getLoc());
			if (template.isOpened())
			{
				door.openMe();
			}
			_doors.put(template.getNpcId(), door);
		}
		initDoors();
		if (!zones.isEmpty())
		{
			_zones = new HashMap<>(zones.size());
		}
		for (ZoneTemplate template : zones.values())
		{
			Zone zone = new Zone(template);
			zone.setReflection(this);
			switch (zone.getType())
			{
				case no_landing:
				case SIEGE:
					zone.addListener(NoLandingZoneListener.STATIC);
					break;
				case AirshipController:
					zone.addListener(new AirshipControllerZoneListener());
					break;
				case RESIDENCE:
					zone.addListener(ResidenceEnterLeaveListenerImpl.STATIC);
					break;
			}
			if (template.isEnabled())
			{
				zone.setActive(true);
			}
			_zones.put(template.getName(), zone);
		}
	}
	
	private void init0(IntObjectMap<InstantZone.DoorInfo> doors, Map<String, InstantZone.ZoneInfo> zones)
	{
		if (!doors.isEmpty())
		{
			_doors = new HashIntObjectMap<>(doors.size());
		}
		for (InstantZone.DoorInfo info : doors.values())
		{
			DoorInstance door = new DoorInstance(IdFactory.getInstance().getNextId(), info.getTemplate());
			door.setReflection(this);
			door.setIsInvul(info.isInvul());
			door.spawnMe(info.getTemplate().getLoc());
			if (info.isOpened())
			{
				door.openMe();
			}
			_doors.put(info.getTemplate().getNpcId(), door);
		}
		initDoors();
		if (!zones.isEmpty())
		{
			_zones = new HashMap<>(zones.size());
		}
		for (InstantZone.ZoneInfo t : zones.values())
		{
			Zone zone = new Zone(t.getTemplate());
			zone.setReflection(this);
			switch (zone.getType())
			{
				case no_landing:
				case SIEGE:
					zone.addListener(NoLandingZoneListener.STATIC);
					break;
				case AirshipController:
					zone.addListener(new AirshipControllerZoneListener());
					break;
				case RESIDENCE:
					zone.addListener(ResidenceEnterLeaveListenerImpl.STATIC);
					break;
			}
			if (t.isActive())
			{
				zone.setActive(true);
			}
			_zones.put(t.getTemplate().getName(), zone);
		}
	}
	
	private void initDoors()
	{
		for (DoorInstance door : _doors.values())
		{
			if (door.getTemplate().getMasterDoor() > 0)
			{
				DoorInstance masterDoor = getDoor(door.getTemplate().getMasterDoor());
				masterDoor.addListener(new MasterOnOpenCloseListenerImpl(door));
			}
		}
	}
	
	public void openDoor(int doorId)
	{
		DoorInstance door = _doors.get(doorId);
		if (door != null)
		{
			door.openMe();
		}
	}
	
	public void closeDoor(int doorId)
	{
		DoorInstance door = _doors.get(doorId);
		if (door != null)
		{
			door.closeMe();
		}
	}
	
	public void clearReflection(int timeInMinutes, boolean message)
	{
		if (isDefault())
		{
			return;
		}
		for (NpcInstance n : getNpcs())
		{
			n.deleteMe();
		}
		startCollapseTimer(timeInMinutes * 60 * 1000L);
		if (message)
		{
			for (Player pl : getPlayers())
			{
				if (pl != null)
				{
					pl.sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(timeInMinutes));
				}
			}
		}
	}
	
	public NpcInstance addSpawnWithoutRespawn(int npcId, Location loc, int randomOffset)
	{
		Location newLoc;
		if (randomOffset > 0)
		{
			newLoc = Location.findPointToStay(loc, 0, randomOffset, getGeoIndex()).setH(loc.h);
		}
		else
		{
			newLoc = loc;
		}
		return NpcUtils.spawnSingle(npcId, newLoc, this);
	}
	
	public NpcInstance addSpawnWithRespawn(int npcId, Location loc, int randomOffset, int respawnDelay)
	{
		SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(npcId));
		sp.setLoc(randomOffset > 0 ? Location.findPointToStay(loc, 0, randomOffset, getGeoIndex()) : loc);
		sp.setReflection(this);
		sp.setAmount(1);
		sp.setRespawnDelay(respawnDelay);
		sp.doSpawn(true);
		sp.startRespawn();
		return sp.getLastSpawn();
	}
	
	public boolean isDefault()
	{
		return getId() <= 0;
	}
	
	public int[] getVisitors()
	{
		return _visitors.toArray();
	}
	
	public void setReenterTime(long time)
	{
		int[] players = null;
		lock.lock();
		try
		{
			players = _visitors.toArray();
		}
		finally
		{
			lock.unlock();
		}
		if (players != null)
		{
			Player player;
			for (int objectId : players)
			{
				try
				{
					player = World.getPlayer(objectId);
					if (player != null)
					{
						player.setInstanceReuse(getInstancedZoneId(), time);
					}
					else
					{
						mysql.set("REPLACE INTO character_instances (obj_id, id, reuse) VALUES (?,?,?)", objectId, getInstancedZoneId(), time);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	protected void onCreate()
	{
		ReflectionManager.getInstance().add(this);
	}
	
	public static Reflection createReflection(int id)
	{
		if (id > 0)
		{
			throw new IllegalArgumentException("id should be <= 0");
		}
		return new Reflection(id);
	}
	
	public void init(InstantZone instantZone)
	{
		setName(instantZone.getName());
		setInstancedZone(instantZone);
		if (instantZone.getMapX() >= 0)
		{
			int geoIndex = GeoEngine.NextGeoIndex(instantZone.getMapX(), instantZone.getMapY(), getId());
			setGeoIndex(geoIndex);
		}
		setTeleportLoc(instantZone.getTeleportCoord());
		if (instantZone.getReturnCoords() != null)
		{
			setReturnLoc(instantZone.getReturnCoords());
		}
		fillSpawns(instantZone.getSpawnsInfo());
		if (instantZone.getSpawns().size() > 0)
		{
			_spawners = new HashMap<>(instantZone.getSpawns().size());
			for (Map.Entry<String, InstantZone.SpawnInfo2> entry : instantZone.getSpawns().entrySet())
			{
				List<Spawner> spawnList = new ArrayList<>(entry.getValue().getTemplates().size());
				_spawners.put(entry.getKey(), spawnList);
				for (SpawnTemplate template : entry.getValue().getTemplates())
				{
					HardSpawner spawner = new HardSpawner(template);
					spawnList.add(spawner);
					spawner.setAmount(template.getCount());
					spawner.setRespawnDelay(template.getRespawn(), template.getRespawnRandom());
					spawner.setReflection(this);
					spawner.setRespawnTime(0);
				}
				if (entry.getValue().isSpawned())
				{
					spawnByGroup(entry.getKey());
				}
			}
		}
		init0(instantZone.getDoors(), instantZone.getZones());
		setCollapseIfEmptyTime(instantZone.getCollapseIfEmpty());
		startCollapseTimer(instantZone.getTimelimit() * 60 * 1000L);
		onCreate();
	}
	
	public void spawnByGroup(String name)
	{
		List<Spawner> list = _spawners.get(name);
		if (list == null)
		{
			throw new IllegalArgumentException(name);
		}
		for (Spawner s : list)
		{
			s.init();
		}
	}
	
	public void despawnByGroup(String name)
	{
		List<Spawner> list = _spawners.get(name);
		if (list == null)
		{
			throw new IllegalArgumentException();
		}
		for (Spawner s : list)
		{
			s.deleteAll();
		}
	}
	
	public Collection<Zone> getZones()
	{
		return _zones.values();
	}
	
	public <T extends Listener<Reflection>> boolean addListener(T listener)
	{
		return listeners.add(listener);
	}
	
	public <T extends Listener<Reflection>> boolean removeListener(T listener)
	{
		return listeners.remove(listener);
	}
}
