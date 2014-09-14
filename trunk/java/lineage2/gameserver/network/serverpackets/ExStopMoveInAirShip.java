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
import lineage2.gameserver.utils.Location;

public class ExStopMoveInAirShip extends L2GameServerPacket
{
	private final int char_id, boat_id, char_heading;
	private final Location _loc;
	
	public ExStopMoveInAirShip(Player cha)
	{
		char_id = cha.getObjectId();
		boat_id = cha.getBoat().getBoatId();
		_loc = cha.getInBoatPosition();
		char_heading = cha.getHeading();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x6F);
		writeD(char_id);
		writeD(boat_id);
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ());
		writeD(char_heading);
	}
}