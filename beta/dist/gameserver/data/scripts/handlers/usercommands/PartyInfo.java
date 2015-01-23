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

import lineage2.gameserver.handlers.IUserCommandHandler;
import lineage2.gameserver.handlers.UserCommandHandler;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PartyInfo implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS =
	{
		81
	};
	
	/**
	 * Method useUserCommand.
	 * @param id int
	 * @param activeChar Player
	 * @return boolean
	 * @see lineage2.gameserver.handlers.IUserCommandHandler#useUserCommand(int, Player)
	 */
	@Override
	public boolean useUserCommand(int id, Player activeChar)
	{
		if (id != COMMAND_IDS[0])
		{
			return false;
		}
		
		Party playerParty = activeChar.getParty();
		
		if (!activeChar.isInParty())
		{
			return false;
		}
		
		Player partyLeader = playerParty.getPartyLeader();
		
		if (partyLeader == null)
		{
			return false;
		}
		
		int memberCount = playerParty.getMemberCount();
		int lootDistribution = playerParty.getLootDistribution();
		activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PARTY_INFORMATION));
		
		switch (lootDistribution)
		{
			case Party.ITEM_LOOTER:
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.LOOTING_METHOD_FINDERS_KEEPERS));
				break;
			
			case Party.ITEM_ORDER:
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.LOOTING_METHOD_BY_TURN));
				break;
			
			case Party.ITEM_ORDER_SPOIL:
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.LOOTING_METHOD_BY_TURN_INCLUDING_SPOIL));
				break;
			
			case Party.ITEM_RANDOM:
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.LOOTING_METHOD_RANDOM));
				break;
			
			case Party.ITEM_RANDOM_SPOIL:
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.LOOTING_METHOD_RANDOM_INCLUDING_SPOIL));
				break;
		}
		
		activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PARTY_LEADER_C1).addString(partyLeader.getName()));
		activeChar.sendMessage("Members: " + memberCount + "/9.");
		activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EMPTY4));
		return true;
	}
	
	/**
	 * Method getUserCommandList.
	 * @return int[]
	 * @see lineage2.gameserver.handlers.IUserCommandHandler#getUserCommandList()
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
