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
package handler.voicecommands;

import lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler;
import lineage2.gameserver.handler.voicecommands.VoicedCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.skills.EffectType;

public class Leadergate implements IVoicedCommandHandler, ScriptFile
{
	private final String[] _commandList = new String[]
	{
		"leadergate"
	};
	
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		if (command.equals("leadergate"))
		{
			if (activeChar.getClan() != null)
			{
				Player clanLeader = activeChar.getClan().getLeader().getPlayer();
				if (clanLeader == null)
				{
					return false;
				}
				if (clanLeader.getEffectList().getEffectByType(EffectType.Immobilize) != null)
				{
					if (!validateGateCondition(clanLeader, activeChar))
					{
						return false;
					}
					activeChar.teleToLocation(clanLeader.getX(), clanLeader.getY(), clanLeader.getZ());
					return true;
				}
				activeChar.sendMessage("I cannot find the lord's signal right now, so it is impossible to cast the spell.");
			}
		}
		return true;
	}
	
	private static boolean validateGateCondition(Player clanLeader, Player player)
	{
		if (clanLeader.isAlikeDead())
		{
			// Need retail message if there's one.
			player.sendMessage("Couldn't teleport to clan leader. The requirements was not meet.");
			return false;
		}
		if (clanLeader.isInStoreMode())
		{
			// Need retail message if there's one.
			player.sendMessage("Couldn't teleport to clan leader. The requirements was not meet.");
			return false;
		}
		if (clanLeader.isRooted() || clanLeader.isInCombat())
		{
			// Need retail message if there's one.
			player.sendMessage("Couldn't teleport to clan leader. The requirements was not meet.");
			return false;
		}
		if (clanLeader.isInOlympiadMode())
		{
			// Need retail message if there's one.
			player.sendMessage("Couldn't teleport to clan leader. The requirements was not meet.");
			return false;
		}
		if (clanLeader.isInObserverMode())
		{
			// Need retail message if there's one.
			player.sendMessage("Couldn't teleport to clan leader. The requirements was not meet.");
			return false;
		}
		
		return true;
		
	}
	
	@Override
	public void onLoad()
	{
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
	}
	
	@Override
	public void onReload()
	{
		
	}
	
	@Override
	public void onShutdown()
	{
		
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}
}
