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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DropItem extends L2GameServerPacket
{
	/**
	 * Field _loc.
	 */
	private final Location _loc;
	/**
	 * Field _stackable. Field item_id. Field item_obj_id. Field _playerId.
	 */
	private final int _playerId, item_obj_id, item_id, _stackable;
	/**
	 * Field _count.
	 */
	private final long _count;
	
	/**
	 * Constructor for DropItem.
	 * @param item ItemInstance
	 * @param playerId int
	 */
	public DropItem(ItemInstance item, int playerId)
	{
		_playerId = playerId;
		item_obj_id = item.getObjectId();
		item_id = item.getItemId();
		_loc = item.getLoc();
		_stackable = item.isStackable() ? 1 : 0;
		_count = item.getCount();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x16);
		writeD(_playerId);
		writeD(item_obj_id);
		writeD(item_id);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z + Config.CLIENT_Z_SHIFT);
		writeD(_stackable);
		writeQ(_count);
		writeD(0);
	}
}
