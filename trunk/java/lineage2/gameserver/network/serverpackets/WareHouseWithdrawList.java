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
import java.util.Collections;
import java.util.List;

import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.Warehouse.ItemClassComparator;
import lineage2.gameserver.model.items.Warehouse.WarehouseType;
import lineage2.gameserver.templates.item.ItemTemplate.ItemClass;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class WareHouseWithdrawList extends L2GameServerPacket
{
	/**
	 * Field _adena.
	 */
	private long _adena;
	/**
	 * Field _itemList.
	 */
	private List<ItemInfo> _itemList = new ArrayList<>();
	/**
	 * Field _type.
	 */
	private int _type;
	
	/**
	 * Constructor for WareHouseWithdrawList.
	 * @param player Player
	 * @param type WarehouseType
	 * @param clss ItemClass
	 */
	public WareHouseWithdrawList(Player player, WarehouseType type, ItemClass clss)
	{
		_adena = player.getAdena();
		_type = type.ordinal();
		ItemInstance[] items;
		switch (type)
		{
			case PRIVATE:
				items = player.getWarehouse().getItems(clss);
				break;
			case FREIGHT:
				items = player.getFreight().getItems(clss);
				break;
			case CLAN:
			case CASTLE:
				items = player.getClan().getWarehouse().getItems(clss);
				break;
			default:
				_itemList = Collections.emptyList();
				return;
		}
		_itemList = new ArrayList<>(items.length);
		ArrayUtils.eqSort(items, ItemClassComparator.getInstance());
		for (ItemInstance item : items)
		{
			_itemList.add(new ItemInfo(item));
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x42);
		writeH(_type);
		writeQ(_adena);
		writeH(_itemList.size());
		writeH(0);
		writeD(0);
		for (ItemInfo item : _itemList)
		{
			writeItemInfo(item);
			writeD(item.getObjectId());
		}
	}
}
