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
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.entry.PledgeRecruitInfo;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;

public class RequestPledgeRecruitBoardAccess extends L2GameClientPacket
{
	private int _applyType;
	private int _karma;
	private String _information;
	private String _datailedInformation;
	
	@Override
	protected void readImpl()
	{
		_applyType = readD();
		_karma = readD();
		_information = readS();
		_datailedInformation = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		final Clan clan = activeChar.getClan();
		
		if (clan == null)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ONLY_THE_CLAN_LEADER_OR_SOMEONE_WITH_RANK_MANAGEMENT_AUTHORITY_MAY_REGISTER_THE_CLAN));
			return;
		}
		
		if ((activeChar.getClanPrivileges() & Clan.CP_CL_MANAGE_RANKS) != Clan.CP_CL_MANAGE_RANKS)
		{
			activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ONLY_THE_CLAN_LEADER_OR_SOMEONE_WITH_RANK_MANAGEMENT_AUTHORITY_MAY_REGISTER_THE_CLAN));
			return;
		}
		
		final PledgeRecruitInfo pledgeRecruitInfo = new PledgeRecruitInfo(clan.getClanId(), _karma, _information, _datailedInformation);
		
		switch (_applyType)
		{
			case 0: // remove
			{
				ClanEntryManager.getInstance().removeFromClanList(clan.getClanId());
				break;
			}
			case 1: // update
			{
				if (ClanEntryManager.getInstance().updateClanList(clan.getClanId(), pledgeRecruitInfo))
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ENTRY_APPLICATION_COMPLETE_USE_ENTRY_APPLICATION_INFO_TO_CHECK_OR_CANCEL_YOUR_APPLICATION_APPLICATION_IS_AUTOMATICALLY_CANCELLED_AFTER_30_DAYS_IF_YOU_CANCEL_APPLICATION_YOU_CANNOT_APPLY_AGAIN_FOR_5_MINUTES));
				}
				else
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_MAY_APPLY_FOR_ENTRY_AFTER_S1_MINUTE_S_DUE_TO_CANCELLING_YOUR_APPLICATION);
					sm.addLong(ClanEntryManager.getInstance().getPlayerLockTime(activeChar.getObjectId()));
					activeChar.sendPacket(sm);
				}
				break;
			}
			case 2: // add
			{
				if (ClanEntryManager.getInstance().addToClanList(clan.getClanId(), pledgeRecruitInfo))
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ENTRY_APPLICATION_COMPLETE_USE_ENTRY_APPLICATION_INFO_TO_CHECK_OR_CANCEL_YOUR_APPLICATION_APPLICATION_IS_AUTOMATICALLY_CANCELLED_AFTER_30_DAYS_IF_YOU_CANCEL_APPLICATION_YOU_CANNOT_APPLY_AGAIN_FOR_5_MINUTES));
				}
				else
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_MAY_APPLY_FOR_ENTRY_AFTER_S1_MINUTE_S_DUE_TO_CANCELLING_YOUR_APPLICATION);
					sm.addLong(ClanEntryManager.getInstance().getClanLockTime(clan.getClanId()));
					activeChar.sendPacket(sm);
				}
				break;
			}
		}
	}
}