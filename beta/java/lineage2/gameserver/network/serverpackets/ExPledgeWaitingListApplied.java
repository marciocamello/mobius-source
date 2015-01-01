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
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.instancemanager.ClanEntryManager;
import lineage2.gameserver.model.pledge.entry.PledgeApplicantInfo;
import lineage2.gameserver.model.pledge.entry.PledgeRecruitInfo;

public class ExPledgeWaitingListApplied extends L2GameServerPacket
{
	private final PledgeApplicantInfo _pledgePlayerRecruitInfo;
	private final PledgeRecruitInfo _pledgeRecruitInfo;
	
	public ExPledgeWaitingListApplied(int clanId, int playerId)
	{
		_pledgePlayerRecruitInfo = ClanEntryManager.getInstance().getPlayerApplication(clanId, playerId);
		_pledgeRecruitInfo = ClanEntryManager.getInstance().getClanById(clanId);
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x143);
		
		writeD(_pledgeRecruitInfo.getClan().getClanId());
		writeS(_pledgeRecruitInfo.getClan().getName());
		writeS(_pledgeRecruitInfo.getClan().getLeaderName());
		writeD(_pledgeRecruitInfo.getClan().getLevel());
		writeD(_pledgeRecruitInfo.getClan().getAllSize());
		writeD(_pledgeRecruitInfo.getKarma());
		writeS(_pledgeRecruitInfo.getInformation());
		writeS(_pledgePlayerRecruitInfo.getMessage());
	}
}