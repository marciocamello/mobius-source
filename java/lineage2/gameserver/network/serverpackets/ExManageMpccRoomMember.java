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

import lineage2.gameserver.instancemanager.MatchingRoomManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.matching.MatchingRoom;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExManageMpccRoomMember extends L2GameServerPacket
{
	/**
	 * Field ADD_MEMBER.
	 */
	public static int ADD_MEMBER = 0;
	/**
	 * Field UPDATE_MEMBER.
	 */
	public static int UPDATE_MEMBER = 1;
	/**
	 * Field REMOVE_MEMBER.
	 */
	public static int REMOVE_MEMBER = 2;
	/**
	 * Field _type.
	 */
	private final int _type;
	/**
	 * Field _memberInfo.
	 */
	private final MpccRoomMemberInfo _memberInfo;
	
	/**
	 * Constructor for ExManageMpccRoomMember.
	 * @param type int
	 * @param room MatchingRoom
	 * @param target Player
	 */
	public ExManageMpccRoomMember(int type, MatchingRoom room, Player target)
	{
		_type = type;
		_memberInfo = (new MpccRoomMemberInfo(target, room.getMemberType(target)));
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x9E);
		writeD(_type);
		writeD(_memberInfo.objectId);
		writeS(_memberInfo.name);
		writeD(_memberInfo.level);
		writeD(_memberInfo.classId);
		writeD(_memberInfo.location);
		writeD(_memberInfo.memberType);
	}
	
	/**
	 * @author Mobius
	 */
	static class MpccRoomMemberInfo
	{
		/**
		 * Field objectId.
		 */
		public final int objectId;
		/**
		 * Field classId.
		 */
		public final int classId;
		/**
		 * Field level.
		 */
		public final int level;
		/**
		 * Field location.
		 */
		public final int location;
		/**
		 * Field memberType.
		 */
		public final int memberType;
		/**
		 * Field name.
		 */
		public final String name;
		
		/**
		 * Constructor for MpccRoomMemberInfo.
		 * @param member Player
		 * @param type int
		 */
		public MpccRoomMemberInfo(Player member, int type)
		{
			objectId = member.getObjectId();
			name = member.getName();
			classId = member.getClassId().ordinal();
			level = member.getLevel();
			location = MatchingRoomManager.getInstance().getLocation(member);
			memberType = type;
		}
	}
}
