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

public class JumpWay
{
	private final int _id;
	private final TIntObjectHashMap<JumpPoint> _points;
	
	public JumpWay(int id)
	{
		_id = id;
		_points = new TIntObjectHashMap<>();
	}
	
	public int getId()
	{
		return _id;
	}
	
	public JumpPoint[] getPoints()
	{
		return _points.values(new JumpPoint[_points.size()]);
	}
	
	public JumpPoint getJumpPoint(int nextWayId)
	{
		return _points.get(nextWayId);
	}
	
	public void addPoint(JumpPoint point)
	{
		_points.put(point.getNextWayId(), point);
	}
}
