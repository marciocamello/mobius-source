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

import java.util.List;

import lineage2.gameserver.handlers.IVoicedCommandHandler;
import lineage2.gameserver.handlers.VoicedCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.skills.skillclasses.Call;
import lineage2.gameserver.utils.Location;

public final class Relocate implements IVoicedCommandHandler, ScriptFile
{
	public static final int SUMMON_PRICE = 5;
	private final String[] _commandList = new String[]
	{
		"summon_clan"
	};
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}
	
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		if (command.equals("summon_clan"))
		{
			if (!activeChar.isClanLeader())
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.ONLY_THE_CLAN_LEADER_IS_ENABLED));
				return false;
			}
			
			SystemMessage msg = Call.canSummonHere(activeChar);
			
			if (msg != null)
			{
				activeChar.sendPacket(msg);
				return false;
			}
			
			if (activeChar.isAlikeDead())
			{
				activeChar.sendMessage(new CustomMessage("scripts.commands.voiced.Relocate.Dead", activeChar));
				return false;
			}
			
			List<Player> clan = activeChar.getClan().getOnlineMembers(activeChar.getObjectId());
			
			for (Player pl : clan)
			{
				if (Call.canBeSummoned(pl) == null)
				{
					pl.summonCharacterRequest(activeChar, Location.findPointToStay(activeChar.getX(), activeChar.getY(), activeChar.getZ(), 100, 150, activeChar.getReflection().getGeoIndex()), SUMMON_PRICE);
				}
			}
			
			return true;
		}
		
		return false;
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