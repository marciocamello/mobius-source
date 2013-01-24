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
import java.util.List;

public class JoinedIterator<E> implements Iterator<E>
{
	private Iterator<E>[] _iterators;
	private int _currentIteratorIndex;
	private Iterator<E> _currentIterator;
	private Iterator<E> _lastUsedIterator;
	
	public JoinedIterator(List<Iterator<E>> iterators)
	{
		this(iterators.toArray(new Iterator[iterators.size()]));
	}
	
	@SuppressWarnings("unchecked")
	public JoinedIterator(Iterator<?>... iterators)
	{
		if (iterators == null)
		{
			throw new NullPointerException("Unexpected NULL iterators argument");
		}
		_iterators = (Iterator<E>[]) iterators;
	}
	
	@Override
	public boolean hasNext()
	{
		updateCurrentIterator();
		return _currentIterator.hasNext();
	}
	
	@Override
	public E next()
	{
		updateCurrentIterator();
		return _currentIterator.next();
	}
	
	@Override
	public void remove()
	{
		updateCurrentIterator();
		_lastUsedIterator.remove();
	}
	
	protected void updateCurrentIterator()
	{
		if (_currentIterator == null)
		{
			if (_iterators.length == 0)
			{
				_currentIterator = EmptyIterator.getInstance();
			}
			else
			{
				_currentIterator = _iterators[0];
			}
			_lastUsedIterator = _currentIterator;
		}
		while (!_currentIterator.hasNext() && (_currentIteratorIndex < (_iterators.length - 1)))
		{
			_currentIteratorIndex++;
			_currentIterator = _iterators[_currentIteratorIndex];
		}
	}
}
