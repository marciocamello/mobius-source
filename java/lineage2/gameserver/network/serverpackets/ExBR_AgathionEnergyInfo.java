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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExBR_AgathionEnergyInfo extends L2GameServerPacket
{
	/**
	 * Field _size.
	 */
	private final int _size;
	/**
	 * Field _itemList.
	 */
	private ItemInstance[] _itemList = null;
	
	/**
	 * Constructor for ExBR_AgathionEnergyInfo.
	 * @param size int
	 * @param item ItemInstance[]
	 */
	public ExBR_AgathionEnergyInfo(int size, ItemInstance... item)
	{
		_itemList = item;
		_size = size;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xDE);
		writeD(_size);
		for (ItemInstance item : _itemList)
		{
			if (item.getTemplate().getAgathionEnergy() == 0)
			{
				continue;
			}
			writeD(item.getObjectId());
			writeD(item.getItemId());
			writeD(0x200000);
			writeD(item.getAgathionEnergy());
			writeD(item.getTemplate().getAgathionEnergy());
		}
	}
}
