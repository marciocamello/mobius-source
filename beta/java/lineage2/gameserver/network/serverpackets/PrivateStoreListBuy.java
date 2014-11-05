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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.TradeItem;

public class PrivateStoreListBuy extends AbstractItemPacket
{
	private final int _buyerId;
	private final long _adena;
	private final List<TradeItem> _sellList;
	
	public PrivateStoreListBuy(Player seller, Player buyer)
	{
		_adena = seller.getAdena();
		_buyerId = buyer.getObjectId();
		_sellList = new ArrayList<>();
		List<TradeItem> buyList = buyer.getBuyList();
		ItemInstance[] items = seller.getInventory().getItems();
		
		for (TradeItem bi : buyList)
		{
			TradeItem si = null;
			
			for (ItemInstance item : items)
			{
				if ((item.getId() == bi.getId()) && item.canBeTraded(seller))
				{
					si = new TradeItem(item);
					_sellList.add(si);
					si.setOwnersPrice(bi.getOwnersPrice());
					si.setCount(bi.getCount());
					si.setCurrentValue(Math.min(bi.getCount(), item.getCount()));
				}
			}
			
			if (si == null)
			{
				si = new TradeItem();
				si.setItemId(bi.getId());
				si.setOwnersPrice(bi.getOwnersPrice());
				si.setCount(bi.getCount());
				si.setCurrentValue(0);
				_sellList.add(si);
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xBE);
		writeD(_buyerId);
		writeQ(_adena);
		writeD(0x00);
		writeD(_sellList.size());
		
		for (TradeItem si : _sellList)
		{
			writeItem(si);
			writeD(si.getObjectId());
			writeQ(si.getOwnersPrice());
			writeQ(si.getStorePrice());
			writeQ(si.getCount()); // maximum possible tradecount
		}
	}
}