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

import java.nio.ByteBuffer;

public abstract class MMOClient<T extends MMOConnection>
{
	private T _connection;
	private boolean isAuthed;
	
	public MMOClient(T con)
	{
		_connection = con;
	}
	
	protected void setConnection(T con)
	{
		_connection = con;
	}
	
	public T getConnection()
	{
		return _connection;
	}
	
	public boolean isAuthed()
	{
		return isAuthed;
	}
	
	public void setAuthed(boolean isAuthed)
	{
		this.isAuthed = isAuthed;
	}
	
	public void closeNow(boolean error)
	{
		if (isConnected())
		{
			_connection.closeNow();
		}
	}
	
	public void closeLater()
	{
		if (isConnected())
		{
			_connection.closeLater();
		}
	}
	
	public boolean isConnected()
	{
		return (_connection != null) && !_connection.isClosed();
	}
	
	public abstract boolean decrypt(ByteBuffer buf, int size);
	
	public abstract boolean encrypt(ByteBuffer buf, int size);
	
	protected void onDisconnection()
	{
	}
	
	protected void onForcedDisconnection()
	{
	}
}
