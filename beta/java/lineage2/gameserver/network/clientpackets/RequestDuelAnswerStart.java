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

import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.impl.DuelEvent;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestDuelAnswerStart extends L2GameClientPacket
{
	private int _response;
	private int _duelType;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_duelType = readD();
		readD();
		_response = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		Request request = activeChar.getRequest();
		
		if ((request == null) || !request.isTypeOf(L2RequestType.DUEL))
		{
			return;
		}
		
		if (!request.isInProgress())
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		
		if (activeChar.isActionsDisabled())
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		
		Player requestor = request.getRequestor();
		
		if (requestor == null)
		{
			request.cancel();
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THAT_PLAYER_IS_NOT_ONLINE));
			activeChar.sendActionFailed();
			return;
		}
		
		if (requestor.getRequest() != request)
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		
		if (_duelType != request.getInteger("duelType"))
		{
			request.cancel();
			activeChar.sendActionFailed();
			return;
		}
		
		DuelEvent duelEvent = EventHolder.getInstance().getEvent(EventType.PVP_EVENT, _duelType);
		
		switch (_response)
		{
			case 0:
				request.cancel();
				
				if (_duelType == 1)
				{
					requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_OPPOSING_PARTY_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL));
				}
				else
				{
					requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_DECLINED_YOUR_CHALLENGE_TO_A_PARTY_DUEL).addPcName(activeChar));
				}
				break;
			
			case -1:
				request.cancel();
				requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_IS_SET_TO_REFUSE_DUEL_REQUESTS_AND_CANNOT_RECEIVE_A_DUEL_REQUEST).addPcName(activeChar));
				break;
			
			case 1:
				if (!duelEvent.canDuel(requestor, activeChar, false))
				{
					request.cancel();
					return;
				}
				
				SystemMessage msg1;
				SystemMessage msg2;
				
				if (_duelType == 1)
				{
					msg1 = SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_ACCEPTED_C1_S_CHALLENGE_TO_A_PARTY_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS);
					msg2 = SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_DUEL_AGAINST_THEIR_PARTY_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS);
				}
				else
				{
					msg1 = SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_ACCEPTED_C1_S_CHALLENGE_A_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS);
					msg2 = SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_A_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS);
				}
				
				activeChar.sendPacket(msg1.addPcName(requestor));
				requestor.sendPacket(msg2.addPcName(activeChar));
				
				try
				{
					duelEvent.createDuel(requestor, activeChar);
				}
				finally
				{
					request.done();
				}
				break;
		}
	}
}
