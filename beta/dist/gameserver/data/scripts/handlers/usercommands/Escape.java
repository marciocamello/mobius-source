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

import lineage2.gameserver.enums.TeamType;
import lineage2.gameserver.handlers.UserCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.interfaces.IUserCommandHandler;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Escape implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS =
	{
		52
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
		if (id != COMMAND_IDS[0])
		{
			return false;
		}
		
		if (activeChar.isMovementDisabled() || activeChar.isInOlympiadMode())
		{
			return false;
		}
		
		if ((activeChar.getTeleMode() != 0) || !activeChar.getPlayerAccess().UseTeleport)
		{
			activeChar.sendMessage("Try later.");
			return false;
		}
		
		if (activeChar.isTerritoryFlagEquipped())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_TELEPORT_WHILE_IN_POSSESSION_OF_A_WARD));
			return false;
		}
		
		if (activeChar.isInDuel() || (activeChar.getTeam() != TeamType.NONE))
		{
			activeChar.sendMessage("You cannot use escape skills during a duel or during event participation.");
			return false;
		}
		
		activeChar.abortAttack(true, true);
		activeChar.abortCast(true, true);
		activeChar.stopMove();
		Skill skill;
		
		if (activeChar.getPlayerAccess().FastUnstuck)
		{
			skill = SkillTable.getInstance().getInfo(1050, 2);
		}
		else
		{
			skill = SkillTable.getInstance().getInfo(2099, 1);
		}
		
		if ((skill != null) && skill.checkCondition(activeChar, activeChar, false, false, true))
		{
			activeChar.getAI().Cast(skill, activeChar, false, true);
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
