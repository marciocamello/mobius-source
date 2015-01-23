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
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Request extends MultiValueSet<String>
{
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(Request.class);
	
	/**
	 * @author Mobius
	 */
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
		MENTEE,
		SUBSTITUTE
	}
	
	private final static AtomicInteger _nextId = new AtomicInteger();
	private final int _id;
	private final L2RequestType _type;
	private final HardReference<Player> _requestor;
	private final HardReference<Player> _receiver;
	private boolean _isRequestorConfirmed;
	private boolean _isRecieverConfirmed;
	private boolean _isCancelled;
	private boolean _isDone;
	private long _timeout;
	private Future<?> _timeoutTask;
	
	/**
	 * Constructor for Request.
	 * @param type L2RequestType
	 * @param requestor Player
	 * @param receiver Player
	 */
	public Request(L2RequestType type, Player requestor, Player receiver)
	{
		_id = _nextId.incrementAndGet();
		_requestor = requestor.getRef();
		_receiver = receiver.getRef();
		_type = type;
		requestor.setRequest(this);
		receiver.setRequest(this);
	}
	
	/**
	 * Method setTimeout.
	 * @param timeout long
	 * @return Request
	 */
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
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method cancel.
	 */
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
		
		player = getReceiver();
		
		if ((player != null) && (player.getRequest() == this))
		{
			player.setRequest(null);
		}
	}
	
	/**
	 * Method done.
	 */
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
		
		player = getReceiver();
		
		if ((player != null) && (player.getRequest() == this))
		{
			player.setRequest(null);
		}
	}
	
	/**
	 * Method timeout.
	 */
	void timeout()
	{
		Player player = getReceiver();
		
		if (player != null)
		{
			if (player.getRequest() == this)
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.TIME_EXPIRED));
			}
		}
		
		cancel();
	}
	
	/**
	 * Method getOtherPlayer.
	 * @param player Player
	 * @return Player
	 */
	public Player getOtherPlayer(Player player)
	{
		if (player == getRequestor())
		{
			return getReceiver();
		}
		
		if (player == getReceiver())
		{
			return getRequestor();
		}
		
		return null;
	}
	
	/**
	 * Method getRequestor.
	 * @return Player
	 */
	public Player getRequestor()
	{
		return _requestor.get();
	}
	
	/**
	 * Method getReciever.
	 * @return Player
	 */
	public Player getReceiver()
	{
		return _receiver.get();
	}
	
	/**
	 * Method isInProgress.
	 * @return boolean
	 */
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
	
	/**
	 * Method isTypeOf.
	 * @param type L2RequestType
	 * @return boolean
	 */
	public boolean isTypeOf(L2RequestType type)
	{
		return _type == type;
	}
	
	/**
	 * Method confirm.
	 * @param player Player
	 */
	public void confirm(Player player)
	{
		if (player == getRequestor())
		{
			_isRequestorConfirmed = true;
		}
		else if (player == getReceiver())
		{
			_isRecieverConfirmed = true;
		}
	}
	
	/**
	 * Method isConfirmed.
	 * @param player Player
	 * @return boolean
	 */
	public boolean isConfirmed(Player player)
	{
		if (player == getRequestor())
		{
			return _isRequestorConfirmed;
		}
		else if (player == getReceiver())
		{
			return _isRecieverConfirmed;
		}
		
		return false;
	}
}
