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
package lineage2.loginserver.serverpackets;

public final class PlayFail extends L2LoginServerPacket
{
	public static final int REASON_SYSTEM_ERROR = 1;
	public static final int REASON_ACCESS_FAILED_1 = 2;
	public static final int REASON_ACCOUNT_INFO_INCORRECT = 3;
	public static final int REASON_PASSWORD_INCORRECT_1 = 4;
	public static final int REASON_PASSWORD_INCORRECT_2 = 5;
	public static final int REASON_NO_REASON = 6;
	public static final int REASON_SYS_ERROR = 7;
	public static final int REASON_ACCESS_FAILED_2 = 8;
	public static final int REASON_HIGH_SERVER_TRAFFIC = 9;
	public static final int REASON_MIN_AGE = 10;
	private final int _reason;
	
	public PlayFail(int reason)
	{
		_reason = reason;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0x06);
		writeC(_reason);
	}
}
