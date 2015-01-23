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

import lineage2.gameserver.instancemanager.ClanEntryManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.entry.PledgeWaitingInfo;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;

public class RequestPledgeDraftListApply extends L2GameClientPacket
{
	int _applyType;
	int _karma;
	
	@Override
	protected void readImpl()
	{
		_applyType = readD();
		_karma = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		
		if ((activeChar == null) || (activeChar.getClan() != null))
		{
			return;
		}
		
		if (activeChar.getClan() != null)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ONLY_THE_CLAN_LEADER_OR_SOMEONE_WITH_RANK_MANAGEMENT_AUTHORITY_MAY_REGISTER_THE_CLAN));
			return;
		}
		
		switch (_applyType)
		{
			case 0: // remove
			{
				if (ClanEntryManager.getInstance().removeFromWaitingList(activeChar.getObjectId()))
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ENTRY_APPLICATION_CANCELLED_YOU_MAY_APPLY_TO_A_NEW_CLAN_AFTER_5_MINUTES));
				}
				break;
			}
			case 1: // add
			{
				final PledgeWaitingInfo pledgeDraftList = new PledgeWaitingInfo(activeChar.getObjectId(), activeChar.getLevel(), _karma, activeChar.getClassId().getId(), activeChar.getName());
				
				if (ClanEntryManager.getInstance().addToWaitingList(activeChar.getObjectId(), pledgeDraftList))
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ENTERED_INTO_WAITING_LIST_NAME_IS_AUTOMATICALLY_DELETED_AFTER_30_DAYS_IF_DELETE_FROM_WAITING_LIST_IS_USED_YOU_CANNOT_ENTER_NAMES_INTO_THE_WAITING_LIST_FOR_5_MINUTES));
				}
				else
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_MAY_APPLY_FOR_ENTRY_AFTER_S1_MINUTE_S_DUE_TO_CANCELLING_YOUR_APPLICATION);
					sm.addLong(ClanEntryManager.getInstance().getPlayerLockTime(activeChar.getObjectId()));
					activeChar.sendPacket(sm);
				}
				break;
			}
		}
	}
}