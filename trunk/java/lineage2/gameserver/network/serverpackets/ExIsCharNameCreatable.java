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

public class ExIsCharNameCreatable extends L2GameServerPacket
{
	private static final String _S__FE_110_EXISCHARNAMECREATABLE = "[S] FE:110 ExIsCharNameCreatable";
	public static final int REASON_CREATION_OK = -1;
	public static final int REASON_CREATION_FAILED = 0x00;
	public static final int REASON_TOO_MANY_CHARACTERS = 0x01;
	public static final int REASON_NAME_ALREADY_EXISTS = 0x02;
	public static final int REASON_16_ENG_CHARS = 0x03;
	public static final int REASON_INCORRECT_NAME = 0x04;
	public static final int REASON_CHARS_CANT_CREATED_FROM_SERVER = 0x05;
	public static final int REASON_UNABLE_CREATE_REASON_TOO_CHAR = 0x06;
	private final int _code;
	
	public ExIsCharNameCreatable(int errorCode)
	{
		_code = errorCode;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x110);
		writeD(_code); // 1 - name is create, 0 - name is not create
	}
	
	@Override
	public String getType()
	{
		return _S__FE_110_EXISCHARNAMECREATABLE;
	}
}