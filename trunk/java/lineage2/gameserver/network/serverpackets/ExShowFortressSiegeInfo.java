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
public class ExShowFortressSiegeInfo extends L2GameServerPacket
{
	/**
	 * Field _fortressId.
	 */
	private final int _fortressId;
	/**
	 * Field _commandersMax.
	 */
	private final int _commandersMax;
	/**
	 * Field _commandersCurrent.
	 */
	private int _commandersCurrent;
	
	/**
	 * Constructor for ExShowFortressSiegeInfo.
	 * @param fortress Fortress
	 */
	public ExShowFortressSiegeInfo(Fortress fortress)
	{
		_fortressId = fortress.getId();
		FortressSiegeEvent siegeEvent = fortress.getSiegeEvent();
		_commandersMax = siegeEvent.getBarrackStatus().length;
		if (fortress.getSiegeEvent().isInProgress())
		{
			for (int i = 0; i < _commandersMax; i++)
			{
				if (siegeEvent.getBarrackStatus()[i])
				{
					_commandersCurrent++;
				}
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x17);
		writeD(_fortressId);
		writeD(_commandersMax);
		writeD(_commandersCurrent);
	}
}
