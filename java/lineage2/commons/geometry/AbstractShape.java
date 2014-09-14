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
 * @version $Revision: 1.0 $
 */
abstract class AbstractShape implements Shape
{
	protected final Point3D max = new Point3D();
	protected final Point3D min = new Point3D();
	
	/**
	 * Method isInside.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return boolean
	 * @see lineage2.commons.geometry.Shape#isInside(int, int, int)
	 */
	@Override
	public boolean isInside(int x, int y, int z)
	{
		return (min.getZ() <= z) && (max.getZ() >= z) && (isInside(x, y));
	}
	
	/**
	 * Method getXmax.
	 * @return int
	 * @see lineage2.commons.geometry.Shape#getXmax()
	 */
	@Override
	public int getXmax()
	{
		return max.getX();
	}
	
	/**
	 * Method getXmin.
	 * @return int
	 * @see lineage2.commons.geometry.Shape#getXmin()
	 */
	@Override
	public int getXmin()
	{
		return min.getX();
	}
	
	/**
	 * Method getYmax.
	 * @return int
	 * @see lineage2.commons.geometry.Shape#getYmax()
	 */
	@Override
	public int getYmax()
	{
		return max.getY();
	}
	
	/**
	 * Method getYmin.
	 * @return int
	 * @see lineage2.commons.geometry.Shape#getYmin()
	 */
	@Override
	public int getYmin()
	{
		return min.getY();
	}
	
	/**
	 * Method setZmax.
	 * @param z int
	 * @return AbstractShape
	 */
	public AbstractShape setZmax(int z)
	{
		max.setZ(z);
		return this;
	}
	
	/**
	 * Method setZmin.
	 * @param z int
	 * @return AbstractShape
	 */
	public AbstractShape setZmin(int z)
	{
		min.setZ(z);
		return this;
	}
	
	/**
	 * Method getZmax.
	 * @return int
	 * @see lineage2.commons.geometry.Shape#getZmax()
	 */
	@Override
	public int getZmax()
	{
		return max.getZ();
	}
	
	/**
	 * Method getZmin.
	 * @return int
	 * @see lineage2.commons.geometry.Shape#getZmin()
	 */
	@Override
	public int getZmin()
	{
		return min.getZ();
	}
}
