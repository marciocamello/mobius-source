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
package events.March8;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class March8 extends Functions implements ScriptFile, OnDeathListener, OnPlayerEnterListener
{
	private static final Logger _log = LoggerFactory.getLogger(March8.class);
	private static final String EVENT_NAME = "March8";
	private static final int RECIPE_PRICE = 50000;
	private static final int RECIPE_ID = 20191;
	private static final int EVENT_MANAGER_ID = 4301;
	private static List<SimpleSpawner> _spawns = new ArrayList<>();
	private static final int[] DROP =
	{
		20192,
		20193,
		20194
	};
	private static boolean _active = false;
	
	private void spawnEventManagers()
	{
		final int EVENT_MANAGERS[][] =
		{
			{
				-14823,
				123567,
				-3143,
				8192
			},
			{
				-83159,
				150914,
				-3155,
				49152
			},
			{
				18600,
				145971,
				-3095,
				40960
			},
			{
				82158,
				148609,
				-3493,
				60
			},
			{
				110992,
				218753,
				-3568,
				0
			},
			{
				116339,
				75424,
				-2738,
				0
			},
			{
				81140,
				55218,
				-1551,
				32768
			},
			{
				147148,
				27401,
				-2231,
				2300
			},
			{
				43532,
				-46807,
				-823,
				31471
			},
			{
				87765,
				-141947,
				-1367,
				6500
			},
			{
				147154,
				-55527,
				-2807,
				61300
			}
		};
		SpawnNPCs(EVENT_MANAGER_ID, EVENT_MANAGERS, _spawns);
	}
	
	private void unSpawnEventManagers()
	{
		deSpawnNPCs(_spawns);
	}
	
	private static boolean isActive()
	{
		return IsActive(EVENT_NAME);
	}
	
	public void startEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (SetActive(EVENT_NAME, true))
		{
			spawnEventManagers();
			System.out.println("Event: March 8 started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.March8.AnnounceEventStarted", null);
		}
		else
		{
			player.sendMessage("Event 'March 8' already started.");
		}
		_active = true;
		show("admin/events.htm", player);
	}
	
	public void stopEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (SetActive(EVENT_NAME, false))
		{
			unSpawnEventManagers();
			System.out.println("Event: March 8 stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.March8.AnnounceEventStoped", null);
		}
		else
		{
			player.sendMessage("Event 'March 8' not started.");
		}
		_active = false;
		show("admin/events.htm", player);
	}
	
	public void buyrecipe()
	{
		Player player = getSelf();
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		if (!NpcInstance.canBypassCheck(player, player.getLastNpc()))
		{
			return;
		}
		long need_adena = (long) (RECIPE_PRICE * Config.EVENT_MARCH8_PRICE_RATE);
		if (player.getAdena() < need_adena)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		player.reduceAdena(need_adena, true);
		Functions.addItem(player, RECIPE_ID, 1);
	}
	
	public String DialogAppend_4301(Integer val)
	{
		if (val != 0)
		{
			return "";
		}
		String price;
		String append = "";
		price = Util.formatAdena((long) (RECIPE_PRICE * Config.EVENT_MARCH8_PRICE_RATE));
		append += "<br><a action=\"bypass -h scripts_events.March8.March8:buyrecipe\">";
		append += new CustomMessage("scripts.events.March8.buyrecipe", getSelf()).addString(price);
		append += "</a><br>";
		return append;
	}
	
	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
		if (isActive())
		{
			_active = true;
			spawnEventManagers();
			_log.info("Loaded Event: March 8 [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: March 8 [state: deactivated]");
		}
	}
	
	@Override
	public void onReload()
	{
		unSpawnEventManagers();
	}
	
	@Override
	public void onShutdown()
	{
		unSpawnEventManagers();
	}
	
	@Override
	public void onPlayerEnter(Player player)
	{
		if (_active)
		{
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.March8.AnnounceEventStarted", null);
		}
	}
	
	@Override
	public void onDeath(Creature cha, Creature killer)
	{
		if (_active && SimpleCheckDrop(cha, killer) && Rnd.chance(Config.EVENT_MARCH8_DROP_CHANCE * killer.getPlayer().getRateItems() * ((NpcInstance) cha).getTemplate().rateHp))
		{
			((NpcInstance) cha).dropItem(killer.getPlayer(), DROP[Rnd.get(DROP.length)], 1);
		}
	}
}
