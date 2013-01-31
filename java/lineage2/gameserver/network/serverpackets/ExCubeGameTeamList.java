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

import java.util.List;

import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExCubeGameTeamList extends L2GameServerPacket
{
	/**
	 * Field _bluePlayers.
	 */
	List<Player> _bluePlayers;
	/**
	 * Field _redPlayers.
	 */
	List<Player> _redPlayers;
	/**
	 * Field _roomNumber.
	 */
	int _roomNumber;
	
	/**
	 * Constructor for ExCubeGameTeamList.
	 * @param redPlayers List<Player>
	 * @param bluePlayers List<Player>
	 * @param roomNumber int
	 */
	public ExCubeGameTeamList(List<Player> redPlayers, List<Player> bluePlayers, int roomNumber)
	{
		_redPlayers = redPlayers;
		_bluePlayers = bluePlayers;
		_roomNumber = roomNumber - 1;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x97);
		writeD(0x00);
		writeD(_roomNumber);
		writeD(0xffffffff);
		writeD(_bluePlayers.size());
		for (Player player : _bluePlayers)
		{
			writeD(player.getObjectId());
			writeS(player.getName());
		}
		writeD(_redPlayers.size());
		for (Player player : _redPlayers)
		{
			writeD(player.getObjectId());
			writeS(player.getName());
		}
	}
}
