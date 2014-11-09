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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.Clan;

public class PledgeStatusChanged extends L2GameServerPacket
{
	private final int leader_id;
	private final int clan_id;
	@SuppressWarnings("unused")
	private final int level;
	private final int type;
	private final int crestId;
	private final int crestLargeId;
	private final int allyId;
	private final int allyCrestId;
	
	public PledgeStatusChanged(Player player)
	{
		Clan clan = player.getClan();
		leader_id = clan.getLeaderId();
		clan_id = clan.getClanId();
		level = clan.getLevel();
		type = clan.getUnionType();
		crestId = clan.getCrestId();
		crestLargeId = clan.getCrestLargeId();
		allyId = clan.getAllyId();
		allyCrestId = clan.getAlliance().getAllyCrestId();
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
		writeD(allyCrestId);
		writeD(crestLargeId);
		writeD(type);
	}
}