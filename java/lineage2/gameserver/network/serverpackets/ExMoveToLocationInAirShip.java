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
import lineage2.gameserver.model.entity.boat.Boat;
import lineage2.gameserver.utils.Location;

public class ExMoveToLocationInAirShip extends L2GameServerPacket
{
	private final int char_id;
	private final int boat_id;
	private final Location _origin;
	private final Location _destination;
	
	public ExMoveToLocationInAirShip(Player cha, Boat boat, Location origin, Location destination)
	{
		char_id = cha.getObjectId();
		boat_id = boat.getBoatId();
		_origin = origin;
		_destination = destination;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x6E);
		writeD(char_id);
		writeD(boat_id);
		writeD(_destination.x);
		writeD(_destination.y);
		writeD(_destination.z);
		writeD(_origin.x);
		writeD(_origin.y);
		writeD(_origin.z);
	}
}