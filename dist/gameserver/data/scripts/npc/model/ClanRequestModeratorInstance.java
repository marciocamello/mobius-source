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
import lineage2.gameserver.network.serverpackets.UnionPledge.ExPledgeUnionState;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Smo
 */
public class ClanRequestModeratorInstance extends NpcInstance
{
	private static final long serialVersionUID = 1L;
	
	public ClanRequestModeratorInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!(canBypassCheck(player, this)))
		{
			return;
		}
		
		if (command.equalsIgnoreCase("guild_1"))
		{
			player.sendPacket(new ExPledgeUnionState(0, 1));
		}
		else if (command.equalsIgnoreCase("guild_2"))
		{
			player.sendPacket(new ExPledgeUnionState(0, 2));
		}
		else if (command.equalsIgnoreCase("guild_3"))
		{
			player.sendPacket(new ExPledgeUnionState(0, 3));
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
