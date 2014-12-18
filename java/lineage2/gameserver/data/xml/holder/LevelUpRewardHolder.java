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

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.data.xml.AbstractHolder;
import gnu.trove.map.hash.TIntObjectHashMap;

public class LevelUpRewardHolder extends AbstractHolder
{
	public static class ItemLevel
	{
		public int id;
		public long count;
	}
	
	private static LevelUpRewardHolder _instance = new LevelUpRewardHolder();
	private final TIntObjectHashMap<ArrayList<ItemLevel>> _itemLevelTemplate;
	
	private LevelUpRewardHolder()
	{
		_itemLevelTemplate = new TIntObjectHashMap<>();
	}
	
	@Override
	public void clear()
	{
		_itemLevelTemplate.clear();
	}
	
	@Override
	public void log()
	{
		info("Loaded " + _itemLevelTemplate.size() + " level up rewards.");
	}
	
	@Override
	public int size()
	{
		return 0;
	}
	
	public static LevelUpRewardHolder getInstance()
	{
		return _instance;
	}
	
	public void addItemLevel(ItemLevel item, int playerLevel)
	{
		if (!_itemLevelTemplate.containsKey(playerLevel))
		{
			_itemLevelTemplate.put(playerLevel, new ArrayList<ItemLevel>());
		}
		_itemLevelTemplate.get(playerLevel).add(item);
	}
	
	public List<ItemLevel> getItemLevel(int playerLevel)
	{
		return _itemLevelTemplate.get(playerLevel) != null ? _itemLevelTemplate.get(playerLevel) : new ArrayList<>(0);
	}
}