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
package lineage2.commons.lang.reference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class HardReferences
{
	private HardReferences()
	{
	}
	
	private static class EmptyReferencedHolder extends AbstractHardReference<Object>
	{
		public EmptyReferencedHolder(Object reference)
		{
			super(reference);
		}
	}
	
	private static HardReference<?> EMPTY_REF = new EmptyReferencedHolder(null);
	
	@SuppressWarnings("unchecked")
	public static <T> HardReference<T> emptyRef()
	{
		return (HardReference<T>) EMPTY_REF;
	}
	
	public static <T> Collection<T> unwrap(Collection<HardReference<T>> refs)
	{
		List<T> result = new ArrayList<>(refs.size());
		for (HardReference<T> ref : refs)
		{
			T obj = ref.get();
			if (obj != null)
			{
				result.add(obj);
			}
		}
		return result;
	}
	
	private static class WrappedIterable<T> implements Iterable<T>
	{
		final Iterable<HardReference<T>> refs;
		
		WrappedIterable(Iterable<HardReference<T>> refs)
		{
			this.refs = refs;
		}
		
		private static class WrappedIterator<T> implements Iterator<T>
		{
			final Iterator<HardReference<T>> iterator;
			
			WrappedIterator(Iterator<HardReference<T>> iterator)
			{
				this.iterator = iterator;
			}
			
			@Override
			public boolean hasNext()
			{
				return iterator.hasNext();
			}
			
			@Override
			public T next()
			{
				return iterator.next().get();
			}
			
			@Override
			public void remove()
			{
				iterator.remove();
			}
		}
		
		@Override
		public Iterator<T> iterator()
		{
			return new WrappedIterator<>(refs.iterator());
		}
	}
	
	public static <T> Iterable<T> iterate(Iterable<HardReference<T>> refs)
	{
		return new WrappedIterable<>(refs);
	}
}
