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

import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DeleteObject extends L2GameServerPacket
{
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	
	/**
	 * Constructor for DeleteObject.
	 * @param obj GameObject
	 */
	public DeleteObject(GameObject obj)
	{
		_objectId = obj.getObjectId();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if ((activeChar == null) || (activeChar.getObjectId() == _objectId))
		{
			return;
		}
		writeC(0x08);
		writeD(_objectId);
		writeD(0x01);
	}
	
	/**
	 * Method getType.
	 * @return String
	 */
	@Override
	public String getType()
	{
		return super.getType() + " " + GameObjectsStorage.findObject(_objectId) + " (" + _objectId + ")";
	}
}
