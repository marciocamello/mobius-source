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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.templates.manor.CropProcure;

public class SellListProcure extends L2GameServerPacket
{
	private final long _money;
	private final Map<ItemInstance, Long> _sellList = new HashMap<>();
	private List<CropProcure> _procureList = new ArrayList<>();
	private final int _castle;
	
	public SellListProcure(Player player, int castleId)
	{
		_money = player.getAdena();
		_castle = castleId;
		_procureList = ResidenceHolder.getInstance().getResidence(Castle.class, _castle).getCropProcure(0);
		for (CropProcure c : _procureList)
		{
			ItemInstance item = player.getInventory().getItemByItemId(c.getId());
			if ((item != null) && (c.getAmount() > 0))
			{
				_sellList.put(item, c.getAmount());
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xef);
		writeQ(_money);
		writeD(0x00); // lease ?
		writeH(_sellList.size()); // list size
		
		for (ItemInstance item : _sellList.keySet())
		{
			writeH(item.getTemplate().getType1());
			writeD(item.getObjectId());
			writeD(item.getItemId());
			writeQ(_sellList.get(item));
			writeH(item.getTemplate().getType2ForPackets());
			writeH(0); // size of [dhhh]
			writeQ(0); // price, u shouldnt get any adena for crops, only raw
						// materials
		}
	}
}