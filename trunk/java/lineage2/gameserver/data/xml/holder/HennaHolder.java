/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.data.xml.holder;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.templates.Henna;

public final class HennaHolder extends AbstractHolder
{
	private static final HennaHolder _instance = new HennaHolder();
	private final TIntObjectHashMap<Henna> _hennas = new TIntObjectHashMap<>();
	
	public static HennaHolder getInstance()
	{
		return _instance;
	}
	
	public void addHenna(Henna h)
	{
		_hennas.put(h.getSymbolId(), h);
	}
	
	public Henna getHenna(int symbolId)
	{
		return _hennas.get(symbolId);
	}
	
	public List<Henna> generateList(Player player)
	{
		List<Henna> list = new ArrayList<>();
		for (TIntObjectIterator<Henna> iterator = _hennas.iterator(); iterator.hasNext();)
		{
			iterator.advance();
			Henna h = iterator.value();
			if (h.isForThisClass(player))
			{
				list.add(h);
			}
		}
		return list;
	}
	
	@Override
	public int size()
	{
		return _hennas.size();
	}
	
	@Override
	public void clear()
	{
		_hennas.clear();
	}
}
