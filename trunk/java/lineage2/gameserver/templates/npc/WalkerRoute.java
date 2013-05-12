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
package lineage2.gameserver.templates.npc;

import java.util.ArrayList;
import java.util.List;

public class WalkerRoute
{
	private final int _id;
	private final WalkerRouteType _type;
	private final List<WalkerRoutePoint> _points = new ArrayList<>();
	
	public WalkerRoute(int id, WalkerRouteType type)
	{
		_id = id;
		_type = type;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public WalkerRouteType getType()
	{
		return _type;
	}
	
	public void addPoint(WalkerRoutePoint route)
	{
		_points.add(route);
	}
	
	public WalkerRoutePoint getPoint(int id)
	{
		return _points.get(id);
	}
	
	public int size()
	{
		return _points.size();
	}
	
	public boolean isValid()
	{
		if ((_type == WalkerRouteType.DELETE) || (_type == WalkerRouteType.FINISH))
		{
			return size() > 0;
		}
		return size() > 1;
	}
}