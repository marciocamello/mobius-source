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

public class FriendAddRequestResult extends L2GameServerPacket
{
	public FriendAddRequestResult()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x55);
		// TODO: when implementing, consult an up-to-date
		// packets_game_server.xml and/or savormix
		writeD(0); // Accepted
		writeD(0); // Character ID
		writeS(""); // Name
		writeD(0); // Online
		writeD(0); // Friend OID
		writeD(0); // Level
		writeD(0); // Class
		writeH(0); // ??? 0
	}
}
