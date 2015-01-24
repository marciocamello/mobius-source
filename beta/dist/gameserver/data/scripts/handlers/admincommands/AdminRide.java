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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.interfaces.IAdminCommandHandler;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.PetDataTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminRide implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_ride",
		"admin_ride_wyvern",
		"admin_ride_strider",
		"admin_unride",
		"admin_wr",
		"admin_sr",
		"admin_ur"
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
		if (!activeChar.getPlayerAccess().Rider)
		{
			return false;
		}
		
		switch (command)
		{
			case "admin_ride":
				if (activeChar.isMounted() || (activeChar.getSummonList().getPet() != null))
				{
					activeChar.sendMessage("Already Have a Pet or Mounted.");
					return false;
				}
				
				if (wordList.length != 2)
				{
					activeChar.sendMessage("Incorrect id.");
					return false;
				}
				
				activeChar.setMount(Integer.parseInt(wordList[1]), 0, 85);
				break;
			
			case "admin_ride_wyvern":
			case "admin_wr":
				if (activeChar.isMounted() || (activeChar.getSummonList().getPet() != null))
				{
					activeChar.sendMessage("Already Have a Pet or Mounted.");
					return false;
				}
				
				activeChar.setMount(PetDataTable.WYVERN_ID, 0, 85);
				break;
			
			case "admin_ride_strider":
			case "admin_sr":
				if (activeChar.isMounted() || (activeChar.getSummonList().getPet() != null))
				{
					activeChar.sendMessage("Already Have a Pet or Mounted.");
					return false;
				}
				
				activeChar.setMount(PetDataTable.STRIDER_WIND_ID, 0, 85);
				break;
			
			case "admin_unride":
			case "admin_ur":
				activeChar.setMount(0, 0, 0);
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
