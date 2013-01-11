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
package lineage2.gameserver.model;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.geometry.Point3D;
import lineage2.commons.geometry.Shape;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.templates.spawn.SpawnRange;
import lineage2.gameserver.utils.Location;

public class Territory implements Shape, SpawnRange
{
	protected final Point3D max = new Point3D();
	protected final Point3D min = new Point3D();
	private final List<Shape> include = new ArrayList<>(1);
	private final List<Shape> exclude = new ArrayList<>(1);
	
	public Territory()
	{
	}
	
	public Territory add(Shape shape)
	{
		if (include.isEmpty())
		{
			max.x = shape.getXmax();
			max.y = shape.getYmax();
			max.z = shape.getZmax();
			min.x = shape.getXmin();
			min.y = shape.getYmin();
			min.z = shape.getZmin();
		}
		else
		{
			max.x = Math.max(max.x, shape.getXmax());
			max.y = Math.max(max.y, shape.getYmax());
			max.z = Math.max(max.z, shape.getZmax());
			min.x = Math.min(min.x, shape.getXmin());
			min.y = Math.min(min.y, shape.getYmin());
			min.z = Math.min(min.z, shape.getZmin());
		}
		include.add(shape);
		return this;
	}
	
	public Territory addBanned(Shape shape)
	{
		exclude.add(shape);
		return this;
	}
	
	public List<Shape> getTerritories()
	{
		return include;
	}
	
	public List<Shape> getBannedTerritories()
	{
		return exclude;
	}
	
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
	
	@Override
	public boolean isInside(int x, int y, int z)
	{
		if ((x < min.x) || (x > max.x) || (y < min.y) || (y > max.y) || (z < min.z) || (z > max.z))
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
	
	public boolean isExcluded(int x, int y)
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
	
	public boolean isExcluded(int x, int y, int z)
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
	
	public static Location getRandomLoc(Territory territory)
	{
		return getRandomLoc(territory, 0);
	}
	
	public static Location getRandomLoc(Territory territory, int geoIndex)
	{
		Location pos = new Location();
		List<Shape> territories = territory.getTerritories();
		loop:
		for (int i = 0; i < 100; i++)
		{
			Shape shape = territories.get(Rnd.get(territories.size()));
			pos.x = Rnd.get(shape.getXmin(), shape.getXmax());
			pos.y = Rnd.get(shape.getYmin(), shape.getYmax());
			pos.z = shape.getZmin() + ((shape.getZmax() - shape.getZmin()) / 2);
			if (territory.isInside(pos.x, pos.y))
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
				pos.z = tempz;
				int geoX = (pos.x - World.MAP_MIN_X) >> 4;
				int geoY = (pos.y - World.MAP_MIN_Y) >> 4;
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
	
	@Override
	public Location getRandomLoc(int geoIndex)
	{
		return getRandomLoc(this, geoIndex);
	}
}
