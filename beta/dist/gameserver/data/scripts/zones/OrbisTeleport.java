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

import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

public final class OrbisTeleport implements ScriptFile
{
	private static ZoneListener _zoneListener;
	private static final String[] zones =
	{
		"[Hall_of_Orbis_0_1_level]",
		"[Hall_of_Orbis_1_0_level]",
		"[Hall_of_Orbis_1_2_level]",
		"[Hall_of_Orbis_2_1_level]",
		"[Hall_of_Orbis_2_3_level]",
		"[Hall_of_Orbis_3_2_level]"
	};
	
	private void init()
	{
		_zoneListener = new ZoneListener();
		
		for (String s : zones)
		{
			Zone zone = ReflectionUtils.getZone(s);
			zone.addListener(_zoneListener);
		}
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
	
	public final class ZoneListener implements OnZoneEnterLeaveListener
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
			
			if (!cha.isPlayer())
			{
				return;
			}
			
			cha.teleToLocation(Location.parseLoc(zone.getParams().getString("tele")));
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
}
