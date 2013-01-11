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
package lineage2.gameserver;

import java.util.Calendar;

import lineage2.commons.listener.Listener;
import lineage2.commons.listener.ListenerList;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.listener.GameListener;
import lineage2.gameserver.listener.game.OnDayNightChangeListener;
import lineage2.gameserver.listener.game.OnStartListener;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ClientSetTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameTimeController
{
	private class OnStartListenerImpl implements OnStartListener
	{
		public OnStartListenerImpl()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onStart()
		{
			ThreadPoolManager.getInstance().execute(_dayChangeNotify);
		}
	}
	
	public class CheckSunState extends RunnableImpl
	{
		@Override
		public void runImpl()
		{
			if (isNowNight())
			{
				getInstance().getListenerEngine().onNight();
			}
			else
			{
				getInstance().getListenerEngine().onDay();
			}
			for (Player player : GameObjectsStorage.getAllPlayersForIterate())
			{
				player.checkDayNightMessages();
				player.sendPacket(new ClientSetTime());
			}
		}
	}
	
	protected class GameTimeListenerList extends ListenerList<GameServer>
	{
		public void onDay()
		{
			for (Listener<GameServer> listener : getListeners())
			{
				if (OnDayNightChangeListener.class.isInstance(listener))
				{
					((OnDayNightChangeListener) listener).onDay();
				}
			}
		}
		
		public void onNight()
		{
			for (Listener<GameServer> listener : getListeners())
			{
				if (OnDayNightChangeListener.class.isInstance(listener))
				{
					((OnDayNightChangeListener) listener).onNight();
				}
			}
		}
	}
	
	private static final Logger _log = LoggerFactory.getLogger(GameTimeController.class);
	public static final int TICKS_PER_SECOND = 10;
	public static final int MILLIS_IN_TICK = 1000 / TICKS_PER_SECOND;
	private static final GameTimeController _instance = new GameTimeController();
	private final long _gameStartTime;
	private final GameTimeListenerList listenerEngine = new GameTimeListenerList();
	final Runnable _dayChangeNotify = new CheckSunState();
	
	public static final GameTimeController getInstance()
	{
		return _instance;
	}
	
	private GameTimeController()
	{
		_gameStartTime = getDayStartTime();
		GameServer.getInstance().addListener(new OnStartListenerImpl());
		StringBuilder msg = new StringBuilder();
		msg.append("GameTimeController: initialized.").append(" ");
		msg.append("Current time is ");
		msg.append(getGameHour()).append(":");
		if (getGameMin() < 10)
		{
			msg.append("0");
		}
		msg.append(getGameMin());
		msg.append(" in the ");
		if (isNowNight())
		{
			msg.append("night");
		}
		else
		{
			msg.append("day");
		}
		msg.append(".");
		_log.info(msg.toString());
		long nightStart = 0;
		long dayStart = 60 * 60 * 1000;
		while ((_gameStartTime + nightStart) < System.currentTimeMillis())
		{
			nightStart += 4 * 60 * 60 * 1000;
		}
		while ((_gameStartTime + dayStart) < System.currentTimeMillis())
		{
			dayStart += 4 * 60 * 60 * 1000;
		}
		dayStart -= System.currentTimeMillis() - _gameStartTime;
		nightStart -= System.currentTimeMillis() - _gameStartTime;
		ThreadPoolManager.getInstance().scheduleAtFixedRate(_dayChangeNotify, nightStart, 4 * 60 * 60 * 1000L);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(_dayChangeNotify, dayStart, 4 * 60 * 60 * 1000L);
	}
	
	private long getDayStartTime()
	{
		Calendar dayStart = Calendar.getInstance();
		int HOUR_OF_DAY = dayStart.get(Calendar.HOUR_OF_DAY);
		dayStart.add(Calendar.HOUR_OF_DAY, -(HOUR_OF_DAY + 1) % 4);
		dayStart.set(Calendar.MINUTE, 0);
		dayStart.set(Calendar.SECOND, 0);
		dayStart.set(Calendar.MILLISECOND, 0);
		return dayStart.getTimeInMillis();
	}
	
	public boolean isNowNight()
	{
		return getGameHour() < 6;
	}
	
	public int getGameTime()
	{
		return getGameTicks() / MILLIS_IN_TICK;
	}
	
	public int getGameHour()
	{
		return (getGameTime() / 60) % 24;
	}
	
	public int getGameMin()
	{
		return getGameTime() % 60;
	}
	
	public int getGameTicks()
	{
		return (int) ((System.currentTimeMillis() - _gameStartTime) / MILLIS_IN_TICK);
	}
	
	public GameTimeListenerList getListenerEngine()
	{
		return listenerEngine;
	}
	
	public <T extends GameListener> boolean addListener(T listener)
	{
		return listenerEngine.add(listener);
	}
	
	public <T extends GameListener> boolean removeListener(T listener)
	{
		return listenerEngine.remove(listener);
	}
}
