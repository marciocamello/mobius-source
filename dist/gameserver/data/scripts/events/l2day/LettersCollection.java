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
package events.l2day;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import lineage2.gameserver.model.reward.RewardData;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LettersCollection extends Functions implements ScriptFile, OnDeathListener, OnPlayerEnterListener
{
	private static final Logger _log = LoggerFactory.getLogger(LettersCollection.class);
	protected static boolean _active;
	protected static String _name;
	protected static int[][] letters;
	protected static int EVENT_MANAGERS[][] = null;
	protected static String _msgStarted;
	protected static String _msgEnded;
	protected static int A = 3875;
	protected static int C = 3876;
	protected static int E = 3877;
	protected static int F = 3878;
	protected static int G = 3879;
	protected static int H = 3880;
	protected static int I = 3881;
	protected static int L = 3882;
	protected static int N = 3883;
	protected static int O = 3884;
	protected static int R = 3885;
	protected static int S = 3886;
	protected static int T = 3887;
	protected static int II = 3888;
	protected static int Y = 13417;
	protected static int _5 = 13418;
	protected static int EVENT_MANAGER_ID = 31230;
	protected static Map<String, Integer[][]> _words = new HashMap<>();
	protected static Map<String, RewardData[]> _rewards = new HashMap<>();
	protected static List<SimpleSpawner> _spawns = new ArrayList<>();
	
	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
		if (isActive())
		{
			_active = true;
			spawnEventManagers();
			_log.info("Loaded Event: " + _name + " [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: " + _name + " [state: deactivated]");
		}
	}
	
	protected static boolean isActive()
	{
		return IsActive(_name);
	}
	
	protected void spawnEventManagers()
	{
		SpawnNPCs(EVENT_MANAGER_ID, EVENT_MANAGERS, _spawns);
	}
	
	protected void unSpawnEventManagers()
	{
		deSpawnNPCs(_spawns);
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
	public void onDeath(Creature cha, Creature killer)
	{
		if (_active && SimpleCheckDrop(cha, killer))
		{
			int[] letter = letters[Rnd.get(letters.length)];
			if (Rnd.chance(letter[1] * Config.EVENT_L2DAY_LETTER_CHANCE * ((NpcTemplate) cha.getTemplate()).rateHp))
			{
				((NpcInstance) cha).dropItem(killer.getPlayer(), letter[0], 1);
			}
		}
	}
	
	public void startEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (SetActive(_name, true))
		{
			spawnEventManagers();
			System.out.println("Event '" + _name + "' started.");
			Announcements.getInstance().announceByCustomMessage(_msgStarted, null);
		}
		else
		{
			player.sendMessage("Event '" + _name + "' already started.");
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
		if (SetActive(_name, false))
		{
			unSpawnEventManagers();
			System.out.println("Event '" + _name + "' stopped.");
			Announcements.getInstance().announceByCustomMessage(_msgEnded, null);
		}
		else
		{
			player.sendMessage("Event '" + _name + "' not started.");
		}
		_active = false;
		show("admin/events.htm", player);
	}
	
	public void exchange(String[] var)
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
		Integer[][] mss = _words.get(var[0]);
		for (Integer[] l : mss)
		{
			if (getItemCount(player, l[0]) < l[1])
			{
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
				return;
			}
		}
		for (Integer[] l : mss)
		{
			removeItem(player, l[0], l[1]);
		}
		RewardData[] rewards = _rewards.get(var[0]);
		int sum = 0;
		for (RewardData r : rewards)
		{
			sum += r.getChance();
		}
		int random = Rnd.get(sum);
		sum = 0;
		for (RewardData r : rewards)
		{
			sum += r.getChance();
			if (sum > random)
			{
				addItem(player, r.getItemId(), Rnd.get(r.getMinDrop(), r.getMaxDrop()));
				return;
			}
		}
	}
	
	@Override
	public void onPlayerEnter(Player player)
	{
		if (_active)
		{
			Announcements.getInstance().announceToPlayerByCustomMessage(player, _msgStarted, null);
		}
	}
	
	public String DialogAppend_31230(Integer val)
	{
		if (!_active)
		{
			return "";
		}
		StringBuilder append = new StringBuilder("<br><br>");
		for (String word : _words.keySet())
		{
			append.append("[scripts_").append(getClass().getName()).append(":exchange ").append(word).append("|").append(word).append("]<br1>");
		}
		return append.toString();
	}
}
