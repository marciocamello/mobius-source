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

import lineage2.gameserver.model.pledge.UnitMember;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PledgeShowMemberListAdd extends L2GameServerPacket
{
	/**
	 * Field _member.
	 */
	private final PledgePacketMember _member;
	
	/**
	 * Constructor for PledgeShowMemberListAdd.
	 * @param member UnitMember
	 */
	public PledgeShowMemberListAdd(UnitMember member)
	{
		_member = new PledgePacketMember(member);
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x5c);
		writeS(_member._name);
		writeD(_member._level);
		writeD(_member._classId);
		writeD(_member._sex);
		writeD(_member._race);
		writeD(_member._online);
		writeD(_member._pledgeType);
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
		 * Field _pledgeType.
		 */
		final int _pledgeType;
		
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
			_pledgeType = m.getPledgeType();
		}
	}
}
