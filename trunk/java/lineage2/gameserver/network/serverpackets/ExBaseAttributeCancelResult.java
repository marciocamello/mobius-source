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

import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExBaseAttributeCancelResult extends L2GameServerPacket
{
	/**
	 * Field _result.
	 */
	private final boolean _result;
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _element.
	 */
	private final Element _element;
	
	/**
	 * Constructor for ExBaseAttributeCancelResult.
	 * @param result boolean
	 * @param item ItemInstance
	 * @param element Element
	 */
	public ExBaseAttributeCancelResult(boolean result, ItemInstance item, Element element)
	{
		_result = result;
		_objectId = item.getObjectId();
		_element = element;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x75);
		writeD(_result);
		writeD(_objectId);
		writeD(_element.getId());
	}
}
