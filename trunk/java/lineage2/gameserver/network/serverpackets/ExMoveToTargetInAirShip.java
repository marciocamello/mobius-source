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
public class ExMoveToTargetInAirShip extends L2GameServerPacket
{
	/**
	 * Field _dist. Field target_id. Field boat_id. Field char_id.
	 */
	private final int char_id, boat_id, target_id, _dist;
	/**
	 * Field _loc.
	 */
	private final Location _loc;
	
	/**
	 * Constructor for ExMoveToTargetInAirShip.
	 * @param cha Player
	 * @param boat Boat
	 * @param targetId int
	 * @param dist int
	 * @param origin Location
	 */
	public ExMoveToTargetInAirShip(Player cha, Boat boat, int targetId, int dist, Location origin)
	{
		char_id = cha.getObjectId();
		boat_id = boat.getObjectId();
		target_id = targetId;
		_dist = dist;
		_loc = origin;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x71);
		writeD(char_id);
		writeD(target_id);
		writeD(_dist);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_loc.h);
		writeD(boat_id);
	}
}
