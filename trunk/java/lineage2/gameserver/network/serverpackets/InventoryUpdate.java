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

import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class InventoryUpdate extends L2GameServerPacket
{
	/**
	 * Field UNCHANGED. (value is 0)
	 */
	public static final int UNCHANGED = 0;
	/**
	 * Field ADDED. (value is 1)
	 */
	public static final int ADDED = 1;
	/**
	 * Field MODIFIED. (value is 2)
	 */
	public static final int MODIFIED = 2;
	/**
	 * Field REMOVED. (value is 3)
	 */
	public static final int REMOVED = 3;
	/**
	 * Field _items.
	 */
	private final List<ItemInfo> _items = new ArrayList<>(1);
	
	/**
	 * Method addNewItem.
	 * @param item ItemInstance
	 * @return InventoryUpdate
	 */
	public InventoryUpdate addNewItem(ItemInstance item)
	{
		addItem(item).setLastChange(ADDED);
		return this;
	}
	
	/**
	 * Method addModifiedItem.
	 * @param item ItemInstance
	 * @return InventoryUpdate
	 */
	public InventoryUpdate addModifiedItem(ItemInstance item)
	{
		addItem(item).setLastChange(MODIFIED);
		return this;
	}
	
	/**
	 * Method addRemovedItem.
	 * @param item ItemInstance
	 * @return InventoryUpdate
	 */
	public InventoryUpdate addRemovedItem(ItemInstance item)
	{
		addItem(item).setLastChange(REMOVED);
		return this;
	}
	
	/**
	 * Method addItem.
	 * @param item ItemInstance
	 * @return ItemInfo
	 */
	private ItemInfo addItem(ItemInstance item)
	{
		ItemInfo info;
		_items.add(info = new ItemInfo(item));
		return info;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x21);
		writeH(_items.size());
		for (ItemInfo temp : _items)
		{
			writeH(temp.getLastChange());
			writeItemInfo(temp);
		}
	}
}
