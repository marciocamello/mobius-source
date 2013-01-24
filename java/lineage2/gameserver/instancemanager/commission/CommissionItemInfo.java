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
package lineage2.gameserver.instancemanager.commission;

import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.templates.item.ExItemType;

public class CommissionItemInfo
{
	private long auctionId;
	private long registeredPrice;
	private ExItemType exItemType;
	private int saleDays;
	private long saleEndTime;
	private String sellerName;
	private final ItemInstance item;
	
	public CommissionItemInfo(ItemInstance item)
	{
		this.item = item;
	}
	
	public long getAuctionId()
	{
		return auctionId;
	}
	
	public long getRegisteredPrice()
	{
		return registeredPrice;
	}
	
	public ExItemType getExItemType()
	{
		return exItemType;
	}
	
	public int getSaleDays()
	{
		return saleDays;
	}
	
	public long getSaleEndTime()
	{
		return saleEndTime;
	}
	
	public String getSellerName()
	{
		return sellerName;
	}
	
	public ItemInstance getItem()
	{
		return item;
	}
	
	public void setAuctionId(long auctionId)
	{
		this.auctionId = auctionId;
	}
	
	public void setRegisteredPrice(long registeredPrice)
	{
		this.registeredPrice = registeredPrice;
	}
	
	public void setExItemType(ExItemType exItemType)
	{
		this.exItemType = exItemType;
	}
	
	public void setSaleDays(int saleDays)
	{
		this.saleDays = saleDays;
	}
	
	public void setSaleEndTime(long saleEndTime)
	{
		this.saleEndTime = saleEndTime;
	}
	
	public void setSellerName(String sellerName)
	{
		this.sellerName = sellerName;
	}
}
