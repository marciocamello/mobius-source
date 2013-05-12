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
import lineage2.gameserver.templates.ShuttleTemplate.ShuttleDoor;

/**
 * @author Bonux
 */
public class ExShuttleInfoPacket extends L2GameServerPacket
{
	private final Shuttle _shuttle;
	private final Collection<ShuttleDoor> _doors;
	
	public ExShuttleInfoPacket(Shuttle shuttle)
	{
		_shuttle = shuttle;
		_doors = shuttle.getTemplate().getDoors();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0xCB);
		writeD(_shuttle.getBoatId()); // Shuttle ID (Arkan: 1,2; Cruma: 3)
		writeD(_shuttle.getX()); // X
		writeD(_shuttle.getY()); // Y
		writeD(_shuttle.getZ()); // Z
		writeD(0/* _shuttle.getHeading() */); // Maybe H
		writeD(_shuttle.getBoatId()); // unk??
		writeD(_doors.size()); // doors_count
		for (ShuttleDoor door : _doors)
		{
			int doorId = door.getId();
			writeD(doorId); // Door ID
			writeD(door.unkParam[0]); // unk0
			writeD(door.unkParam[1]); // unk1
			writeD(door.unkParam[2]); // unk2
			writeD(door.unkParam[3]); // unk3
			writeD(door.unkParam[4]); // unk4
			writeD(door.unkParam[5]); // unk5
			writeD(door.unkParam[6]); // unk6
			writeD(door.unkParam[7]); // unk7
			writeD(door.unkParam[8]); // unk8
			boolean thisFloorDoor = _shuttle.getCurrentFloor().isThisFloorDoor(doorId);
			writeD(thisFloorDoor && _shuttle.isDocked());
			writeD(thisFloorDoor);
		}
	}
}