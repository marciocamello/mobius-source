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

import lineage2.gameserver.data.xml.holder.BuyListHolder.NpcTradeList;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.TradeItem;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class ExBuySellList extends L2GameServerPacket
{
	/**
	 * @author Mobius
	 */
	public static class BuyList extends ExBuySellList
	{
		/**
		 * Field _listId.
		 */
		private final int _listId;
		/**
		 * Field _buyList.
		 */
		private final List<TradeItem> _buyList;
		/**
		 * Field _adena.
		 */
		private final long _adena;
		/**
		 * Field _taxRate.
		 */
		private final double _taxRate;
		
		/**
		 * Constructor for BuyList.
		 * @param tradeList NpcTradeList
		 * @param activeChar Player
		 * @param taxRate double
		 */
		public BuyList(NpcTradeList tradeList, Player activeChar, double taxRate)
		{
			super(0);
			_adena = activeChar.getAdena();
			_taxRate = taxRate;
			if (tradeList != null)
			{
				_listId = tradeList.getListId();
				_buyList = tradeList.getItems();
				activeChar.setBuyListId(_listId);
			}
			else
			{
				_listId = 0;
				_buyList = Collections.emptyList();
				activeChar.setBuyListId(0);
			}
		}
		
		/**
		 * Method writeImpl.
		 */
		@Override
		protected void writeImpl()
		{
			super.writeImpl();
			writeQ(_adena);
			writeD(_listId);
			writeD(0x00);
			writeH(_buyList.size());
			for (TradeItem item : _buyList)
			{
				writeItemInfo(item, item.getCurrentValue());
				writeQ((long) (item.getOwnersPrice() * (1. + _taxRate)));
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class SellRefundList extends ExBuySellList
	{
		/**
		 * Field _sellList.
		 */
		private final List<TradeItem> _sellList;
		/**
		 * Field _refundList.
		 */
		private final List<TradeItem> _refundList;
		/**
		 * Field _done.
		 */
		private final int _done;
		
		/**
		 * Constructor for SellRefundList.
		 * @param activeChar Player
		 * @param done boolean
		 */
		public SellRefundList(Player activeChar, boolean done)
		{
			super(1);
			_done = done ? 1 : 0;
			if (done)
			{
				_refundList = Collections.emptyList();
				_sellList = Collections.emptyList();
			}
			else
			{
				ItemInstance[] items = activeChar.getRefund().getItems();
				_refundList = new ArrayList<>(items.length);
				for (ItemInstance item : items)
				{
					_refundList.add(new TradeItem(item));
				}
				items = activeChar.getInventory().getItems();
				_sellList = new ArrayList<>(items.length);
				for (ItemInstance item : items)
				{
					if (item.canBeSold(activeChar))
					{
						_sellList.add(new TradeItem(item));
					}
				}
			}
		}
		
		/**
		 * Method writeImpl.
		 */
		@Override
		protected void writeImpl()
		{
			super.writeImpl();
			writeD(0x00);
			writeH(_sellList.size());
			for (TradeItem item : _sellList)
			{
				writeItemInfo(item);
				writeQ(item.getReferencePrice() / 2);
			}
			writeH(_refundList.size());
			for (TradeItem item : _refundList)
			{
				writeItemInfo(item);
				writeD(item.getObjectId());
				writeQ((item.getCount() * item.getReferencePrice()) / 2);
			}
			writeC(_done);
		}
	}
	
	/**
	 * Field _type.
	 */
	private final int _type;
	
	/**
	 * Constructor for ExBuySellList.
	 * @param type int
	 */
	public ExBuySellList(int type)
	{
		_type = type;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xB7);
		writeD(_type);
	}
}
