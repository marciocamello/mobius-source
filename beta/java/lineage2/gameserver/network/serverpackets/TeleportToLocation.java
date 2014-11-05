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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.utils.Location;

/**
 * format dddd
 * <p/>
 * sample 0000: 3a 69 08 10 48 02 c1 00 00 f7 56 00 00 89 ea ff :i..H.....V..... 0010: ff 0c b2 d8 61 ....a
 */
public class TeleportToLocation extends L2GameServerPacket
{
	private final int _targetId;
	private final Location _loc;
	
	public TeleportToLocation(GameObject cha, Location loc)
	{
		_targetId = cha.getObjectId();
		_loc = loc;
	}
	
	public TeleportToLocation(GameObject cha, int x, int y, int z)
	{
		_targetId = cha.getObjectId();
		_loc = new Location(x, y, z, cha.getHeading());
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x22);
		writeD(_targetId);
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ() + Config.CLIENT_Z_SHIFT);
		writeD(0x00); // IsValidation
		writeD(_loc.getHeading());
		// writeD(0); // ??? 0 я вот это чо то упустил) ну поставлю пока так
	}
}