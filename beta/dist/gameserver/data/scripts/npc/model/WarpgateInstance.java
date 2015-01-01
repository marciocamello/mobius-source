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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class WarpgateInstance extends NpcInstance
{
	
	/**
	 * Constructor for WarpgateInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public WarpgateInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		
		if (command.startsWith("enter_hellbound"))
		{
			if ((player.getLevel() >= Config.HELLBOUND_MIN_PLAYER_LEVEL) || (Functions.getItemCount(player, 45448) != 0))
			{
				player.teleToLocation(-28632, 255640, -2230);
				return;
			}
			
			showChatWindow(player, "default/33900-1.htm");
			return;
		}
		
		super.onBypassFeedback(player, command);
	}
}
