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
public class LoginFail extends L2GameServerPacket
{
	/**
	 * Field NO_TEXT.
	 */
	public static int NO_TEXT = 0;
	/**
	 * Field SYSTEM_ERROR_LOGIN_LATER.
	 */
	public static int SYSTEM_ERROR_LOGIN_LATER = 1;
	/**
	 * Field PASSWORD_DOES_NOT_MATCH_THIS_ACCOUNT.
	 */
	public static int PASSWORD_DOES_NOT_MATCH_THIS_ACCOUNT = 2;
	/**
	 * Field PASSWORD_DOES_NOT_MATCH_THIS_ACCOUNT2.
	 */
	public static int PASSWORD_DOES_NOT_MATCH_THIS_ACCOUNT2 = 3;
	/**
	 * Field ACCESS_FAILED_TRY_LATER.
	 */
	public static int ACCESS_FAILED_TRY_LATER = 4;
	/**
	 * Field INCORRECT_ACCOUNT_INFO_CONTACT_CUSTOMER_SUPPORT.
	 */
	public static int INCORRECT_ACCOUNT_INFO_CONTACT_CUSTOMER_SUPPORT = 5;
	/**
	 * Field ACCESS_FAILED_TRY_LATER2.
	 */
	public static int ACCESS_FAILED_TRY_LATER2 = 6;
	/**
	 * Field ACOUNT_ALREADY_IN_USE.
	 */
	public static int ACOUNT_ALREADY_IN_USE = 7;
	/**
	 * Field ACCESS_FAILED_TRY_LATER3.
	 */
	public static int ACCESS_FAILED_TRY_LATER3 = 8;
	/**
	 * Field ACCESS_FAILED_TRY_LATER4.
	 */
	public static int ACCESS_FAILED_TRY_LATER4 = 9;
	/**
	 * Field ACCESS_FAILED_TRY_LATER5.
	 */
	public static int ACCESS_FAILED_TRY_LATER5 = 10;
	/**
	 * Field _reason.
	 */
	private final int _reason;
	
	/**
	 * Constructor for LoginFail.
	 * @param reason int
	 */
	public LoginFail(int reason)
	{
		_reason = reason;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x0a);
		writeD(_reason);
	}
}
