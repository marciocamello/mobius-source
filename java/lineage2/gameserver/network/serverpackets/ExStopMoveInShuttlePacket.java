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

/**
 * @author Bonux
 */
public class ExStopMoveInShuttlePacket extends L2GameServerPacket
{
	private final int _playerObjectId, _shuttleId, _playerHeading;
	private final Location _loc;
	
	public ExStopMoveInShuttlePacket(Player cha)
	{
		_playerObjectId = cha.getObjectId();
		_shuttleId = cha.getBoat().getBoatId();
		_loc = cha.getInBoatPosition();
		_playerHeading = cha.getHeading();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0xD0);
		writeD(_playerObjectId);// Player ObjID
		writeD(_shuttleId);// Shuttle ID (Arkan: 1,2)
		writeD(_loc.x); // X in shuttle
		writeD(_loc.y); // Y in shuttle
		writeD(_loc.z); // Z in shuttle
		writeD(_playerHeading); // H in shuttle
	}
}