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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author Janiko
 */
public class GMViewItemList extends L2GameServerPacket
{
	private final List<ItemInstance> _items = new ArrayList<>();
	private final int _limit;
	private final String _playerName;
	
	public GMViewItemList(Player cha)
	{
		_playerName = cha.getName();
		_limit = cha.getInventoryLimit();
		for (ItemInstance item : cha.getInventory().getItems())
		{
			_items.add(item);
		}
	}
	
	public GMViewItemList(PetInstance cha)
	{
		_playerName = cha.getName();
		_limit = cha.getInventoryLimit();
		for (ItemInstance item : cha.getInventory().getItems())
		{
			_items.add(item);
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x9a);
		writeS(_playerName);
		writeD(_limit); // c4?
		writeH(0x01); // show window ??
		writeH(_items.size());
		
		for (ItemInstance temp : _items)
		{
			if (!temp.getTemplate().isQuest())
			{
				writeItemInfo(temp);
			}
		}
	}
}