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

import java.util.Map;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.PremiumItem;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExGetPremiumItemList extends L2GameServerPacket
{
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _list.
	 */
	private final Map<Integer, PremiumItem> _list;
	
	/**
	 * Constructor for ExGetPremiumItemList.
	 * @param activeChar Player
	 */
	public ExGetPremiumItemList(Player activeChar)
	{
		_objectId = activeChar.getObjectId();
		_list = activeChar.getPremiumItemList();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x86);
		if (!_list.isEmpty())
		{
			writeD(_list.size());
			for (Map.Entry<Integer, PremiumItem> entry : _list.entrySet())
			{
				writeD(entry.getKey());
				writeD(_objectId);
				writeD(entry.getValue().getItemId());
				writeQ(entry.getValue().getCount());
				writeD(0);
				writeS(entry.getValue().getSender());
			}
		}
	}
}
