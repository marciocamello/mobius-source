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
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PrivateStoreManageListSell extends L2GameServerPacket
{
	/**
	 * Field _sellerId.
	 */
	private final int _sellerId;
	/**
	 * Field _adena.
	 */
	private final long _adena;
	/**
	 * Field _package.
	 */
	private final boolean _package;
	/**
	 * Field _sellList.
	 */
	private final List<TradeItem> _sellList;
	/**
	 * Field _sellList0.
	 */
	private final List<TradeItem> _sellList0;
	
	/**
	 * Constructor for PrivateStoreManageListSell.
	 * @param seller Player
	 * @param pkg boolean
	 */
	public PrivateStoreManageListSell(Player seller, boolean pkg)
	{
		_sellerId = seller.getObjectId();
		_adena = seller.getAdena();
		_package = pkg;
		_sellList0 = seller.getSellList(_package);
		_sellList = new ArrayList<>();
		for (TradeItem si : _sellList0)
		{
			if (si.getCount() <= 0)
			{
				_sellList0.remove(si);
				continue;
			}
			ItemInstance item = seller.getInventory().getItemByObjectId(si.getObjectId());
			if (item == null)
			{
				item = seller.getInventory().getItemByItemId(si.getItemId());
			}
			if ((item == null) || !item.canBeTraded(seller) || (item.getItemId() == ItemTemplate.ITEM_ID_ADENA))
			{
				_sellList0.remove(si);
				continue;
			}
			si.setCount(Math.min(item.getCount(), si.getCount()));
		}
		ItemInstance[] items = seller.getInventory().getItems();
		loop:
		for (ItemInstance item : items)
		{
			if (item.canBeTraded(seller) && (item.getItemId() != ItemTemplate.ITEM_ID_ADENA))
			{
				for (TradeItem si : _sellList0)
				{
					if (si.getObjectId() == item.getObjectId())
					{
						if (si.getCount() == item.getCount())
						{
							continue loop;
						}
						TradeItem ti = new TradeItem(item);
						ti.setCount(item.getCount() - si.getCount());
						_sellList.add(ti);
						continue loop;
					}
				}
				_sellList.add(new TradeItem(item));
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xA0);
		writeD(_sellerId);
		writeD(_package ? 1 : 0);
		writeQ(_adena);
		writeD(_sellList.size());
		for (TradeItem si : _sellList)
		{
			writeItemInfo(si);
			writeQ(si.getStorePrice());
		}
		writeD(_sellList0.size());
		for (TradeItem si : _sellList0)
		{
			writeItemInfo(si);
			writeQ(si.getOwnersPrice());
			writeQ(si.getStorePrice());
		}
	}
}
