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

import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetItemList extends L2GameServerPacket
{
	/**
	 * Field items.
	 */
	private final ItemInstance[] items;
	
	/**
	 * Constructor for PetItemList.
	 * @param cha PetInstance
	 */
	public PetItemList(PetInstance cha)
	{
		items = cha.getInventory().getItems();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xb3);
		writeH(items.length);
		for (ItemInstance item : items)
		{
			writeItemInfo(item);
		}
	}
}
