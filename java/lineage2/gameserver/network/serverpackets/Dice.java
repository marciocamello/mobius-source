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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Dice extends L2GameServerPacket
{
	/**
	 * Field _playerId.
	 */
	private final int _playerId;
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _number.
	 */
	private final int _number;
	/**
	 * Field _x.
	 */
	private final int _x;
	/**
	 * Field _y.
	 */
	private final int _y;
	/**
	 * Field _z.
	 */
	private final int _z;
	
	/**
	 * Constructor for Dice.
	 * @param playerId int
	 * @param itemId int
	 * @param number int
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public Dice(int playerId, int itemId, int number, int x, int y, int z)
	{
		_playerId = playerId;
		_itemId = itemId;
		_number = number;
		_x = x;
		_y = y;
		_z = z;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xDA);
		writeD(_playerId);
		writeD(_itemId);
		writeD(_number);
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}
}
