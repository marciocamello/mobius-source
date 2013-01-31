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
package lineage2.gameserver.network.serverpackets.components;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum SysString
{
	/**
	 * Field PASSENGER_BOAT_INFO.
	 */
	PASSENGER_BOAT_INFO(801),
	/**
	 * Field PREVIOUS.
	 */
	PREVIOUS(1037),
	/**
	 * Field NEXT.
	 */
	NEXT(1038);
	/**
	 * Field VALUES.
	 */
	private static final SysString[] VALUES = values();
	/**
	 * Field _id.
	 */
	private final int _id;
	
	/**
	 * Constructor for SysString.
	 * @param i int
	 */
	SysString(int i)
	{
		_id = i;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method valueOf2.
	 * @param id String
	 * @return SysString
	 */
	public static SysString valueOf2(String id)
	{
		for (SysString m : VALUES)
		{
			if (m.name().equals(id))
			{
				return m;
			}
		}
		return null;
	}
	
	/**
	 * Method valueOf.
	 * @param id int
	 * @return SysString
	 */
	public static SysString valueOf(int id)
	{
		for (SysString m : VALUES)
		{
			if (m.getId() == id)
			{
				return m;
			}
		}
		return null;
	}
}
