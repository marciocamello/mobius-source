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
import lineage2.gameserver.network.serverpackets.ExRegistWaitingSubstituteOk;
import lineage2.gameserver.network.serverpackets.ExWaitWaitingSubStituteInfo;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RequestRegistWaitingSubstitute extends L2GameClientPacket
{
	/**
	 * Field id.
	 */
	int id = 0;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		id = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		FindPartyManager psm = FindPartyManager.getInstance();
		Player player = getClient().getActiveChar();
		if (player.getParty() == null)
		{
			player.setPartySearchStatus(player.getPartySearchStatus() ? false : true);
			player.sendPacket(new ExWaitWaitingSubStituteInfo(player.getPartySearchStatus()));
			if (player.getPartySearchStatus())
			{
				player.sendPacket(SystemMsg.YOU_ARE_REGISTERED_ON_THE_WAITING_LIST);
				psm.addLookingForParty(player);
				for (Player activeChar : psm.getWannaToChangeThisPlayers())
				{
					if (psm.getLookingForParty(activeChar.getLevel(), activeChar.getClassId().getId()))
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
				player.sendPacket(SystemMsg.STOPPED_SEARCHING_THE_PARTY);
				psm.removeLookingForParty(player);
			}
		}
	}
}
