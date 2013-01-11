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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.InstantZoneHolder;
import lineage2.gameserver.instancemanager.DelusionChamberManager;
import lineage2.gameserver.instancemanager.DelusionChamberManager.DelusionChamberRoom;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.InstantZone;
import lineage2.gameserver.utils.Location;

public class DelusionChamber extends Reflection
{
	private Future<?> killChamberTask;
	protected static final int MILLISECONDS_IN_MINUTE = 60000;
	protected int _roomType;
	protected int _choosenRoom = -1;
	protected boolean isBossRoom = false;
	protected List<Integer> _completedRooms = new ArrayList<>();
	protected int jumps_current = 0;
	protected boolean _hasJumped = false;
	private Future<?> teleporterTask;
	private Future<?> spawnTask;
	
	public DelusionChamber(Party party, int type, int room)
	{
		super();
		onCreate();
		startCollapseTimer(7200000);
		InstantZone iz = InstantZoneHolder.getInstance().getInstantZone(type + 120);
		setInstancedZone(iz);
		setName(iz.getName());
		_roomType = type;
		setParty(party);
		party.setReflection(this);
		_choosenRoom = room;
		checkBossRoom(_choosenRoom);
		Location coords = getRoomCoord(_choosenRoom);
		setReturnLoc(party.getPartyLeader().getLoc());
		setTeleportLoc(coords);
		for (Player p : party.getPartyMembers())
		{
			p.setVar("backCoords", getReturnLoc().toXYZString(), -1);
			DelusionChamberManager.teleToLocation(p, Location.findPointToStay(coords, 50, 100, getGeoIndex()), this);
			p.setReflection(this);
		}
		createSpawnTimer(_choosenRoom);
		createTeleporterTimer();
	}
	
	public void createSpawnTimer(int room)
	{
		if (spawnTask != null)
		{
			spawnTask.cancel(false);
			spawnTask = null;
		}
		final DelusionChamberRoom riftRoom = DelusionChamberManager.getInstance().getRoom(_roomType, room);
		spawnTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				for (SimpleSpawner s : riftRoom.getSpawns())
				{
					SimpleSpawner sp = s.clone();
					sp.setReflection(DelusionChamber.this);
					addSpawn(sp);
					if (!isBossRoom)
					{
						sp.startRespawn();
					}
					for (int i = 0; i < sp.getAmount(); i++)
					{
						sp.doSpawn(true);
					}
				}
				DelusionChamber.this.addSpawnWithoutRespawn(getManagerId(), riftRoom.getTeleportCoords(), 0);
			}
		}, 10000);
	}
	
	protected void createTeleporterTimer()
	{
		if (teleporterTask != null)
		{
			teleporterTask.cancel(false);
			teleporterTask = null;
		}
		teleporterTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if ((jumps_current < 4) && (getPlayersInside(true) > 0))
				{
					jumps_current++;
					teleportToNextRoom();
					createTeleporterTimer();
				}
				else
				{
					createNewKillChamberTimer();
				}
			}
		}, calcTimeToNextJump());
	}
	
	protected long calcTimeToNextJump()
	{
		if (isBossRoom)
		{
			return 60 * MILLISECONDS_IN_MINUTE;
		}
		return (8 * MILLISECONDS_IN_MINUTE) + Rnd.get(120000);
	}
	
	public Location getRoomCoord(int room)
	{
		return DelusionChamberManager.getInstance().getRoom(_roomType, room).getTeleportCoords();
	}
	
	public synchronized void createNewKillChamberTimer()
	{
		if (killChamberTask != null)
		{
			killChamberTask.cancel(false);
			killChamberTask = null;
		}
		killChamberTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl() throws Exception
			{
				if ((getParty() != null) && !getParty().getPartyMembers().isEmpty())
				{
					for (Player p : getParty().getPartyMembers())
					{
						if (p.getReflection() == DelusionChamber.this)
						{
							String var = p.getVar("backCoords");
							if ((var == null) || var.equals(""))
							{
								continue;
							}
							p.teleToLocation(Location.parseLoc(var), ReflectionManager.DEFAULT);
							p.unsetVar("backCoords");
						}
					}
				}
				collapse();
			}
		}, 100L);
	}
	
	public int getType()
	{
		return _roomType;
	}
	
	protected void teleportToNextRoom()
	{
		_completedRooms.add(_choosenRoom);
		for (Spawner s : getSpawns())
		{
			s.deleteAll();
		}
		int size = DelusionChamberManager.getInstance().getRooms(_roomType).size();
		if ((getType() >= 11) && (jumps_current == 4))
		{
			_choosenRoom = 9;
		}
		else
		{
			List<Integer> notCompletedRooms = new ArrayList<>();
			for (int i = 1; i <= size; i++)
			{
				if (!_completedRooms.contains(i))
				{
					notCompletedRooms.add(i);
				}
			}
			_choosenRoom = notCompletedRooms.get(Rnd.get(notCompletedRooms.size()));
		}
		checkBossRoom(_choosenRoom);
		setTeleportLoc(getRoomCoord(_choosenRoom));
		for (Player p : getParty().getPartyMembers())
		{
			if (p.getReflection() == this)
			{
				DelusionChamberManager.teleToLocation(p, Location.findPointToStay(getRoomCoord(_choosenRoom), 50, 100, DelusionChamber.this.getGeoIndex()), this);
			}
		}
		createSpawnTimer(_choosenRoom);
	}
	
	public void partyMemberExited(Player player)
	{
		if ((getPlayersInside(false) < 2) || (getPlayersInside(true) == 0))
		{
			createNewKillChamberTimer();
			return;
		}
	}
	
	protected int getPlayersInside(boolean alive)
	{
		if (_playerCount == 0)
		{
			return 0;
		}
		int sum = 0;
		for (Player p : getPlayers())
		{
			if (!alive || !p.isDead())
			{
				sum++;
			}
		}
		return sum;
	}
	
	public void manualExitChamber(Player player, NpcInstance npc)
	{
		if (!player.isInParty() || (player.getParty().getReflection() != this))
		{
			return;
		}
		if (!player.getParty().isLeader(player))
		{
			DelusionChamberManager.getInstance().showHtmlFile(player, "delusionchamber/NotPartyLeader.htm", npc);
			return;
		}
		createNewKillChamberTimer();
	}
	
	@Override
	public String getName()
	{
		InstantZone iz = InstantZoneHolder.getInstance().getInstantZone(_roomType + 120);
		return iz.getName();
	}
	
	protected int getManagerId()
	{
		return 32664;
	}
	
	public void checkBossRoom(int room)
	{
		isBossRoom = DelusionChamberManager.getInstance().getRoom(_roomType, room).isBossRoom();
	}
	
	public void manualTeleport(Player player, NpcInstance npc)
	{
		if (!player.isInParty() || !player.getParty().isInReflection() || !(player.getParty().getReflection() instanceof DelusionChamber))
		{
			return;
		}
		if (!player.getParty().isLeader(player))
		{
			DelusionChamberManager.getInstance().showHtmlFile(player, "delusionchamber/NotPartyLeader.htm", npc);
			return;
		}
		if (!isBossRoom)
		{
			if (_hasJumped)
			{
				DelusionChamberManager.getInstance().showHtmlFile(player, "delusionchamber/AlreadyTeleported.htm", npc);
				return;
			}
			_hasJumped = true;
		}
		else
		{
			manualExitChamber(player, npc);
			return;
		}
		teleportToNextRoom();
	}
}
