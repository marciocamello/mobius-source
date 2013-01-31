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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExMoveToLocationInAirShip extends L2GameServerPacket
{
	/**
	 * Field boat_id. Field char_id.
	 */
	private final int char_id, boat_id;
	/**
	 * Field _destination. Field _origin.
	 */
	private final Location _origin, _destination;
	
	/**
	 * Constructor for ExMoveToLocationInAirShip.
	 * @param cha Player
	 * @param boat Boat
	 * @param origin Location
	 * @param destination Location
	 */
	public ExMoveToLocationInAirShip(Player cha, Boat boat, Location origin, Location destination)
	{
		char_id = cha.getObjectId();
		boat_id = boat.getObjectId();
		_origin = origin;
		_destination = destination;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x6D);
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
