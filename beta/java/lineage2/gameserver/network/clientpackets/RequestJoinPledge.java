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

import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.AskJoinPledge;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;

/**
 * @author Creative Infinity
 * @version $Revision: 1.0 $
 */
public class RequestJoinPledge extends L2GameClientPacket
{
	private int _objectId;
	private int _pledgeType;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_pledgeType = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		
		if ((activeChar == null) || (activeChar.getClan() == null))
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
		
		Clan clan = activeChar.getClan();
		
		if (!clan.canInvite())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.AFTER_A_CLAN_MEMBER_IS_DISMISSED_FROM_A_CLAN_THE_CLAN_MUST_WAIT_AT_LEAST_A_DAY_BEFORE_ACCEPTING_A_NEW_MEMBER));
			return;
		}
		
		if (_objectId == activeChar.getObjectId())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_ASK_YOURSELF_TO_APPLY_TO_A_CLAN));
			return;
		}
		
		if ((activeChar.getClanPrivileges() & Clan.CP_CL_INVITE_CLAN) != Clan.CP_CL_INVITE_CLAN)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ONLY_THE_LEADER_CAN_GIVE_OUT_INVITATIONS));
			return;
		}
		
		GameObject object = activeChar.getVisibleObject(_objectId);
		
		if ((object == null) || !object.isPlayer())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THAT_IS_AN_INCORRECT_TARGET));
			return;
		}
		
		Player member = (Player) object;
		
		if (member.getClan() == activeChar.getClan())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THAT_IS_AN_INCORRECT_TARGET));
			return;
		}
		
		if (!member.getPlayerAccess().CanJoinClan)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_CANNOT_JOIN_THE_CLAN_BECAUSE_ONE_DAY_HAS_NOT_YET_PASSED_SINCE_THEY_LEFT_ANOTHER_CLAN).addPcName(member));
			return;
		}
		
		if (member.getClan() != null)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_ALREADY_A_MEMBER_OF_ANOTHER_CLAN).addPcName(member));
			return;
		}
		
		if (member.isBusy())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_IS_ON_ANOTHER_TASK_PLEASE_TRY_AGAIN_LATER).addPcName(member));
			return;
		}
		
		if ((_pledgeType == Clan.SUBUNIT_ACADEMY) && ((member.getLevel() > 75) || (member.getClassLevel() > 3)))
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.IN_ORDER_TO_JOIN_THE_CLAN_ACADEMY_YOU_MUST_BE_UNAFFILIATED_WITH_A_CLAN_AND_BE_AN_UNAWAKENED_CHARACTER_LV_84_OR_BELOW_FPR_BOTH_MAIN_AND_SUBCLASS));
			return;
		}
		
		if (clan.getUnitMembersSize(_pledgeType) >= clan.getSubPledgeLimit(_pledgeType))
		{
			if (_pledgeType == 0)
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_FULL_AND_CANNOT_ACCEPT_ADDITIONAL_CLAN_MEMBERS_AT_THIS_TIME).addString(clan.getName()));
			}
			else
			{
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_ACADEMY_ROYAL_GUARD_ORDER_OF_KNIGHTS_IS_FULL_AND_CANNOT_ACCEPT_NEW_MEMBERS_AT_THIS_TIME));
			}
			
			return;
		}
		
		Request request = new Request(L2RequestType.CLAN, activeChar, member).setTimeout(10000L);
		request.set("pledgeType", _pledgeType);
		final String pledgeName = activeChar.getClan().getName();
		final String subPledgeName = (activeChar.getClan().getSubUnit(_pledgeType) != null ? activeChar.getClan().getSubUnit(_pledgeType).getName() : null);
		member.sendPacket(new AskJoinPledge(activeChar.getObjectId(), subPledgeName, _pledgeType, pledgeName));
	}
}
