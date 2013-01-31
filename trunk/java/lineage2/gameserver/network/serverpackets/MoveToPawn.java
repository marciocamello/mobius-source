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
public class MoveToPawn extends L2GameServerPacket
{
	/**
	 * Field _distance. Field _targetId. Field _chaId.
	 */
	private final int _chaId, _targetId, _distance;
	/**
	 * Field _tz. Field _ty. Field _tx. Field _z. Field _y. Field _x.
	 */
	private final int _x, _y, _z, _tx, _ty, _tz;
	
	/**
	 * Constructor for MoveToPawn.
	 * @param cha Creature
	 * @param target Creature
	 * @param distance int
	 */
	public MoveToPawn(Creature cha, Creature target, int distance)
	{
		_chaId = cha.getObjectId();
		_targetId = target.getObjectId();
		_distance = distance;
		_x = cha.getX();
		_y = cha.getY();
		_z = cha.getZ();
		_tx = target.getX();
		_ty = target.getY();
		_tz = target.getZ();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x72);
		writeD(_chaId);
		writeD(_targetId);
		writeD(_distance);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(_tx);
		writeD(_ty);
		writeD(_tz);
	}
}
