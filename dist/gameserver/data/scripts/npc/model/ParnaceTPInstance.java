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

import instances.Baylor;
import instances.CrystalHall;
import instances.Vullock;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Awakeninger
 */
public final class ParnaceTPInstance extends NpcInstance
{
	private static final long serialVersionUID = 1L;
	private static final int CrystalHallInstance = 163;
	private static final int VullockInstance = 167;
	private static final int BaylorInstance = 168;
	
	public ParnaceTPInstance(int objectId, NpcTemplate template)
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
		
		if (command.startsWith("request_ch"))
		{
			Reflection r = player.getActiveReflection();
			
			if (r != null)
			{
				if (player.canReenterInstance(CrystalHallInstance))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(CrystalHallInstance))
			{
				ReflectionUtils.enterReflection(player, new CrystalHall(), CrystalHallInstance);
			}
		}
		else if (command.startsWith("request_vallock"))
		{
			Reflection r = player.getActiveReflection();
			
			if (r != null)
			{
				if (player.canReenterInstance(VullockInstance))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(VullockInstance))
			{
				ReflectionUtils.enterReflection(player, new Vullock(), VullockInstance);
			}
		}
		else if (command.startsWith("request_Baylor"))
		{
			Reflection r = player.getActiveReflection();
			
			if (r != null)
			{
				if (player.canReenterInstance(BaylorInstance))
				{
					player.teleToLocation(r.getTeleportLoc(), r);
				}
			}
			else if (player.canEnterInstance(BaylorInstance))
			{
				ReflectionUtils.enterReflection(player, new Baylor(), BaylorInstance);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}