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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExChangeClientEffectInfo;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.ArrayUtils;

public class SirraInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int[] questInstances =
	{
		140,
		138,
		141
	};
	private static final int[] warInstances =
	{
		139,
		144
	};
	
	public SirraInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		String htmlpath = null;
		if (ArrayUtils.contains(questInstances, getReflection().getInstancedZoneId()))
		{
			htmlpath = "default/32762.htm";
		}
		else if (ArrayUtils.contains(warInstances, getReflection().getInstancedZoneId()))
		{
			DoorInstance door = getReflection().getDoor(23140101);
			if (door.isOpen())
			{
				htmlpath = "default/32762_opened.htm";
			}
			else
			{
				htmlpath = "default/32762_closed.htm";
			}
		}
		else
		{
			htmlpath = "default/32762.htm";
		}
		return htmlpath;
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if (command.equalsIgnoreCase("teleport_in"))
		{
			for (NpcInstance n : getReflection().getNpcs())
			{
				if ((n.getNpcId() == 29179) || (n.getNpcId() == 29180))
				{
					player.sendPacket(new ExChangeClientEffectInfo(2));
				}
			}
			player.teleToLocation(new Location(114712, -113544, -11225));
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
