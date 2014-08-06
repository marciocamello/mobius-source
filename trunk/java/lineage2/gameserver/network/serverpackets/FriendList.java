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
import java.util.Map;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.instances.player.Friend;

/**
 * @author VISTALL
 * @date 23:37/22.03.2011
 */
public class FriendList extends L2GameServerPacket
{
	private List<FriendInfo> _friends = Collections.emptyList();
	
	public FriendList(Player player)
	{
		Map<Integer, Friend> friends = player.getFriendList().getList();
		_friends = new ArrayList<>(friends.size());
		
		for (Map.Entry<Integer, Friend> entry : friends.entrySet())
		{
			Friend friend = entry.getValue();
			FriendInfo f = new FriendInfo();
			f.name = friend.getName();
			f.classId = friend.getClassId();
			f.objectId = entry.getKey();
			f.level = friend.getLevel();
			f.online = friend.isOnline();
			_friends.add(f);
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0x75);
		writeD(_friends.size());
		
		for (FriendInfo f : _friends)
		{
			writeD(0x00);
			writeS(f.name);
			writeD(f.online);
			
			if (f.online)
			{
				writeD(f.objectId);
				writeD(f.level);
				writeD(f.classId);
			}
			else
			{
				writeD(0);
				writeD(f.classId);
				writeD(f.level);
			}
			
			writeS(f.name);
		}
	}
	
	private class FriendInfo
	{
		/**
		 *
		 */
		public FriendInfo()
		{
			// TODO Auto-generated constructor stub
		}
		
		String name;
		int objectId;
		boolean online;
		int level;
		int classId;
	}
}
