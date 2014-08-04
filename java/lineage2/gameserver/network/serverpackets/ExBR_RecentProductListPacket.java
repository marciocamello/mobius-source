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
import java.util.Collection;

import lineage2.gameserver.data.xml.holder.ProductHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.ProductItem;

public class ExBR_RecentProductListPacket extends L2GameServerPacket
{
	private final Collection<ProductItem> _products;
	
	public ExBR_RecentProductListPacket(Player activeChar)
	{
		_products = new ArrayList<>();
		int[] products = activeChar.getRecentProductList();
		
		if (products != null)
		{
			for (int productId : products)
			{
				ProductItem product = ProductHolder.getInstance().getProduct(productId);
				
				if (product == null)
				{
					continue;
				}
				
				_products.add(product);
			}
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xDD);
		writeD(_products.size());
		
		for (ProductItem template : _products)
		{
			writeD(template.getProductId()); // product id
			writeH(template.getCategory()); // category 1 - enchant 2 - supplies 3 - decoration 4 - package 5 - other
			writeD(template.getPoints()); // points
			writeD(template.getTabId()); // show tab 2-th group - 1 ?????????? ?????? ??? ????
			writeD((int) (template.getStartTimeSale() / 1000)); // start sale unix date in seconds
			writeD((int) (template.getEndTimeSale() / 1000)); // end sale unix date in seconds
			writeC(127); // day week (127 = not daily goods)
			writeC(template.getStartHour()); // start hour
			writeC(template.getStartMin()); // start min
			writeC(template.getEndHour()); // end hour
			writeC(template.getEndMin()); // end min
			writeD(0); // stock
			writeD(-1); // max stock
		}
	}
}