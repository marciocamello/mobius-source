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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PledgeShowMemberListUpdate extends L2GameServerPacket
{
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _lvl.
	 */
	private final int _lvl;
	/**
	 * Field _classId.
	 */
	private final int _classId;
	/**
	 * Field _sex.
	 */
	private final int _sex;
	/**
	 * Field _isOnline.
	 */
	private final int _isOnline;
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _pledgeType.
	 */
	private final int _pledgeType;
	/**
	 * Field _isApprentice.
	 */
	private int _isApprentice;
	
	/**
	 * Constructor for PledgeShowMemberListUpdate.
	 * @param player Player
	 */
	public PledgeShowMemberListUpdate(final Player player)
	{
		_name = player.getName();
		_lvl = player.getLevel();
		_classId = player.getClassId().getId();
		_sex = player.getSex();
		_objectId = player.getObjectId();
		_isOnline = player.isOnline() ? 1 : 0;
		_pledgeType = player.getPledgeType();
		SubUnit subUnit = player.getSubUnit();
		UnitMember member = subUnit == null ? null : subUnit.getUnitMember(_objectId);
		if (member != null)
		{
			_isApprentice = member.hasSponsor() ? 1 : 0;
		}
	}
	
	/**
	 * Constructor for PledgeShowMemberListUpdate.
	 * @param cm UnitMember
	 */
	public PledgeShowMemberListUpdate(final UnitMember cm)
	{
		_name = cm.getName();
		_lvl = cm.getLevel();
		_classId = cm.getClassId();
		_sex = cm.getSex();
		_objectId = cm.getObjectId();
		_isOnline = cm.isOnline() ? 1 : 0;
		_pledgeType = cm.getPledgeType();
		_isApprentice = cm.hasSponsor() ? 1 : 0;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x5b);
		writeS(_name);
		writeD(_lvl);
		writeD(_classId);
		writeD(_sex);
		writeD(_objectId);
		writeD(_isOnline);
		writeD(_pledgeType);
		writeD(_isApprentice);
	}
}
