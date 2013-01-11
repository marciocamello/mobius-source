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
package lineage2.commons.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

@SuppressWarnings("unchecked")
public class LazyArrayList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
	private static final long serialVersionUID = 8683452581122892189L;
	
	@SuppressWarnings("rawtypes")
	private static class PoolableLazyArrayListFactory implements PoolableObjectFactory
	{
		public PoolableLazyArrayListFactory()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public Object makeObject()
		{
			return new LazyArrayList();
		}
		
		@Override
		public void destroyObject(Object obj)
		{
			((LazyArrayList) obj).clear();
		}
		
		@Override
		public boolean validateObject(Object obj)
		{
			return true;
		}
		
		@Override
		public void activateObject(Object obj)
		{
		}
		
		@Override
		public void passivateObject(Object obj)
		{
			((LazyArrayList) obj).clear();
		}
	}
	
	private static final int POOL_SIZE = Integer.parseInt(System.getProperty("lazyarraylist.poolsize", "-1"));
	private static final ObjectPool POOL = new GenericObjectPool(new PoolableLazyArrayListFactory(), POOL_SIZE, GenericObjectPool.WHEN_EXHAUSTED_GROW, 0L, -1);
	
	public static <E> LazyArrayList<E> newInstance()
	{
		try
		{
			return (LazyArrayList<E>) POOL.borrowObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new LazyArrayList<>();
	}
	
	public static <E> void recycle(LazyArrayList<E> obj)
	{
		try
		{
			POOL.returnObject(obj);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static final int L = 1 << 3;
	private static final int H = 1 << 10;
	protected transient Object[] elementData;
	protected transient int size = 0;
	protected transient int capacity = L;
	
	public LazyArrayList(int initialCapacity)
	{
		if (initialCapacity < H)
		{
			while (capacity < initialCapacity)
			{
				capacity <<= 1;
			}
		}
		else
		{
			capacity = initialCapacity;
		}
	}
	
	public LazyArrayList()
	{
		this(8);
	}
	
	@Override
	public boolean add(E element)
	{
		ensureCapacity(size + 1);
		elementData[size++] = element;
		return true;
	}
	
	@Override
	public E set(int index, E element)
	{
		E e = null;
		if ((index >= 0) && (index < size))
		{
			e = (E) elementData[index];
			elementData[index] = element;
		}
		return e;
	}
	
	@Override
	public void add(int index, E element)
	{
		if ((index >= 0) && (index < size))
		{
			ensureCapacity(size + 1);
			System.arraycopy(elementData, index, elementData, index + 1, size - index);
			elementData[index] = element;
			size++;
		}
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		if ((c == null) || c.isEmpty())
		{
			return false;
		}
		if ((index >= 0) && (index < size))
		{
			Object[] a = c.toArray();
			int numNew = a.length;
			ensureCapacity(size + numNew);
			int numMoved = size - index;
			if (numMoved > 0)
			{
				System.arraycopy(elementData, index, elementData, index + numNew, numMoved);
			}
			System.arraycopy(a, 0, elementData, index, numNew);
			size += numNew;
			return true;
		}
		return false;
	}
	
	protected void ensureCapacity(int newSize)
	{
		if (newSize > capacity)
		{
			if (newSize < H)
			{
				while (capacity < newSize)
				{
					capacity <<= 1;
				}
			}
			else
			{
				while (capacity < newSize)
				{
					capacity = (capacity * 3) / 2;
				}
			}
			Object[] elementDataResized = new Object[capacity];
			if (elementData != null)
			{
				System.arraycopy(elementData, 0, elementDataResized, 0, size);
			}
			elementData = elementDataResized;
		}
		else if (elementData == null)
		{
			elementData = new Object[capacity];
		}
	}
	
	@Override
	public E remove(int index)
	{
		E e = null;
		if ((index >= 0) && (index < size))
		{
			size--;
			e = (E) elementData[index];
			elementData[index] = elementData[size];
			elementData[size] = null;
			trim();
		}
		return e;
	}
	
	@Override
	public boolean remove(Object o)
	{
		if (size == 0)
		{
			return false;
		}
		int index = -1;
		for (int i = 0; i < size; i++)
		{
			if (elementData[i] == o)
			{
				index = i;
				break;
			}
		}
		if (index == -1)
		{
			return false;
		}
		size--;
		elementData[index] = elementData[size];
		elementData[size] = null;
		trim();
		return true;
	}
	
	@Override
	public boolean contains(Object o)
	{
		if (size == 0)
		{
			return false;
		}
		for (int i = 0; i < size; i++)
		{
			if (elementData[i] == o)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int indexOf(Object o)
	{
		if (size == 0)
		{
			return -1;
		}
		int index = -1;
		for (int i = 0; i < size; i++)
		{
			if (elementData[i] == o)
			{
				index = i;
				break;
			}
		}
		return index;
	}
	
	@Override
	public int lastIndexOf(Object o)
	{
		if (size == 0)
		{
			return -1;
		}
		int index = -1;
		for (int i = 0; i < size; i++)
		{
			if (elementData[i] == o)
			{
				index = i;
			}
		}
		return index;
	}
	
	protected void trim()
	{
	}
	
	@Override
	public E get(int index)
	{
		if ((size > 0) && (index >= 0) && (index < size))
		{
			return (E) elementData[index];
		}
		return null;
	}
	
	@Override
	public Object clone()
	{
		LazyArrayList<E> clone = new LazyArrayList<>();
		if (size > 0)
		{
			clone.capacity = capacity;
			clone.elementData = new Object[elementData.length];
			System.arraycopy(elementData, 0, clone.elementData, 0, size);
		}
		return clone;
	}
	
	@Override
	public void clear()
	{
		if (size == 0)
		{
			return;
		}
		for (int i = 0; i < size; i++)
		{
			elementData[i] = null;
		}
		size = 0;
		trim();
	}
	
	@Override
	public int size()
	{
		return size;
	}
	
	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	public int capacity()
	{
		return capacity;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		if ((c == null) || c.isEmpty())
		{
			return false;
		}
		Object[] a = c.toArray();
		int numNew = a.length;
		ensureCapacity(size + numNew);
		System.arraycopy(a, 0, elementData, size, numNew);
		size += numNew;
		return true;
	}
	
	@Override
	public boolean containsAll(Collection<?> c)
	{
		if (c == null)
		{
			return false;
		}
		if (c.isEmpty())
		{
			return true;
		}
		Iterator<?> e = c.iterator();
		while (e.hasNext())
		{
			if (!contains(e.next()))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean retainAll(Collection<?> c)
	{
		if (c == null)
		{
			return false;
		}
		boolean modified = false;
		Iterator<E> e = iterator();
		while (e.hasNext())
		{
			if (!c.contains(e.next()))
			{
				e.remove();
				modified = true;
			}
		}
		return modified;
	}
	
	@Override
	public boolean removeAll(Collection<?> c)
	{
		if ((c == null) || c.isEmpty())
		{
			return false;
		}
		boolean modified = false;
		Iterator<?> e = iterator();
		while (e.hasNext())
		{
			if (c.contains(e.next()))
			{
				e.remove();
				modified = true;
			}
		}
		return modified;
	}
	
	@Override
	public Object[] toArray()
	{
		Object[] r = new Object[size];
		if (size > 0)
		{
			System.arraycopy(elementData, 0, r, 0, size);
		}
		return r;
	}
	
	@Override
	public <T> T[] toArray(T[] a)
	{
		T[] r = a.length >= size ? a : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
		if (size > 0)
		{
			System.arraycopy(elementData, 0, r, 0, size);
		}
		if (r.length > size)
		{
			r[size] = null;
		}
		return r;
	}
	
	@Override
	public Iterator<E> iterator()
	{
		return new LazyItr();
	}
	
	@Override
	public ListIterator<E> listIterator()
	{
		return new LazyListItr(0);
	}
	
	@Override
	public ListIterator<E> listIterator(int index)
	{
		return new LazyListItr(index);
	}
	
	private class LazyItr implements Iterator<E>
	{
		int cursor = 0;
		int lastRet = -1;
		
		public LazyItr()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public boolean hasNext()
		{
			return cursor < size();
		}
		
		@Override
		public E next()
		{
			E next = get(cursor);
			lastRet = cursor++;
			return next;
		}
		
		@Override
		public void remove()
		{
			if (lastRet == -1)
			{
				throw new IllegalStateException();
			}
			LazyArrayList.this.remove(lastRet);
			if (lastRet < cursor)
			{
				cursor--;
			}
			lastRet = -1;
		}
	}
	
	private class LazyListItr extends LazyItr implements ListIterator<E>
	{
		LazyListItr(int index)
		{
			cursor = index;
		}
		
		@Override
		public boolean hasPrevious()
		{
			return cursor > 0;
		}
		
		@Override
		public E previous()
		{
			int i = cursor - 1;
			E previous = get(i);
			lastRet = cursor = i;
			return previous;
		}
		
		@Override
		public int nextIndex()
		{
			return cursor;
		}
		
		@Override
		public int previousIndex()
		{
			return cursor - 1;
		}
		
		@Override
		public void set(E e)
		{
			if (lastRet == -1)
			{
				throw new IllegalStateException();
			}
			LazyArrayList.this.set(lastRet, e);
		}
		
		@Override
		public void add(E e)
		{
			LazyArrayList.this.add(cursor++, e);
			lastRet = -1;
		}
	}
	
	@Override
	public String toString()
	{
		if (size == 0)
		{
			return "[]";
		}
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < size; i++)
		{
			Object e = elementData[i];
			sb.append(e == this ? "this" : e);
			if (i == (size - 1))
			{
				sb.append(']').toString();
			}
			else
			{
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	
	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		throw new UnsupportedOperationException();
	}
}
