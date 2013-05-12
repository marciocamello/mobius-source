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
 * @author : Darvin
 */
public class ExResponseCommissionInfo extends L2GameServerPacket
{
	private final ItemInstance _item;
	
	public ExResponseCommissionInfo(ItemInstance item)
	{
		_item = item;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xF4);
		writeD(_item.getItemId()); // ItemId
		writeD(_item.getObjectId());
		writeQ(_item.getCount()); // TODO
		writeQ(0/* _item.getCount() */); // TODO
		writeD(0); // TODO
	}
}
