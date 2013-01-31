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
public class ExResponseCommissionDelete extends L2GameServerPacket
{
	/**
	 * Field _code.
	 */
	private final int _code;
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _count.
	 */
	private final long _count;
	
	/**
	 * Constructor for ExResponseCommissionDelete.
	 * @param code int
	 * @param itemId int
	 * @param count long
	 */
	public ExResponseCommissionDelete(int code, int itemId, long count)
	{
		_code = code;
		_itemId = itemId;
		_count = count;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xF5);
		writeD(_code);
		writeD(_itemId);
		writeQ(_count);
	}
}
