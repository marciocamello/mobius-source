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

import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.instancemanager.MatchingRoomManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.matching.MatchingRoom;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExPartyRoomMember extends L2GameServerPacket
{
	/**
	 * Field _type.
	 */
	private final int _type;
	/**
	 * Field _members.
	 */
	private List<PartyRoomMemberInfo> _members = Collections.emptyList();
	
	/**
	 * Constructor for ExPartyRoomMember.
	 * @param room MatchingRoom
	 * @param activeChar Player
	 */
	public ExPartyRoomMember(MatchingRoom room, Player activeChar)
	{
		_type = room.getMemberType(activeChar);
		_members = new ArrayList<>(room.getPlayers().size());
		for (Player $member : room.getPlayers())
		{
			_members.add(new PartyRoomMemberInfo($member, room.getMemberType($member)));
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x08);
		writeD(_type);
		writeD(_members.size());
		for (PartyRoomMemberInfo member_info : _members)
		{
			writeD(member_info.objectId);
			writeS(member_info.name);
			writeD(member_info.classId);
			writeD(member_info.level);
			writeD(member_info.location);
			writeD(member_info.memberType);
			writeD(member_info.instanceReuses.length);
			for (int i : member_info.instanceReuses)
			{
				writeD(i);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class PartyRoomMemberInfo
	{
		/**
		 * Field memberType.
		 */
		/**
		 * Field location.
		 */
		/**
		 * Field level.
		 */
		/**
		 * Field classId.
		 */
		/**
		 * Field objectId.
		 */
		public final int objectId, classId, level, location, memberType;
		/**
		 * Field name.
		 */
		public final String name;
		/**
		 * Field instanceReuses.
		 */
		public final int[] instanceReuses;
		
		/**
		 * Constructor for PartyRoomMemberInfo.
		 * @param member Player
		 * @param type int
		 */
		public PartyRoomMemberInfo(Player member, int type)
		{
			objectId = member.getObjectId();
			name = member.getName();
			classId = member.getClassId().ordinal();
			level = member.getLevel();
			location = MatchingRoomManager.getInstance().getLocation(member);
			memberType = type;
			instanceReuses = ArrayUtils.toArray(member.getInstanceReuses().keySet());
		}
	}
}