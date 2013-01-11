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
package lineage2.gameserver.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameObjectArray<E extends GameObject> implements Iterable<E>
{
	private static final Logger _log = LoggerFactory.getLogger(GameObjectArray.class);
	public final String name;
	public final int resizeStep, initCapacity;
	private final List<Integer> freeIndexes;
	E[] elementData;
	int size = 0;
	private int real_size = 0;
	
	@SuppressWarnings("unchecked")
	public GameObjectArray(String _name, int initialCapacity, int _resizeStep)
	{
		name = _name;
		resizeStep = _resizeStep;
		initCapacity = initialCapacity;
		if (initialCapacity < 0)
		{
			throw new IllegalArgumentException("Illegal Capacity (" + name + "): " + initialCapacity);
		}
		if (resizeStep < 1)
		{
			throw new IllegalArgumentException("Illegal resize step (" + name + "): " + resizeStep);
		}
		freeIndexes = new ArrayList<>(resizeStep);
		elementData = (E[]) new GameObject[initialCapacity];
	}
	
	public int size()
	{
		return size;
	}
	
	public int getRealSize()
	{
		return real_size;
	}
	
	public int capacity()
	{
		return elementData.length;
	}
	
	public synchronized int add(E e)
	{
		Integer freeIndex = null;
		if (freeIndexes.size() > 0)
		{
			freeIndex = freeIndexes.remove(freeIndexes.size() - 1);
		}
		if (freeIndex != null)
		{
			real_size++;
			elementData[freeIndex] = e;
			return freeIndex;
		}
		if (elementData.length <= size)
		{
			int newCapacity = elementData.length + resizeStep;
			_log.warn("Object array [" + name + "] resized: " + elementData.length + " -> " + newCapacity);
			elementData = Arrays.copyOf(elementData, newCapacity);
		}
		elementData[size++] = e;
		real_size++;
		return size - 1;
	}
	
	public synchronized E remove(int index, int expectedObjId)
	{
		if (index >= size)
		{
			return null;
		}
		E old = elementData[index];
		if ((old == null) || (old.getObjectId() != expectedObjId))
		{
			return null;
		}
		elementData[index] = null;
		real_size--;
		if (index == (size - 1))
		{
			size--;
		}
		else
		{
			freeIndexes.add(index);
		}
		return old;
	}
	
	public E get(int index)
	{
		return index >= size ? null : elementData[index];
	}
	
	public E findByObjectId(int objId)
	{
		if (objId <= 0)
		{
			return null;
		}
		E o;
		for (int i = 0; i < size; i++)
		{
			o = elementData[i];
			if ((o != null) && (o.getObjectId() == objId))
			{
				return o;
			}
		}
		return null;
	}
	
	public E findByName(String s)
	{
		if (s == null)
		{
			return null;
		}
		E o;
		for (int i = 0; i < size; i++)
		{
			o = elementData[i];
			if ((o != null) && s.equalsIgnoreCase(o.getName()))
			{
				return o;
			}
		}
		return null;
	}
	
	public List<E> findAllByName(String s)
	{
		if (s == null)
		{
			return null;
		}
		List<E> result = new ArrayList<>();
		E o;
		for (int i = 0; i < size; i++)
		{
			o = elementData[i];
			if ((o != null) && s.equalsIgnoreCase(o.getName()))
			{
				result.add(o);
			}
		}
		return result;
	}
	
	public List<E> getAll()
	{
		return getAll(new ArrayList<E>(size));
	}
	
	public List<E> getAll(List<E> list)
	{
		E o;
		for (int i = 0; i < size; i++)
		{
			o = elementData[i];
			if (o != null)
			{
				list.add(o);
			}
		}
		return list;
	}
	
	private int indexOf(E o)
	{
		if (o == null)
		{
			return -1;
		}
		for (int i = 0; i < size; i++)
		{
			if (o.equals(elementData[i]))
			{
				return i;
			}
		}
		return -1;
	}
	
	public boolean contains(E o)
	{
		return indexOf(o) > -1;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void clear()
	{
		elementData = (E[]) new GameObject[0];
		size = 0;
		real_size = 0;
	}
	
	@Override
	public Iterator<E> iterator()
	{
		return new Itr();
	}
	
	class Itr implements Iterator<E>
	{
		private int cursor = 0;
		private E _next;
		
		@Override
		public boolean hasNext()
		{
			while (cursor < size)
			{
				if ((_next = elementData[cursor++]) != null)
				{
					return true;
				}
			}
			return false;
		}
		
		@Override
		public E next()
		{
			E result = _next;
			_next = null;
			if (result == null)
			{
				throw new NoSuchElementException();
			}
			return result;
		}
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
}
