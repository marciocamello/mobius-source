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

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.instances.StaticObjectInstance;
import lineage2.gameserver.templates.StaticObjectTemplate;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

public final class StaticObjectHolder extends AbstractHolder
{
	private static final StaticObjectHolder _instance = new StaticObjectHolder();
	private final IntObjectMap<StaticObjectTemplate> _templates = new HashIntObjectMap<>();
	private final IntObjectMap<StaticObjectInstance> _spawned = new HashIntObjectMap<>();
	
	public static StaticObjectHolder getInstance()
	{
		return _instance;
	}
	
	public void addTemplate(StaticObjectTemplate template)
	{
		_templates.put(template.getUId(), template);
	}
	
	public StaticObjectTemplate getTemplate(int id)
	{
		return _templates.get(id);
	}
	
	public void spawnAll()
	{
		for (StaticObjectTemplate template : _templates.values())
		{
			if (template.isSpawn())
			{
				StaticObjectInstance obj = template.newInstance();
				_spawned.put(template.getUId(), obj);
			}
		}
		info("spawned: " + _spawned.size() + " static object(s).");
	}
	
	public StaticObjectInstance getObject(int id)
	{
		return _spawned.get(id);
	}
	
	@Override
	public int size()
	{
		return _templates.size();
	}
	
	@Override
	public void clear()
	{
		_templates.clear();
	}
}
