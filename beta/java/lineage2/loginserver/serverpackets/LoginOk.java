/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.loginserver.serverpackets;

import lineage2.loginserver.SessionKey;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class LoginOk extends L2LoginServerPacket
{
	private final int _loginOk1, _loginOk2;
	
	/**
	 * Constructor for LoginOk.
	 * @param sessionKey SessionKey
	 */
	public LoginOk(SessionKey sessionKey)
	{
		_loginOk1 = sessionKey.loginOkID1;
		_loginOk2 = sessionKey.loginOkID2;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x03);
		writeD(_loginOk1);
		writeD(_loginOk2);
		writeB(new byte[8]);
		writeD(1002);
		writeH(60872);
		writeC(35);
		writeC(6);
		writeB(new byte[28]);
	}
}
