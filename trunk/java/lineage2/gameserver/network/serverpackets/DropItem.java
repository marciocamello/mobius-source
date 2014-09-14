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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.utils.Location;

public class DropItem extends L2GameServerPacket
{
	private final Location _loc;
	private final int _playerId;
	private final int item_obj_id;
	private final int item_id;
	private final int _stackable;
	private final long _count;
	
	/**
	 * Constructor<?> of the DropItem server packet
	 * @param item : L2ItemInstance designating the item
	 * @param playerId : int designating the player ID who dropped the item
	 */
	public DropItem(ItemInstance item, int playerId)
	{
		_playerId = playerId;
		item_obj_id = item.getObjectId();
		item_id = item.getId();
		_loc = item.getLoc();
		_stackable = item.isStackable() ? 1 : 0;
		_count = item.getCount();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x16);
		writeD(_playerId);
		writeD(item_obj_id);
		writeD(item_id);
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ() + Config.CLIENT_Z_SHIFT);
		writeD(_stackable);
		writeQ(_count);
		writeD(1); // unknown
	}
}