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

import instances.IstinaInstance;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 */
public final class RumieseNpcInstance extends NpcInstance
{
	private static final int NORMAL_MODE_ISTINA_INS_ID = 169;
	private static final int HARD_MODE_ISTINA_INS_ID = 170;
	
	/**
	 * Constructor for RumieseNpcInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public RumieseNpcInstance(int objectId, NpcTemplate template)
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
		
		Reflection r = player.getActiveReflection();
		
		switch (command)
		{
			case "request_normalisthina":
				if (r != null)
				{
					if (player.canReenterInstance(NORMAL_MODE_ISTINA_INS_ID))
					{
						player.teleToLocation(-177104, 146452, -11389, r);
					}
				}
				else if (player.canEnterInstance(NORMAL_MODE_ISTINA_INS_ID))
				{
					ReflectionUtils.enterReflection(player, new IstinaInstance(), NORMAL_MODE_ISTINA_INS_ID);
				}
				break;
			
			case "request_hardisthina":
				if (r != null)
				{
					if (player.canReenterInstance(HARD_MODE_ISTINA_INS_ID))
					{
						player.teleToLocation(-177104, 146452, -11389, r);
					}
				}
				else if (player.canEnterInstance(HARD_MODE_ISTINA_INS_ID))
				{
					ReflectionUtils.enterReflection(player, new IstinaInstance(), HARD_MODE_ISTINA_INS_ID);
				}
				break;
			
			case "request_Device":
				if (ItemFunctions.getItemCount(player, 17608) > 0)
				{
					showChatWindow(player, "default/" + getId() + "-no.htm");
					return;
				}
				Functions.addItem(player, 17608, 1);
				showChatWindow(player, "default/" + getId() + "-ok.htm");
				break;
			
			default:
				super.onBypassFeedback(player, command);
				break;
		}
	}
}
