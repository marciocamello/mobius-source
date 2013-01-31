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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExDominionWarStart extends L2GameServerPacket
{
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _territoryId.
	 */
	private final int _territoryId;
	/**
	 * Field _isDisguised.
	 */
	private final boolean _isDisguised;
	
	/**
	 * Constructor for ExDominionWarStart.
	 * @param player Player
	 */
	public ExDominionWarStart(Player player)
	{
		_objectId = player.getObjectId();
		DominionSiegeEvent siegeEvent = player.getEvent(DominionSiegeEvent.class);
		_territoryId = siegeEvent.getId();
		_isDisguised = siegeEvent.getObjects(DominionSiegeEvent.DISGUISE_PLAYERS).contains(_objectId);
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xA3);
		writeD(_objectId);
		writeD(1);
		writeD(_territoryId);
		writeD(_isDisguised ? 1 : 0);
		writeD(_isDisguised ? _territoryId : 0);
	}
}
