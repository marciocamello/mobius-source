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

import java.util.Calendar;

import lineage2.gameserver.handlers.IUserCommandHandler;
import lineage2.gameserver.handlers.UserCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MyBirthday implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS =
	{
		126
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
		if (activeChar.getCreateTime() == 0)
		{
			return false;
		}
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(activeChar.getCreateTime());
		activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_S_BIRTHDAY_IS_S3_S4_S2).addPcName(activeChar).addInt(c.get(Calendar.YEAR)).addInt(c.get(Calendar.MONTH) + 1).addInt(c.get(Calendar.DAY_OF_MONTH)));
		
		if ((c.get(Calendar.MONTH) == Calendar.FEBRUARY) && (c.get(Calendar.DAY_OF_WEEK) == 29))
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.A_CHARACTER_BORN_ON_FEBRUARY_29_WILL_RECEIVE_A_GIFT_ON_FEBRUARY_28));
		}
		
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
