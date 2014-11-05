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

import instances.OctavisInstance;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius + Nache
 */
public final class LydiaNpcInstance extends NpcInstance
{
	private static final int NORMAL_OCTAVIS_INST_ID = 180;
	private static final int HARD_OCTAVIS_INST_ID = 181;
	
	public LydiaNpcInstance(int objectId, NpcTemplate template)
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
			case "request_normaloctavis":
			{
				if (r != null)
				{
					if (player.canReenterInstance(NORMAL_OCTAVIS_INST_ID))
					{
						player.teleToLocation(208404, 120572, -10014, r);
					}
				}
				else if (player.canEnterInstance(NORMAL_OCTAVIS_INST_ID))
				{
					ReflectionUtils.enterReflection(player, new OctavisInstance(), NORMAL_OCTAVIS_INST_ID);
				}
				break;
			}
			case "request_hardoctavis":
			{
				if (r != null)
				{
					if (player.canReenterInstance(HARD_OCTAVIS_INST_ID))
					{
						player.teleToLocation(208404, 120572, -10014, r);
					}
				}
				else if (player.canEnterInstance(HARD_OCTAVIS_INST_ID))
				{
					ReflectionUtils.enterReflection(player, new OctavisInstance(), HARD_OCTAVIS_INST_ID);
				}
				break;
			}
			default:
				super.onBypassFeedback(player, command);
				break;
		}
	}
}
