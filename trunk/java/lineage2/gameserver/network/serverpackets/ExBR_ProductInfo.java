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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExBR_ProductInfo extends L2GameServerPacket
{
	/**
	 * Field _productId.
	 */
	private final ProductItem _productId;
	
	/**
	 * Constructor for ExBR_ProductInfo.
	 * @param id int
	 */
	public ExBR_ProductInfo(int id)
	{
		_productId = ProductHolder.getInstance().getProduct(id);
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		if (_productId == null)
		{
			return;
		}
		writeEx(0xD7);
		writeD(_productId.getProductId());
		writeD(_productId.getPoints());
		writeD(_productId.getComponents().size());
		for (ProductItemComponent com : _productId.getComponents())
		{
			writeD(com.getItemId());
			writeD(com.getCount());
			writeD(com.getWeight());
			writeD(com.isDropable() ? 1 : 0);
		}
	}
}
