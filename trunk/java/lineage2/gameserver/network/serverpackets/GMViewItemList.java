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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GMViewItemList extends L2GameServerPacket
{
	/**
	 * Field _size.
	 */
	private final int _size;
	/**
	 * Field _items.
	 */
	private final ItemInstance[] _items;
	/**
	 * Field _limit.
	 */
	private final int _limit;
	/**
	 * Field _name.
	 */
	private final String _name;
	
	/**
	 * Constructor for GMViewItemList.
	 * @param cha Player
	 * @param items ItemInstance[]
	 * @param size int
	 */
	public GMViewItemList(Player cha, ItemInstance[] items, int size)
	{
		_size = size;
		_items = items;
		_name = cha.getName();
		_limit = cha.getInventoryLimit();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x9a);
		writeS(_name);
		writeD(_limit);
		writeH(1);
		writeH(_size);
		for (ItemInstance temp : _items)
		{
			if (!temp.getTemplate().isQuest())
			{
				writeItemInfo(temp);
			}
		}
	}
}
