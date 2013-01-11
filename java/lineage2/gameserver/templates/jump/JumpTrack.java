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

import gnu.trove.map.hash.TIntObjectHashMap;
import lineage2.gameserver.utils.Location;

public class JumpTrack
{
	private final int _id;
	private final TIntObjectHashMap<JumpWay> _trackWays;
	private final Location _startLoc;
	
	public JumpTrack(int id, Location startLoc)
	{
		_id = id;
		_trackWays = new TIntObjectHashMap<>();
		_startLoc = startLoc;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public JumpWay getWay(int id)
	{
		return _trackWays.get(id);
	}
	
	public void addWay(JumpWay way)
	{
		_trackWays.put(way.getId(), way);
	}
	
	public Location getStartLocation()
	{
		return _startLoc;
	}
}
