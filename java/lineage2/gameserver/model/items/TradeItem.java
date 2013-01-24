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
package lineage2.gameserver.model.items;

public final class TradeItem extends ItemInfo
{
	private long _price;
	private long _referencePrice;
	private long _currentValue;
	private int _lastRechargeTime;
	private int _rechargeTime;
	
	public TradeItem()
	{
		super();
	}
	
	public TradeItem(ItemInstance item)
	{
		super(item);
		setReferencePrice(item.getReferencePrice());
	}
	
	public void setOwnersPrice(long price)
	{
		_price = price;
	}
	
	public long getOwnersPrice()
	{
		return _price;
	}
	
	public void setReferencePrice(long price)
	{
		_referencePrice = price;
	}
	
	public long getReferencePrice()
	{
		return _referencePrice;
	}
	
	public long getStorePrice()
	{
		return getReferencePrice() / 2;
	}
	
	public void setCurrentValue(long value)
	{
		_currentValue = value;
	}
	
	public long getCurrentValue()
	{
		return _currentValue;
	}
	
	public void setRechargeTime(int rechargeTime)
	{
		_rechargeTime = rechargeTime;
	}
	
	public int getRechargeTime()
	{
		return _rechargeTime;
	}
	
	public boolean isCountLimited()
	{
		return getCount() > 0;
	}
	
	public void setLastRechargeTime(int lastRechargeTime)
	{
		_lastRechargeTime = lastRechargeTime;
	}
	
	public int getLastRechargeTime()
	{
		return _lastRechargeTime;
	}
}
