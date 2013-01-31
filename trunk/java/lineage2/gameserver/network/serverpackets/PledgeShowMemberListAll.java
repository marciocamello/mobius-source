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

import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.SubUnit;
import lineage2.gameserver.model.pledge.UnitMember;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PledgeShowMemberListAll extends L2GameServerPacket
{
	/**
	 * Field _reputation. Field _rank. Field _level. Field _clanCrestId. Field _clanObjectId.
	 */
	private final int _clanObjectId, _clanCrestId, _level, _rank, _reputation;
	/**
	 * Field _allianceObjectId.
	 */
	private int _allianceObjectId;
	/**
	 * Field _allianceCrestId.
	 */
	private int _allianceCrestId;
	/**
	 * Field _atClanWar. Field _hasFortress. Field _hasClanHall. Field _hasCastle.
	 */
	private final int _hasCastle, _hasClanHall, _hasFortress, _atClanWar;
	/**
	 * Field _leaderName. Field _unitName.
	 */
	private final String _unitName, _leaderName;
	/**
	 * Field _allianceName.
	 */
	private String _allianceName;
	/**
	 * Field _territorySide. Field _pledgeType.
	 */
	private final int _pledgeType, _territorySide;
	/**
	 * Field _members.
	 */
	private final List<PledgePacketMember> _members;
	
	/**
	 * Constructor for PledgeShowMemberListAll.
	 * @param clan Clan
	 * @param sub SubUnit
	 */
	public PledgeShowMemberListAll(Clan clan, final SubUnit sub)
	{
		_pledgeType = sub.getType();
		_clanObjectId = clan.getClanId();
		_unitName = sub.getName();
		_leaderName = sub.getLeaderName();
		_clanCrestId = clan.getCrestId();
		_level = clan.getLevel();
		_hasCastle = clan.getCastle();
		_hasClanHall = clan.getHasHideout();
		_hasFortress = clan.getHasFortress();
		_rank = clan.getRank();
		_reputation = clan.getReputationScore();
		_atClanWar = clan.isAtWarOrUnderAttack();
		_territorySide = clan.getWarDominion();
		Alliance ally = clan.getAlliance();
		if (ally != null)
		{
			_allianceObjectId = ally.getAllyId();
			_allianceName = ally.getAllyName();
			_allianceCrestId = ally.getAllyCrestId();
		}
		_members = new ArrayList<>(sub.size());
		for (UnitMember m : sub.getUnitMembers())
		{
			_members.add(new PledgePacketMember(m));
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x5a);
		writeD(_pledgeType == Clan.SUBUNIT_MAIN_CLAN ? 0 : 1);
		writeD(_clanObjectId);
		writeD(_pledgeType);
		writeS(_unitName);
		writeS(_leaderName);
		writeD(_clanCrestId);
		writeD(_level);
		writeD(_hasCastle);
		writeD(_hasClanHall);
		writeD(_hasFortress);
		writeD(_rank);
		writeD(_reputation);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(_allianceObjectId);
		writeS(_allianceName);
		writeD(_allianceCrestId);
		writeD(_atClanWar);
		writeD(_territorySide);
		writeD(_members.size());
		for (PledgePacketMember m : _members)
		{
			writeS(m._name);
			writeD(m._level);
			writeD(m._classId);
			writeD(m._sex);
			writeD(m._race);
			writeD(m._online);
			writeD(m._hasSponsor ? 1 : 0);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class PledgePacketMember
	{
		/**
		 * Field _name.
		 */
		final String _name;
		/**
		 * Field _level.
		 */
		final int _level;
		/**
		 * Field _classId.
		 */
		final int _classId;
		/**
		 * Field _sex.
		 */
		final int _sex;
		/**
		 * Field _race.
		 */
		final int _race;
		/**
		 * Field _online.
		 */
		final int _online;
		/**
		 * Field _hasSponsor.
		 */
		final boolean _hasSponsor;
		
		/**
		 * Constructor for PledgePacketMember.
		 * @param m UnitMember
		 */
		public PledgePacketMember(UnitMember m)
		{
			_name = m.getName();
			_level = m.getLevel();
			_classId = m.getClassId();
			_sex = m.getSex();
			_race = 0;
			_online = m.isOnline() ? m.getObjectId() : 0;
			_hasSponsor = m.getSponsor() != 0;
		}
	}
}
