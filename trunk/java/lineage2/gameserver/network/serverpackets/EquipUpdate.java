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

import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@Deprecated
public class EquipUpdate extends L2GameServerPacket
{
	/**
	 * Field _item.
	 */
	private final ItemInfo _item;
	
	/**
	 * Constructor for EquipUpdate.
	 * @param item ItemInstance
	 * @param change int
	 */
	public EquipUpdate(ItemInstance item, int change)
	{
		_item = new ItemInfo(item);
		_item.setLastChange(change);
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x4B);
		writeD(_item.getLastChange());
		writeD(_item.getObjectId());
		writeD(_item.getEquipSlot());
	}
}
