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
import lineage2.gameserver.model.entity.boat.Shuttle;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExMTLInShuttlePacket extends L2GameServerPacket
{
	/**
	 * Field _shuttleId. Field _playerObjectId.
	 */
	private final int _playerObjectId, _shuttleId;
	/**
	 * Field _destination. Field _origin.
	 */
	private final Location _origin, _destination;
	
	/**
	 * Constructor for ExMTLInShuttlePacket.
	 * @param player Player
	 * @param shuttle Shuttle
	 * @param origin Location
	 * @param destination Location
	 */
	public ExMTLInShuttlePacket(Player player, Shuttle shuttle, Location origin, Location destination)
	{
		_playerObjectId = player.getObjectId();
		_shuttleId = shuttle.getBoatId();
		_origin = origin;
		_destination = destination;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0xCE);
		writeD(_playerObjectId);
		writeD(_shuttleId);
		writeD(_destination.x);
		writeD(_destination.y);
		writeD(_destination.z);
		writeD(_origin.x);
		writeD(_origin.y);
		writeD(_origin.z);
	}
}
