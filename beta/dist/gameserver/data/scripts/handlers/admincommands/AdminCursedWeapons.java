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
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.model.CursedWeapon;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.interfaces.IAdminCommandHandler;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminCursedWeapons implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_cw_info",
		"admin_cw_remove",
		"admin_cw_goto",
		"admin_cw_reload",
		"admin_cw_add",
		"admin_cw_drop"
	};
	
	/**
	 * Method useAdminCommand.
	 * @param command String
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean
	 * @see lineage2.gameserver.model.interfaces.IAdminCommandHandler#useAdminCommand(String, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(String command, String[] wordList, String fullString, Player activeChar)
	{
		if (!activeChar.getPlayerAccess().Menu)
		{
			return false;
		}
		
		CursedWeaponsManager cwm = CursedWeaponsManager.getInstance();
		CursedWeapon cw = null;
		
		switch (command)
		{
			case "admin_cw_remove":
			case "admin_cw_goto":
			case "admin_cw_add":
			case "admin_cw_drop":
				if (wordList.length < 2)
				{
					activeChar.sendMessage("You have not specified id.");
					return false;
				}
				
				for (CursedWeapon cwp : CursedWeaponsManager.getInstance().getCursedWeapons())
				{
					if (cwp.getName().toLowerCase().contains(wordList[1].toLowerCase()))
					{
						cw = cwp;
					}
				}
				
				if (cw == null)
				{
					activeChar.sendMessage("Unknown id.");
					return false;
				}
				break;
			
			default:
				break;
		}
		
		switch (command)
		{
			case "admin_cw_info":
				activeChar.sendMessage("======= Cursed Weapons: =======");
				
				for (CursedWeapon c : cwm.getCursedWeapons())
				{
					activeChar.sendMessage("> " + c.getName() + " (" + c.getItemId() + ")");
					
					if (c.isActivated())
					{
						Player pl = c.getPlayer();
						activeChar.sendMessage("  Player holding: " + pl.getName());
						activeChar.sendMessage("  Player karma: " + c.getPlayerKarma());
						activeChar.sendMessage("  Time Remaining: " + (c.getTimeLeft() / 60000) + " min.");
						activeChar.sendMessage("  Kills : " + c.getNbKills());
					}
					else if (c.isDropped())
					{
						activeChar.sendMessage("  Lying on the ground.");
						activeChar.sendMessage("  Time Remaining: " + (c.getTimeLeft() / 60000) + " min.");
						activeChar.sendMessage("  Kills : " + c.getNbKills());
					}
					else
					{
						activeChar.sendMessage("  Don't exist in the world.");
					}
				}
				break;
			
			case "admin_cw_reload":
				activeChar.sendMessage("Cursed weapons can't be reloaded.");
				break;
			
			case "admin_cw_remove":
				if (cw == null)
				{
					return false;
				}
				
				CursedWeaponsManager.getInstance().endOfLife(cw);
				break;
			
			case "admin_cw_goto":
				if (cw == null)
				{
					return false;
				}
				
				activeChar.teleToLocation(cw.getLoc());
				break;
			
			case "admin_cw_add":
				if (cw == null)
				{
					return false;
				}
				
				if (cw.isActive())
				{
					activeChar.sendMessage("This cursed weapon is already active.");
				}
				else
				{
					GameObject target = activeChar.getTarget();
					
					if ((target != null) && target.isPlayer() && !((Player) target).isInOlympiadMode())
					{
						Player player = (Player) target;
						ItemInstance item = ItemFunctions.createItem(cw.getItemId());
						cwm.activate(player, player.getInventory().addItem(item));
						cwm.showUsageTime(player, cw);
					}
				}
				break;
			
			case "admin_cw_drop":
				if (cw == null)
				{
					return false;
				}
				
				if (cw.isActive())
				{
					activeChar.sendMessage("This cursed weapon is already active.");
				}
				else
				{
					GameObject target = activeChar.getTarget();
					
					if ((target != null) && target.isPlayer() && !((Player) target).isInOlympiadMode())
					{
						Player player = (Player) target;
						cw.create(null, player);
					}
				}
				break;
		}
		
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return String[]
	 * @see lineage2.gameserver.model.interfaces.IAdminCommandHandler#getAdminCommandList()
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
