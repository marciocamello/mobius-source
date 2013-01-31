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
import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ManufactureItem;
import lineage2.gameserver.templates.item.RecipeTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RecipeShopManageList extends L2GameServerPacket
{
	/**
	 * Field createList.
	 */
	private final List<ManufactureItem> createList;
	/**
	 * Field recipes.
	 */
	private Collection<RecipeTemplate> recipes;
	/**
	 * Field sellerId.
	 */
	private final int sellerId;
	/**
	 * Field adena.
	 */
	private final long adena;
	/**
	 * Field isDwarven.
	 */
	private final boolean isDwarven;
	
	/**
	 * Constructor for RecipeShopManageList.
	 * @param seller Player
	 * @param isDwarvenCraft boolean
	 */
	public RecipeShopManageList(Player seller, boolean isDwarvenCraft)
	{
		sellerId = seller.getObjectId();
		adena = seller.getAdena();
		isDwarven = isDwarvenCraft;
		if (isDwarven)
		{
			recipes = seller.getDwarvenRecipeBook();
		}
		else
		{
			recipes = seller.getCommonRecipeBook();
		}
		createList = seller.getCreateList();
		for (ManufactureItem mi : createList)
		{
			if (!seller.findRecipe(mi.getRecipeId()))
			{
				createList.remove(mi);
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xde);
		writeD(sellerId);
		writeD((int) Math.min(adena, Integer.MAX_VALUE));
		writeD(isDwarven ? 0x00 : 0x01);
		writeD(recipes.size());
		int i = 1;
		for (RecipeTemplate recipe : recipes)
		{
			writeD(recipe.getId());
			writeD(i++);
		}
		writeD(createList.size());
		for (ManufactureItem mi : createList)
		{
			writeD(mi.getRecipeId());
			writeD(0x00);
			writeQ(mi.getCost());
		}
	}
}
