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

import lineage2.gameserver.utils.Location;

public class ExJumpToLocation extends L2GameServerPacket
{
	private final int _objectId;
	private final Location _current;
	private final Location _destination;
	
	public ExJumpToLocation(int objectId, Location from, Location to)
	{
		_objectId = objectId;
		_current = from;
		_destination = to;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x89);
		
		writeD(_objectId);
		
		writeD(_destination.x);
		writeD(_destination.y);
		writeD(_destination.z);
		
		writeD(_current.x);
		writeD(_current.y);
		writeD(_current.z);
	}
}