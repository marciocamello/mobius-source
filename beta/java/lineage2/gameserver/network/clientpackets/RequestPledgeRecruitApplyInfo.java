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
import lineage2.gameserver.model.base.ClanEntryStatus;
import lineage2.gameserver.network.serverpackets.ExPledgeRecruitApplyInfo;

public class RequestPledgeRecruitApplyInfo extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		
	}
	
	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		ClanEntryStatus status = ClanEntryStatus.DEFAULT;
		
		if ((activeChar.getClan() != null) && activeChar.isClanLeader() && ClanEntryManager.getInstance().isClanRegistred(activeChar.getClanId()))
		{
			status = ClanEntryStatus.ORDERED;
		}
		else if ((activeChar.getClan() == null) && (ClanEntryManager.getInstance().isPlayerRegistred(activeChar.getObjectId())))
		{
			status = ClanEntryStatus.WAITING;
		}
		
		activeChar.sendPacket(new ExPledgeRecruitApplyInfo(status));
	}
}