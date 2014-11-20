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
package lineage2.gameserver.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminCommandHandler extends AbstractHolder
{
	private static final AdminCommandHandler _instance = new AdminCommandHandler();
	
	/**
	 * Method getInstance.
	 * @return AdminCommandHandler
	 */
	public static AdminCommandHandler getInstance()
	{
		return _instance;
	}
	
	private final Map<String, IAdminCommandHandler> _datatable = new HashMap<>();
	
	/**
	 * Constructor for AdminCommandHandler.
	 */
	private AdminCommandHandler()
	{
	}
	
	/**
	 * Method registerAdminCommandHandler.
	 * @param handler IAdminCommandHandler
	 */
	public void registerAdminCommandHandler(IAdminCommandHandler handler)
	{
		for (String e : handler.getAdminCommandList())
		{
			_datatable.put(e.toString().toLowerCase(), handler);
		}
	}
	
	/**
	 * Method getAdminCommandHandler.
	 * @param adminCommand String
	 * @return IAdminCommandHandler
	 */
	IAdminCommandHandler getAdminCommandHandler(String adminCommand)
	{
		String command = adminCommand;
		
		if (adminCommand.contains(" "))
		{
			command = adminCommand.substring(0, adminCommand.indexOf(" "));
		}
		
		return _datatable.get(command);
	}
	
	/**
	 * Method useAdminCommandHandler.
	 * @param activeChar Player
	 * @param adminCommand String
	 */
	public void useAdminCommandHandler(Player activeChar, String adminCommand)
	{
		if (!(activeChar.isGM() || activeChar.getPlayerAccess().CanUseGMCommand))
		{
			activeChar.sendMessage("No access to use command, or command " + adminCommand + " not recognized.");
			return;
		}
		
		String[] wordList = adminCommand.split(" ");
		IAdminCommandHandler handler = _datatable.get(wordList[0]);
		
		if (handler != null)
		{
			boolean success = false;
			
			try
			{
				for (String e : handler.getAdminCommandList())
				{
					if (e.toString().equalsIgnoreCase(wordList[0]))
					{
						success = handler.useAdminCommand(e, wordList, adminCommand, activeChar);
						break;
					}
				}
			}
			catch (Exception e)
			{
				error("", e);
			}
			
			Log.LogCommand(activeChar, activeChar.getTarget(), adminCommand, success);
		}
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _datatable.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_datatable.clear();
	}
	
	/**
	 * Method getAllCommands.
	 * @return Set<String>
	 */
	public Set<String> getAllCommands()
	{
		return _datatable.keySet();
	}
}
