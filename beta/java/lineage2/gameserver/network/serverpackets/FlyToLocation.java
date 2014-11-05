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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.utils.Location;

public class FlyToLocation extends L2GameServerPacket
{
	private final int _chaObjId;
	private final FlyType _type;
	private final Location _loc;
	private final Location _destLoc;
	private final int _speed;
	
	public enum FlyType
	{
		THROW_UP,
		THROW_HORIZONTAL,
		DUMMY,
		CHARGE,
		PUSH_HORIZONTAL,
		JUMP_EFFECTED,
		NOT_USED,
		PUSH_DOWN_HORIZONTAL,
		WARP_BACK,
		WARP_FORWARD,
		NONE;
	}
	
	public FlyToLocation(Creature cha, Location destLoc, FlyType type, int speed)
	{
		_destLoc = destLoc;
		_type = type;
		_chaObjId = cha.getObjectId();
		_loc = cha.getLoc();
		_speed = speed;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xd4);
		writeD(_chaObjId);
		writeD(_destLoc.getX());
		writeD(_destLoc.getY());
		writeD(_destLoc.getZ());
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ());
		writeD(_type.ordinal());
		writeD(_speed);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
	}
}