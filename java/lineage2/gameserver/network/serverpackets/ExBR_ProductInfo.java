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

import lineage2.gameserver.data.xml.holder.ProductHolder;
import lineage2.gameserver.model.ProductItem;
import lineage2.gameserver.model.ProductItemComponent;

public class ExBR_ProductInfo extends L2GameServerPacket
{
	private final ProductItem _productId;
	
	public ExBR_ProductInfo(int id)
	{
		_productId = ProductHolder.getInstance().getProduct(id);
	}
	
	@Override
	protected void writeImpl()
	{
		if (_productId == null)
		{
			return;
		}
		
		writeEx(0xD8);
		
		writeD(_productId.getProductId()); // product id
		writeD(_productId.getPoints()); // points
		writeD(_productId.getComponents().size()); // size
		
		for (ProductItemComponent com : _productId.getComponents())
		{
			writeD(com.getItemId()); // item id
			writeD(com.getCount()); // quality
			writeD(com.getWeight()); // weight
			writeD(com.isDropable() ? 1 : 0); // 0 - dont drop/trade
		}
	}
}