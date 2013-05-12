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
import lineage2.gameserver.model.pledge.SubUnit;
import lineage2.gameserver.model.pledge.UnitMember;

public class PledgeShowMemberListUpdate extends L2GameServerPacket
{
	private final String _name;
	private final int _lvl;
	private final int _classId;
	private final int _sex;
	private final int _race;
	private final boolean _isOnline;
	private final int _objectId;
	private final int _pledgeType;
	private int _isApprentice;
	
	public PledgeShowMemberListUpdate(final Player player)
	{
		_name = player.getName();
		_lvl = player.getLevel();
		_classId = player.getClassId().getId();
		_sex = player.getSex();
		_race = player.getRace().ordinal();
		_objectId = player.getObjectId();
		_isOnline = player.isOnline();
		_pledgeType = player.getPledgeType();
		SubUnit subUnit = player.getSubUnit();
		UnitMember member = subUnit == null ? null : subUnit.getUnitMember(_objectId);
		if (member != null)
		{
			_isApprentice = member.hasSponsor() ? 1 : 0;
		}
	}
	
	public PledgeShowMemberListUpdate(final UnitMember cm)
	{
		_name = cm.getName();
		_lvl = cm.getLevel();
		_classId = cm.getClassId();
		_sex = cm.getSex();
		_race = cm.getRace();
		_objectId = cm.getObjectId();
		_isOnline = cm.isOnline();
		_pledgeType = cm.getPledgeType();
		_isApprentice = cm.hasSponsor() ? 1 : 0;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x5b);
		writeS(_name);
		writeD(_lvl);
		writeD(_classId);
		writeD(_sex);
		writeD(_race);
		if (_isOnline)
		{
			writeD(_objectId);
			writeD(_pledgeType);
		}
		else
		{
			writeD(0);
			writeD(0);
		}
		writeD(_isApprentice); // does a clan member have a sponsor
	}
}