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
package lineage2.commons.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 * @param <E>
 */
public class GArray<E> implements Collection<E>
{
	private transient E[] elementData;
	transient int modCount = 0;
	private int size;
	
	/**
	 * Constructor for GArray.
	 * @param initialCapacity int
	 */
	@SuppressWarnings("unchecked")
	private GArray(int initialCapacity)
	{
		if (initialCapacity < 0)
		{
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		}
		
		elementData = (E[]) new Object[initialCapacity];
	}
	
	/**
	 * Constructor for GArray.
	 */
	public GArray()
	{
		this(10);
	}
	
	/**
	 * Method getCapacity.
	 * @return int
	 */
	public int getCapacity()
	{
		return elementData.length;
	}
	
	/**
	 * Method ensureCapacity.
	 * @param minCapacity int
	 */
	private void ensureCapacity(int minCapacity)
	{
		modCount++;
		int oldCapacity = elementData.length;
		
		if (minCapacity > oldCapacity)
		{
			int newCapacity = ((oldCapacity * 3) / 2) + 1;
			
			if (newCapacity < minCapacity)
			{
				newCapacity = minCapacity;
			}
			
			elementData = Arrays.copyOf(elementData, newCapacity);
		}
	}
	
	/**
	 * Method size.
	 * @return int
	 * @see java.util.Collection#size()
	 */
	@Override
	public int size()
	{
		return size;
	}
	
	/**
	 * Method isEmpty.
	 * @return boolean
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	/**
	 * Method toNativeArray.
	 * @return E[]
	 */
	public E[] toNativeArray()
	{
		return Arrays.copyOf(elementData, size);
	}
	
	/**
	 * Method toArray.
	 * @return Object[] * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray()
	{
		return Arrays.copyOf(elementData, size);
	}
	
	/**
	 * Method toArray.
	 * @param a T[]
	 * @return T[] * @see java.util.Collection#toArray(T[])
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a)
	{
		if (a.length < size)
		{
			return (T[]) Arrays.copyOf(elementData, size, a.getClass());
		}
		
		System.arraycopy(elementData, 0, a, 0, size);
		
		if (a.length > size)
		{
			a[size] = null;
		}
		
		return a;
	}
	
	/**
	 * Method get.
	 * @param index int
	 * @return E
	 */
	E get(int index)
	{
		RangeCheck(index);
		return elementData[index];
	}
	
	/**
	 * Method add.
	 * @param e E
	 * @return boolean
	 */
	@Override
	public boolean add(E e)
	{
		ensureCapacity(size + 1);
		elementData[size++] = e;
		return true;
	}
	
