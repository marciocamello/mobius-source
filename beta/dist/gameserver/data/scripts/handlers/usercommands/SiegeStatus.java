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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SiegeStatus implements IUserCommandHandler, ScriptFile
{
	public static final int[] COMMANDS =
	{
		99
	};
	
	/**
	 * Method useUserCommand.
	 * @param id int
	 * @param player Player
	 * @return boolean
	 * @see lineage2.gameserver.handlers.IUserCommandHandler#useUserCommand(int, Player)
	 */
	@Override
	public boolean useUserCommand(int id, Player player)
	{
		if (!player.isClanLeader())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ONLY_THE_CLAN_LEADER_MAY_ISSUE_COMMANDS));
			return false;
		}
		
		Castle castle = player.getCastle();
		
		if (castle == null)
		{
			return false;
		}
		
		if (castle.getSiegeEvent().isInProgress())
		{
			if (!player.isNoble())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ONLY_A_CLAN_LEADER_THAT_IS_A_NOBLESSE_EXALTED_CAN_VIEW_THE_SIEGE_WAR_STATUS_WINDOW_DURING_A_SIEGE_WAR));
				return false;
			}
		}
		
		NpcHtmlMessage msg = new NpcHtmlMessage(5);
		msg.setFile("siege_status.htm");
		msg.replace("%name%", player.getName());
		msg.replace("%kills%", String.valueOf(0));
		msg.replace("%deaths%", String.valueOf(0));
		msg.replace("%type%", String.valueOf(0));
		player.sendPacket(msg);
		return true;
	}
	
	/**
	 * Method getUserCommandList.
	 * @return int[]
	 * @see lineage2.gameserver.handlers.IUserCommandHandler#getUserCommandList()
	 */
	@Override
	public int[] getUserCommandList()
	{
		return COMMANDS;
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
