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

import lineage2.gameserver.model.entity.boat.Shuttle;
import lineage2.gameserver.templates.ShuttleTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShuttleInfoPacket extends L2GameServerPacket
{
	/**
	 * Field _shuttle.
	 */
	private final Shuttle _shuttle;
	/**
	 * Field _doors.
	 */
	private final Collection<ShuttleTemplate.ShuttleDoor> _doors;
	
	/**
	 * Constructor for ExShuttleInfoPacket.
	 * @param shuttle Shuttle
	 */
	public ExShuttleInfoPacket(Shuttle shuttle)
	{
		_shuttle = shuttle;
		_doors = shuttle.getTemplate().getDoors();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0xCA);
		writeD(_shuttle.getBoatId());
		writeD(_shuttle.getX());
		writeD(_shuttle.getY());
		writeD(_shuttle.getZ());
		writeD(_shuttle.getHeading());
		writeD(_shuttle.getBoatId());
		writeD(_doors.size());
		for (ShuttleTemplate.ShuttleDoor door : _doors)
		{
			int doorId = door.getId();
			writeD(doorId);
			writeD(door.unkParam[0]);
			writeD(door.unkParam[1]);
			writeD(door.unkParam[2]);
			writeD(door.unkParam[3]);
			writeD(door.unkParam[4]);
			writeD(door.unkParam[5]);
			writeD(door.unkParam[6]);
			writeD(door.unkParam[7]);
			writeD(door.unkParam[8]);
			boolean thisFloorDoor = _shuttle.getCurrentFloor().isThisFloorDoor(doorId);
			writeD(thisFloorDoor && _shuttle.isDocked());
			writeD(thisFloorDoor);
		}
	}
}
