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
import lineage2.gameserver.model.base.Experience;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.PetDataTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminLevel implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_add_level",
		"admin_addLevel",
		"admin_set_level",
		"admin_setLevel"
	};
	
	/**
	 * Method setLevel.
	 * @param activeChar Player
	 * @param target GameObject
	 * @param level int
	 */
	private void setLevel(Player activeChar, GameObject target, int level)
	{
		if ((target == null) || !(target.isPlayer() || target.isPet()))
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.INVALID_TARGET));
			return;
		}
		
		if ((level < 1) || (level > Experience.getMaxLevel()))
		{
			activeChar.sendMessage("You must specify level 1 - " + Experience.getMaxLevel());
			return;
		}
		
		if (target.isPlayer())
		{
			Long exp_add = Experience.LEVEL[level] - ((Player) target).getExp();
			((Player) target).addExpAndSp(exp_add, 0, true);
			return;
		}
		
		if (target.isPet())
		{
			Long exp_add = PetDataTable.getInstance().getInfo(((PetInstance) target).getId(), level).getExp() - ((PetInstance) target).getExp();
			((PetInstance) target).addExpAndSp(exp_add, 0);
		}
	}
	
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
		if (!activeChar.getPlayerAccess().CanEditChar)
		{
			return false;
		}
		
		GameObject target = activeChar.getTarget();
		
		if ((target == null) || !(target.isPlayer() || target.isPet()))
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.INVALID_TARGET));
			return false;
		}
		
		int level;
		
		switch (command)
		{
			case "admin_add_level":
			case "admin_addLevel":
				if (wordList.length < 2)
				{
					activeChar.sendMessage("USAGE: //addLevel level");
					return false;
				}
				
				try
				{
					level = Integer.parseInt(wordList[1]);
				}
				catch (NumberFormatException e)
				{
					activeChar.sendMessage("You must specify level");
					return false;
				}
				
				setLevel(activeChar, target, level + ((Creature) target).getLevel());
				break;
			
			case "admin_set_level":
			case "admin_setLevel":
				if (wordList.length < 2)
				{
					activeChar.sendMessage("USAGE: //setlevel level");
					return false;
				}
				
				try
				{
					level = Integer.parseInt(wordList[1]);
				}
				catch (NumberFormatException e)
				{
					activeChar.sendMessage("You must specify level");
					return false;
				}
				
				setLevel(activeChar, target, level);
				break;
		}
		
		return true;
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
