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

import lineage2.gameserver.model.PremiumItem;

/**
 * @author VISTALL
 * @date 23:37/23.03.2011
 */
public class ExGoodsInventoryInfo extends L2GameServerPacket
{
	private final Map<Integer, PremiumItem> _premiumItemMap;
	
	public ExGoodsInventoryInfo(Map<Integer, PremiumItem> premiumItemMap)
	{
		_premiumItemMap = premiumItemMap;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void writeImpl()
	{
		writeEx(0x112);
		if (!_premiumItemMap.isEmpty())
		{
			writeH(_premiumItemMap.size());
			for (Map.Entry entry : _premiumItemMap.entrySet())
			{
				writeQ((Integer) entry.getKey());
				writeC(0);
				writeD(10003);
				writeS(((PremiumItem) entry.getValue()).getSender());
				writeS(((PremiumItem) entry.getValue()).getSender());// ((PremiumItem)entry.getValue()).getSenderMessage());
				writeQ(0);
				writeC(2);
				writeC(0);
				
				writeS(null);
				writeS(null);
				
				writeH(1);
				writeD(((PremiumItem) entry.getValue()).getItemId());
				writeD((int) ((PremiumItem) entry.getValue()).getCount());
			}
		}
		else
		{
			writeH(0);
		}
	}
}
