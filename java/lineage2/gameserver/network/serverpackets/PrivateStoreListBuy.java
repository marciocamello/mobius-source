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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PrivateStoreListBuy extends L2GameServerPacket
{
	/**
	 * Field _buyerId.
	 */
	private final int _buyerId;
	/**
	 * Field _adena.
	 */
	private final long _adena;
	/**
	 * Field _sellList.
	 */
	private final List<TradeItem> _sellList;
	
	/**
	 * Constructor for PrivateStoreListBuy.
	 * @param seller Player
	 * @param buyer Player
	 */
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
				if ((item.getItemId() == bi.getItemId()) && item.canBeTraded(seller))
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
				si.setItemId(bi.getItemId());
				si.setOwnersPrice(bi.getOwnersPrice());
				si.setCount(bi.getCount());
				si.setCurrentValue(0);
				_sellList.add(si);
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
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
			writeItemInfo(si, si.getCurrentValue());
			writeD(si.getObjectId());
			writeQ(si.getOwnersPrice());
			writeQ(si.getStorePrice());
			writeQ(si.getCount());
		}
	}
}
