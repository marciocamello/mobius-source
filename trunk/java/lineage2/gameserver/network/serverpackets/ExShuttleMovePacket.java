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

import lineage2.gameserver.model.entity.boat.Shuttle;

/**
 * @author Bonux
 */
public class ExShuttleMovePacket extends L2GameServerPacket
{
	private final Shuttle _shuttle;
	
	public ExShuttleMovePacket(Shuttle shuttle)
	{
		_shuttle = shuttle;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0xCE);
		writeD(_shuttle.getBoatId()); // Shuttle ID (Arkan: 1,2; Cruma: 3)
		writeD(_shuttle.getMoveSpeed()); // Speed
		writeD(0x00); // unk: 0 (0x00000000)
		writeD(_shuttle.getDestination().getX()); // Destination X
		writeD(_shuttle.getDestination().getY()); // Destination Y
		writeD(_shuttle.getDestination().getZ()); // Destination Z
	}
}