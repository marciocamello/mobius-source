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
package lineage2.gameserver.data;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.lang.reflect.Constructor;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.data.xml.holder.ShuttleTemplateHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.entity.boat.Boat;
import lineage2.gameserver.model.entity.boat.Shuttle;
import lineage2.gameserver.templates.CharTemplate;
import lineage2.gameserver.templates.ShuttleTemplate;

public final class BoatHolder extends AbstractHolder
{
	public static final CharTemplate TEMPLATE = new CharTemplate(CharTemplate.getEmptyStatsSet());
	private static BoatHolder _instance = new BoatHolder();
	private final TIntObjectHashMap<Boat> _boats = new TIntObjectHashMap<>();
	
	public static BoatHolder getInstance()
	{
		return _instance;
	}
	
	public void spawnAll()
	{
		log();
		for (TIntObjectIterator<Boat> iterator = _boats.iterator(); iterator.hasNext();)
		{
			iterator.advance();
			iterator.value().spawnMe();
			info("Spawning: " + iterator.value().getName());
		}
	}
	
	public Boat initBoat(String name, String clazz)
	{
		try
		{
			Class<?> cl = Class.forName("lineage2.gameserver.model.entity.boat." + clazz);
			Constructor constructor = cl.getConstructor(Integer.TYPE, CharTemplate.class);
			Boat boat = (Boat) constructor.newInstance(IdFactory.getInstance().getNextId(), TEMPLATE);
			boat.setName(name);
			addBoat(boat);
			return boat;
		}
		catch (Exception e)
		{
			error("Fail to init boat: " + clazz, e);
		}
		return null;
	}
	
	public Shuttle initShuttle(String name, int shuttleId)
	{
		try
		{
			ShuttleTemplate template = ShuttleTemplateHolder.getInstance().getTemplate(shuttleId);
			Shuttle shuttle = new Shuttle(IdFactory.getInstance().getNextId(), template);
			shuttle.setName(name);
			addBoat(shuttle);
			return shuttle;
		}
		catch (Exception e)
		{
			error("Fail to init shuttle id: " + shuttleId, e);
		}
		return null;
	}
	
	public Boat getBoat(String name)
	{
		for (TIntObjectIterator<Boat> iterator = _boats.iterator(); iterator.hasNext();)
		{
			iterator.advance();
			if (iterator.value().getName().equals(name))
			{
				return iterator.value();
			}
		}
		return null;
	}
	
	public Boat getBoat(int objectId)
	{
		return _boats.get(objectId);
	}
	
	public void addBoat(Boat boat)
	{
		_boats.put(boat.getObjectId(), boat);
	}
	
	public void removeBoat(Boat boat)
	{
		_boats.remove(boat.getObjectId());
	}
	
	@Override
	public int size()
	{
		return _boats.size();
	}
	
	@Override
	public void clear()
	{
		_boats.clear();
	}
}
