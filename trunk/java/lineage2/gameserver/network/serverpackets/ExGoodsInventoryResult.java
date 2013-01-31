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
public class ExGoodsInventoryResult extends L2GameServerPacket
{
	/**
	 * Field NOTHING.
	 */
	public static L2GameServerPacket NOTHING = new ExGoodsInventoryResult(1);
	/**
	 * Field SUCCESS.
	 */
	public static L2GameServerPacket SUCCESS = new ExGoodsInventoryResult(2);
	/**
	 * Field ERROR.
	 */
	public static L2GameServerPacket ERROR = new ExGoodsInventoryResult(-1);
	/**
	 * Field TRY_AGAIN_LATER.
	 */
	public static L2GameServerPacket TRY_AGAIN_LATER = new ExGoodsInventoryResult(-2);
	/**
	 * Field INVENTORY_FULL.
	 */
	public static L2GameServerPacket INVENTORY_FULL = new ExGoodsInventoryResult(-3);
	/**
	 * Field NOT_CONNECT_TO_PRODUCT_SERVER.
	 */
	public static L2GameServerPacket NOT_CONNECT_TO_PRODUCT_SERVER = new ExGoodsInventoryResult(-4);
	/**
	 * Field CANT_USE_AT_TRADE_OR_PRIVATE_SHOP.
	 */
	public static L2GameServerPacket CANT_USE_AT_TRADE_OR_PRIVATE_SHOP = new ExGoodsInventoryResult(-5);
	/**
	 * Field NOT_EXISTS.
	 */
	public static L2GameServerPacket NOT_EXISTS = new ExGoodsInventoryResult(-6);
	/**
	 * Field TO_MANY_USERS_TRY_AGAIN_INVENTORY.
	 */
	public static L2GameServerPacket TO_MANY_USERS_TRY_AGAIN_INVENTORY = new ExGoodsInventoryResult(-101);
	/**
	 * Field TO_MANY_USERS_TRY_AGAIN.
	 */
	public static L2GameServerPacket TO_MANY_USERS_TRY_AGAIN = new ExGoodsInventoryResult(-102);
	/**
	 * Field PREVIOS_REQUEST_IS_NOT_COMPLETE.
	 */
	public static L2GameServerPacket PREVIOS_REQUEST_IS_NOT_COMPLETE = new ExGoodsInventoryResult(-103);
	/**
	 * Field NOTHING2.
	 */
	public static L2GameServerPacket NOTHING2 = new ExGoodsInventoryResult(-104);
	/**
	 * Field ALREADY_RETRACTED.
	 */
	public static L2GameServerPacket ALREADY_RETRACTED = new ExGoodsInventoryResult(-105);
	/**
	 * Field ALREADY_RECIVED.
	 */
	public static L2GameServerPacket ALREADY_RECIVED = new ExGoodsInventoryResult(-106);
	/**
	 * Field PRODUCT_CANNOT_BE_RECEIVED_AT_CURRENT_SERVER.
	 */
	public static L2GameServerPacket PRODUCT_CANNOT_BE_RECEIVED_AT_CURRENT_SERVER = new ExGoodsInventoryResult(-107);
	/**
	 * Field PRODUCT_CANNOT_BE_RECEIVED_AT_CURRENT_PLAYER.
	 */
	public static L2GameServerPacket PRODUCT_CANNOT_BE_RECEIVED_AT_CURRENT_PLAYER = new ExGoodsInventoryResult(-108);
	/**
	 * Field _result.
	 */
	private final int _result;
	
	/**
	 * Constructor for ExGoodsInventoryResult.
	 * @param result int
	 */
	private ExGoodsInventoryResult(int result)
	{
		_result = result;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xE4);
		writeD(_result);
	}
}
