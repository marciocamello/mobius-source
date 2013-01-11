/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.handler.voicecommands.impl;

import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.NetPingPacket;
import lineage2.gameserver.scripts.Functions;

public class Ping extends Functions implements IVoicedCommandHandler
{
	private final String[] _commandList = new String[]
	{
		"ping"
	};
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}
	
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		if (command.equals("ping"))
		{
			activeChar.sendMessage("Processing request...");
			activeChar.sendPacket(new NetPingPacket(activeChar));
			ThreadPoolManager.getInstance().schedule(new AnswerTask(activeChar), 3000L);
			return true;
		}
		return false;
	}
	
	static final class AnswerTask implements Runnable
	{
		private final Player _player;
		
		public AnswerTask(Player player)
		{
			_player = player;
		}
		
		@Override
		public void run()
		{
			int ping = _player.getPing();
			if (ping != -1)
			{
				_player.sendMessage("Current ping: " + ping + " ms.");
			}
			else
			{
				_player.sendMessage("The data from the client was not received.");
			}
		}
	}
}
