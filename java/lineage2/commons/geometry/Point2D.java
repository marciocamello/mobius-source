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

public class Point2D implements Cloneable
{
	public static final Point2D[] EMPTY_ARRAY = new Point2D[0];
	public int x;
	public int y;
	
	public Point2D()
	{
	}
	
	public Point2D(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Point2D clone()
	{
		return new Point2D(x, y);
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
		return equals((Point2D) o);
	}
	
	public boolean equals(Point2D p)
	{
		return equals(p.x, p.y);
	}
	
	public boolean equals(int x, int y)
	{
		return (this.x == x) && (this.y == y);
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	@Override
	public String toString()
	{
		return "[x: " + x + " y: " + y + "]";
	}
}
