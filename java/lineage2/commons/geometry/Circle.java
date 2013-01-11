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
package lineage2.commons.geometry;

public class Circle extends AbstractShape
{
	protected final Point2D c;
	protected final int r;
	
	public Circle(Point2D center, int radius)
	{
		c = center;
		r = radius;
		min.x = (c.x - r);
		max.x = (c.x + r);
		min.y = (c.y - r);
		max.y = (c.y + r);
	}
	
	public Circle(int x, int y, int radius)
	{
		this(new Point2D(x, y), radius);
	}
	
	@Override
	public Circle setZmax(int z)
	{
		max.z = z;
		return this;
	}
	
	@Override
	public Circle setZmin(int z)
	{
		min.z = z;
		return this;
	}
	
	@Override
	public boolean isInside(int x, int y)
	{
		return (((x - c.x) * (c.x - x)) + ((y - c.y) * (c.y - y))) <= (r * r);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(c).append("{ radius: ").append(r).append("}");
		sb.append("]");
		return sb.toString();
	}
}