	/**
	 * Method remove.
	 * @param o Object
	 * @return boolean
	 * @see java.util.Collection#remove(Object)
	 */
	@Override
	public boolean remove(Object o)
	{
		if (o == null)
		{
			for (int index = 0; index < size; index++)
			{
				if (elementData[index] == null)
				{
					remove(index);
					return true;
				}
			}
		}
		else
		{
			for (int index = 0; index < size; index++)
			{
				if (o.equals(elementData[index]))
				{
					remove(index);
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Method remove.
	 * @param index int
	 * @return E
	 */
	E remove(int index)
	{
		modCount++;
		RangeCheck(index);
		E old = elementData[index];
		elementData[index] = elementData[size - 1];
		elementData[--size] = null;
		return old;
	}
	
	/**
	 * Method removeFirst.
	 * @return E
	 */
	E removeFirst()
	{
		return size > 0 ? remove(0) : null;
	}
	
	/**
	 * Method removeLast.
	 * @return E
	 */
	E removeLast()
	{
		if (size > 0)
		{
			modCount++;
			size--;
			E old = elementData[size];
			elementData[size] = null;
			return old;
		}
		
		return null;
	}
	
	/**
	 * Method set.
	 * @param index int
	 * @param element E
	 * @return E
	 */
	E set(int index, E element)
	{
		RangeCheck(index);
		E oldValue = elementData[index];
		elementData[index] = element;
		return oldValue;
	}
	
	/**
	 * Method indexOf.
	 * @param o Object
	 * @return int
	 */
	int indexOf(Object o)
	{
		if (o == null)
		{
			for (int i = 0; i < size; i++)
			{
				if (elementData[i] == null)
				{
					return i;
				}
			}
		}
		else
		{
			for (int i = 0; i < size; i++)
			{
				if (o.equals(elementData[i]))
				{
					return i;
				}
			}
		}
		
		return -1;
	}
	
	/**
	 * Method contains.
	 * @param o Object
	 * @return boolean
	 * @see java.util.Collection#contains(Object)
	 */
	@Override
	public boolean contains(Object o)
	{
		if (o == null)
		{
			for (int i = 0; i < size; i++)
			{
				if (elementData[i] == null)
				{
					return true;
				}
			}
		}
		else
		{
			for (int i = 0; i < size; i++)
			{
				if (o.equals(elementData[i]))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Method addAll.
	 * @param c Collection<? extends E>
	 * @return boolean
	 * @see java.util.Collection#addAll(Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		if (c == null)
		{
			return false;
		}
		
		boolean modified = false;
		Iterator<? extends E> e = c.iterator();
		
		while (e.hasNext())
		{
			if (add(e.next()))
			{
				modified = true;
			}
		}
		
		return modified;
	}
	
	/**
	 * Method removeAll.
	 * @param c Collection<?>
	 * @return boolean
	 * @see java.util.Collection#removeAll(Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		boolean modified = false;
		
		for (int i = 0; i < size; i++)
		{
			if (c.contains(elementData[i]))
			{
				modCount++;
				elementData[i] = elementData[size - 1];
				elementData[--size] = null;
				modified = true;
			}
		}
		
		return modified;
	}
	
	/**
	 * Method retainAll.
	 * @param c Collection<?>
	 * @return boolean
	 * @see java.util.Collection#retainAll(Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		boolean modified = false;
		
		for (int i = 0; i < size; i++)
		{
			if (!c.contains(elementData[i]))
			{
				modCount++;
				elementData[i] = elementData[size - 1];
				elementData[--size] = null;
				modified = true;
			}
		}
		
		return modified;
	}
	
	/**
	 * Method containsAll.
	 * @param c Collection<?>
	 * @return boolean
	 * @see java.util.Collection#containsAll(Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c)
	{
		for (int i = 0; i < size; i++)
		{
			if (!contains(elementData[i]))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Method RangeCheck.
	 * @param index int
	 */
	private void RangeCheck(int index)
	{
		if (index >= size)
		{
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
	}
	
	/**
	 * Method clear.
	 * @see java.util.Collection#clear()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void clear()
	{
		modCount++;
		int oldSize = size;
		size = 0;
		
		if (oldSize > 1000)
		{
			elementData = (E[]) new Object[10];
		}
		else
		{
			for (int i = 0; i < oldSize; i++)
			{
				elementData[i] = null;
			}
		}
		
		size = 0;
	}
	
	/**
	 * Method clearSize.
	 */
	void clearSize()
	{
		modCount++;
		size = 0;
	}
	
	/**
	 * Method iterator.
	 * @return Iterator<E> * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<E> iterator()
	{
		return new Itr();
	}
	
	/**
	 * @author Mobius
	 */
	private class Itr implements Iterator<E>
	{
		int cursor = 0;
		int lastRet = -1;
		int expectedModCount = modCount;
		
		/**
		 * Constructor for Itr.
		 */
		public Itr()
		{
		}
		
		/**
		 * Method hasNext.
		 * @return boolean
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext()
		{
			return cursor < size();
		}
		
		/**
		 * Method next.
		 * @return E * @see java.util.Iterator#next()
		 */
		@Override
		public E next()
		{
			checkForComodification();
			
			try
			{
				E next = get(cursor);
				lastRet = cursor++;
				return next;
			}
			catch (IndexOutOfBoundsException e)
			{
				checkForComodification();
				throw new NoSuchElementException();
			}
		}
		
		/**
		 * Method remove.
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove()
		{
			if (lastRet == -1)
			{
				throw new IllegalStateException();
			}
			
			checkForComodification();
			
			try
			{
				GArray.this.remove(lastRet);
				
				if (lastRet < cursor)
				{
					cursor--;
				}
				
				lastRet = -1;
				expectedModCount = modCount;
			}
			catch (IndexOutOfBoundsException e)
			{
				throw new ConcurrentModificationException();
			}
		}
		
		/**
		 * Method checkForComodification.
		 */
		final void checkForComodification()
		{
			if (modCount != expectedModCount)
			{
				throw new ConcurrentModificationException();
			}
		}
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		StringBuffer bufer = new StringBuffer();
		
		for (int i = 0; i < size; i++)
		{
			if (i != 0)
			{
				bufer.append(", ");
			}
			
			bufer.append(elementData[i]);
		}
		
		return "<" + bufer + ">";
	}
}
