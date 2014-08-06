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

import instances.SpezionNormal;
import java.util.HashMap;
import java.util.Map;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author cruel
 */
public final class PortalCubeInstance extends NpcInstance
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	final Map<Integer, Integer> players = new HashMap<>();
	
	public PortalCubeInstance(int objectId, NpcTemplate template)
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
		
		if (command.equalsIgnoreCase("register"))
		{
			players.put(player.getObjectId(), player.getObjectId());
		}
		else if (command.equalsIgnoreCase("exit"))
		{
			for (Player p : ((SpezionNormal) getReflection()).getPlayers())
			{
				if (players.get(p.getObjectId()) == null)
				{
					return;
				}
				
				players.clear();
				((SpezionNormal) getReflection()).SecondRoom();
			}
		}
		else if (command.equalsIgnoreCase("opengate"))
		{
			if (getNpcId() == 32951)
			{
				((SpezionNormal) getReflection()).openGate(26190001);
			}
			else if (getNpcId() == 32952)
			{
				((SpezionNormal) getReflection()).openGate(26190006);
			}
			else if (getNpcId() == 32953)
			{
				((SpezionNormal) getReflection()).openGate(26190005);
			}
		}
		else if (command.equalsIgnoreCase("stage_third"))
		{
			((SpezionNormal) getReflection()).thirdStage();
		}
		else if (command.equalsIgnoreCase("spawn_spezion"))
		{
			((SpezionNormal) getReflection()).spazionSpawn();
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}