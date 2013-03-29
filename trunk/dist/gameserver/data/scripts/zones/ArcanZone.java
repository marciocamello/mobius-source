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
package zones;

import lineage2.gameserver.instancemanager.ArcanManager;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ReflectionUtils;

public class ArcanZone implements ScriptFile
{
	private static final String TELEPORT_ZONE_NAME = "[Arcan_0]";
	private static ZoneListener _zoneListener;
	static int _BLUE = 262001;
	static int _RED = 262003;
	
	private void init()
	{
		_zoneListener = new ZoneListener();
		Zone zone = ReflectionUtils.getZone(TELEPORT_ZONE_NAME);
		zone.addListener(_zoneListener);
	}
	
	@Override
	public void onLoad()
	{
		init();
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (zone == null)
			{
				return;
			}
			
			if (cha == null)
			{
				return;
			}
			
			if (cha instanceof Player)
			{
				Player player = cha.getPlayer();
				if (ArcanManager.getStage() == _RED)
				{
					player.sendPacket(new EventTrigger(_BLUE, false));
					player.sendPacket(new EventTrigger(_RED, true));
				}
				else
				{
					player.sendPacket(new EventTrigger(_RED, false));
					player.sendPacket(new EventTrigger(_BLUE, true));
				}
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
}