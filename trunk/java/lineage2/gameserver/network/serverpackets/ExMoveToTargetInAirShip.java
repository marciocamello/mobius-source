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

public class ExMoveToTargetInAirShip extends L2GameServerPacket
{
	private final int char_id, boat_id, target_id, _dist;
	private final Location _loc;
	
	public ExMoveToTargetInAirShip(Player cha, Boat boat, int targetId, int dist, Location origin)
	{
		char_id = cha.getObjectId();
		boat_id = boat.getBoatId();
		target_id = targetId;
		_dist = dist;
		_loc = origin;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x72);
		
		writeD(char_id); // ID:%d
		writeD(target_id); // TargetID:%d
		writeD(_dist); // Dist:%d
		writeD(_loc.y); // OriginX:%d
		writeD(_loc.z); // OriginY:%d
		writeD(_loc.h); // OriginZ:%d
		writeD(boat_id); // AirShipID:%d
	}
}