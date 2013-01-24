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
package lineage2.gameserver.taskmanager.actionrunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.commons.logging.LoggerObject;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.taskmanager.actionrunner.tasks.AutomaticTask;
import lineage2.gameserver.taskmanager.actionrunner.tasks.CommissionShopExpiredItemsTask;
import lineage2.gameserver.taskmanager.actionrunner.tasks.DeleteExpiredMailTask;
import lineage2.gameserver.taskmanager.actionrunner.tasks.DeleteExpiredVarsTask;
import lineage2.gameserver.taskmanager.actionrunner.tasks.OlympiadSaveTask;

public class ActionRunner extends LoggerObject
{
	private static ActionRunner _instance = new ActionRunner();
	private final Map<String, List<ActionWrapper>> _futures = new HashMap<>();
	private final Lock _lock = new ReentrantLock();
	
	public static ActionRunner getInstance()
	{
		return _instance;
	}
	
	private ActionRunner()
	{
		if (Config.ENABLE_OLYMPIAD)
		{
			register(new OlympiadSaveTask());
		}
		register(new DeleteExpiredVarsTask());
		register(new DeleteExpiredMailTask());
		register(new CommissionShopExpiredItemsTask());
	}
	
	public void register(AutomaticTask task)
	{
		register(task.reCalcTime(true), task);
	}
	
	public void register(long time, ActionWrapper wrapper)
	{
		if (time == 0)
		{
			info("Try register " + wrapper.getName() + " not defined time.");
			return;
		}
		if (time <= System.currentTimeMillis())
		{
			ThreadPoolManager.getInstance().execute(wrapper);
			return;
		}
		addScheduled(wrapper.getName(), wrapper, time - System.currentTimeMillis());
	}
	
	protected void addScheduled(String name, final ActionWrapper r, long diff)
	{
		_lock.lock();
		try
		{
			String lower = name.toLowerCase();
			List<ActionWrapper> wrapperList = _futures.get(lower);
			if (wrapperList == null)
			{
				_futures.put(lower, (wrapperList = new ArrayList<>()));
			}
			r.schedule(diff);
			wrapperList.add(r);
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	protected void remove(String name, ActionWrapper f)
	{
		_lock.lock();
		try
		{
			final String lower = name.toLowerCase();
			List<ActionWrapper> wrapperList = _futures.get(lower);
			if (wrapperList == null)
			{
				return;
			}
			wrapperList.remove(f);
			if (wrapperList.isEmpty())
			{
				_futures.remove(lower);
			}
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	public void clear(String name)
	{
		_lock.lock();
		try
		{
			final String lower = name.toLowerCase();
			List<ActionWrapper> wrapperList = _futures.remove(lower);
			if (wrapperList == null)
			{
				return;
			}
			for (ActionWrapper f : wrapperList)
			{
				f.cancel();
			}
			wrapperList.clear();
		}
		finally
		{
			_lock.unlock();
		}
	}
	
	public void info()
	{
		_lock.lock();
		try
		{
			for (Map.Entry<String, List<ActionWrapper>> entry : _futures.entrySet())
			{
				info("Name: " + entry.getKey() + "; size: " + entry.getValue().size());
			}
		}
		finally
		{
			_lock.unlock();
		}
	}
}
