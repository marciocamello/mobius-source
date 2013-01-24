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
package lineage2.commons.geometry;

public class Point3D extends Point2D
{
	public static final Point3D[] EMPTY_ARRAY = new Point3D[0];
	public int z;
	
	public Point3D()
	{
	}
	
	public Point3D(int x, int y, int z)
	{
		super(x, y);
		this.z = z;
	}
	
	public int getZ()
	{
		return z;
	}
	
	@Override
	public Point3D clone()
	{
		return new Point3D(x, y, z);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (o.getClass() != getClass())
		{
			return false;
		}
		return equals((Point3D) o);
	}
	
	public boolean equals(Point3D p)
	{
		return equals(p.x, p.y, p.z);
	}
	
	public boolean equals(int x, int y, int z)
	{
		return (this.x == x) && (this.y == y) && (this.z == z);
	}
	
	@Override
	public String toString()
	{
		return "[x: " + x + " y: " + y + " z: " + z + "]";
	}
}
