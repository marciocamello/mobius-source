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

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.Collection;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.templates.item.RecipeTemplate;

public final class RecipeHolder extends AbstractHolder
{
	private static final RecipeHolder _instance = new RecipeHolder();
	private final TIntObjectHashMap<RecipeTemplate> _listByRecipeId = new TIntObjectHashMap<>();
	private final TIntObjectHashMap<RecipeTemplate> _listByRecipeItem = new TIntObjectHashMap<>();
	
	public static RecipeHolder getInstance()
	{
		return _instance;
	}
	
	public void addRecipe(RecipeTemplate recipe)
	{
		_listByRecipeId.put(recipe.getId(), recipe);
		_listByRecipeItem.put(recipe.getItemId(), recipe);
	}
	
	public RecipeTemplate getRecipeByRecipeId(int id)
	{
		return _listByRecipeId.get(id);
	}
	
	public RecipeTemplate getRecipeByRecipeItem(int id)
	{
		return _listByRecipeItem.get(id);
	}
	
	public Collection<RecipeTemplate> getRecipes()
	{
		Collection<RecipeTemplate> result = new ArrayList<>(size());
		for (int key : _listByRecipeId.keys())
		{
			result.add(_listByRecipeId.get(key));
		}
		return result;
	}
	
	@Override
	public int size()
	{
		return _listByRecipeId.size();
	}
	
	@Override
	public void clear()
	{
		_listByRecipeId.clear();
		_listByRecipeItem.clear();
	}
}
