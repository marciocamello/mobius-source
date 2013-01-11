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
package events.AprilFoolsDay;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExBR_BroadcastEventState;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AprilFoolsDay extends Functions implements ScriptFile, OnDeathListener, OnPlayerEnterListener
{
	private static final Logger _log = LoggerFactory.getLogger(AprilFoolsDay.class);
	private static final int[] HERBS = new int[]
	{
		20923,
		20924,
		20925
	};
	private static boolean _active = false;
	
	private static boolean isActive()
	{
		return IsActive("AprilFoolsDay");
	}
	
	public void startEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (SetActive("AprilFoolsDay", true))
		{
			System.out.println("Event: 'April Fools Day' started.");
			ExBR_BroadcastEventState es = new ExBR_BroadcastEventState(ExBR_BroadcastEventState.APRIL_FOOLS, 1);
			for (Player p : GameObjectsStorage.getAllPlayersForIterate())
			{
				p.sendPacket(es);
			}
		}
		else
		{
			player.sendMessage("Event 'April Fools Day' already started.");
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
		if (SetActive("AprilFoolsDay", false))
		{
			System.out.println("Event: 'April Fools Day' stopped.");
		}
		else
		{
			player.sendMessage("Event: 'April Fools Day' not started.");
		}
		_active = false;
		show("admin/events.htm", player);
	}
	
	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
		if (isActive())
		{
			_active = true;
			_log.info("Loaded Event: Apil Fool's Day [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: Apil Fool's Day [state: deactivated]");
		}
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	@Override
	public void onPlayerEnter(Player player)
	{
		if (_active)
		{
			player.sendPacket(new ExBR_BroadcastEventState(ExBR_BroadcastEventState.APRIL_FOOLS_10, 1));
		}
	}
	
	@Override
	public void onDeath(Creature cha, Creature killer)
	{
		if (_active && SimpleCheckDrop(cha, killer) && Rnd.chance(Config.EVENT_APIL_FOOLS_DROP_CHANCE / 10.0D))
		{
			((NpcInstance) cha).dropItem(killer.getPlayer(), HERBS[Rnd.get(HERBS.length)], 1);
		}
	}
}
