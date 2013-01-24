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
package lineage2.commons.util;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;

public class TroveUtils
{
	@SuppressWarnings("rawtypes")
	private static final TIntObjectHashMap EMPTY_INT_OBJECT_MAP = new TIntObjectHashMapEmpty();
	public static final TIntArrayList EMPTY_INT_ARRAY_LIST = new TIntArrayListEmpty();
	
	@SuppressWarnings("unchecked")
	public static <V> TIntObjectHashMap<V> emptyIntObjectMap()
	{
		return EMPTY_INT_OBJECT_MAP;
	}
	
	private static class TIntObjectHashMapEmpty<V> extends TIntObjectHashMap<V>
	{
		TIntObjectHashMapEmpty()
		{
			super(0);
		}
		
		@Override
		public V put(int key, V value)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public V putIfAbsent(int key, V value)
		{
			throw new UnsupportedOperationException();
		}
	}
	
	private static class TIntArrayListEmpty extends TIntArrayList
	{
		TIntArrayListEmpty()
		{
			super(0);
		}
		
		@Override
		public boolean add(int val)
		{
			throw new UnsupportedOperationException();
		}
	}
}
