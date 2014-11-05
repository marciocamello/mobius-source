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
package lineage2.gameserver.utils;

import java.io.Serializable;

import lineage2.commons.geometry.Point3D;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.World;
import lineage2.gameserver.templates.spawn.SpawnRange;

import org.dom4j.Element;

/**
 * @author Mobius
 */
public class Location extends Point3D implements SpawnRange, Serializable
{
	private int _heading;
	
	/**
	 * Constructor for Location.
	 */
	public Location()
	{
	}
	
	/**
	 * Constructor for Location.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param heading int
	 */
	public Location(int x, int y, int z, int heading)
	{
		super(x, y, z);
		_heading = heading;
	}
	
	/**
	 * Constructor for Location.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public Location(int x, int y, int z)
	{
		this(x, y, z, 0);
	}
	
	/**
	 * Constructor for Location.
	 * @param obj GameObject
	 */
	private Location(GameObject obj)
	{
		this(obj.getX(), obj.getY(), obj.getZ(), obj.getHeading());
	}
	
	/**
	 * Method changeZ.
	 * @param zDiff int
	 * @return Location
	 */
	public Location changeZ(int zDiff)
	{
		setZ(getZ() + zDiff);
		return this;
	}
	
	/**
	 * Method correctGeoZ.
	 * @return Location
	 */
	public Location correctGeoZ()
	{
		setZ(GeoEngine.getHeight(getX(), getY(), getZ(), 0));
		return this;
	}
	
	/**
	 * Method setH.
	 * @param heading int
	 * @return Location
	 */
	public Location setHeading(int heading)
	{
		_heading = heading;
		return this;
	}
	
	/**
	 * Method getHeading.
	 * @return heading int
	 */
	public int getHeading()
	{
		return _heading;
	}
	
	/**
	 * Method set.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return Location
	 */
	public Location set(int x, int y, int z)
	{
		setX(x);
		setY(y);
		setZ(z);
		return this;
	}
	
	/**
	 * Method set.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param h int
	 * @return Location
	 */
	public Location set(int x, int y, int z, int h)
	{
		set(x, y, z);
		_heading = h;
		return this;
	}
	
	/**
	 * Method set.
	 * @param loc Location
	 * @return Location
	 */
	public Location set(Location loc)
	{
		setX(loc.getX());
		setY(loc.getY());
		setZ(loc.getZ());
		_heading = loc.getHeading();
		return this;
	}
	
	/**
	 * Method world2geo.
	 * @return Location
	 */
	public Location world2geo()
	{
		setX((getX() - World.MAP_MIN_X) >> 4);
		setY((getY() - World.MAP_MIN_Y) >> 4);
		return this;
	}
	
	/**
	 * Method geo2world.
	 * @return Location
	 */
	public Location geo2world()
	{
		setX((getX() << 4) + World.MAP_MIN_X + 8);
		setY((getY() << 4) + World.MAP_MIN_Y + 8);
		return this;
	}
	
	/**
	 * Method distance.
	 * @param loc Location
	 * @return double
	 */
	public double distance(Location loc)
	{
		return distance(loc.getX(), loc.getY());
	}
	
	/**
	 * Method distance.
	 * @param x int
	 * @param y int
	 * @return double
	 */
	private double distance(int x, int y)
	{
		long dx = getX() - x;
		long dy = getY() - y;
		return Math.sqrt((dx * dx) + (dy * dy));
	}
	
	/**
	 * Method distance3D.
	 * @param loc Location
	 * @return double
	 */
	public double distance3D(Location loc)
	{
		return distance3D(loc.getX(), loc.getY(), loc.getZ());
	}
	
	/**
	 * Method distance3D.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return double
	 */
	private double distance3D(int x, int y, int z)
	{
		long dx = getX() - x;
		long dy = getY() - y;
		long dz = getZ() - z;
		return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
	}
	
