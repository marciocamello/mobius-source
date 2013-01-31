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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.data.xml.holder.BuyListHolder.NpcTradeList;
import lineage2.gameserver.model.items.TradeItem;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class BuyListSeed extends L2GameServerPacket
{
	/**
	 * Field _manorId.
	 */
	private final int _manorId;
	/**
	 * Field _list.
	 */
	private List<TradeItem> _list = new ArrayList<>();
	/**
	 * Field _money.
	 */
	private final long _money;
	
	/**
	 * Constructor for BuyListSeed.
	 * @param list NpcTradeList
	 * @param manorId int
	 * @param currentMoney long
	 */
	public BuyListSeed(NpcTradeList list, int manorId, long currentMoney)
	{
		_money = currentMoney;
		_manorId = manorId;
		_list = list.getItems();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xE9);
		writeQ(_money);
		writeD(_manorId);
		writeH(_list.size());
		for (TradeItem item : _list)
		{
			writeItemInfo(item);
			writeQ(item.getOwnersPrice());
		}
	}
}
