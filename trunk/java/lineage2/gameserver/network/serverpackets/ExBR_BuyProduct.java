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
public class ExBR_BuyProduct extends L2GameServerPacket
{
	/**
	 * Field RESULT_OK. (value is 1)
	 */
	public static final int RESULT_OK = 1;
	/**
	 * Field RESULT_NOT_ENOUGH_POINTS. (value is -1)
	 */
	public static final int RESULT_NOT_ENOUGH_POINTS = -1;
	/**
	 * Field RESULT_WRONG_PRODUCT. (value is -2)
	 */
	public static final int RESULT_WRONG_PRODUCT = -2;
	/**
	 * Field RESULT_INVENTORY_FULL. (value is -4)
	 */
	public static final int RESULT_INVENTORY_FULL = -4;
	/**
	 * Field RESULT_SALE_PERIOD_ENDED. (value is -7)
	 */
	public static final int RESULT_SALE_PERIOD_ENDED = -7;
	/**
	 * Field RESULT_WRONG_USER_STATE. (value is -9)
	 */
	public static final int RESULT_WRONG_USER_STATE = -9;
	/**
	 * Field RESULT_WRONG_PRODUCT_ITEM. (value is -10)
	 */
	public static final int RESULT_WRONG_PRODUCT_ITEM = -10;
	/**
	 * Field _result.
	 */
	private final int _result;
	
	/**
	 * Constructor for ExBR_BuyProduct.
	 * @param result int
	 */
	public ExBR_BuyProduct(int result)
	{
		_result = result;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xD8);
		writeD(_result);
	}
}
