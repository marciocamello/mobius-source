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

import java.util.Iterator;

public class EmptyIterator<E> implements Iterator<E>
{
	private static final Iterator INSTANCE = new EmptyIterator();
	
	@SuppressWarnings("unchecked")
	public static <E> Iterator<E> getInstance()
	{
		return INSTANCE;
	}
	
	private EmptyIterator()
	{
	}
	
	@Override
	public boolean hasNext()
	{
		return false;
	}
	
	@Override
	public E next()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}
}
