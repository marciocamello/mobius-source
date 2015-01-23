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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.matching.MatchingRoom;
import lineage2.gameserver.network.serverpackets.ExAskJoinPartyRoom;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestAskJoinPartyRoom extends L2GameClientPacket
{
	private String _name;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_name = readS(16);
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		
		if (player == null)
		{
			return;
		}
		
		Player targetPlayer = World.getPlayer(_name);
		
		if ((targetPlayer == null) || (targetPlayer == player))
		{
			player.sendActionFailed();
			return;
		}
		
		if (player.isProcessingRequest())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.WAITING_FOR_ANOTHER_REPLY));
			return;
		}
		
		if (targetPlayer.isProcessingRequest())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_IS_ON_ANOTHER_TASK_PLEASE_TRY_AGAIN_LATER).addPcName(targetPlayer));
			return;
		}
		
		if (targetPlayer.getMatchingRoom() != null)
		{
			return;
		}
		
		MatchingRoom room = player.getMatchingRoom();
		
		if ((room == null) || (room.getType() != MatchingRoom.PARTY_MATCHING))
		{
			return;
		}
		
		if (room.getLeader() != player)
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ONLY_A_ROOM_LEADER_MAY_INVITE_OTHERS_TO_A_PARTY_ROOM));
			return;
		}
		
		if (room.getPlayers().size() >= room.getMaxMembersSize())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_PARTY_ROOM_IS_FULL_NO_MORE_CHARACTERS_CAN_BE_INVITED_IN));
			return;
		}
		
		new Request(L2RequestType.PARTY_ROOM, player, targetPlayer).setTimeout(10000L);
		targetPlayer.sendPacket(new ExAskJoinPartyRoom(player.getName(), room.getTopic()));
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_SENT_AN_INVITATION_TO_ROOM_S2).addPcName(player).addString(room.getTopic()));
		targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_SENT_AN_INVITATION_TO_ROOM_S2).addPcName(player).addString(room.getTopic()));
	}
}
