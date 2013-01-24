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
package lineage2.commons.net.nio.impl;

import java.util.concurrent.atomic.AtomicLong;

public class SelectorStats
{
	private final AtomicLong _connectionsTotal = new AtomicLong();
	private final AtomicLong _connectionsCurrent = new AtomicLong();
	private final AtomicLong _connectionsMax = new AtomicLong();
	private final AtomicLong _incomingBytesTotal = new AtomicLong();
	private final AtomicLong _outgoingBytesTotal = new AtomicLong();
	private final AtomicLong _incomingPacketsTotal = new AtomicLong();
	private final AtomicLong _outgoingPacketsTotal = new AtomicLong();
	private final AtomicLong _bytesMaxPerRead = new AtomicLong();
	private final AtomicLong _bytesMaxPerWrite = new AtomicLong();
	
	public void increaseOpenedConnections()
	{
		if (_connectionsCurrent.incrementAndGet() > _connectionsMax.get())
		{
			_connectionsMax.incrementAndGet();
		}
		_connectionsTotal.incrementAndGet();
	}
	
	public void decreseOpenedConnections()
	{
		_connectionsCurrent.decrementAndGet();
	}
	
	public void increaseIncomingBytes(int size)
	{
		if (size > _bytesMaxPerRead.get())
		{
			_bytesMaxPerRead.set(size);
		}
		_incomingBytesTotal.addAndGet(size);
	}
	
	public void increaseOutgoingBytes(int size)
	{
		if (size > _bytesMaxPerWrite.get())
		{
			_bytesMaxPerWrite.set(size);
		}
		_outgoingBytesTotal.addAndGet(size);
	}
	
	public void increaseIncomingPacketsCount()
	{
		_incomingPacketsTotal.incrementAndGet();
	}
	
	public void increaseOutgoingPacketsCount()
	{
		_outgoingPacketsTotal.incrementAndGet();
	}
	
	public long getTotalConnections()
	{
		return _connectionsTotal.get();
	}
	
	public long getCurrentConnections()
	{
		return _connectionsCurrent.get();
	}
	
	public long getMaximumConnections()
	{
		return _connectionsMax.get();
	}
	
	public long getIncomingBytesTotal()
	{
		return _incomingBytesTotal.get();
	}
	
	public long getOutgoingBytesTotal()
	{
		return _outgoingBytesTotal.get();
	}
	
	public long getIncomingPacketsTotal()
	{
		return _incomingPacketsTotal.get();
	}
	
	public long getOutgoingPacketsTotal()
	{
		return _outgoingPacketsTotal.get();
	}
	
	public long getMaxBytesPerRead()
	{
		return _bytesMaxPerRead.get();
	}
	
	public long getMaxBytesPerWrite()
	{
		return _bytesMaxPerWrite.get();
	}
}