	/**
	 * Method clone.
	 * @return Location
	 */
	@Override
	public Location clone()
	{
		return new Location(getX(), getY(), getZ(), _heading);
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public final String toString()
	{
		return getX() + "," + getY() + "," + getZ() + "," + _heading;
	}
	
	/**
	 * Method isNull.
	 * @return boolean
	 */
	public boolean isNull()
	{
		return (getX() == 0) || (getY() == 0) || (getZ() == 0);
	}
	
	/**
	 * Method toXYZString.
	 * @return String
	 */
	public final String toXYZString()
	{
		return getX() + " " + getY() + " " + getZ();
	}
	
	/**
	 * Method parseLoc.
	 * @param s String
	 * @return Location
	 * @throws IllegalArgumentException
	 */
	public static Location parseLoc(String s) throws IllegalArgumentException
	{
		String[] xyzh = s.split("[\\s,;]+");
		
		if (xyzh.length < 3)
		{
			throw new IllegalArgumentException("Can't parse location from string: " + s);
		}
		
		int x = Integer.parseInt(xyzh[0]);
		int y = Integer.parseInt(xyzh[1]);
		int z = Integer.parseInt(xyzh[2]);
		int h = xyzh.length < 4 ? 0 : Integer.parseInt(xyzh[3]);
		return new Location(x, y, z, h);
	}
	
	/**
	 * Method parse.
	 * @param element Element
	 * @return Location
	 */
	public static Location parse(Element element)
	{
		int x = Integer.parseInt(element.attributeValue("x"));
		int y = Integer.parseInt(element.attributeValue("y"));
		int z = Integer.parseInt(element.attributeValue("z"));
		int h = element.attributeValue("h") == null ? 0 : Integer.parseInt(element.attributeValue("h"));
		return new Location(x, y, z, h);
	}
	
	/**
	 * Method findFrontPosition.
	 * @param obj GameObject
	 * @param obj2 GameObject
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	public static Location findFrontPosition(GameObject obj, GameObject obj2, int radiusmin, int radiusmax)
	{
		if ((radiusmax == 0) || (radiusmax < radiusmin))
		{
			return new Location(obj);
		}
		
		double collision = obj.getColRadius() + obj2.getColRadius();
		int randomRadius, randomAngle, tempz;
		int minangle = 0;
		int maxangle = 360;
		
		if (!obj.equals(obj2))
		{
			double angle = PositionUtils.calculateAngleFrom(obj, obj2);
			minangle = (int) angle - 45;
			maxangle = (int) angle + 45;
		}
		
		Location pos = new Location();
		
		for (int i = 0; i < 100; i++)
		{
			randomRadius = Rnd.get(radiusmin, radiusmax);
			randomAngle = Rnd.get(minangle, maxangle);
			pos.setX(obj.getX() + (int) ((collision + randomRadius) * Math.cos(Math.toRadians(randomAngle))));
			pos.setY(obj.getY() + (int) ((collision + randomRadius) * Math.sin(Math.toRadians(randomAngle))));
			pos.setZ(obj.getZ());
			tempz = GeoEngine.getHeight(pos.getX(), pos.getY(), pos.getZ(), obj.getGeoIndex());
			
			if ((Math.abs(pos.getZ() - tempz) < 200) && (GeoEngine.getNSWE(pos.getX(), pos.getY(), tempz, obj.getGeoIndex()) == GeoEngine.NSWE_ALL))
			{
				pos.setZ(tempz);
				
				if (!obj.equals(obj2))
				{
					pos.setHeading(PositionUtils.getHeadingTo(pos, obj2.getLoc()));
				}
				else
				{
					pos.setHeading(obj.getHeading());
				}
				
				return pos;
			}
		}
		
		return new Location(obj);
	}
	
	/**
	 * Method findAroundPosition.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param radiusmin int
	 * @param radiusmax int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location findAroundPosition(int x, int y, int z, int radiusmin, int radiusmax, int geoIndex)
	{
		Location pos;
		int tempz;
		
		for (int i = 0; i < 100; i++)
		{
			pos = Location.coordsRandomize(x, y, z, 0, radiusmin, radiusmax);
			tempz = GeoEngine.getHeight(pos.getX(), pos.getY(), pos.getZ(), geoIndex);
			
			if (GeoEngine.canMoveToCoord(x, y, z, pos.getX(), pos.getY(), tempz, geoIndex) && GeoEngine.canMoveToCoord(pos.getX(), pos.getY(), tempz, x, y, z, geoIndex))
			{
				pos.setZ(tempz);
				return pos;
			}
		}
		
		return new Location(x, y, z);
	}
	
	/**
	 * Method findAroundPosition.
	 * @param obj GameObject
	 * @param loc Location
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	private static Location findAroundPosition(GameObject obj, Location loc, int radiusmin, int radiusmax)
	{
		return findAroundPosition(loc.getX(), loc.getY(), loc.getZ(), radiusmin, radiusmax, obj.getGeoIndex());
	}
	
	/**
	 * Method findAroundPosition.
	 * @param obj GameObject
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	public static Location findAroundPosition(GameObject obj, int radiusmin, int radiusmax)
	{
		return findAroundPosition(obj, obj.getLoc(), radiusmin, radiusmax);
	}
	
	/**
	 * Method findAroundPosition.
	 * @param obj GameObject
	 * @param radius int
	 * @return Location
	 */
	public static Location findAroundPosition(GameObject obj, int radius)
	{
		return findAroundPosition(obj, 0, radius);
	}
	
	/**
	 * Method findPointToStay.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param radiusmin int
	 * @param radiusmax int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location findPointToStay(int x, int y, int z, int radiusmin, int radiusmax, int geoIndex)
	{
		Location pos;
		int tempz;
		
		for (int i = 0; i < 100; i++)
		{
			pos = Location.coordsRandomize(x, y, z, 0, radiusmin, radiusmax);
			tempz = GeoEngine.getHeight(pos.getX(), pos.getY(), pos.getZ(), geoIndex);
			
			if ((Math.abs(pos.getZ() - tempz) < 200) && (GeoEngine.getNSWE(pos.getX(), pos.getY(), tempz, geoIndex) == GeoEngine.NSWE_ALL))
			{
				pos.setZ(tempz);
				return pos;
			}
		}
		
		return new Location(x, y, z);
	}
	
	/**
	 * Method findPointToStay.
	 * @param loc Location
	 * @param radius int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location findPointToStay(Location loc, int radius, int geoIndex)
	{
		return findPointToStay(loc.getX(), loc.getY(), loc.getZ(), 0, radius, geoIndex);
	}
	
	/**
	 * Method findPointToStay.
	 * @param loc Location
	 * @param radiusmin int
	 * @param radiusmax int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location findPointToStay(Location loc, int radiusmin, int radiusmax, int geoIndex)
	{
		return findPointToStay(loc.getX(), loc.getY(), loc.getZ(), radiusmin, radiusmax, geoIndex);
	}
	
	/**
	 * Method findPointToStay.
	 * @param obj GameObject
	 * @param loc Location
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	public static Location findPointToStay(GameObject obj, Location loc, int radiusmin, int radiusmax)
	{
		return findPointToStay(loc.getX(), loc.getY(), loc.getZ(), radiusmin, radiusmax, obj.getGeoIndex());
	}
	
	/**
	 * Method findPointToStay.
	 * @param obj GameObject
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	public static Location findPointToStay(GameObject obj, int radiusmin, int radiusmax)
	{
		return findPointToStay(obj, obj.getLoc(), radiusmin, radiusmax);
	}
	
	/**
	 * Method findPointToStay.
	 * @param obj GameObject
	 * @param radius int
	 * @return Location
	 */
	public static Location findPointToStay(GameObject obj, int radius)
	{
		return findPointToStay(obj, 0, radius);
	}
	
	/**
	 * Method coordsRandomize.
	 * @param loc Location
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	public static Location coordsRandomize(Location loc, int radiusmin, int radiusmax)
	{
		return coordsRandomize(loc.getX(), loc.getY(), loc.getZ(), loc.getHeading(), radiusmin, radiusmax);
	}
	
	/**
	 * Method coordsRandomize.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param heading int
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	private static Location coordsRandomize(int x, int y, int z, int heading, int radiusmin, int radiusmax)
	{
		if ((radiusmax == 0) || (radiusmax < radiusmin))
		{
			return new Location(x, y, z, heading);
		}
		
		int radius = Rnd.get(radiusmin, radiusmax);
		double angle = Rnd.nextDouble() * 2 * Math.PI;
		return new Location((int) (x + (radius * Math.cos(angle))), (int) (y + (radius * Math.sin(angle))), z, heading);
	}
	
	/**
	 * Method findNearest.
	 * @param creature Creature
	 * @param locs Location[]
	 * @return Location
	 */
	public static Location findNearest(Creature creature, Location[] locs)
	{
		Location defloc = null;
		
		for (Location loc : locs)
		{
			if (defloc == null)
			{
				defloc = loc;
			}
			else if (creature.getDistance(loc) < creature.getDistance(defloc))
			{
				defloc = loc;
			}
		}
		
		return defloc;
	}
	
	/**
	 * Method getRandomHeading.
	 * @return int
	 */
	public static int getRandomHeading()
	{
		return Rnd.get(65535);
	}
	
	/**
	 * Method getRandomLoc.
	 * @param ref int
	 * @return Location
	 * @see lineage2.gameserver.templates.spawn.SpawnRange#getRandomLoc(int)
	 */
	@Override
	public Location getRandomLoc(int ref)
	{
		return this;
	}
}