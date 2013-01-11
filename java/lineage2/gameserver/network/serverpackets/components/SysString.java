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
package lineage2.gameserver.network.serverpackets.components;

public enum SysString
{
	PASSENGER_BOAT_INFO(801),
	PREVIOUS(1037),
	NEXT(1038);
	private static final SysString[] VALUES = values();
	private final int _id;
	
	SysString(int i)
	{
		_id = i;
	}
	
	public int getId()
	{
		return _id;
	}
	
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
