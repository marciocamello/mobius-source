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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.network.serverpackets.AskJoinAlliance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestJoinAlly extends L2GameClientPacket
{
	private int _objectId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		
		if ((activeChar == null) || (activeChar.getClan() == null) || (activeChar.getAlliance() == null))
		{
			return;
		}
		
		if (activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}
		
		if (activeChar.isProcessingRequest())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.WAITING_FOR_ANOTHER_REPLY));
			return;
		}
		
		if (activeChar.getAlliance().getMembersCount() >= Config.ALT_MAX_ALLY_SIZE)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_FAILED_TO_INVITE_A_CLAN_INTO_THE_ALLIANCE));
			return;
		}
		
		GameObject obj = activeChar.getVisibleObject(_objectId);
		
		if ((obj == null) || !obj.isPlayer() || (obj == activeChar))
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THAT_IS_AN_INCORRECT_TARGET));
			return;
		}
		
		Player target = (Player) obj;
		
		if (!activeChar.isAllyLeader())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THIS_FEATURE_IS_ONLY_AVAILABLE_TO_ALLIANCE_LEADERS));
			return;
		}
		
		if ((target.getAlliance() != null) || activeChar.getAlliance().isMember(target.getClan().getClanId()))
		{
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CLAN_IS_ALREADY_A_MEMBER_OF_S2_ALLIANCE);
			sm.addString(target.getClan().getName());
			sm.addString(target.getAlliance().getAllyName());
			activeChar.sendPacket(sm);
			return;
		}
		
		if (!target.isClanLeader())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_NOT_A_CLAN_LEADER).addString(target.getName()));
			return;
		}
		
		if (activeChar.isAtWarWith(target.getClanId()) > 0)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_MAY_NOT_ALLY_WITH_A_CLAN_YOU_ARE_CURRENTLY_AT_WAR_WITH_THAT_WOULD_BE_DIABOLICAL_AND_TREACHEROUS));
			return;
		}
		
		if (!target.getClan().canJoinAlly())
		{
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CLAN_CANNOT_JOIN_THE_ALLIANCE_BECAUSE_ONE_DAY_HAS_NOT_YET_PASSED_SINCE_THEY_LEFT_ANOTHER_ALLIANCE);
			sm.addString(target.getClan().getName());
			activeChar.sendPacket(sm);
			return;
		}
		
		if (!activeChar.getAlliance().canInvite())
		{
			activeChar.sendMessage("After a clan is dismissed from an alliance, the alliance must wait at least a day before accepting a new member.");
			return;
		}
		
		if (target.isBusy())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_IS_ON_ANOTHER_TASK_PLEASE_TRY_AGAIN_LATER).addString(target.getName()));
			return;
		}
		
		new Request(L2RequestType.ALLY, activeChar, target).setTimeout(10000L);
		target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_LEADER_S2_HAS_REQUESTED_AN_ALLIANCE).addString(activeChar.getAlliance().getAllyName()).addPcName(activeChar));
		target.sendPacket(new AskJoinAlliance(activeChar.getObjectId(), activeChar.getName(), activeChar.getAlliance().getAllyName()));
		return;
	}
}
