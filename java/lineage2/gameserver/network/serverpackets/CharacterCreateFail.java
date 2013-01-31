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
public class CharacterCreateFail extends L2GameServerPacket
{
	/**
	 * Field REASON_TOO_MANY_CHARACTERS.
	 */
	public static final L2GameServerPacket REASON_TOO_MANY_CHARACTERS = new CharacterCreateFail(0x01);
	/**
	 * Field REASON_NAME_ALREADY_EXISTS.
	 */
	public static final L2GameServerPacket REASON_NAME_ALREADY_EXISTS = new CharacterCreateFail(0x02);
	/**
	 * Field REASON_16_ENG_CHARS.
	 */
	public static final L2GameServerPacket REASON_16_ENG_CHARS = new CharacterCreateFail(0x03);
	/**
	 * Field _error.
	 */
	private final int _error;
	
	/**
	 * Constructor for CharacterCreateFail.
	 * @param errorCode int
	 */
	private CharacterCreateFail(int errorCode)
	{
		_error = errorCode;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x10);
		writeD(_error);
	}
}
