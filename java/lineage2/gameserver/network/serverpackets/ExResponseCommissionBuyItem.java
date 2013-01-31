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
public class ExResponseCommissionBuyItem extends L2GameServerPacket
{
	/**
	 * Field FAILED.
	 */
	public static final ExResponseCommissionBuyItem FAILED = new ExResponseCommissionBuyItem(0);
	/**
	 * Field _code.
	 */
	private final int _code;
	/**
	 * Field _itemId.
	 */
	private int _itemId;
	/**
	 * Field _count.
	 */
	private long _count;
	
	/**
	 * Constructor for ExResponseCommissionBuyItem.
	 * @param code int
	 */
	public ExResponseCommissionBuyItem(int code)
	{
		_code = code;
	}
	
	/**
	 * Constructor for ExResponseCommissionBuyItem.
	 * @param code int
	 * @param itemId int
	 * @param count long
	 */
	public ExResponseCommissionBuyItem(int code, int itemId, long count)
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
		writeEx(0xF8);
		writeD(_code);
		if (_code == 0)
		{
			return;
		}
		writeD(0x00);
		writeD(_itemId);
		writeQ(_count);
	}
}
