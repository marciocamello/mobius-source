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

import instances.OctavisNormal;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class TipiaNpcInstance extends NpcInstance
{
	/**
	 * Field serialVersionUID. (value is 5984176213940365077)
	 */
	private static final long serialVersionUID = 5984176213940365077L;
	/**
	 * Field normalOctavisInstId. (value is 180)
	 */
	private static final int normalOctavisInstId = 180;
	/**
	 * Field hardOctavisInstId. (value is 181)
	 */
	private static final int hardOctavisInstId = 181;
	
	/**
	 * Constructor for TipiaNpcInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public TipiaNpcInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("request_normaloctavis"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(normalOctavisInstId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(normalOctavisInstId))
			{
				ReflectionUtils.enterReflection(player, new OctavisNormal(), normalOctavisInstId);
			}
		}
		else if (command.equalsIgnoreCase("request_hardoctavis"))
		{
			Reflection r = player.getActiveReflection();
			if (r != null)
			{
				if (player.canReenterInstance(hardOctavisInstId))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(hardOctavisInstId))
			{
				ReflectionUtils.enterReflection(player, new OctavisNormal(), hardOctavisInstId);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
