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
public class CharacterDeleteFail extends L2GameServerPacket
{
	/**
	 * Field REASON_DELETION_FAILED.
	 */
	public static int REASON_DELETION_FAILED = 0x01;
	/**
	 * Field REASON_YOU_MAY_NOT_DELETE_CLAN_MEMBER.
	 */
	public static int REASON_YOU_MAY_NOT_DELETE_CLAN_MEMBER = 0x02;
	/**
	 * Field REASON_CLAN_LEADERS_MAY_NOT_BE_DELETED.
	 */
	public static int REASON_CLAN_LEADERS_MAY_NOT_BE_DELETED = 0x03;
	/**
	 * Field _error.
	 */
	int _error;
	
	/**
	 * Constructor for CharacterDeleteFail.
	 * @param error int
	 */
	public CharacterDeleteFail(int error)
	{
		_error = error;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x1E);
		writeD(_error);
	}
}
