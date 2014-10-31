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
import lineage2.gameserver.instancemanager.PetitionManager;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminPetition implements IAdminCommandHandler, ScriptFile
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_view_petitions",
		"admin_view_petition",
		"admin_accept_petition",
		"admin_reject_petition",
		"admin_reset_petitions",
		"admin_force_peti"
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
		if (!activeChar.getPlayerAccess().CanEditChar)
		{
			return false;
		}
		
		int petitionId = Integer.valueOf(wordList.length > 1 ? wordList[1] : "-1");
		switch (command)
		{
			case "admin_view_petitions":
				PetitionManager.getInstance().sendPendingPetitionList(activeChar);
				break;
			
			case "admin_view_petition":
				PetitionManager.getInstance().viewPetition(activeChar, petitionId);
				break;
			
			case "admin_accept_petition":
				if (petitionId < 0)
				{
					activeChar.sendMessage("Usage: //accept_petition id");
					return false;
				}
				
				if (PetitionManager.getInstance().isPlayerInConsultation(activeChar))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.ALREADY_APPLIED_FOR_PETITION));
					return true;
				}
				
				if (PetitionManager.getInstance().isPetitionInProcess(petitionId))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.PETITION_UNDER_PROCESS));
					return true;
				}
				
				if (!PetitionManager.getInstance().acceptPetition(activeChar, petitionId))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.NOT_UNDER_PETITION_CONSULTATION));
				}
				
				break;
			
			case "admin_reject_petition":
				if (petitionId < 0)
				{
					activeChar.sendMessage("Usage: //accept_petition id");
					return false;
				}
				
				if (!PetitionManager.getInstance().rejectPetition(activeChar, petitionId))
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.FAILED_TO_CANCEL_PETITION_PLEASE_TRY_AGAIN_LATER));
				}
				
				PetitionManager.getInstance().sendPendingPetitionList(activeChar);
				break;
			
			case "admin_reset_petitions":
				if (PetitionManager.getInstance().isPetitionInProcess())
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.PETITION_UNDER_PROCESS));
					return false;
				}
				
				PetitionManager.getInstance().clearPendingPetitions();
				PetitionManager.getInstance().sendPendingPetitionList(activeChar);
				break;
			
			case "admin_force_peti":
				if (fullString.length() < 11)
				{
					activeChar.sendMessage("Usage: //force_peti text");
					return false;
				}
				
				try
				{
					GameObject targetChar = activeChar.getTarget();
					
					if ((targetChar == null) || !(targetChar instanceof Player))
					{
						activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
						return false;
					}
					
					Player targetPlayer = (Player) targetChar;
					petitionId = PetitionManager.getInstance().submitPetition(targetPlayer, fullString.substring(10), 9);
					PetitionManager.getInstance().acceptPetition(activeChar, petitionId);
				}
				catch (StringIndexOutOfBoundsException e)
				{
					activeChar.sendMessage("Usage: //force_peti text");
					return false;
				}
				
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
