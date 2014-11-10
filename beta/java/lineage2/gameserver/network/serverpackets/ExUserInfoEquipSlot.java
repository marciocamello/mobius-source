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
import lineage2.gameserver.model.items.Inventory;

/**
 * @author blacksmoke
 */
public class ExUserInfoEquipSlot extends L2GameServerPacket
{
	private final Player _activeChar;
	
	public ExUserInfoEquipSlot(Player player)
	{
		_activeChar = player;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x156);
		writeD(_activeChar.getObjectId());
		writeH(Inventory.PAPERDOLL_MAX);
		
		// TODO: BitMask
		writeC(0xFF);
		writeC(0xFF);
		writeC(0xFF);
		writeC(0xFF);
		writeC(0xFF);
		
		for (int order : Inventory.PAPERDOLL_ORDER)
		{
			writeH(0x12); // 16 + 2
			writeD(_activeChar.getInventory().getPaperdollObjectId(order));
			writeD(_activeChar.getInventory().getPaperdollItemId(order));
			writeH(_activeChar.getInventory().getPaperdollAugmentationId(order));
			writeH(_activeChar.getInventory().getPaperdollAugmentationId(order));
			writeD(_activeChar.getInventory().getVisualItemId(order));
		}
	}
}
