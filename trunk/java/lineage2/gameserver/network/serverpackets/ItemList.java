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
import lineage2.gameserver.model.items.LockType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ItemList extends L2GameServerPacket
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
	 * Field _showWindow.
	 */
	private final boolean _showWindow;
	/**
	 * Field _lockType.
	 */
	private final LockType _lockType;
	/**
	 * Field _lockItems.
	 */
	private final int[] _lockItems;
	
	/**
	 * Constructor for ItemList.
	 * @param size int
	 * @param items ItemInstance[]
	 * @param showWindow boolean
	 * @param lockType LockType
	 * @param lockItems int[]
	 */
	public ItemList(int size, ItemInstance[] items, boolean showWindow, LockType lockType, int[] lockItems)
	{
		_size = size;
		_items = items;
		_showWindow = showWindow;
		_lockType = lockType;
		_lockItems = lockItems;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x11);
		writeH(_showWindow ? 1 : 0);
		writeH(_size);
		for (ItemInstance temp : _items)
		{
			if (temp.getTemplate().isQuest())
			{
				continue;
			}
			writeItemInfo(temp);
		}
		writeH(_lockItems.length);
		if (_lockItems.length > 0)
		{
			writeC(_lockType.ordinal());
			for (int i : _lockItems)
			{
				writeD(i);
			}
		}
	}
}
