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
package handlers.admincommands;

import lineage2.gameserver.handlers.AdminCommandHandler;
import lineage2.gameserver.handlers.IAdminCommandHandler;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminKill implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_kill",
		"admin_damage"
	};
	
	/**
	 * Method useAdminCommand.
	 * @param command String
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean
	 * @see lineage2.gameserver.handlers.IAdminCommandHandler#useAdminCommand(String, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(String command, String[] wordList, String fullString, Player activeChar)
	{
		if (!activeChar.getPlayerAccess().CanEditNPC)
		{
			return false;
		}
		
		switch (command)
		{
			case "admin_kill":
				if (wordList.length == 1)
				{
					handleKill(activeChar);
				}
				else
				{
					handleKill(activeChar, wordList[1]);
				}
				
				break;
			
			case "admin_damage":
				handleDamage(activeChar, Integer.valueOf(wordList[1]));
				break;
		}
		
		return true;
	}
	
	/**
	 * Method handleKill.
	 * @param activeChar Player
	 */
	private void handleKill(Player activeChar)
	{
		handleKill(activeChar, null);
	}
	
	/**
	 * Method handleKill.
	 * @param activeChar Player
	 * @param player String
	 */
	private void handleKill(Player activeChar, String player)
	{
		GameObject obj = activeChar.getTarget();
		
		if (player != null)
		{
			Player plyr = World.getPlayer(player);
			
			if (plyr != null)
			{
				obj = plyr;
			}
			else
			{
				int radius = Math.max(Integer.parseInt(player), 100);
				
				for (Creature character : activeChar.getAroundCharacters(radius, 200))
				{
					if (!character.isDoor())
					{
						character.doDie(activeChar);
					}
				}
				
				activeChar.sendMessage("Killed within " + radius + " unit radius.");
				return;
			}
		}
		
		if ((obj != null) && obj.isCreature())
		{
			Creature target = (Creature) obj;
			target.doDie(activeChar);
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
		}
	}
	
	/**
	 * Method handleDamage.
	 * @param activeChar Player
	 * @param damage int
	 */
	private void handleDamage(Player activeChar, int damage)
	{
		GameObject obj = activeChar.getTarget();
		
		if (obj == null)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.SELECT_TARGET));
			return;
		}
		
		if (!obj.isCreature())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return;
		}
		
		Creature cha = (Creature) obj;
		cha.reduceCurrentHp(damage, 0, activeChar, null, true, true, false, false, false, false, true);
		activeChar.sendMessage("You gave " + damage + " damage to " + cha.getName() + ".");
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return String[]
	 * @see lineage2.gameserver.handlers.IAdminCommandHandler#getAdminCommandList()
	 */
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
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
