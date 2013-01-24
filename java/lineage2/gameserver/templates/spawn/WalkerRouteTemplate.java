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
package lineage2.gameserver.templates.spawn;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.utils.Location;

public class WalkerRouteTemplate
{
	public enum RouteType
	{
		CYCLE(0),
		LINEAR(1),
		RANDOM(2),
		TELEPORT(3);
		int _id;
		
		RouteType(int id)
		{
			_id = id;
		}
	}
	
	public class Route
	{
		private final int _x, _y, _z, _h;
		private final long _delay;
		private Location _loc;
		private boolean _end = false;
		
		public Route(int x, int y, int z, int h, long delay, boolean end)
		{
			_x = x;
			_y = y;
			_z = z;
			_h = h;
			_delay = delay;
			_end = end;
		}
		
		public Route(int x, int y, int z)
		{
			this(x, y, z, 0, 0, false);
		}
		
		public Route(int x, int y, int z, int h)
		{
			this(x, y, z, h, 0, false);
		}
		
		public Route(int x, int y, int z, long delay)
		{
			this(x, y, z, 0, delay, false);
		}
		
		public Route(int x, int y, int z, long delay, boolean end)
		{
			this(x, y, z, 0, delay, end);
		}
		
		public Location getLoc()
		{
			if (_loc == null)
			{
				_loc = new Location(_x, _y, _z, _h);
			}
			return _loc;
		}
		
		public int getHeading()
		{
			return _h;
		}
		
		public long getDelay()
		{
			return _delay;
		}
		
		public boolean getLastPoint()
		{
			return _end;
		}
	}
	
	private final int _npcId;
	private final int _walkRange;
	private final long _delay;
	private final RouteType _type;
	private final List<Route> _routes = new ArrayList<>();
	private boolean _isRunning = false;
	
	public WalkerRouteTemplate(int npcId, long delay, RouteType type, boolean isRunning, int walkRange)
	{
		_npcId = npcId;
		_delay = delay;
		_type = type;
		_isRunning = isRunning;
		_walkRange = walkRange;
	}
	
	public void setRoute(int x, int y, int z, int h, long delay, boolean end)
	{
		_routes.add(new Route(x, y, z, h, delay, end));
	}
	
	public void setRoute(int x, int y, int z)
	{
		setRoute(x, y, z, 0, 0, false);
	}
	
	public void setRoute(Route route)
	{
		setRoute(route);
	}
	
	public int getNpcId()
	{
		return _npcId;
	}
	
	public int getWalkRange()
	{
		return _walkRange;
	}
	
	public long getDelay()
	{
		return _delay;
	}
	
	public RouteType getRouteType()
	{
		return _type;
	}
	
	public List<Route> getPoints()
	{
		return _routes;
	}
	
	public int getPointsCount()
	{
		return _routes.size();
	}
	
	public boolean getIsRunning()
	{
		return _isRunning;
	}
}
