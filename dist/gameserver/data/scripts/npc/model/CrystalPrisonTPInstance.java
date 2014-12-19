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

import instances.Balok;
import instances.Baylor;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Awakeninger + Nache
 */
public final class CrystalPrisonTPInstance extends NpcInstance
{
	private static final int BAYLOR_INSTANCE = 166;
	private static final int BALOK_INSTANCE = 167;
	
	public CrystalPrisonTPInstance(int objectId, NpcTemplate template)
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
			case "request_Balok":
				if (r != null)
				{
					if (player.canReenterInstance(BALOK_INSTANCE))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(BALOK_INSTANCE))
				{
					ReflectionUtils.enterReflection(player, new Balok(), BALOK_INSTANCE);
				}
				break;
			
			case "request_Baylor":
				if (r != null)
				{
					if (player.canReenterInstance(BAYLOR_INSTANCE))
					{
						player.teleToLocation(r.getTeleportLoc(), r);
					}
				}
				else if (player.canEnterInstance(BAYLOR_INSTANCE))
				{
					ReflectionUtils.enterReflection(player, new Baylor(), BAYLOR_INSTANCE);
				}
				break;
			
			default:
				super.onBypassFeedback(player, command);
				break;
		}
	}
}