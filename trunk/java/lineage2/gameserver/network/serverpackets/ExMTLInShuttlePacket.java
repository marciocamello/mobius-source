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
 * @author Bonux
 */
public class ExMTLInShuttlePacket extends L2GameServerPacket
{
	private final int _playableObjectId;
	private final int _shuttleId;
	private final Location _origin;
	private final Location _destination;
	
	public ExMTLInShuttlePacket(Player player, Shuttle shuttle, Location origin, Location destination)
	{
		_playableObjectId = player.getObjectId();
		_shuttleId = shuttle.getBoatId();
		_origin = origin;
		_destination = destination;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0xCF);
		writeD(_playableObjectId); // Player ObjID
		writeD(_shuttleId); // Shuttle ID (Arkan: 1,2; Cruma: 3)
		writeD(_destination.x); // Destination X in shuttle
		writeD(_destination.y); // Destination Y in shuttle
		writeD(_destination.z); // Destination Z in shuttle
		writeD(_origin.x); // X in shuttle
		writeD(_origin.y); // Y in shuttle
		writeD(_origin.z); // Z in shuttle
	}
}