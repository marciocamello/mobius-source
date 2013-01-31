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

import java.util.Collection;

import lineage2.gameserver.data.xml.holder.ProductHolder;
import lineage2.gameserver.model.ProductItem;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExBR_ProductList extends L2GameServerPacket
{
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xD6);
		Collection<ProductItem> items = ProductHolder.getInstance().getAllItems();
		writeD(items.size());
		for (ProductItem template : items)
		{
			if (System.currentTimeMillis() < template.getStartTimeSale())
			{
				continue;
			}
			if (System.currentTimeMillis() > template.getEndTimeSale())
			{
				continue;
			}
			writeD(template.getProductId());
			writeH(template.getCategory());
			writeD(template.getPoints());
			writeD(template.getTabId());
			writeD((int) (template.getStartTimeSale() / 1000));
			writeD((int) (template.getEndTimeSale() / 1000));
			writeC(127);
			writeC(template.getStartHour());
			writeC(template.getStartMin());
			writeC(template.getEndHour());
			writeC(template.getEndMin());
			writeD(0);
			writeD(-1);
		}
	}
}
