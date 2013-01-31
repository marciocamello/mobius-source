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
public class ExCleftList extends L2GameServerPacket
{
	/**
	 * Field CleftType_Close. (value is -1)
	 */
	public static final int CleftType_Close = -1;
	/**
	 * Field CleftType_Total. (value is 0)
	 */
	public static final int CleftType_Total = 0;
	/**
	 * Field CleftType_Add. (value is 1)
	 */
	public static final int CleftType_Add = 1;
	/**
	 * Field CleftType_Remove. (value is 2)
	 */
	public static final int CleftType_Remove = 2;
	/**
	 * Field CleftType_TeamChange. (value is 3)
	 */
	public static final int CleftType_TeamChange = 3;
	/**
	 * Field CleftType.
	 */
	private final int CleftType = 0;
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x94);
		writeD(CleftType);
		switch (CleftType)
		{
			case CleftType_Total:
				break;
			case CleftType_Add:
				break;
			case CleftType_Remove:
				break;
			case CleftType_TeamChange:
				break;
			case CleftType_Close:
				break;
		}
	}
}
