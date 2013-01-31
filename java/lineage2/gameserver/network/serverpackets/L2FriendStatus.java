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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class L2FriendStatus extends L2GameServerPacket
{
	/**
	 * Field _charName.
	 */
	private final String _charName;
	/**
	 * Field _login.
	 */
	private final boolean _login;
	
	/**
	 * Constructor for L2FriendStatus.
	 * @param player Player
	 * @param login boolean
	 */
	public L2FriendStatus(Player player, boolean login)
	{
		_login = login;
		_charName = player.getName();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x77);
		writeD(_login ? 1 : 0);
		writeS(_charName);
		writeD(0);
	}
}
