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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FlyToLocation extends L2GameServerPacket
{
	/**
	 * Field _chaObjId.
	 */
	private final int _chaObjId;
	/**
	 * Field _type.
	 */
	private final FlyType _type;
	/**
	 * Field _loc.
	 */
	private final Location _loc;
	/**
	 * Field _destLoc.
	 */
	private final Location _destLoc;
	/**
	 * Field _speed.
	 */
	private final int _speed;
	
	/**
	 * @author Mobius
	 */
	public enum FlyType
	{
		/**
		 * Field THROW_UP.
		 */
		THROW_UP,
		/**
		 * Field THROW_HORIZONTAL.
		 */
		THROW_HORIZONTAL,
		/**
		 * Field DUMMY.
		 */
		DUMMY,
		/**
		 * Field CHARGE.
		 */
		CHARGE,
		/**
		 * Field PUSH_HORIZONTAL.
		 */
		PUSH_HORIZONTAL,
		/**
		 * Field JUMP_EFFECTED.
		 */
		JUMP_EFFECTED,
		/**
		 * Field NOT_USED.
		 */
		NOT_USED,
		/**
		 * Field PUSH_DOWN_HORIZONTAL.
		 */
		PUSH_DOWN_HORIZONTAL,
		/**
		 * Field WARP_BACK.
		 */
		WARP_BACK,
		/**
		 * Field WARP_FORWARD.
		 */
		WARP_FORWARD,
		/**
		 * Field NONE.
		 */
		NONE
	}
	
	/**
	 * Constructor for FlyToLocation.
	 * @param cha Creature
	 * @param destLoc Location
	 * @param type FlyType
	 * @param speed int
	 */
	public FlyToLocation(Creature cha, Location destLoc, FlyType type, int speed)
	{
		_destLoc = destLoc;
		_type = type;
		_chaObjId = cha.getObjectId();
		_loc = cha.getLoc();
		_speed = speed;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0xD4);
		writeD(_chaObjId);
		writeD(_destLoc.x);
		writeD(_destLoc.y);
		writeD(_destLoc.z);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_type.ordinal());
		writeD(_speed);
		writeD(0x00);
	}
}
