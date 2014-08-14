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
package events.l2day;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.reward.RewardData;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LettersCollection extends Functions implements ScriptFile, OnDeathListener, OnPlayerEnterListener
{
	private static final Logger _log = LoggerFactory.getLogger(LettersCollection.class);
	private static boolean _active;
	protected static String _name;
	protected static int[][] letters;
	protected static int[][] EVENT_MANAGERS = null;
	protected static String _msgStarted;
	protected static String _msgEnded;
	protected static final int A = 3875;
	protected static final int C = 3876;
	protected static final int E = 3877;
	protected static final int F = 3878;
	protected static final int G = 3879;
	protected static final int H = 3880;
	protected static final int I = 3881;
	protected static final int L = 3882;
	protected static final int N = 3883;
	protected static final int O = 3884;
	protected static final int R = 3885;
	protected static final int S = 3886;
	protected static final int T = 3887;
	protected static final int II = 3888;
	protected static final int Y = 13417;
	protected static final int _5 = 13418;
	private static final int EVENT_MANAGER_ID = 31230;
	protected static final Map<String, Integer[][]> _words = new HashMap<>();
	protected static final Map<String, RewardData[]> _rewards = new HashMap<>();
	private static final List<SimpleSpawner> _spawns = new ArrayList<>();
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
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
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	private static boolean isActive()
	{
		return IsActive(_name);
	}
	
	/**
	 * Method spawnEventManagers.
	 */
	private void spawnEventManagers()
	{
		SpawnNPCs(EVENT_MANAGER_ID, EVENT_MANAGERS, _spawns);
	}
	
	/**
	 * Method unSpawnEventManagers.
	 */
	private void unSpawnEventManagers()
	{
		deSpawnNPCs(_spawns);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		unSpawnEventManagers();
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
		unSpawnEventManagers();
	}
	
	/**
	 * Method onDeath.
	 * @param cha Creature
	 * @param killer Creature
	 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
	 */
	@Override
	public void onDeath(Creature cha, Creature killer)
	{
		if (_active && SimpleCheckDrop(cha, killer))
		{
			final int[] letter = letters[Rnd.get(letters.length)];
			
			if (Rnd.chance(letter[1] * Config.EVENT_L2DAY_LETTER_CHANCE * ((NpcTemplate) cha.getTemplate()).rateHp))
			{
				((NpcInstance) cha).dropItem(killer.getPlayer(), letter[0], 1);
			}
		}
	}
	
	/**
	 * Method startEvent.
	 */
	public void startEvent()
	{
		final Player player = getSelf();
		
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
	
	/**
	 * Method stopEvent.
	 */
	public void stopEvent()
	{
		final Player player = getSelf();
		
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
	
	/**
	 * Method exchange.
	 * @param var String[]
	 */
	public void exchange(String[] var)
	{
		final Player player = getSelf();
		
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		
		if (!NpcInstance.canBypassCheck(player, player.getLastNpc()))
		{
			return;
		}
		
		final Integer[][] mss = _words.get(var[0]);
		
		for (Integer[] l : mss)
		{
			if (getItemCount(player, l[0]) < l[1])
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS));
				return;
			}
		}
		
		for (Integer[] l : mss)
		{
			removeItem(player, l[0], l[1]);
		}
		
		final RewardData[] rewards = _rewards.get(var[0]);
		int sum = 0;
		
		for (RewardData r : rewards)
		{
			sum += r.getChance();
		}
		
		final int random = Rnd.get(sum);
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
	
	/**
	 * Method onPlayerEnter.
	 * @param player Player
	 * @see lineage2.gameserver.listener.actor.player.OnPlayerEnterListener#onPlayerEnter(Player)
	 */
	@Override
	public void onPlayerEnter(Player player)
	{
		if (_active)
		{
			Announcements.getInstance().announceToPlayerByCustomMessage(player, _msgStarted, null);
		}
	}
	
	/**
	 * Method DialogAppend_31230.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31230(Integer val)
	{
		if (!_active)
		{
			return "";
		}
		
		final StringBuilder append = new StringBuilder("<br><br>");
		
		for (String word : _words.keySet())
		{
			append.append("[scripts_").append(getClass().getName()).append(":exchange ").append(word).append('|').append(word).append("]<br1>");
		}
		
		return append.toString();
	}
}
