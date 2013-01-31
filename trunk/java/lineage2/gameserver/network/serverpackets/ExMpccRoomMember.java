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

import lineage2.gameserver.instancemanager.MatchingRoomManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.matching.MatchingRoom;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExMpccRoomMember extends L2GameServerPacket
{
	/**
	 * Field _type.
	 */
	private final int _type;
	/**
	 * Field _members.
	 */
	private List<MpccRoomMemberInfo> _members = Collections.emptyList();
	
	/**
	 * Constructor for ExMpccRoomMember.
	 * @param room MatchingRoom
	 * @param player Player
	 */
	public ExMpccRoomMember(MatchingRoom room, Player player)
	{
		_type = room.getMemberType(player);
		_members = new ArrayList<>(room.getPlayers().size());
		for (Player member : room.getPlayers())
		{
			_members.add(new MpccRoomMemberInfo(member, room.getMemberType(member)));
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	public void writeImpl()
	{
		writeEx(0x9F);
		writeD(_type);
		writeD(_members.size());
		for (MpccRoomMemberInfo member : _members)
		{
			writeD(member.objectId);
			writeS(member.name);
			writeD(member.level);
			writeD(member.classId);
			writeD(member.location);
			writeD(member.memberType);
		}
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
