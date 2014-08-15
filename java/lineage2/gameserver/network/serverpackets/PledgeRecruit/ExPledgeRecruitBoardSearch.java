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
package lineage2.gameserver.network.serverpackets.PledgeRecruit;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author Smotocel-PC
 */
public class ExPledgeRecruitBoardSearch extends L2GameServerPacket
{
	public String leadername;
	public int ClanLvl;
	public String clanname;
	public String notice;
	public int ClanSize;
	public int ClanKarma;
	public int size;
	public int crest_id;
	
	public ExPledgeRecruitBoardSearch(Player pl)
	{
		leadername = pl.getClan().getLeaderName();
		clanname = pl.getClan().getName();
		ClanLvl = pl.getClan().getLevel();
		ClanSize = pl.getClan().getAllSize();
		notice = pl.getClan().getNotice();
		crest_id = pl.getClan().getCrestId();
		ClanKarma = 2; // 0 - Networking, 1 - Clan Wars, 2 - Hunting, 3 - Etc
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x14B);
		writeD(1);
		writeD(1); // page count number
		writeD(1); // clan numbers per page ??
		writeS("NULL"); // Crest ???
		writeD(1); // ???
		writeS(" " + clanname); // Clan Name
		writeS(leadername); // Clan Leader Name
		writeD(ClanLvl); // Clan Lvl
		writeD(ClanSize); // Clan Members
		writeD(ClanKarma); // Clan Type
		writeS(notice);
	}
}
