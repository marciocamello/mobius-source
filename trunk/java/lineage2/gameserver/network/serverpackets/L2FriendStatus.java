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

public class L2FriendStatus extends L2GameServerPacket
{
	private final String _charName;
	private final boolean _login;
	
	public L2FriendStatus(Player player, boolean login)
	{
		_login = login;
		_charName = player.getName();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x77);
		writeD(_login ? 1 : 0); // Logged in 1 logged off 0
		writeS(_charName);
		writeD(0); // id персонажа с базы оффа, не object_id
	}
}