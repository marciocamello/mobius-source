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

import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.clientpackets.RequestRefineCancel;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExPutItemResultForVariationCancel extends L2GameServerPacket
{
	/**
	 * Field _itemObjectId.
	 */
	private final int _itemObjectId;
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _aug1.
	 */
	private final int _aug1;
	/**
	 * Field _aug2.
	 */
	private final int _aug2;
	/**
	 * Field _price.
	 */
	private final long _price;
	
	/**
	 * Constructor for ExPutItemResultForVariationCancel.
	 * @param item ItemInstance
	 */
	public ExPutItemResultForVariationCancel(ItemInstance item)
	{
		_itemObjectId = item.getObjectId();
		_itemId = item.getItemId();
		_aug1 = 0x0000FFFF & item.getAugmentationId();
		_aug2 = item.getAugmentationId() >> 16;
		_price = RequestRefineCancel.getRemovalPrice(item.getTemplate());
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x57);
		writeD(_itemObjectId);
		writeD(_itemId);
		writeD(_aug1);
		writeD(_aug2);
		writeQ(_price);
		writeD(0x01);
	}
}
