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

import instances.Teredor;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class TeredorGatekeeperInstance extends NpcInstance
{
	/**
	 * Field serialVersionUID. (value is -7905905093708290805)
	 */
	private static final long serialVersionUID = -7905905093708290805L;
	/**
	 * Field teredorInstanceId. (value is 160)
	 */
	private static final int teredorInstanceId = 160;
	
	/**
	 * Constructor for TeredorGatekeeperInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public TeredorGatekeeperInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("teredor_enter"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(teredorInstanceId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(teredorInstanceId))
			{
				ReflectionUtils.enterReflection(player, new Teredor(), teredorInstanceId);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
