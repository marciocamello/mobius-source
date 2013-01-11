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
package lineage2.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.templates.spawn.SpawnTemplate;

public final class SpawnHolder extends AbstractHolder
{
	private static final SpawnHolder _instance = new SpawnHolder();
	private final Map<String, List<SpawnTemplate>> _spawns = new HashMap<>();
	
	public static SpawnHolder getInstance()
	{
		return _instance;
	}
	
	public void addSpawn(String group, SpawnTemplate spawn)
	{
		List<SpawnTemplate> spawns = _spawns.get(group);
		if (spawns == null)
		{
			_spawns.put(group, (spawns = new ArrayList<>()));
		}
		spawns.add(spawn);
	}
	
	public List<SpawnTemplate> getSpawn(String name)
	{
		List<SpawnTemplate> template = _spawns.get(name);
		return template == null ? Collections.<SpawnTemplate> emptyList() : template;
	}
	
	@Override
	public int size()
	{
		int i = 0;
		for (List l : _spawns.values())
		{
			i += l.size();
		}
		return i;
	}
	
	@Override
	public void clear()
	{
		_spawns.clear();
	}
	
	public Map<String, List<SpawnTemplate>> getSpawns()
	{
		return _spawns;
	}
}
