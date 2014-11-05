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

/**
 * @author Smo
 */
public class ExAdenaInvenCount extends L2GameServerPacket
{
	private final int invsize;
	private final long count;
	
	public ExAdenaInvenCount(Player pl)
	{
		count = pl.getInventory().getAdena();
		invsize = pl.getInventory().getSize();
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x13E);
		writeQ(count);
		writeH(invsize);
	}
}