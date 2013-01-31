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

import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.Warehouse.ItemClassComparator;
import lineage2.gameserver.model.items.Warehouse.WarehouseType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class WareHouseDepositList extends L2GameServerPacket
{
	/**
	 * Field _whtype.
	 */
	private final int _whtype;
	/**
	 * Field _adena.
	 */
	private final long _adena;
	/**
	 * Field _itemList.
	 */
	private final List<ItemInfo> _itemList;
	
	/**
	 * Constructor for WareHouseDepositList.
	 * @param cha Player
	 * @param whtype WarehouseType
	 */
	public WareHouseDepositList(Player cha, WarehouseType whtype)
	{
		_whtype = whtype.ordinal();
		_adena = cha.getAdena();
		ItemInstance[] items = cha.getInventory().getItems();
		ArrayUtils.eqSort(items, ItemClassComparator.getInstance());
		_itemList = new ArrayList<>(items.length);
		for (ItemInstance item : items)
		{
			if (item.canBeStored(cha, _whtype == 1))
			{
				_itemList.add(new ItemInfo(item));
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x41);
		writeH(_whtype);
		writeQ(_adena);
		writeD(0x00);
		writeH(0x00);
		writeH(_itemList.size());
		for (ItemInfo item : _itemList)
		{
			writeItemInfo(item);
			writeD(item.getObjectId());
		}
	}
}
