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
package lineage2.gameserver.model;

import lineage2.gameserver.templates.item.RecipeTemplate.RecipeComponent;

public class Recipe
{
	private RecipeComponent[] _recipes;
	private final int _id;
	private final int _level;
	private final int _recipeId;
	private final String _recipeName;
	private final int _successRate;
	private final int _mpCost;
	private final int _itemId;
	private final int _foundation;
	private final int _count;
	private final boolean _isdwarvencraft;
	private final long _exp, _sp;
	
	public Recipe(int id, int level, int recipeId, String recipeName, int successRate, int mpCost, int itemId, int foundation, int count, long exp, long sp, boolean isdwarvencraft)
	{
		_id = id;
		_recipes = new RecipeComponent[0];
		_level = level;
		_recipeId = recipeId;
		_recipeName = recipeName;
		_successRate = successRate;
		_mpCost = mpCost;
		_itemId = itemId;
		_foundation = foundation;
		_count = count;
		_exp = exp;
		_sp = sp;
		_isdwarvencraft = isdwarvencraft;
	}
	
	public void addRecipe(RecipeComponent recipe)
	{
		int len = _recipes.length;
		RecipeComponent[] tmp = new RecipeComponent[len + 1];
		System.arraycopy(_recipes, 0, tmp, 0, len);
		tmp[len] = recipe;
		_recipes = tmp;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public int getRecipeId()
	{
		return _recipeId;
	}
	
	public String getRecipeName()
	{
		return _recipeName;
	}
	
	public int getSuccessRate()
	{
		return _successRate;
	}
	
	public int getMpCost()
	{
		return _mpCost;
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public int getCount()
	{
		return _count;
	}
	
	public RecipeComponent[] getRecipes()
	{
		return _recipes;
	}
	
	public boolean isDwarvenRecipe()
	{
		return _isdwarvencraft;
	}
	
	public long getExp()
	{
		return _exp;
	}
	
	public long getSp()
	{
		return _sp;
	}
	
	public int getFoundation()
	{
		return _foundation;
	}
}
