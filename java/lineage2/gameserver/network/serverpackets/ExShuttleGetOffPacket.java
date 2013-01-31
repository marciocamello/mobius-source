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

import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.entity.boat.Shuttle;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShuttleGetOffPacket extends L2GameServerPacket
{
	/**
	 * Field _shuttleId. Field _playerObjectId.
	 */
	private final int _playerObjectId, _shuttleId;
	/**
	 * Field _loc.
	 */
	private final Location _loc;
	
	/**
	 * Constructor for ExShuttleGetOffPacket.
	 * @param cha Playable
	 * @param shuttle Shuttle
	 * @param loc Location
	 */
	public ExShuttleGetOffPacket(Playable cha, Shuttle shuttle, Location loc)
	{
		_playerObjectId = cha.getObjectId();
		_shuttleId = shuttle.getBoatId();
		_loc = loc;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0xCC);
		writeD(_playerObjectId);
		writeD(_shuttleId);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
	}
}
