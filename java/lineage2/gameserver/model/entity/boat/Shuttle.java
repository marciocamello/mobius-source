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
package lineage2.gameserver.model.entity.boat;

import gnu.trove.map.hash.TIntObjectHashMap;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.ShuttleWayEvent;
import lineage2.gameserver.network.serverpackets.ExMTLInShuttlePacket;
import lineage2.gameserver.network.serverpackets.ExShuttleGetOffPacket;
import lineage2.gameserver.network.serverpackets.ExShuttleGetOnPacket;
import lineage2.gameserver.network.serverpackets.ExShuttleInfoPacket;
import lineage2.gameserver.network.serverpackets.ExShuttleMovePacket;
import lineage2.gameserver.network.serverpackets.ExStopMoveInShuttlePacket;
import lineage2.gameserver.network.serverpackets.ExValidateLocationInShuttlePacket;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.StopMove;
import lineage2.gameserver.network.serverpackets.VehicleCheckLocation;
import lineage2.gameserver.network.serverpackets.VehicleStart;
import lineage2.gameserver.templates.ShuttleTemplate;
import lineage2.gameserver.utils.Location;

public class Shuttle extends Boat
{
	private static class Docked extends RunnableImpl
	{
		private final Shuttle _shuttle;
		
		public Docked(Shuttle shuttle)
		{
			_shuttle = shuttle;
		}
		
		@Override
		public void runImpl()
		{
			if (_shuttle == null)
			{
				return;
			}
			_shuttle.getCurrentFloor().stopEvent();
			_shuttle.getNextFloor().reCalcNextTime(false);
		}
	}
	
	private static final long serialVersionUID = 1L;
	private final TIntObjectHashMap<ShuttleWayEvent> _floors = new TIntObjectHashMap<>();
	private boolean _moveBack;
	public int _currentWay;
	
	public Shuttle(int objectId, ShuttleTemplate template)
	{
		super(objectId, template);
		setFlying(true);
	}
	
	@Override
	public int getBoatId()
	{
		return getTemplate().getId();
	}
	
	@Override
	public final ShuttleTemplate getTemplate()
	{
		return (ShuttleTemplate) _template;
	}
	
	@Override
	public void onSpawn()
	{
		_moveBack = false;
		_currentWay = 0;
		getCurrentFloor().reCalcNextTime(false);
	}
	
	@Override
	public void onEvtArrived()
	{
		ThreadPoolManager.getInstance().schedule(new Docked(this), 1500L);
	}
	
	@Override
	public L2GameServerPacket infoPacket()
	{
		return new ExShuttleInfoPacket(this);
	}
	
	@Override
	public L2GameServerPacket movePacket()
	{
		return new ExShuttleMovePacket(this);
	}
	
	@Override
	public L2GameServerPacket inMovePacket(Player player, Location src, Location desc)
	{
		return new ExMTLInShuttlePacket(player, this, src, desc);
	}
	
	@Override
	public L2GameServerPacket stopMovePacket()
	{
		return new StopMove(this);
	}
	
	@Override
	public L2GameServerPacket inStopMovePacket(Player player)
	{
		return new ExStopMoveInShuttlePacket(player);
	}
	
	@Override
	public L2GameServerPacket startPacket()
	{
		return new VehicleStart(this);
	}
	
	@Override
	public L2GameServerPacket checkLocationPacket()
	{
		return new VehicleCheckLocation(this);
	}
	
	@Override
	public L2GameServerPacket validateLocationPacket(Player player)
	{
		return new ExValidateLocationInShuttlePacket(player);
	}
	
	@Override
	public L2GameServerPacket getOnPacket(Playable playable, Location location)
	{
		if (!playable.isPlayer())
		{
			return null;
		}
		return new ExShuttleGetOnPacket(playable, this, location);
	}
	
	@Override
	public L2GameServerPacket getOffPacket(Playable playable, Location location)
	{
		return new ExShuttleGetOffPacket(playable, this, location);
	}
	
	@Override
	public boolean isShuttle()
	{
		return true;
	}
	
	@Override
	public void oustPlayers()
	{
	}
	
	@Override
	public void trajetEnded(boolean oust)
	{
	}
	
	@Override
	public Location getReturnLoc()
	{
		return getCurrentFloor().getReturnLoc();
	}
	
	public void addFloor(ShuttleWayEvent floor)
	{
		_floors.put((floor.getId() % 100), floor);
	}
	
	public ShuttleWayEvent getCurrentFloor()
	{
		return _floors.get(_currentWay);
	}
	
	ShuttleWayEvent getNextFloor()
	{
		int floors = _floors.size() - 1;
		if (!_moveBack)
		{
			_currentWay++;
			if (_currentWay > floors)
			{
				_currentWay = floors - 1;
				_moveBack = true;
			}
		}
		else
		{
			_currentWay--;
			if (_currentWay < 0)
			{
				_currentWay = 1;
				_moveBack = false;
			}
		}
		return _floors.get(_currentWay);
	}
}
