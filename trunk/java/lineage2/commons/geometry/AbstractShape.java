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

public abstract class AbstractShape implements Shape
{
	protected final Point3D max = new Point3D();
	protected final Point3D min = new Point3D();
	
	@Override
	public boolean isInside(int x, int y, int z)
	{
		return (min.z <= z) && (max.z >= z) && (isInside(x, y));
	}
	
	@Override
	public int getXmax()
	{
		return max.x;
	}
	
	@Override
	public int getXmin()
	{
		return min.x;
	}
	
	@Override
	public int getYmax()
	{
		return max.y;
	}
	
	@Override
	public int getYmin()
	{
		return min.y;
	}
	
	public AbstractShape setZmax(int z)
	{
		max.z = z;
		return this;
	}
	
	public AbstractShape setZmin(int z)
	{
		min.z = z;
		return this;
	}
	
	@Override
	public int getZmax()
	{
		return max.z;
	}
	
	@Override
	public int getZmin()
	{
		return min.z;
	}
}
