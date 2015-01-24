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
package handlers.usercommands;

import lineage2.gameserver.handlers.UserCommandHandler;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.interfaces.IUserCommandHandler;
import lineage2.gameserver.network.serverpackets.ExMultiPartyCommandChannelInfo;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CommandChannel implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS =
	{
		92,
		93,
		96,
		97
	};
	
	/**
	 * Method useUserCommand.
	 * @param id int
	 * @param activeChar Player
	 * @return boolean
	 * @see lineage2.gameserver.model.interfaces.IUserCommandHandler#useUserCommand(int, Player)
	 */
	@Override
	public boolean useUserCommand(int id, Player activeChar)
	{
		if ((id != COMMAND_IDS[0]) && (id != COMMAND_IDS[1]) && (id != COMMAND_IDS[2]) && (id != COMMAND_IDS[3]))
		{
			return false;
		}
		
		switch (id)
		{
			case 92:
				activeChar.sendMessage("Use /channelinvite <player> to create a Command Channel.");
				break;
			
			case 93:
				if (!activeChar.isInParty() || !activeChar.getParty().isInCommandChannel())
				{
					return true;
				}
				
				if (activeChar.getParty().getCommandChannel().getChannelLeader() == activeChar)
				{
					lineage2.gameserver.model.CommandChannel channel = activeChar.getParty().getCommandChannel();
					channel.disbandChannel();
				}
				else
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ONLY_THE_CREATOR_OF_A_COMMAND_CHANNEL_CAN_USE_THE_CHANNEL_DISMISS_COMMAND));
				}
				break;
			
			case 96:
				if (!activeChar.isInParty() || !activeChar.getParty().isInCommandChannel())
				{
					return true;
				}
				
				if (!activeChar.getParty().isLeader(activeChar))
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ONLY_A_PARTY_LEADER_CAN_LEAVE_A_COMMAND_CHANNEL));
					return true;
				}
				
				lineage2.gameserver.model.CommandChannel channel = activeChar.getParty().getCommandChannel();
				
				if (channel.getChannelLeader() == activeChar)
				{
					if (channel.getParties().size() > 1)
					{
						return false;
					}
					
					channel.disbandChannel();
					return true;
				}
				
				Party party = activeChar.getParty();
				channel.removeParty(party);
				party.broadCast(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_QUIT_THE_COMMAND_CHANNEL));
				channel.broadCast(SystemMessage.getSystemMessage(SystemMessageId.C1_S_PARTY_HAS_LEFT_THE_COMMAND_CHANNEL).addString(activeChar.getName()));
				break;
			
			case 97:
				if (!activeChar.isInParty() || !activeChar.getParty().isInCommandChannel())
				{
					return false;
				}
				
				activeChar.sendPacket(new ExMultiPartyCommandChannelInfo(activeChar.getParty().getCommandChannel()));
				break;
		}
		
		return true;
	}
	
	/**
	 * Method getUserCommandList.
	 * @return int[]
	 * @see lineage2.gameserver.model.interfaces.IUserCommandHandler#getUserCommandList()
	 */
	@Override
	public final int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		UserCommandHandler.getInstance().registerUserCommandHandler(this);
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
