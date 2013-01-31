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
package lineage2.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowTrace extends L2GameServerPacket
{
	/**
	 * Field _traces.
	 */
	private final List<Trace> _traces = new ArrayList<>();
	
	/**
	 * @author Mobius
	 */
	static final class Trace
	{
		/**
		 * Field _x.
		 */
		public final int _x;
		/**
		 * Field _y.
		 */
		public final int _y;
		/**
		 * Field _z.
		 */
		public final int _z;
		/**
		 * Field _time.
		 */
		public final int _time;
		
		/**
		 * Constructor for Trace.
		 * @param x int
		 * @param y int
		 * @param z int
		 * @param time int
		 */
		public Trace(int x, int y, int z, int time)
		{
			_x = x;
			_y = y;
			_z = z;
			_time = time;
		}
	}
	
	/**
	 * Method addTrace.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param time int
	 */
	public void addTrace(int x, int y, int z, int time)
	{
		_traces.add(new Trace(x, y, z, time));
	}
	
	/**
	 * Method addLine.
	 * @param from Location
	 * @param to Location
	 * @param step int
	 * @param time int
	 */
	public void addLine(Location from, Location to, int step, int time)
	{
		addLine(from.x, from.y, from.z, to.x, to.y, to.z, step, time);
	}
	
	/**
	 * Method addLine.
	 * @param from_x int
	 * @param from_y int
	 * @param from_z int
	 * @param to_x int
	 * @param to_y int
	 * @param to_z int
	 * @param step int
	 * @param time int
	 */
	public void addLine(int from_x, int from_y, int from_z, int to_x, int to_y, int to_z, int step, int time)
	{
		int x_diff = to_x - from_x;
		int y_diff = to_y - from_y;
		int z_diff = to_z - from_z;
		double xy_dist = Math.sqrt((x_diff * x_diff) + (y_diff * y_diff));
		double full_dist = Math.sqrt((xy_dist * xy_dist) + (z_diff * z_diff));
		int steps = (int) (full_dist / step);
		addTrace(from_x, from_y, from_z, time);
		if (steps > 1)
		{
			int step_x = x_diff / steps;
			int step_y = y_diff / steps;
			int step_z = z_diff / steps;
			for (int i = 1; i < steps; i++)
			{
				addTrace(from_x + (step_x * i), from_y + (step_y * i), from_z + (step_z * i), time);
			}
		}
		addTrace(to_x, to_y, to_z, time);
	}
	
	/**
	 * Method addTrace.
	 * @param obj GameObject
	 * @param time int
	 */
	public void addTrace(GameObject obj, int time)
	{
		this.addTrace(obj.getX(), obj.getY(), obj.getZ(), time);
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x67);
		writeH(_traces.size());
		for (Trace t : _traces)
		{
			writeD(t._x);
			writeD(t._y);
			writeD(t._z);
			writeH(t._time);
		}
	}
}
