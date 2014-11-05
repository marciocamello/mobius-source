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
 * @author Gnacik
 * @corrected by n0nam3
 */
public class ExGetPremiumItemList extends L2GameServerPacket
{
	@SuppressWarnings("unused")
	private final int _objectId;
	private final Map<Integer, PremiumItem> _list;
	
	public ExGetPremiumItemList(Player activeChar)
	{
		_objectId = activeChar.getObjectId();
		_list = activeChar.getPremiumItemList();
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x87);
		
		if (!_list.isEmpty())
		{
			writeD(_list.size());
			
			for (Map.Entry<Integer, PremiumItem> entry : _list.entrySet())
			{
				writeQ(entry.getKey());
				writeD(entry.getValue().getId());
				writeQ(entry.getValue().getCount());
				writeD(0x01);
				writeS(entry.getValue().getSender());
			}
		}
	}
}