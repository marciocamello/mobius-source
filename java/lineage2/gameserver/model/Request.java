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
package lineage2.gameserver.model;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Request extends MultiValueSet<String>
{
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(Request.class);
	
	public static enum L2RequestType
	{
		CUSTOM,
		PARTY,
		PARTY_ROOM,
		CLAN,
		ALLY,
		TRADE,
		TRADE_REQUEST,
		FRIEND,
		CHANNEL,
		DUEL,
		COUPLE_ACTION,
		MENTEE
	}
	
	private final static AtomicInteger _nextId = new AtomicInteger();
	private final int _id;
	private final L2RequestType _type;
	private final HardReference<Player> _requestor;
	private final HardReference<Player> _reciever;
	private boolean _isRequestorConfirmed;
	private boolean _isRecieverConfirmed;
	private boolean _isCancelled;
	private boolean _isDone;
	private long _timeout;
	private Future<?> _timeoutTask;
	
	public Request(L2RequestType type, Player requestor, Player reciever)
	{
		_id = _nextId.incrementAndGet();
		_requestor = requestor.getRef();
		_reciever = reciever.getRef();
		_type = type;
		requestor.setRequest(this);
		reciever.setRequest(this);
	}
	
	public Request setTimeout(long timeout)
	{
		_timeout = timeout > 0 ? System.currentTimeMillis() + timeout : 0;
		_timeoutTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				timeout();
			}
		}, timeout);
		return this;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public void cancel()
	{
		_isCancelled = true;
		if (_timeoutTask != null)
		{
			_timeoutTask.cancel(false);
		}
		_timeoutTask = null;
		Player player = getRequestor();
		if ((player != null) && (player.getRequest() == this))
		{
			player.setRequest(null);
		}
		player = getReciever();
		if ((player != null) && (player.getRequest() == this))
		{
			player.setRequest(null);
		}
	}
	
	public void done()
	{
		_isDone = true;
		if (_timeoutTask != null)
		{
			_timeoutTask.cancel(false);
		}
		_timeoutTask = null;
		Player player = getRequestor();
		if ((player != null) && (player.getRequest() == this))
		{
			player.setRequest(null);
		}
		player = getReciever();
		if ((player != null) && (player.getRequest() == this))
		{
			player.setRequest(null);
		}
	}
	
	public void timeout()
	{
		Player player = getReciever();
		if (player != null)
		{
			if (player.getRequest() == this)
			{
				player.sendPacket(Msg.TIME_EXPIRED);
			}
		}
		cancel();
	}
	
	public Player getOtherPlayer(Player player)
	{
		if (player == getRequestor())
		{
			return getReciever();
		}
		if (player == getReciever())
		{
			return getRequestor();
		}
		return null;
	}
	
	public Player getRequestor()
	{
		return _requestor.get();
	}
	
	public Player getReciever()
	{
		return _reciever.get();
	}
	
	public boolean isInProgress()
	{
		if (_isCancelled)
		{
			return false;
		}
		if (_isDone)
		{
			return false;
		}
		if (_timeout == 0)
		{
			return true;
		}
		if (_timeout > System.currentTimeMillis())
		{
			return true;
		}
		return false;
	}
	
	public boolean isTypeOf(L2RequestType type)
	{
		return _type == type;
	}
	
	public void confirm(Player player)
	{
		if (player == getRequestor())
		{
			_isRequestorConfirmed = true;
		}
		else if (player == getReciever())
		{
			_isRecieverConfirmed = true;
		}
	}
	
	public boolean isConfirmed(Player player)
	{
		if (player == getRequestor())
		{
			return _isRequestorConfirmed;
		}
		else if (player == getReciever())
		{
			return _isRecieverConfirmed;
		}
		return false;
	}
}
