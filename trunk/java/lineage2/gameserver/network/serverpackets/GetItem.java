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
import lineage2.gameserver.utils.Location;

/**
 * 0000: 17 1a 95 20 48 9b da 12 40 44 17 02 00 03 f0 fc ff 98 f1 ff ff ..... format ddddd
 */
public class GetItem extends L2GameServerPacket
{
	private final int _playerId;
	private final int _itemObjId;
	private final Location _loc;
	
	public GetItem(ItemInstance item, int playerId)
	{
		_itemObjId = item.getObjectId();
		_loc = item.getLoc();
		_playerId = playerId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x17);
		writeD(_playerId);
		writeD(_itemObjId);
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ());
	}
}