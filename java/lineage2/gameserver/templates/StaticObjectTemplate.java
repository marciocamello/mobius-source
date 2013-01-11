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
package lineage2.gameserver.templates;

import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.instances.StaticObjectInstance;
import lineage2.gameserver.utils.Location;

public class StaticObjectTemplate
{
	private final int _uid;
	private final int _type;
	private final String _filePath;
	private final int _mapX;
	private final int _mapY;
	private final String _name;
	private final int _x;
	private final int _y;
	private final int _z;
	private final boolean _spawn;
	
	public StaticObjectTemplate(StatsSet set)
	{
		_uid = set.getInteger("uid");
		_type = set.getInteger("stype");
		_mapX = set.getInteger("map_x");
		_mapY = set.getInteger("map_y");
		_filePath = set.getString("path");
		_name = set.getString("name");
		_x = set.getInteger("x");
		_y = set.getInteger("y");
		_z = set.getInteger("z");
		_spawn = set.getBool("spawn");
	}
	
	public int getUId()
	{
		return _uid;
	}
	
	public int getType()
	{
		return _type;
	}
	
	public String getFilePath()
	{
		return _filePath;
	}
	
	public int getMapX()
	{
		return _mapX;
	}
	
	public int getMapY()
	{
		return _mapY;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getX()
	{
		return _x;
	}
	
	public int getY()
	{
		return _y;
	}
	
	public int getZ()
	{
		return _z;
	}
	
	public boolean isSpawn()
	{
		return _spawn;
	}
	
	public StaticObjectInstance newInstance()
	{
		StaticObjectInstance instance = new StaticObjectInstance(IdFactory.getInstance().getNextId(), this);
		instance.spawnMe(new Location(getX(), getY(), getZ()));
		return instance;
	}
}
