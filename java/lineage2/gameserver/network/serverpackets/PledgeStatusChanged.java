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

import lineage2.gameserver.model.pledge.Clan;

/**
 * sample 0000: cd b0 98 a0 48 1e 01 00 00 00 00 00 00 00 00 00 ....H........... 0010: 00 00 00 00 00 .....
 * <p/>
 * format ddddd
 */
public class PledgeStatusChanged extends L2GameServerPacket
{
	@SuppressWarnings("unused")
	private final int leader_id, clan_id, level, type, crestId, allyId;
	
	public PledgeStatusChanged(Clan clan)
	{
		leader_id = clan.getLeaderId();
		clan_id = clan.getClanId();
		level = clan.getLevel();
		type = clan.getUnionType();
		crestId = clan.getCrestId();
		allyId = clan.getAllyId();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xCD);
		writeD(0);
		writeD(leader_id);
		writeD(clan_id);
		writeD(crestId);
		writeD(allyId);
		writeD(0);
		writeD(0);
		writeD(0);
	}
}