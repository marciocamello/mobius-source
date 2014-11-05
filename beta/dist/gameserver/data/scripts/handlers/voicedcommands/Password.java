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
package handlers.voicedcommands;

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.handlers.IVoicedCommandHandler;
import lineage2.gameserver.handlers.VoicedCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.loginservercon.LoginServerCommunication;
import lineage2.gameserver.network.loginservercon.gspackets.ChangePassword;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Password extends Functions implements IVoicedCommandHandler, ScriptFile
{
	private final String[] _commandList = new String[]
	{
		"password",
		"check"
	};
	
	/**
	 * Method useVoicedCommand.
	 * @param command String
	 * @param activeChar Player
	 * @param args String
	 * @return boolean
	 * @see lineage2.gameserver.handlers.IVoicedCommandHandler#useVoicedCommand(String, Player, String)
	 */
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String args)
	{
		command = command.intern();
		
		if (command.equalsIgnoreCase("password"))
		{
			return password(command, activeChar, args);
		}
		
		if (command.equalsIgnoreCase("check"))
		{
			return check(command, activeChar, args);
		}
		
		return false;
	}
	
	/**
	 * Method password.
	 * @param command String
	 * @param activeChar Player
	 * @param target String
	 * @return boolean
	 */
	private boolean password(String command, Player activeChar, String target)
	{
		if (command.equals("password"))
		{
			String dialog = HtmCache.getInstance().getNotNull("command/password.html", activeChar);
			show(dialog, activeChar);
			return true;
		}
		
		return true;
	}
	
	/**
	 * Method getVoicedCommandList.
	 * @return String[] * @see lineage2.gameserver.handlers.voicedcommands.IVoicedCommandHandler#getVoicedCommandList()
	 */
	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}
	
	/**
	 * Method check.
	 * @param command String
	 * @param activeChar Player
	 * @param target String
	 * @return boolean
	 */
	private boolean check(String command, Player activeChar, String target)
	{
		String[] parts = target.split(" ");
		
		if (parts.length != 3)
		{
			show(new CustomMessage("scripts.commands.user.password.IncorrectValues", activeChar), activeChar);
			return false;
		}
		
		if (!parts[1].equals(parts[2]))
		{
			show(new CustomMessage("scripts.commands.user.password.IncorrectConfirmation", activeChar), activeChar);
			return false;
		}
		
		if (parts[1].equals(parts[0]))
		{
			show(new CustomMessage("scripts.commands.user.password.NewPassIsOldPass", activeChar), activeChar);
			return false;
		}
		
		if ((parts[1].length() < 5) || (parts[1].length() > 20))
		{
			show(new CustomMessage("scripts.commands.user.password.IncorrectSize", activeChar), activeChar);
			return false;
		}
		
		LoginServerCommunication.getInstance().sendPacket(new ChangePassword(activeChar.getAccountName(), parts[0], parts[1], "null"));
		show(new CustomMessage("scripts.commands.user.password.ResultTrue", activeChar), activeChar);
		return true;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
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
