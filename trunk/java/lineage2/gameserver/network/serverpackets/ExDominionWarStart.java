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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.DominionSiegeEvent;

/**
 * @author VISTALL
 * @date 12:08/05.03.2011
 */
public class ExDominionWarStart extends L2GameServerPacket
{
	private final int _objectId;
	private final int _territoryId;
	private final boolean _isDisguised;
	
	public ExDominionWarStart(Player player)
	{
		_objectId = player.getObjectId();
		DominionSiegeEvent siegeEvent = player.getEvent(DominionSiegeEvent.class);
		_territoryId = siegeEvent.getId();
		_isDisguised = siegeEvent.getObjects(DominionSiegeEvent.DISGUISE_PLAYERS).contains(_objectId);
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xA4);
		writeD(_objectId);
		writeD(1);
		writeD(_territoryId); // territory Id
		writeD(_isDisguised ? 1 : 0);
		writeD(_isDisguised ? _territoryId : 0); // territory Id
	}
}
