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
package instances;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExSendUIEvent;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Location;

/**
 * @author Awakeninger + Nache
 */
public class SteamCorridor extends Reflection
{
	private long _savedTime;
	private final DeathListener _deathListener = new DeathListener();
	private final ZoneListener _epicZoneListener = new ZoneListener();
	private final int Kechi = 25797;
	public static final String[] zones =
	{
		"[Steam1to2]",
		"[Steam2to3]",
		"[Steam3to4]",
		"[Steam4to5]",
		"[Steam5toBoss]"
	};
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
	}
	
	@Override
	protected void onCollapse()
	{
		super.onCollapse();
	}
	
	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
		_savedTime = System.currentTimeMillis();
		player.sendPacket(new ExSendUIEvent(player, 0, 1, (int) (System.currentTimeMillis() - _savedTime) / 1000, 0, NpcString.ELAPSED_TIME));
		NpcInstance Boss = addSpawnWithoutRespawn(Kechi, new Location(154088, 215128, -12152, 31900), 0);
		player.setVar("SteamStart", "true", -1);
		Boss.addListener(_deathListener);
		getZone("[Steam1to2]").addListener(_epicZoneListener);
	}
	
	@Override
	public void onPlayerExit(Player player)
	{
		player.sendPacket(new ExSendUIEvent(player, 1, 1, 0, 0));
	}
	
	private class DeathListener implements OnDeathListener
	{
		public DeathListener()
		{
		}
		
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (self.isNpc() && (self.getId() == Kechi))
			{
				for (Player p : getPlayers())
				{
					p.sendPacket(new ExSendUIEvent(p, 1, 1, 0, 0));
					p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5));
					startCollapseTimer(5 * 60 * 1000L);
					return;
				}
			}
		}
	}
	
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			Player player = cha.getPlayer();
			if ((player == null) || !cha.isPlayer())
			{
				return;
			}
			
			if (zone.getName().equalsIgnoreCase("[Steam1to2]"))
			{
				ThreadPoolManager.getInstance().schedule(new First(), 15000);
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
	
	private class First extends RunnableImpl
	{
		public First()
		{
		}
		
		@Override
		public void runImpl()
		{
			for (Player p : getPlayers())
			{
				Reflection ref = p.getActiveReflection();
				p.teleToLocation(147528, 218200, -12162, ref);
				p.sendPacket(new ExShowScreenMessage("Opened a portal leading in the following room.", 6000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));
			}
		}
	}
}