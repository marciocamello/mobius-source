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

import java.util.Collection;

import lineage2.gameserver.model.Player;

/**
 * Format: (chd) ddd[dS]d[dS] d: unknown d: always -1 d: blue players number [ d: player object id S: player name ] d: blue players number [ d: player object id S: player name ]
 */
public class ExCubeGameTeamList extends L2GameServerPacket
{
	Collection<Player> _bluePlayers;
	Collection<Player> _redPlayers;
	int _roomNumber;
	int _timeleft;
	
	public ExCubeGameTeamList(Collection<Player> redPlayers, Collection<Player> bluePlayers, int roomNumber)
	{
		_redPlayers = redPlayers;
		_bluePlayers = bluePlayers;
		_roomNumber = roomNumber - 1;
	}
	
	public ExCubeGameTeamList(Collection<Player> redPlayers, Collection<Player> bluePlayers, int roomNumber, int timeleft)
	{
		_redPlayers = redPlayers;
		_bluePlayers = bluePlayers;
		_roomNumber = roomNumber - 1;
		_timeleft = timeleft;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x97);
		writeD(0x00);
		
		writeD(_roomNumber);
		writeD(_timeleft);
		
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