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

import lineage2.gameserver.data.xml.holder.BuyListHolder.NpcTradeList;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ShopPreviewList extends L2GameServerPacket
{
	/**
	 * Field _listId.
	 */
	private final int _listId;
	/**
	 * Field _itemList.
	 */
	private final List<ItemInfo> _itemList;
	/**
	 * Field _money.
	 */
	private final long _money;
	
	/**
	 * Constructor for ShopPreviewList.
	 * @param list NpcTradeList
	 * @param player Player
	 */
	public ShopPreviewList(NpcTradeList list, Player player)
	{
		_listId = list.getListId();
		_money = player.getAdena();
		List<TradeItem> tradeList = list.getItems();
		_itemList = new ArrayList<>(tradeList.size());
		for (TradeItem item : list.getItems())
		{
			if (item.getItem().isEquipable())
			{
				_itemList.add(item);
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xf5);
		writeD(0x13c0);
		writeQ(_money);
		writeD(_listId);
		writeH(_itemList.size());
		for (ItemInfo item : _itemList)
		{
			if (item.getItem().isEquipable())
			{
				writeD(item.getItemId());
				writeH(item.getItem().getType2ForPackets());
				writeH(item.getItem().isEquipable() ? item.getItem().getBodyPart() : 0x00);
				writeQ(getWearPrice(item.getItem()));
			}
		}
	}
	
	/**
	 * Method getWearPrice.
	 * @param item ItemTemplate
	 * @return int
	 */
	public static int getWearPrice(ItemTemplate item)
	{
		switch (item.getItemGrade())
		{
			case D:
				return 50;
			case C:
				return 100;
			case B:
				return 200;
			case A:
				return 500;
			case S:
				return 1000;
			case S80:
				return 2000;
			case S84:
				return 2500;
			default:
				return 10;
		}
	}
}
