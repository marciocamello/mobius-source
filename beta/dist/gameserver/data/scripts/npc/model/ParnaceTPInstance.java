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
package npc.model;

import instances.CrystalHall;
import instances.SteamCorridor;

import java.util.Calendar;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Awakeninger + Nache
 */
public final class ParnaceTPInstance extends NpcInstance
{
	private static final int CRYSTAL_HALL_INSTANCE = 163;
	private static final int STEAM_CORRIDOR_INSTANCE = 164;
	
	public ParnaceTPInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		
		Reflection r = player.getActiveReflection();
		
		switch (command)
		{
			case "request_CrystalHall":
				if (r != null)
				{
					if (player.canReenterInstance(CRYSTAL_HALL_INSTANCE))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(CRYSTAL_HALL_INSTANCE))
				{
					ReflectionUtils.enterReflection(player, new CrystalHall(), CRYSTAL_HALL_INSTANCE);
				}
				break;
			
			case "request_SteamCorridor":
				if (r != null)
				{
					if (player.canReenterInstance(STEAM_CORRIDOR_INSTANCE))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(STEAM_CORRIDOR_INSTANCE))
				{
					ReflectionUtils.enterReflection(player, new SteamCorridor(), STEAM_CORRIDOR_INSTANCE);
				}
				break;
			
			case "request_CoralGarden":
				// FIXME Not Done
				break;
			
			default:
				super.onBypassFeedback(player, command);
				break;
		}
	}
	
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		NpcHtmlMessage msg = new NpcHtmlMessage(player, this);
		
		switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
		{
			case 0:
			case 2:
			case 4:
				msg.setFile("default/33522.htm");
				msg.replace("%instance%", "Steam Corridor");
				msg.replace("%enter%", "request_SteamCorridor");
				break;
			
			case 1:
			case 3:
			case 5:
				msg.setFile("default/33522.htm");
				msg.replace("%instance%", "Emerald Square");
				msg.replace("%enter%", "request_CrystalHall");
				break;
			
			case 6:
				msg.setFile("default/33522.htm");
				msg.replace("%instance%", "Coral Garden");
				msg.replace("%enter%", "request_CoralGarden");
				break;
		}
		
		player.sendPacket(msg);
	}
}