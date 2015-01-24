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

import lineage2.gameserver.Config;
import lineage2.gameserver.handlers.VoicedCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.interfaces.IVoicedCommandHandler;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Offline extends Functions implements IVoicedCommandHandler, ScriptFile
{
	private final String[] _commandList = new String[]
	{
		"offline"
	};
	
	/**
	 * Method useVoicedCommand.
	 * @param command String
	 * @param activeChar Player
	 * @param args String
	 * @return boolean
	 * @see lineage2.gameserver.model.interfaces.IVoicedCommandHandler#useVoicedCommand(String, Player, String)
	 */
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String args)
	{
		if (!Config.SERVICES_OFFLINE_TRADE_ALLOW)
		{
			return false;
		}
		
		if ((activeChar.getOlympiadObserveGame() != null) || (activeChar.getOlympiadGame() != null) || Olympiad.isRegisteredInComp(activeChar))
		{
			activeChar.sendActionFailed();
			return false;
		}
		
		if (activeChar.getLevel() < Config.SERVICES_OFFLINE_TRADE_MIN_LEVEL)
		{
			show("Minimal level to use this service is " + Config.SERVICES_OFFLINE_TRADE_MIN_LEVEL, activeChar);
			return false;
		}
		
		if (!activeChar.isInZone(Zone.ZoneType.Offshore) && Config.SERVICES_OFFLINE_TRADE_ALLOW_OFFSHORE)
		{
			show("You cannot trade offline in this zone. Offline trade allowed only in Giran Harbor or Parnassus.", activeChar);
			return false;
		}
		
		if (!activeChar.isInStoreMode())
		{
			show("This command usable only in private store mode.", activeChar);
			return false;
		}
		
		if (activeChar.getNoChannelRemained() > 0)
		{
			show("You can't use this service while your chat is banned.", activeChar);
			return false;
		}
		
		if (activeChar.isActionBlocked(Zone.BLOCKED_ACTION_PRIVATE_STORE))
		{
			show("You cannot trade offline in this zone.", activeChar);
			return false;
		}
		
		if ((Config.SERVICES_OFFLINE_TRADE_PRICE > 0) && (Config.SERVICES_OFFLINE_TRADE_PRICE_ITEM > 0))
		{
			if (getItemCount(activeChar, Config.SERVICES_OFFLINE_TRADE_PRICE_ITEM) < Config.SERVICES_OFFLINE_TRADE_PRICE)
			{
				show("Price of this service " + Config.SERVICES_OFFLINE_TRADE_PRICE_ITEM + " x " + Config.SERVICES_OFFLINE_TRADE_PRICE, activeChar);
				return false;
			}
			
			removeItem(activeChar, Config.SERVICES_OFFLINE_TRADE_PRICE_ITEM, Config.SERVICES_OFFLINE_TRADE_PRICE);
		}
		
		activeChar.offline();
		return true;
	}
	
	/**
	 * Method getVoicedCommandList.
	 * @return String[]
	 * @see lineage2.gameserver.model.interfaces.IVoicedCommandHandler#getVoicedCommandList()
	 */
	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
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
