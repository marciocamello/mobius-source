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
public class ExConfirmAddingPostFriend extends L2GameServerPacket
{
	/**
	 * Field NAME_IS_NOT_EXISTS.
	 */
	public static int NAME_IS_NOT_EXISTS = 0;
	/**
	 * Field SUCCESS.
	 */
	public static int SUCCESS = 1;
	/**
	 * Field PREVIOS_NAME_IS_BEEN_REGISTERED.
	 */
	public static int PREVIOS_NAME_IS_BEEN_REGISTERED = -1;
	/**
	 * Field NAME_IS_NOT_EXISTS2.
	 */
	public static int NAME_IS_NOT_EXISTS2 = -2;
	/**
	 * Field LIST_IS_FULL.
	 */
	public static int LIST_IS_FULL = -3;
	/**
	 * Field ALREADY_ADDED.
	 */
	public static int ALREADY_ADDED = -4;
	/**
	 * Field NAME_IS_NOT_REGISTERED.
	 */
	public static int NAME_IS_NOT_REGISTERED = -4;
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _result.
	 */
	private final int _result;
	
	/**
	 * Constructor for ExConfirmAddingPostFriend.
	 * @param name String
	 * @param s int
	 */
	public ExConfirmAddingPostFriend(String name, int s)
	{
		_name = name;
		_result = s;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	public void writeImpl()
	{
		writeEx(0xD2);
		writeS(_name);
		writeD(_result);
	}
}
