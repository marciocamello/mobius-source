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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.enums.Element;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author SYS
 */
public class ExShowBaseAttributeCancelWindow extends L2GameServerPacket
{
	private final List<ItemInstance> _items = new ArrayList<>();
	
	public ExShowBaseAttributeCancelWindow(Player activeChar)
	{
		for (ItemInstance item : activeChar.getInventory().getItems())
		{
			if ((item.getAttributeElement() == Element.NONE) || !item.canBeEnchanted() || (getAttributeRemovePrice(item) == 0))
			{
				continue;
			}
			
			_items.add(item);
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x75);
		writeD(_items.size());
		
		for (ItemInstance item : _items)
		{
			writeD(item.getObjectId());
			writeQ(getAttributeRemovePrice(item));
		}
	}
	
	public static long getAttributeRemovePrice(ItemInstance item)
	{
		switch (item.getCrystalType())
		{
			case S:
				return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 50000 : 40000;
				
			case S80:
				return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 100000 : 80000;
				
			case S84:
				return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 200000 : 160000;
				
			case R:
				return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 400000 : 320000;
				
			case R95:
				return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 800000 : 640000;
				
			case R99:
				return item.getTemplate().getType2() == ItemTemplate.TYPE2_WEAPON ? 3200000 : 2560000;
				
			default:
				break;
		}
		
		return 0;
	}
}