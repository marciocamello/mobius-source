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

import java.util.Map;

import lineage2.gameserver.model.items.Inventory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ShopPreviewInfo extends L2GameServerPacket
{
	/**
	 * Field _itemlist.
	 */
	private final Map<Integer, Integer> _itemlist;
	
	/**
	 * Constructor for ShopPreviewInfo.
	 * @param itemlist Map<Integer,Integer>
	 */
	public ShopPreviewInfo(Map<Integer, Integer> itemlist)
	{
		_itemlist = itemlist;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0xF6);
		writeD(Inventory.PAPERDOLL_MAX);
		for (int PAPERDOLL_ID : Inventory.PAPERDOLL_ORDER)
		{
			writeD(getFromList(PAPERDOLL_ID));
		}
	}
	
	/**
	 * Method getFromList.
	 * @param key int
	 * @return int
	 */
	private int getFromList(int key)
	{
		return ((_itemlist.get(key) != null) ? _itemlist.get(key) : 0);
	}
}
