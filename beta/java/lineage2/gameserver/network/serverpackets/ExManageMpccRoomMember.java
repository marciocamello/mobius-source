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
 * @author VISTALL
 */
public class ExManageMpccRoomMember extends L2GameServerPacket
{
	public static final int ADD_MEMBER = 0;
	public static final int UPDATE_MEMBER = 1;
	public static final int REMOVE_MEMBER = 2;
	private final int _type;
	private final MpccRoomMemberInfo _memberInfo;
	
	public ExManageMpccRoomMember(int type, MatchingRoom room, Player target)
	{
		_type = type;
		_memberInfo = new MpccRoomMemberInfo(target, room.getMemberType(target));
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x0A);
		writeD(_type);
		writeD(_memberInfo.objectId);
		writeS(_memberInfo.name);
		writeD(_memberInfo.classId);
		writeD(_memberInfo.level);
		writeD(_memberInfo.location);
		writeD(_memberInfo.memberType);
	}
	
	private static class MpccRoomMemberInfo
	{
		final int objectId;
		final int classId;
		final int level;
		final int location;
		final int memberType;
		final String name;
		
		MpccRoomMemberInfo(Player member, int type)
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