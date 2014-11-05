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
class Point2D implements Cloneable
{
	static final Point2D[] EMPTY_ARRAY = new Point2D[0];
	private int _x;
	private int _y;
	
	/**
	 * Constructor for Point2D.
	 */
	public Point2D()
	{
	}
	
	/**
	 * Constructor for Point2D.
	 * @param x int
	 * @param y int
	 */
	Point2D(int x, int y)
	{
		this._x = x;
		this._y = y;
	}
	
	/**
	 * Method clone.
	 * @return Point2D
	 */
	@Override
	public Point2D clone()
	{
		return new Point2D(_x, _y);
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
		
		return equals((Point2D) o);
	}
	
	/**
	 * Method equals.
	 * @param p Point2D
	 * @return boolean
	 */
	public boolean equals(Point2D p)
	{
		return equals(p._x, p._y);
	}
	
	/**
	 * Method equals.
	 * @param x int
	 * @param y int
	 * @return boolean
	 */
	private boolean equals(int x, int y)
	{
		return (this._x == x) && (this._y == y);
	}
	
	/**
	 * Get the x coordinate.
	 * @return the x coordinate
	 */
	public int getX()
	{
		return _x;
	}
	
	/**
	 * Set the x coordinate.
	 * @param x the x coordinate
	 */
	public void setX(int x)
	{
		_x = x;
	}
	
	/**
	 * Get the y coordinate.
	 * @return the y coordinate
	 */
	public int getY()
	{
		return _y;
	}
	
	/**
	 * Set the y coordinate.
	 * @param y the x coordinate
	 */
	public void setY(int y)
	{
		_y = y;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "[x: " + _x + " y: " + _y + "]";
	}
}