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
package lineage2.gameserver.templates.jump;

import lineage2.gameserver.utils.Location;

public class JumpPoint
{
	private final Location _loc;
	private final int _nextWayId;
	
	public JumpPoint(Location loc, int nextWayId)
	{
		_loc = loc;
		_nextWayId = nextWayId;
	}
	
	public Location getLocation()
	{
		return _loc;
	}
	
	public int getNextWayId()
	{
		return _nextWayId;
	}
}
