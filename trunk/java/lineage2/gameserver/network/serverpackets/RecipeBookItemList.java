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
package lineage2.gameserver.network.serverpackets;

import java.util.Collection;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.templates.item.RecipeTemplate;

public class RecipeBookItemList extends L2GameServerPacket
{
	private Collection<RecipeTemplate> _recipes;
	private final boolean _isDwarvenCraft;
	private final int _currentMp;
	
	public RecipeBookItemList(Player player, boolean isDwarvenCraft)
	{
		_isDwarvenCraft = isDwarvenCraft;
		_currentMp = (int) player.getCurrentMp();
		if (isDwarvenCraft)
		{
			_recipes = player.getDwarvenRecipeBook();
		}
		else
		{
			_recipes = player.getCommonRecipeBook();
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xdc);
		writeD(_isDwarvenCraft ? 0x00 : 0x01);
		writeD(_currentMp);
		
		writeD(_recipes.size());
		
		for (RecipeTemplate recipe : _recipes)
		{
			writeD(recipe.getId());
			writeD(1); // ??
		}
	}
}