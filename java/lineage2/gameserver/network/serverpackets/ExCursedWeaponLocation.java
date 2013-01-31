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

import java.util.List;

import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExCursedWeaponLocation extends L2GameServerPacket
{
	/**
	 * Field _cursedWeaponInfo.
	 */
	private final List<CursedWeaponInfo> _cursedWeaponInfo;
	
	/**
	 * Constructor for ExCursedWeaponLocation.
	 * @param cursedWeaponInfo List<CursedWeaponInfo>
	 */
	public ExCursedWeaponLocation(List<CursedWeaponInfo> cursedWeaponInfo)
	{
		_cursedWeaponInfo = cursedWeaponInfo;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x47);
		if (_cursedWeaponInfo.isEmpty())
		{
			writeD(0);
		}
		else
		{
			writeD(_cursedWeaponInfo.size());
			for (CursedWeaponInfo w : _cursedWeaponInfo)
			{
				writeD(w._id);
				writeD(w._status);
				writeD(w._pos.x);
				writeD(w._pos.y);
				writeD(w._pos.z);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class CursedWeaponInfo
	{
		/**
		 * Field _pos.
		 */
		public Location _pos;
		/**
		 * Field _id.
		 */
		public int _id;
		/**
		 * Field _status.
		 */
		public int _status;
		
		/**
		 * Constructor for CursedWeaponInfo.
		 * @param p Location
		 * @param ID int
		 * @param status int
		 */
		public CursedWeaponInfo(Location p, int ID, int status)
		{
			_pos = p;
			_id = ID;
			_status = status;
		}
	}
}
