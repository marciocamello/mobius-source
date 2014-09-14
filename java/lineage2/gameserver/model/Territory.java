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
package lineage2.gameserver.model;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.geometry.Point3D;
import lineage2.commons.geometry.Shape;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.templates.spawn.SpawnRange;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Territory implements Shape, SpawnRange
{
	private final Point3D max = new Point3D();
	private final Point3D min = new Point3D();
	private final List<Shape> include = new ArrayList<>(1);
	private final List<Shape> exclude = new ArrayList<>(1);
	
	/**
	 * Constructor for Territory.
	 */
	public Territory()
	{
	}
	
	/**
	 * Method add.
	 * @param shape Shape
	 * @return Territory
	 */
	public Territory add(Shape shape)
	{
		if (include.isEmpty())
		{
			max.setX(shape.getXmax());
			max.setY(shape.getYmax());
			max.setZ(shape.getZmax());
			min.setX(shape.getXmin());
			min.setY(shape.getYmin());
			min.setZ(shape.getZmin());
		}
		else
		{
			max.setX(Math.max(max.getX(), shape.getXmax()));
			max.setY(Math.max(max.getY(), shape.getYmax()));
			max.setZ(Math.max(max.getZ(), shape.getZmax()));
			min.setX(Math.min(min.getX(), shape.getXmin()));
			min.setY(Math.min(min.getY(), shape.getYmin()));
			min.setZ(Math.min(min.getZ(), shape.getZmin()));
		}
		
		include.add(shape);
		return this;
	}
	
	/**
	 * Method addBanned.
	 * @param shape Shape
	 * @return Territory
	 */
	public Territory addBanned(Shape shape)
	{
		exclude.add(shape);
		return this;
	}
	
	/**
	 * Method getTerritories.
	 * @return List<Shape>
	 */
	public List<Shape> getTerritories()
	{
		return include;
	}
	
	/**
	 * Method getBannedTerritories.
	 * @return List<Shape>
	 */
	public List<Shape> getBannedTerritories()
	{
		return exclude;
	}
	
	/**
	 * Method isInside.
	 * @param x int
	 * @param y int
	 * @return boolean
	 * @see lineage2.commons.geometry.Shape#isInside(int, int)
	 */
	@Override
	public boolean isInside(int x, int y)
	{
		Shape shape;
		
		for (int i = 0; i < include.size(); i++)
		{
			shape = include.get(i);
			
			if (shape.isInside(x, y))
			{
				return !isExcluded(x, y);
			}
		}
		
		return false;
	}
	
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
		if ((x < min.getX()) || (x > max.getX()) || (y < min.getY()) || (y > max.getY()) || (z < min.getZ()) || (z > max.getZ()))
		{
			return false;
		}
		
		Shape shape;
		
		for (int i = 0; i < include.size(); i++)
		{
			shape = include.get(i);
			
			if (shape.isInside(x, y, z))
			{
				return !isExcluded(x, y, z);
			}
		}
		
		return false;
	}
	
	/**
	 * Method isExcluded.
	 * @param x int
	 * @param y int
	 * @return boolean
	 */
	private boolean isExcluded(int x, int y)
	{
		Shape shape;
		
		for (int i = 0; i < exclude.size(); i++)
		{
			shape = exclude.get(i);
			
			if (shape.isInside(x, y))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Method isExcluded.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return boolean
	 */
	private boolean isExcluded(int x, int y, int z)
	{
		Shape shape;
		
		for (int i = 0; i < exclude.size(); i++)
		{
			shape = exclude.get(i);
			
			if (shape.isInside(x, y, z))
			{
				return true;
			}
		}
		
		return false;
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
	
	/**
	 * Method getRandomLoc.
	 * @param territory Territory
	 * @return Location
	 */
	public static Location getRandomLoc(Territory territory)
	{
		return getRandomLoc(territory, 0);
	}
	
	/**
	 * Method getRandomLoc.
	 * @param territory Territory
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location getRandomLoc(Territory territory, int geoIndex)
	{
		Location pos = new Location();
		List<Shape> territories = territory.getTerritories();
		loop:
		
		for (int i = 0; i < 100; i++)
		{
			Shape shape = territories.get(Rnd.get(territories.size()));
			pos.setX(Rnd.get(shape.getXmin(), shape.getXmax()));
			pos.setY(Rnd.get(shape.getYmin(), shape.getYmax()));
			pos.setZ(shape.getZmin() + ((shape.getZmax() - shape.getZmin()) / 2));
			
			if (territory.isInside(pos.getX(), pos.getY()))
			{
				int tempz = GeoEngine.getHeight(pos, geoIndex);
				
				if (shape.getZmin() != shape.getZmax())
				{
					if ((tempz < shape.getZmin()) || (tempz > shape.getZmax()))
					{
						continue;
					}
				}
				else if ((tempz < (shape.getZmin() - 200)) || (tempz > (shape.getZmin() + 200)))
				{
					continue;
				}
				
				pos.setZ(tempz);
				int geoX = (pos.getX() - World.MAP_MIN_X) >> 4;
				int geoY = (pos.getY() - World.MAP_MIN_Y) >> 4;
				
				for (int x = geoX - 1; x <= (geoX + 1); x++)
				{
					for (int y = geoY - 1; y <= (geoY + 1); y++)
					{
						if (GeoEngine.NgetNSWE(x, y, tempz, geoIndex) != GeoEngine.NSWE_ALL)
						{
							continue loop;
						}
					}
				}
				
				return pos;
			}
		}
		
		return pos;
	}
	
	/**
	 * Method getRandomLoc.
	 * @param geoIndex int
	 * @return Location
	 * @see lineage2.gameserver.templates.spawn.SpawnRange#getRandomLoc(int)
	 */
	@Override
	public Location getRandomLoc(int geoIndex)
	{
		return getRandomLoc(this, geoIndex);
	}
}
