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
 * @author : Darvin
 * @date : 26.01.12 18:13
 */
public class ExBR_PresentBuyProductPacket extends L2GameServerPacket
{
	public static final int RESULT_OK = 1; // OK
	public static final int RESULT_NOT_ENOUGH_POINTS = -1;
	public static final int RESULT_WRONG_PRODUCT = -2;
	public static final int RESULT_INVENTORY_FULL = -4;
	public static final int RESULT_WRONG_ITEM = -5;
	public static final int RESULT_SALE_PERIOD_ENDED = -7;
	public static final int RESULT_WRONG_USER_STATE = -9; // also -11
	public static final int RESULT_WRONG_PACKAGE_ITEMS = -10;
	public static final int RESULT_WRONG_DAY_OF_WEEK = -12;
	public static final int RESULT_WRONG_SALE_PERIOD = -13;
	public static final int RESULT_ITEM_WAS_SALED = -14;
	private final int result;
	
	public ExBR_PresentBuyProductPacket(int result)
	{
		this.result = result;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x11A);
		writeD(result);
		writeC(0x00);
	}
}
