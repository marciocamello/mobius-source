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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.MultiSellHolder.MultiSellListContainer;
import lineage2.gameserver.model.base.MultiSellEntry;
import lineage2.gameserver.model.base.MultiSellIngredient;
import lineage2.gameserver.templates.item.ItemTemplate;

public class MultiSellList extends L2GameServerPacket
{
	private final int _page;
	private final int _finished;
	private final int _listId;
	private final boolean _isnew;
	private final boolean _isNewProduction;
	private final List<MultiSellEntry> _list;
	
	public MultiSellList(MultiSellListContainer list, int page, int finished, boolean isNew, boolean isNewProduction)
	{
		_list = list.getEntries();
		_listId = list.getListId();
		_page = page;
		_finished = finished;
		_isnew = isNew;
		_isNewProduction = isNewProduction;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xD0);
		writeD(_listId);
		writeD(_page);
		writeD(_finished);
		writeD(Config.MULTISELL_SIZE);
		writeD(_list.size());
		writeC(_isnew ? 0x01 : 0x00);
		List<MultiSellIngredient> ingredients;
		List<MultiSellIngredient> production;
		for (MultiSellEntry ent : _list)
		{
			ingredients = fixIngredients(ent.getIngredients());
			production = ent.getProduction();
			
			writeD(ent.getEntryId());
			
			final MultiSellIngredient firstEntry = ent.getProduction().get(0);
			if (firstEntry == null)
			{
				writeC(0x00);
				writeH(0x00);
				writeD(0x00);
				writeD(0x00);
				writeItemElements();
			}
			else
			{
				writeC(firstEntry.isStackable() ? 1 : 0);
				writeH(firstEntry.getItemEnchant());
				writeAugmentationInfo(firstEntry);
				writeItemElements(firstEntry);
			}
			
			writeH(_isnew ? production.size() + 1 : production.size());
			writeH(ingredients.size());
			
			if (_isnew && _isNewProduction)
			{
				writeInfo(production.get(0), true);
			}
			else if (_isnew && !_isNewProduction)
			{
				writeInfo(ingredients.get(0), true);
			}
			
			for (final MultiSellIngredient prod : ent.getProduction())
			{
				writeInfo(prod, true);
			}
			
			for (final MultiSellIngredient i : ingredients)
			{
				writeInfo(i, false);
			}
		}
	}
	
	protected void writeInfo(MultiSellIngredient ingr, boolean product)
	{
		final int itemId = ingr.getItemId();
		final ItemTemplate template = (itemId > 0) ? ItemHolder.getInstance().getTemplate(ingr.getItemId()) : null;
		
		writeD(itemId);
		
		if (product)
		{
			writeD((itemId > 0) && (template != null) ? template.getBodyPart() : 0);
		}
		writeH((itemId > 0) && (template != null) ? template.getType2ForPackets() : 0);
		writeQ(ingr.getItemCount());
		writeH(ingr.getItemEnchant());
		
		if (product)
		{
			writeD(ingr.getChance(true));
		}
		writeAugmentationInfo(ingr);
		writeItemElements(ingr);
	}
	
	private static List<MultiSellIngredient> fixIngredients(List<MultiSellIngredient> ingredients)
	{
		int needFix = 0;
		for (MultiSellIngredient ingredient : ingredients)
		{
			if (ingredient.getItemCount() > Integer.MAX_VALUE)
			{
				needFix++;
			}
		}
		
		if (needFix == 0)
		{
			return ingredients;
		}
		
		MultiSellIngredient temp;
		List<MultiSellIngredient> result = new ArrayList<>(ingredients.size() + needFix);
		for (MultiSellIngredient ingredient : ingredients)
		{
			ingredient = ingredient.clone();
			while (ingredient.getItemCount() > Integer.MAX_VALUE)
			{
				temp = ingredient.clone();
				temp.setItemCount(2000000000);
				result.add(temp);
				ingredient.setItemCount(ingredient.getItemCount() - 2000000000);
			}
			if (ingredient.getItemCount() > 0)
			{
				result.add(ingredient);
			}
		}
		
		return result;
	}
	
	private void writeAugmentationInfo(final MultiSellIngredient ingr)
	{
		if (ingr.getAugmentationId() != 0)
		{
			final int augm = ingr.getAugmentationId();
			
			writeD(augm & 0x0000FFFF);
			writeD(augm >> 16);
		}
		else
		{
			writeD(0x00);
			writeD(0x00);
		}
	}
}
