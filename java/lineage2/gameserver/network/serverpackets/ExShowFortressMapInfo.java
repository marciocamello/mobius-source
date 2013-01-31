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

import lineage2.gameserver.model.entity.events.impl.FortressSiegeEvent;
import lineage2.gameserver.model.entity.residence.Fortress;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowFortressMapInfo extends L2GameServerPacket
{
	/**
	 * Field _fortressId.
	 */
	private final int _fortressId;
	/**
	 * Field _fortressStatus.
	 */
	private final boolean _fortressStatus;
	/**
	 * Field _commanders.
	 */
	private final boolean[] _commanders;
	
	/**
	 * Constructor for ExShowFortressMapInfo.
	 * @param fortress Fortress
	 */
	public ExShowFortressMapInfo(Fortress fortress)
	{
		_fortressId = fortress.getId();
		_fortressStatus = fortress.getSiegeEvent().isInProgress();
		FortressSiegeEvent siegeEvent = fortress.getSiegeEvent();
		_commanders = siegeEvent.getBarrackStatus();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x7d);
		writeD(_fortressId);
		writeD(_fortressStatus);
		writeD(_commanders.length);
		for (boolean b : _commanders)
		{
			writeD(b);
		}
	}
}
