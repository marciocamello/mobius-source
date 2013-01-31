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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ChangeWaitType extends L2GameServerPacket
{
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _moveType.
	 */
	private final int _moveType;
	/**
	 * Field _z. Field _y. Field _x.
	 */
	private final int _x, _y, _z;
	/**
	 * Field WT_SITTING. (value is 0)
	 */
	public static final int WT_SITTING = 0;
	/**
	 * Field WT_STANDING. (value is 1)
	 */
	public static final int WT_STANDING = 1;
	/**
	 * Field WT_START_FAKEDEATH. (value is 2)
	 */
	public static final int WT_START_FAKEDEATH = 2;
	/**
	 * Field WT_STOP_FAKEDEATH. (value is 3)
	 */
	public static final int WT_STOP_FAKEDEATH = 3;
	
	/**
	 * Constructor for ChangeWaitType.
	 * @param cha Creature
	 * @param newMoveType int
	 */
	public ChangeWaitType(Creature cha, int newMoveType)
	{
		_objectId = cha.getObjectId();
		_moveType = newMoveType;
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x29);
		writeD(_objectId);
		writeD(_moveType);
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}
}
