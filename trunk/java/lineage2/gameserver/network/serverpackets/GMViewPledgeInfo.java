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
import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.UnitMember;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GMViewPledgeInfo extends L2GameServerPacket
{
	/**
	 * Field char_name.
	 */
	private String char_name;
	/**
	 * Field clan_name.
	 */
	private final String clan_name;
	/**
	 * Field leader_name.
	 */
	private final String leader_name;
	/**
	 * Field ally_name.
	 */
	private String ally_name;
	/**
	 * Field clan_level. Field clan_crest_id. Field clan_id.
	 */
	private int clan_id, clan_crest_id, clan_level;
	/**
	 * Field rank.
	 */
	private final int rank;
	/**
	 * Field rep.
	 */
	private int rep;
	/**
	 * Field ally_id.
	 */
	private final int ally_id;
	/**
	 * Field ally_crest_id.
	 */
	private int ally_crest_id;
	/**
	 * Field atWar. Field hasFortress. Field hasHideout. Field hasCastle.
	 */
	private final int hasCastle, hasHideout, hasFortress, atWar;
	/**
	 * Field infos.
	 */
	private final List<PledgeMemberInfo> infos = new ArrayList<>();
	
	/**
	 * Constructor for GMViewPledgeInfo.
	 * @param activeChar Player
	 */
	public GMViewPledgeInfo(Player activeChar)
	{
		Clan clan = activeChar.getClan();
		for (UnitMember member : clan)
		{
			if (member == null)
			{
				continue;
			}
			char_name = member.getName();
			clan_level = member.getLevel();
			clan_id = member.getClassId();
			clan_crest_id = member.isOnline() ? member.getObjectId() : 0;
			rep = member.getSponsor() != 0 ? 1 : 0;
			infos.add(new PledgeMemberInfo(char_name, clan_level, clan_id, clan_crest_id, member.getSex(), 1, rep));
		}
		char_name = activeChar.getName();
		clan_id = clan.getClanId();
		clan_name = clan.getName();
		leader_name = clan.getLeaderName();
		clan_crest_id = clan.getCrestId();
		clan_level = clan.getLevel();
		hasCastle = clan.getCastle();
		hasHideout = clan.getHasHideout();
		hasFortress = clan.getHasFortress();
		rank = clan.getRank();
		rep = clan.getReputationScore();
		ally_id = clan.getAllyId();
		if (clan.getAlliance() != null)
		{
			ally_name = clan.getAlliance().getAllyName();
			ally_crest_id = clan.getAlliance().getAllyCrestId();
		}
		else
		{
			ally_name = "";
			ally_crest_id = 0;
		}
		atWar = clan.isAtWar();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x96);
		writeS(char_name);
		writeD(clan_id);
		writeD(0x00);
		writeS(clan_name);
		writeS(leader_name);
		writeD(clan_crest_id);
		writeD(clan_level);
		writeD(hasCastle);
		writeD(hasHideout);
		writeD(hasFortress);
		writeD(rank);
		writeD(rep);
		writeD(0);
		writeD(0);
		writeD(ally_id);
		writeS(ally_name);
		writeD(ally_crest_id);
		writeD(atWar);
		writeD(0);
		writeD(infos.size());
		for (PledgeMemberInfo _info : infos)
		{
			writeS(_info._name);
			writeD(_info.level);
			writeD(_info.class_id);
			writeD(_info.sex);
			writeD(_info.race);
			writeD(_info.online);
			writeD(_info.sponsor);
		}
		infos.clear();
	}
	
	/**
	 * @author Mobius
	 */
	static class PledgeMemberInfo
	{
		/**
		 * Field _name.
		 */
		public String _name;
		/**
		 * Field sponsor.
		 */
		/**
		 * Field race.
		 */
		/**
		 * Field sex.
		 */
		/**
		 * Field online.
		 */
		/**
		 * Field class_id.
		 */
		/**
		 * Field level.
		 */
		public int level, class_id, online, sex, race, sponsor;
		
		/**
		 * Constructor for PledgeMemberInfo.
		 * @param __name String
		 * @param _level int
		 * @param _class_id int
		 * @param _online int
		 * @param _sex int
		 * @param _race int
		 * @param _sponsor int
		 */
		public PledgeMemberInfo(String __name, int _level, int _class_id, int _online, int _sex, int _race, int _sponsor)
		{
			_name = __name;
			level = _level;
			class_id = _class_id;
			online = _online;
			sex = _sex;
			race = _race;
			sponsor = _sponsor;
		}
	}
}
