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
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.entry.PledgeRecruitInfo;
import lineage2.gameserver.tables.ClanTable;

public class ExPledgeRecruitInfo extends L2GameServerPacket
{
	private final PledgeRecruitInfo _pledgeRecruitInfo;
	private final Clan _clan;
	
	public ExPledgeRecruitInfo(int clanId)
	{
		_pledgeRecruitInfo = ClanEntryManager.getInstance().getClanById(clanId);
		_clan = ClanTable.getInstance().getClan(clanId);
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x13F);
		if (_pledgeRecruitInfo == null)
		{
			writeS(_clan.getName());
			writeS(_clan.getLeaderName());
			writeD(_clan.getLevel());
			writeD(_clan.getAllSize());
			writeD(0x00);
		}
		else
		{
			writeS(_pledgeRecruitInfo.getClan().getName());
			writeS(_pledgeRecruitInfo.getClan().getLeaderName());
			writeD(_pledgeRecruitInfo.getClan().getLevel());
			writeD(_pledgeRecruitInfo.getClan().getAllSize());
			writeD(_pledgeRecruitInfo.getKarma());
		}
	}
}