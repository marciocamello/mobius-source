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
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestVoteNew extends L2GameClientPacket
{
	private int _targetObjectId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_targetObjectId = readD();
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
		
		if (!activeChar.getPlayerAccess().CanEvaluate)
		{
			return;
		}
		
		GameObject target = activeChar.getTarget();
		
		if ((target == null) || !target.isPlayer() || (target.getObjectId() != _targetObjectId))
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THAT_IS_AN_INCORRECT_TARGET));
			return;
		}
		
		if (target.getObjectId() == activeChar.getObjectId())
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_CANNOT_RECOMMEND_YOURSELF));
			return;
		}
		
		Player targetPlayer = (Player) target;
		
		if (activeChar.getRecomLeft() <= 0)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_MAKE_FURTHER_RECOMMENDATIONS_AT_THIS_TIME_YOU_WILL_RECEIVE_MORE_RECOMMENDATION_CREDITS_EACH_DAY_AT_1_P_M));
			return;
		}
		
		if (targetPlayer.getRecomHave() >= 255)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOUR_SELECTED_TARGET_CAN_NO_LONGER_RECEIVE_A_RECOMMENDATION));
			return;
		}
		
		activeChar.giveRecom(targetPlayer);
		SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_RECOMMENDED_C1_YOU_HAVE_S2_RECOMMENDATIONS_LEFT);
		sm.addString(target.getName());
		sm.addInt(activeChar.getRecomLeft());
		activeChar.sendPacket(sm);
		sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_BEEN_RECOMMENDED_BY_C1);
		sm.addString(activeChar.getName());
		targetPlayer.sendPacket(sm);
	}
}
