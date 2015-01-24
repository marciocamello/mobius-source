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

import lineage2.gameserver.Config;
import lineage2.gameserver.handlers.UserCommandHandler;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.interfaces.IUserCommandHandler;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OlympiadStat implements IUserCommandHandler, ScriptFile
{
	private static final int[] COMMAND_IDS =
	{
		109
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
		
		GameObject objectTarget = Config.OLYMPIAD_OLDSTYLE_STAT ? activeChar : activeChar.getTarget();
		
		if ((objectTarget == null) || !objectTarget.isPlayer() || !objectTarget.getPlayer().isNoble())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THIS_COMMAND_CAN_ONLY_BE_USED_WHEN_THE_TARGET_IS_AN_AWAKENED_NOBLESSE_EXALTED));
			return true;
		}
		
		Player playerTarget = objectTarget.getPlayer();
		SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.FOR_THE_CURRENT_OLYMPIAD_YOU_HAVE_PARTICIPATED_IN_S1_MATCH_ES_AND_HAD_S2_WIN_S_AND_S3_DEFEAT_S_YOU_CURRENTLY_HAVE_S4_OLYMPIAD_POINT_S);
		sm.addInt(Olympiad.getCompetitionDone(playerTarget.getObjectId()));
		sm.addInt(Olympiad.getCompetitionWin(playerTarget.getObjectId()));
		sm.addInt(Olympiad.getCompetitionLoose(playerTarget.getObjectId()));
		sm.addInt(Olympiad.getNoblePoints(playerTarget.getObjectId()));
		activeChar.sendPacket(sm);
		int[] ar = Olympiad.getWeekGameCounts(playerTarget.getObjectId());
		sm = SystemMessage.getSystemMessage(SystemMessageId.THE_MATCHES_THIS_WEEK_ARE_ALL_CLASS_BATTLES_THE_NUMBER_OF_MATCHES_THAT_ARE_ALLOWED_TO_PARTICIPATE_IS_S1);
		sm.addInt(ar[0]);
		sm.addInt(ar[1]);
		sm.addInt(ar[2]);
		sm.addInt(ar[2]);
		activeChar.sendPacket(sm);
		return true;
	}
	
	/**
	 * Method getUserCommandList.
	 * @return int[]
	 * @see lineage2.gameserver.model.interfaces.IUserCommandHandler#getUserCommandList()
	 */
	@Override
	public int[] getUserCommandList()
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
