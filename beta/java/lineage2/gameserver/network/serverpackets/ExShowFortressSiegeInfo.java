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
 * @author VISTALL
 */
public class ExShowFortressSiegeInfo extends L2GameServerPacket
{
	@SuppressWarnings("unused")
	private final int _fortressId;
	private final int _commandersMax;
	@SuppressWarnings("unused")
	private int _commandersCurrent;
	
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
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x17);
		writeH(0x00);
		// writeD(_fortressId);
		// writeD(_commandersMax);
		// writeD(_commandersCurrent);
	}
}