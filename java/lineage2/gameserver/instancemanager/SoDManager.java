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
package lineage2.gameserver.instancemanager;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoDManager
{
	private static final String SPAWN_GROUP = "sod_free";
	private static final Logger _log = LoggerFactory.getLogger(SoDManager.class);
	private static SoDManager _instance;
	private static long SOD_OPEN_TIME = 12 * 60 * 60 * 1000L;
	private static Zone _zone;
	private static boolean _isOpened = false;
	
	public static SoDManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new SoDManager();
		}
		return _instance;
	}
	
	public SoDManager()
	{
		_log.info("Seed of Destruction Manager: Loaded");
		_zone = ReflectionUtils.getZone("[inner_destruction01]");
		if (!isAttackStage())
		{
			openSeed(getOpenedTimeLimit());
		}
	}
	
	public static boolean isAttackStage()
	{
		return getOpenedTimeLimit() <= 0;
	}
	
	public static void addTiatKill()
	{
		if (!isAttackStage())
		{
			return;
		}
		if (getTiatKills() < 9)
		{
			ServerVariables.set("Tial_kills", getTiatKills() + 1);
		}
		else
		{
			openSeed(SOD_OPEN_TIME);
		}
	}
	
	public static int getTiatKills()
	{
		return ServerVariables.getInt("Tial_kills", 0);
	}
	
	private static long getOpenedTimeLimit()
	{
		return (ServerVariables.getLong("SoD_opened", 0) * 1000L) - System.currentTimeMillis();
	}
	
	private static Zone getZone()
	{
		return _zone;
	}
	
	public static void teleportIntoSeed(Player p)
	{
		p.teleToLocation(new Location(-245800, 220488, -12112));
	}
	
	public static void handleDoors(boolean doOpen)
	{
		for (int i = 12240003; i <= 12240031; i++)
		{
			if (doOpen)
			{
				ReflectionUtils.getDoor(i).openMe();
			}
			else
			{
				ReflectionUtils.getDoor(i).closeMe();
			}
		}
	}
	
	public static void openSeed(long timelimit)
	{
		if (_isOpened)
		{
			return;
		}
		_isOpened = true;
		ServerVariables.unset("Tial_kills");
		ServerVariables.set("SoD_opened", (System.currentTimeMillis() + timelimit) / 1000L);
		_log.info("Seed of Destruction Manager: Opening the seed for " + Util.formatTime((int) timelimit / 1000));
		SpawnManager.getInstance().spawn(SPAWN_GROUP);
		handleDoors(true);
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				closeSeed();
			}
		}, timelimit);
	}
	
	public static void closeSeed()
	{
		if (!_isOpened)
		{
			return;
		}
		_isOpened = false;
		_log.info("Seed of Destruction Manager: Closing the seed.");
		ServerVariables.unset("SoD_opened");
		SpawnManager.getInstance().despawn(SPAWN_GROUP);
		for (Playable p : getZone().getInsidePlayables())
		{
			p.teleToLocation(getZone().getRestartPoints().get(0));
		}
		handleDoors(false);
	}
}
