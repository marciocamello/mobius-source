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
package lineage2.gameserver.network.clientpackets;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.math.SafeMath;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.TradeHelper;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestPrivateStoreBuySellList extends L2GameClientPacket
{
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(RequestPrivateStoreBuySellList.class);
	private int _buyerId, _count;
	private int[] _items;
	private long[] _itemQ;
	private long[] _itemP;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_buyerId = readD();
		_count = readD();
		
		if (((_count * 28) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
		{
			_count = 0;
			return;
		}
		
		_items = new int[_count];
		_itemQ = new long[_count];
		_itemP = new long[_count];
		
		for (int i = 0; i < _count; i++)
		{
			_items[i] = readD();
			readD();
			readH();
			readH();
			_itemQ[i] = readQ();
			_itemP[i] = readQ();
			readD();
			
			if ((_itemQ[i] < 1) || (_itemP[i] < 1) || (ArrayUtils.indexOf(_items, _items[i]) < i))
			{
				_count = 0;
				break;
			}
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player seller = getClient().getActiveChar();
		
		if ((seller == null) || (_count == 0))
		{
			return;
		}
		
		if (seller.isActionsDisabled())
		{
			seller.sendActionFailed();
			return;
		}
		
		if (seller.isInStoreMode())
		{
			seller.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM));
			return;
		}
		
		if (seller.isInTrade())
		{
			seller.sendActionFailed();
			return;
		}
		
		if (seller.isFishing())
		{
			seller.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_DO_THAT_WHILE_FISHING3));
			return;
		}
		
		if (!seller.getPlayerAccess().UseTrade)
		{
			seller.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_TRIAL_ACCOUNTS_AREN_T_ALLOWED_BUY_ITEMS_FROM_PRIVATE_STORES_TO_UNLOCK_ALL_OF_THE_FEATURES_OF_LINEAGE_II_PURCHASE_THE_FULL_VERSION_TODAY));
			return;
		}
		
		Player buyer = (Player) seller.getVisibleObject(_buyerId);
		
		if ((buyer == null) || (buyer.getPrivateStoreType() != Player.STORE_PRIVATE_BUY) || !seller.isInRangeZ(buyer, Creature.INTERACTION_DISTANCE))
		{
			seller.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_ATTEMPT_TO_SELL_HAS_FAILED));
			seller.sendActionFailed();
			return;
		}
		
		List<TradeItem> buyList = buyer.getBuyList();
		
		if (buyList.isEmpty())
		{
			seller.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_ATTEMPT_TO_SELL_HAS_FAILED));
			seller.sendActionFailed();
			return;
		}
		
		List<TradeItem> sellList = new ArrayList<>();
		long totalCost = 0;
		int slots = 0;
		long weight = 0;
		buyer.getInventory().writeLock();
		seller.getInventory().writeLock();
		
		try
		{
			loop:
			
			for (int i = 0; i < _count; i++)
			{
				int objectId = _items[i];
				long count = _itemQ[i];
				long price = _itemP[i];
				ItemInstance item = seller.getInventory().getItemByObjectId(objectId);
				
				if ((item == null) || (item.getCount() < count) || !item.canBeTraded(seller))
				{
					break loop;
				}
				
				TradeItem si = null;
				
				for (TradeItem bi : buyList)
				{
					if (bi.getId() == item.getId())
					{
						if (bi.getOwnersPrice() == price)
						{
							if (count > bi.getCount())
							{
								break loop;
							}
							
							totalCost = SafeMath.addAndCheck(totalCost, SafeMath.mulAndCheck(count, price));
							weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(count, item.getTemplate().getWeight()));
							
							if (!item.isStackable() || (buyer.getInventory().getItemByItemId(item.getId()) == null))
							{
								slots++;
							}
							
							si = new TradeItem();
							si.setObjectId(objectId);
							si.setItemId(item.getId());
							si.setCount(count);
							si.setOwnersPrice(price);
							sellList.add(si);
							break;
						}
					}
				}
			}
		}
		catch (ArithmeticException ae)
		{
			sellList.clear();
			sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED));
			return;
		}
		finally
		{
			try
			{
				if (sellList.size() != _count)
				{
					seller.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_ATTEMPT_TO_SELL_HAS_FAILED));
					seller.sendActionFailed();
					return;
				}
				
				if (!buyer.getInventory().validateWeight(weight))
				{
					buyer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT));
					seller.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_ATTEMPT_TO_SELL_HAS_FAILED));
					seller.sendActionFailed();
					return;
				}
				
				if (!buyer.getInventory().validateCapacity(slots))
				{
					buyer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOUR_INVENTORY_IS_FULL));
					seller.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_ATTEMPT_TO_SELL_HAS_FAILED));
					seller.sendActionFailed();
					return;
				}
				
				if (!buyer.reduceAdena(totalCost))
				{
					buyer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_ADENA));
					seller.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_ATTEMPT_TO_SELL_HAS_FAILED));
					seller.sendActionFailed();
					return;
				}
				
				ItemInstance item;
				
				for (TradeItem si : sellList)
				{
					item = seller.getInventory().removeItemByObjectId(si.getObjectId(), si.getCount());
					
					for (TradeItem bi : buyList)
					{
						if (bi.getId() == si.getId())
						{
							if (bi.getOwnersPrice() == si.getOwnersPrice())
							{
								bi.setCount(bi.getCount() - si.getCount());
								
								if (bi.getCount() < 1L)
								{
									buyList.remove(bi);
								}
								break;
							}
						}
					}
					
					Log.LogItem(seller, Log.PrivateStoreSell, item);
					Log.LogItem(buyer, Log.PrivateStoreBuy, item);
					buyer.getInventory().addItem(item);
					TradeHelper.purchaseItem(buyer, seller, si);
				}
				
				long tax = TradeHelper.getTax(seller, totalCost);
				
				if (tax > 0)
				{
					totalCost -= tax;
					seller.sendMessage("You have paid the trade tax at a rate of " + tax + " adena.");
				}
				
				seller.addAdena(totalCost);
				buyer.saveTradeList();
			}
			finally
			{
				seller.getInventory().writeUnlock();
				buyer.getInventory().writeUnlock();
			}
		}
		
		if (buyList.isEmpty())
		{
			TradeHelper.cancelStore(buyer);
		}
		
		seller.sendChanges();
		buyer.sendChanges();
		seller.sendActionFailed();
	}
}
