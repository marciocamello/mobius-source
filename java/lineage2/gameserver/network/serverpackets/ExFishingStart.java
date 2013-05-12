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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.utils.Location;

/**
 * Format (ch)ddddd
 */
public class ExFishingStart extends L2GameServerPacket
{
	private final int _charObjId;
	private final Location _loc;
	private final int _fishType;
	private final boolean _isNightLure;
	
	public ExFishingStart(Creature character, int fishType, Location loc, boolean isNightLure)
	{
		_charObjId = character.getObjectId();
		_fishType = fishType;
		_loc = loc;
		_isNightLure = isNightLure;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x1e);
		writeD(_charObjId);
		writeD(_fishType); // fish type
		writeD(_loc.x); // x poisson
		writeD(_loc.y); // y poisson
		writeD(_loc.z); // z poisson
		writeC(_isNightLure ? 0x01 : 0x00); // 0 = day lure 1 = night lure
		writeC(0x01); // result Button
	}
}