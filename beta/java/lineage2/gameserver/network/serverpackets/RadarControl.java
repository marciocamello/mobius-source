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

public class RadarControl extends L2GameServerPacket
{
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _type;
	private final int _showRadar;
	
	public RadarControl(int showRadar, int type, Location loc)
	{
		this(showRadar, type, loc.getX(), loc.getY(), loc.getZ());
	}
	
	public RadarControl(int showRadar, int type, int x, int y, int z)
	{
		_showRadar = showRadar; // showRadar?? 0 = showRadar; 1 = delete radar;
		_type = type;
		_x = x;
		_y = y;
		_z = z;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xf1);
		writeD(_showRadar);
		writeD(_type); // maybe type
		writeD(_x); // x
		writeD(_y); // y
		writeD(_z); // z
	}
}