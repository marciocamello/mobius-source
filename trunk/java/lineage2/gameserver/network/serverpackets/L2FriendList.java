/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.instances.player.Friend;

public class L2FriendList extends L2GameServerPacket
{
	private List<FriendInfo> _list = Collections.emptyList();
	
	public L2FriendList(Player player)
	{
		Map<Integer, Friend> list = player.getFriendList().getList();
		_list = new ArrayList<>(list.size());
		for (Map.Entry<Integer, Friend> entry : list.entrySet())
		{
			FriendInfo f = new FriendInfo();
			f._objectId = entry.getKey();
			f._name = entry.getValue().getName();
			f._online = entry.getValue().isOnline();
			f._lvl = entry.getValue().getLevel();
			f._classId = entry.getValue().getClassId();
			_list.add(f);
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x75);
		writeD(_list.size());
		for (FriendInfo friendInfo : _list)
		{
			writeD(friendInfo._objectId);
			writeS(friendInfo._name);
			writeD(friendInfo._online);
			writeD(friendInfo._online ? friendInfo._objectId : 0);
			writeD(friendInfo._lvl);
			writeD(friendInfo._classId);
			writeH(0);
		}
	}
	
	private static class FriendInfo
	{
		public FriendInfo()
		{
			// TODO Auto-generated constructor stub
		}
		
		int _objectId;
		String _name;
		boolean _online;
		int _lvl;
		int _classId;
	}
}
