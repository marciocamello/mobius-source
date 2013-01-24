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

public class PledgeShowMemberListAdd extends L2GameServerPacket
{
	private final PledgePacketMember _member;
	
	public PledgeShowMemberListAdd(UnitMember member)
	{
		_member = new PledgePacketMember(member);
	}
	
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
	
	private class PledgePacketMember
	{
		final String _name;
		final int _level;
		final int _classId;
		final int _sex;
		final int _race;
		final int _online;
		final int _pledgeType;
		
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
