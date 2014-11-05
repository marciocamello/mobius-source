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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author KilRoy & Mangol
 */
public final class TransportationAssistantInstance extends NpcInstance
{
	
	public TransportationAssistantInstance(int objectId, NpcTemplate template)
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
		
		if (player.isCursedWeaponEquipped())
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_MOVE_IN_THIS_STATE));
			return;
		}
		
		if (player.isDead())
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_MOVE_IN_THIS_STATE));
			return;
		}
		
		if (player.isCastingNow() || player.isInCombat() || player.isAttackingNow())
		{
			player.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_MOVE_IN_THIS_STATE));
			return;
		}
		
		if (command.equalsIgnoreCase("goto1ln"))
		{
			player.teleToLocation(-147711, 152768, -14056);
		}
		
		if (command.equalsIgnoreCase("goto1ls"))
		{
			player.teleToLocation(-147867, 250710, -14024);
		}
		
		if (command.equalsIgnoreCase("goto2ln"))
		{
			player.teleToLocation(-150131, 143145, -11960);
		}
		
		if (command.equalsIgnoreCase("goto2ls"))
		{
			player.teleToLocation(-150169, 241022, -11928);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}