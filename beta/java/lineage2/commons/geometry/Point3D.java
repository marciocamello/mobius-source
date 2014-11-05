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

/**
 * @author Mobius
 */
public class Point3D extends Point2D
{
	static final Point3D[] EMPTY_ARRAY = new Point3D[0];
	private int _z;
	
	/**
	 * Constructor for Point3D.
	 */
	public Point3D()
	{
	}
	
	/**
	 * Constructor for Point3D.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public Point3D(int x, int y, int z)
	{
		super(x, y);
		_z = z;
	}
	
	/**
	 * Get the z coordinate.
	 * @return the z coordinate
	 */
	public int getZ()
	{
		return _z;
	}
	
	/**
	 * Set the z coordinate.
	 * @param z the z coordinate
	 */
	public void setZ(int z)
	{
		_z = z;
	}
	
	/**
	 * Method clone.
	 * @return Point3D
	 */
	@Override
	public Point3D clone()
	{
		return new Point3D(getX(), getY(), _z);
	}
	
	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
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
	
	/**
	 * Method equals.
	 * @param p Point3D
	 * @return boolean
	 */
	public boolean equals(Point3D p)
	{
		return equals(p.getX(), p.getY(), p._z);
	}
	
	/**
	 * Method equals.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return boolean
	 */
	public boolean equals(int x, int y, int z)
	{
		return (getX() == x) && (getY() == y) && (_z == z);
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "[x: " + getX() + " y: " + getY() + " z: " + _z + "]";
	}
}
