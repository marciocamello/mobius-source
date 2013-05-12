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
 * 5e 01 00 00 00 01 - added ? 02 - modified 7b 86 73 42 object id 08 00 00 00 body slot
 * <p/>
 * body slot 0000 ?? underwear 0001 ear 0002 ear 0003 neck 0004 finger (magic ring) 0005 finger (magic ring) 0006 head (l.cap) 0007 r.hand (dagger) 0008 l.hand (arrows) 0009 hands (int gloves) 000a chest (squire shirt) 000b legs (squire pants) 000c feet 000d ?? back 000e lr.hand (bow)
 * <p/>
 * format ddd
 */
@Deprecated
public class EquipUpdate extends L2GameServerPacket
{
	private final ItemInfo _item;
	
	public EquipUpdate(ItemInstance item, int change)
	{
		_item = new ItemInfo(item);
		_item.setLastChange(change);
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4B);
		writeD(_item.getLastChange());
		writeD(_item.getObjectId());
		writeD(_item.getEquipSlot());
	}
}