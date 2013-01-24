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

import lineage2.gameserver.model.items.CrystallizationItem;

public class ExGetCrystalizingEstimation extends L2GameServerPacket
{
	private List<CrystallizationItem> _items;
	
	public ExGetCrystalizingEstimation(List<CrystallizationItem> _items)
	{
		this._items = _items;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xE0);
		writeD(_items.size());
		for (CrystallizationItem i : _items)
		{
			writeD(i.getItemId());
			writeQ(i.getCount());
			writeF(i.getChance());
		}
	}
	
	public void addCrystallizationItem(CrystallizationItem i)
	{
		if (_items == null)
		{
			_items = new ArrayList<>();
		}
		_items.add(i);
	}
}
