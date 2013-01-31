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

import lineage2.gameserver.model.entity.boat.Shuttle;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShuttleMovePacket extends L2GameServerPacket
{
	/**
	 * Field _shuttle.
	 */
	private final Shuttle _shuttle;
	
	/**
	 * Constructor for ExShuttleMovePacket.
	 * @param shuttle Shuttle
	 */
	public ExShuttleMovePacket(Shuttle shuttle)
	{
		_shuttle = shuttle;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0xCD);
		writeD(_shuttle.getBoatId());
		writeD(_shuttle.getMoveSpeed());
		writeD(0x00);
		writeD(_shuttle.getDestination().x);
		writeD(_shuttle.getDestination().y);
		writeD(_shuttle.getDestination().z);
	}
}
