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

import instances.KartiaLabyrinth85Solo;
import instances.KartiaLabyrinth90Solo;
import instances.KartiaLabyrinth95Solo;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

@SuppressWarnings("serial")
public final class KartiaManagerInstance extends NpcInstance
{
	private static final Location TELEPORT_POSITION = new Location(-109032, -10440, -11949);
	
	public KartiaManagerInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("request_zellaka_solo"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(205))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(205))
			{
				ReflectionUtils.enterReflection(player, new KartiaLabyrinth85Solo(), 205);
			}
		}
		if (command.equalsIgnoreCase("request_pelline_solo"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(206))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(206))
			{
				ReflectionUtils.enterReflection(player, new KartiaLabyrinth90Solo(), 206);
			}
		}
		if (command.equalsIgnoreCase("request_kalios_solo"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(207))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(207))
			{
				ReflectionUtils.enterReflection(player, new KartiaLabyrinth95Solo(), 207);
			}
		}
		if (command.startsWith("start_zellaka_solo"))
		{
			player.teleToLocation(TELEPORT_POSITION);
		}
		super.onBypassFeedback(player, command);
	}
}