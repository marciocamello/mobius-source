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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.instancemanager.FindPartyManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.ExRegistWaitingSubstituteOk;
import lineage2.gameserver.network.serverpackets.ExWaitWaitingSubStituteInfo;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestRegistPartySubstitute extends L2GameClientPacket
{
	/**
	 * Field objId.
	 */
	int objId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		objId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player leader = getClient().getActiveChar();
		FindPartyManager psm = FindPartyManager.getInstance();
		if (World.getPlayer(objId) != null)
		{
			Player activeChar = World.getPlayer(objId);
			if (!psm.getWannaToChangeThisPlayer(activeChar.getLevel(), activeChar.getClassId().getId()))
			{
				SystemMessage sm1 = new SystemMessage(SystemMessage.LOOKING_FOR_A_PLAYER_WHO_WILL_REPLACE_S1);
				sm1.addName(activeChar);
				leader.sendPacket(sm1);
				psm.addChangeThisPlayer(activeChar);
			}
			else
			{
				SystemMessage sm1 = new SystemMessage(SystemMessage.STOPPED_LOOKING_FOR_A_PLAYER_WHO_WILL_REPLACE_S1);
				sm1.addName(activeChar);
				leader.sendPacket(sm1);
				psm.removeChangeThisPlayer(activeChar);
			}
			for (Player player : FindPartyManager.getInstance().getLookingForPartyPlayers())
			{
				if (psm.getWannaToChangeThisPlayer(player.getLevel(), player.getClassId().getId()))
				{
					player.sendPacket(new ExRegistWaitingSubstituteOk(player.getClassId().getId(), activeChar));
					player.setPartySearchStatus(player.getPartySearchStatus() ? false : true);
					player.setPlayerForChange(activeChar);
					player.sendPacket(new ExWaitWaitingSubStituteInfo(player.getPartySearchStatus()));
					FindPartyManager.getInstance().removeLookingForParty(player);
					FindPartyManager.getInstance().removeChangeThisPlayer(activeChar);
				}
			}
		}
		else
		{
			leader.sendPacket(SystemMsg.THE_PLAYER_TO_BE_REPLACED_DOES_NOT_EXIST);
		}
	}
}
