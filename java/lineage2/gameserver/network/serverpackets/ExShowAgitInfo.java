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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.entity.events.impl.ClanHallAuctionEvent;
import lineage2.gameserver.model.entity.events.impl.ClanHallMiniGameEvent;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.tables.ClanTable;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowAgitInfo extends L2GameServerPacket
{
	/**
	 * Field _clanHalls.
	 */
	private List<AgitInfo> _clanHalls = Collections.emptyList();
	
	/**
	 * Constructor for ExShowAgitInfo.
	 */
	public ExShowAgitInfo()
	{
		List<ClanHall> chs = ResidenceHolder.getInstance().getResidenceList(ClanHall.class);
		_clanHalls = new ArrayList<>(chs.size());
		for (ClanHall clanHall : chs)
		{
			int ch_id = clanHall.getId();
			int getType;
			if (clanHall.getSiegeEvent().getClass() == ClanHallAuctionEvent.class)
			{
				getType = 0;
			}
			else if (clanHall.getSiegeEvent().getClass() == ClanHallMiniGameEvent.class)
			{
				getType = 2;
			}
			else
			{
				getType = 1;
			}
			Clan clan = ClanTable.getInstance().getClan(clanHall.getOwnerId());
			String clan_name = (clanHall.getOwnerId() == 0) || (clan == null) ? StringUtils.EMPTY : clan.getName();
			String leader_name = (clanHall.getOwnerId() == 0) || (clan == null) ? StringUtils.EMPTY : clan.getLeaderName();
			_clanHalls.add(new AgitInfo(clan_name, leader_name, ch_id, getType));
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x16);
		writeD(_clanHalls.size());
		for (AgitInfo info : _clanHalls)
		{
			writeD(info.ch_id);
			writeS(info.clan_name);
			writeS(info.leader_name);
			writeD(info.getType);
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class AgitInfo
	{
		/**
		 * Field leader_name.
		 */
		/**
		 * Field clan_name.
		 */
		public String clan_name, leader_name;
		/**
		 * Field getType.
		 */
		/**
		 * Field ch_id.
		 */
		public int ch_id, getType;
		
		/**
		 * Constructor for AgitInfo.
		 * @param clan_name String
		 * @param leader_name String
		 * @param ch_id int
		 * @param lease int
		 */
		public AgitInfo(String clan_name, String leader_name, int ch_id, int lease)
		{
			this.clan_name = clan_name;
			this.leader_name = leader_name;
			this.ch_id = ch_id;
			getType = lease;
		}
	}
}
