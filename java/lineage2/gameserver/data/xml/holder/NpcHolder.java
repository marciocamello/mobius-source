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

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.templates.npc.NpcTemplate;

public final class NpcHolder extends AbstractHolder
{
	private static final NpcHolder _instance = new NpcHolder();
	private final TIntObjectHashMap<NpcTemplate> _npcs = new TIntObjectHashMap<>(30000);
	private TIntObjectHashMap<List<NpcTemplate>> _npcsByLevel;
	private NpcTemplate[] _allTemplates;
	private Map<String, NpcTemplate> _npcsNames;
	
	public static NpcHolder getInstance()
	{
		return _instance;
	}
	
	NpcHolder()
	{
	}
	
	public void addTemplate(NpcTemplate template)
	{
		_npcs.put(template.npcId, template);
	}
	
	public NpcTemplate getTemplate(int id)
	{
		NpcTemplate npc = ArrayUtils.valid(_allTemplates, id);
		if (npc == null)
		{
			warn("Not defined npc id : " + id + ", or out of range!", new Exception());
			return null;
		}
		return _allTemplates[id];
	}
	
	public NpcTemplate getTemplateByName(String name)
	{
		return _npcsNames.get(name.toLowerCase());
	}
	
	public List<NpcTemplate> getAllOfLevel(int lvl)
	{
		return _npcsByLevel.get(lvl);
	}
	
	public NpcTemplate[] getAll()
	{
		return _npcs.values(new NpcTemplate[_npcs.size()]);
	}
	
	private void buildFastLookupTable()
	{
		_npcsByLevel = new TIntObjectHashMap<>();
		_npcsNames = new HashMap<>();
		int highestId = 0;
		for (int id : _npcs.keys())
		{
			if (id > highestId)
			{
				highestId = id;
			}
		}
		_allTemplates = new NpcTemplate[highestId + 1];
		for (TIntObjectIterator<NpcTemplate> iterator = _npcs.iterator(); iterator.hasNext();)
		{
			iterator.advance();
			int npcId = iterator.key();
			NpcTemplate npc = iterator.value();
			_allTemplates[npcId] = npc;
			List<NpcTemplate> byLevel;
			if ((byLevel = _npcsByLevel.get(npc.level)) == null)
			{
				_npcsByLevel.put(npcId, byLevel = new ArrayList<>());
			}
			byLevel.add(npc);
			_npcsNames.put(npc.name.toLowerCase(), npc);
		}
	}
	
	@Override
	protected void process()
	{
		buildFastLookupTable();
	}
	
	@Override
	public int size()
	{
		return _npcs.size();
	}
	
	@Override
	public void clear()
	{
		_npcsNames.clear();
		_npcs.clear();
	}
}
