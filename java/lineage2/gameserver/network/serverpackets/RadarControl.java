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

import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RadarControl extends L2GameServerPacket
{
	/**
	 * Field _showRadar. Field _type. Field _z. Field _y. Field _x.
	 */
	private final int _x, _y, _z, _type, _showRadar;
	
	/**
	 * Constructor for RadarControl.
	 * @param showRadar int
	 * @param type int
	 * @param loc Location
	 */
	public RadarControl(int showRadar, int type, Location loc)
	{
		this(showRadar, type, loc.x, loc.y, loc.z);
	}
	
	/**
	 * Constructor for RadarControl.
	 * @param showRadar int
	 * @param type int
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public RadarControl(int showRadar, int type, int x, int y, int z)
	{
		_showRadar = showRadar;
		_type = type;
		_x = x;
		_y = y;
		_z = z;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xf1);
		writeD(_showRadar);
		writeD(_type);
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}
}
