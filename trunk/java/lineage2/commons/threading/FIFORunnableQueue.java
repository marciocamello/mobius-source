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
package lineage2.commons.threading;

import java.util.Queue;

public abstract class FIFORunnableQueue<T extends Runnable> implements Runnable
{
	private static final int NONE = 0;
	private static final int QUEUED = 1;
	private static final int RUNNING = 2;
	private int _state = NONE;
	private final Queue<T> _queue;
	
	public FIFORunnableQueue(Queue<T> queue)
	{
		_queue = queue;
	}
	
	public void execute(T t)
	{
		_queue.add(t);
		synchronized (this)
		{
			if (_state != NONE)
			{
				return;
			}
			_state = QUEUED;
		}
		execute();
	}
	
	protected abstract void execute();
	
	public void clear()
	{
		_queue.clear();
	}
	
	@Override
	public void run()
	{
		synchronized (this)
		{
			if (_state == RUNNING)
			{
				return;
			}
			_state = RUNNING;
		}
		try
		{
			for (;;)
			{
				final Runnable t = _queue.poll();
				if (t == null)
				{
					break;
				}
				t.run();
			}
		}
		finally
		{
			synchronized (this)
			{
				_state = NONE;
			}
		}
	}
}
