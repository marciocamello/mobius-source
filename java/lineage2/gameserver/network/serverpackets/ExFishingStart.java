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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExFishingStart extends L2GameServerPacket
{
	/**
	 * Field _charObjId.
	 */
	private final int _charObjId;
	/**
	 * Field _loc.
	 */
	private final Location _loc;
	/**
	 * Field _fishType.
	 */
	private final int _fishType;
	/**
	 * Field _isNightLure.
	 */
	private final boolean _isNightLure;
	
	/**
	 * Constructor for ExFishingStart.
	 * @param character Creature
	 * @param fishType int
	 * @param loc Location
	 * @param isNightLure boolean
	 */
	public ExFishingStart(Creature character, int fishType, Location loc, boolean isNightLure)
	{
		_charObjId = character.getObjectId();
		_fishType = fishType;
		_loc = loc;
		_isNightLure = isNightLure;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x1e);
		writeD(_charObjId);
		writeD(_fishType);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeC(_isNightLure ? 0x01 : 0x00);
		writeC(0x01);
	}
}
