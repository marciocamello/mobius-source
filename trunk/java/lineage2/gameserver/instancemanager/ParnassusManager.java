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

import java.util.Calendar;

import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @Author Awakeninger
 * @Date: 26.04.2013
 **/
public class ParnassusManager
{
	private static final String TELEPORT_ZONE_NAME = "[parnassus]";
	private static ParnassusManager _instance;
	private static ZoneListener _zoneListener;
	
	public ParnassusManager()
	{
		_zoneListener = new ZoneListener();
		Zone zone = ReflectionUtils.getZone(TELEPORT_ZONE_NAME);
		zone.addListener(_zoneListener);
	}
	
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if ((zone == null) || (cha == null) || !cha.isPlayer())
			{
				return;
			}
			
			broadcastPacket(24230010, true);
			broadcastPacket(24230012, false);
			
			switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
			{
				case 0:
				case 2:
				case 4:
					broadcastPacket(24230014, true);
					broadcastPacket(24230016, false);
					broadcastPacket(24230018, false);
					break;
				
				case 1:
				case 3:
				case 5:
					broadcastPacket(24230014, false);
					broadcastPacket(24230016, true);
					broadcastPacket(24230018, false);
					break;
				
				default:
					broadcastPacket(24230014, false);
					broadcastPacket(24230016, false);
					broadcastPacket(24230018, true);
					break;
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
	
	public void broadcastPacket(int value, boolean b)
	{
		final Zone zone = ReflectionUtils.getZone(TELEPORT_ZONE_NAME);
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if (zone.getInsidePlayers().contains(player))
			{
				player.sendPacket(new EventTrigger(value, b));
			}
		}
	}
	
	public static ParnassusManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new ParnassusManager();
		}
		return _instance;
	}
}