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
package handlers.bypass;

import lineage2.gameserver.handlers.BypassHandler;
import lineage2.gameserver.handlers.IBypassHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.TeleportLocation;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 */
public final class Teleport implements IBypassHandler, ScriptFile
{
	/**
	 * Method getBypasses.
	 * @return String[]
	 * @see lineage2.gameserver.handlers.IBypassHandler#getBypasses()
	 */
	@Override
	public String[] getBypasses()
	{
		return new String[]
		{
			"Teleport",
			"Tele20Lvl"
		};
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param npc NpcInstance
	 * @param player Player
	 * @param command String
	 * @see lineage2.gameserver.handlers.IBypassHandler#onBypassFeedback(NpcInstance, Player, String)
	 */
	@Override
	public void onBypassFeedback(NpcInstance npc, Player player, String command)
	{
		if ((npc.getTemplate().getTeleportList().size() > 0) && npc.checkForDominionWard(player)) // remove teleport list check?
		{
			return;
		}
		
		if (command.startsWith("Teleport"))
		{
			int cmdChoice = Integer.parseInt(command.substring(9, 10).trim());
			TeleportLocation[] list = npc.getTemplate().getTeleportList(cmdChoice);
			
			if (list != null)
			{
				npc.showTeleportList(player, list);
			}
			else
			{
				player.sendMessage("Link is faulty, contact an administrator.");
			}
		}
		else if (command.startsWith("Tele20Lvl"))
		{
			int cmdChoice = Integer.parseInt(command.substring(10, 11).trim());
			TeleportLocation[] list = npc.getTemplate().getTeleportList(cmdChoice);
			
			if (player.getLevel() > 20)
			{
				npc.showChatWindow(player, "teleporter/" + npc.getNpcId() + "-no.htm");
			}
			else if (list != null)
			{
				npc.showTeleportList(player, list);
			}
			else
			{
				player.sendMessage("Link is faulty, contact an administrator.");
			}
		}
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		BypassHandler.getInstance().registerBypass(this);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
}