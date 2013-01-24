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
package lineage2.gameserver.model.base;

import java.util.ArrayList;
import java.util.List;

public class MultiSellEntry
{
	private int _entryId;
	private final List<MultiSellIngredient> _ingredients = new ArrayList<>();
	private final List<MultiSellIngredient> _production = new ArrayList<>();
	private long _tax;
	
	public MultiSellEntry()
	{
	}
	
	public MultiSellEntry(int id)
	{
		_entryId = id;
	}
	
	public MultiSellEntry(int id, int product, int prod_count, int enchant)
	{
		_entryId = id;
		addProduct(new MultiSellIngredient(product, prod_count, enchant));
	}
	
	public void setEntryId(int entryId)
	{
		_entryId = entryId;
	}
	
	public int getEntryId()
	{
		return _entryId;
	}
	
	public void addIngredient(MultiSellIngredient ingredient)
	{
		if (ingredient.getItemCount() > 0)
		{
			_ingredients.add(ingredient);
		}
	}
	
	public List<MultiSellIngredient> getIngredients()
	{
		return _ingredients;
	}
	
	public void addProduct(MultiSellIngredient ingredient)
	{
		_production.add(ingredient);
	}
	
	public List<MultiSellIngredient> getProduction()
	{
		return _production;
	}
	
	public long getTax()
	{
		return _tax;
	}
	
	public void setTax(long tax)
	{
		_tax = tax;
	}
	
	@Override
	public int hashCode()
	{
		return _entryId;
	}
	
	@Override
	public MultiSellEntry clone()
	{
		MultiSellEntry ret = new MultiSellEntry(_entryId);
		for (MultiSellIngredient i : _ingredients)
		{
			ret.addIngredient(i.clone());
		}
		for (MultiSellIngredient i : _production)
		{
			ret.addProduct(i.clone());
		}
		return ret;
	}
}
