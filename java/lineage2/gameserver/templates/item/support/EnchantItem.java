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
package lineage2.gameserver.templates.item.support;

import java.util.Collections;
import java.util.Set;

import lineage2.gameserver.templates.item.ItemTemplate;

import org.napile.primitive.Containers;
import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

public class EnchantItem
{
	private final int _itemId;
	private final int _chance;
	private final int _maxEnchant;
	private IntSet _items = Containers.EMPTY_INT_SET;
	private final Set<ItemTemplate.Grade> _grades = Collections.emptySet();
	
	public EnchantItem(int itemId, int chance, int maxEnchant)
	{
		_itemId = itemId;
		_chance = chance;
		_maxEnchant = maxEnchant;
	}
	
	public void addItemId(int id)
	{
		if (_items.isEmpty())
		{
			_items = new HashIntSet();
		}
		_items.add(id);
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public int getChance()
	{
		return _chance;
	}
	
	public int getMaxEnchant()
	{
		return _maxEnchant;
	}
	
	public Set<ItemTemplate.Grade> getGrades()
	{
		return _grades;
	}
	
	public IntSet getItems()
	{
		return _items;
	}
}
