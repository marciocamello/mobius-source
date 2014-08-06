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
package lineage2.gameserver.data.xml.holder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.worldstatistics.CategoryType;
import lineage2.gameserver.utils.Location;

public class StatuesHolder extends AbstractHolder
{
	private static StatuesHolder _instance;
	private final Map<CategoryType, List<Location>> spawnLocations;
	
	private StatuesHolder()
	{
		spawnLocations = new HashMap<>();
	}
	
	public static StatuesHolder getInstance()
	{
		if (_instance == null)
		{
			_instance = new StatuesHolder();
		}
		
		return _instance;
	}
	
	@Override
	public int size()
	{
		return spawnLocations.size();
	}
	
	@Override
	public void clear()
	{
		spawnLocations.clear();
	}
	
	public void addSpawnInfo(CategoryType categoryType, List<Location> locations)
	{
		spawnLocations.put(categoryType, locations);
	}
	
	public Map<CategoryType, List<Location>> getSpawnLocations()
	{
		return Collections.unmodifiableMap(spawnLocations);
	}
}
