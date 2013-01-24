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
import lineage2.gameserver.model.actor.instances.player.Mentee;

public class ExMentorList extends L2GameServerPacket
{
	private List<MenteeInfo> _list = Collections.emptyList();
	private final int _mentor;
	
	public ExMentorList(Player player)
	{
		_mentor = player.getMenteeList().getMentor();
		Map<Integer, Mentee> list = player.getMenteeList().getList();
		_list = new ArrayList<>(list.size());
		for (Map.Entry<Integer, Mentee> entry : list.entrySet())
		{
			MenteeInfo m = new MenteeInfo();
			m.objectId = entry.getKey();
			m.name = entry.getValue().getName();
			m.online = entry.getValue().isOnline();
			m.level = entry.getValue().getLevel();
			m.classId = entry.getValue().getClassId();
			_list.add(m);
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x120);
		writeD(_mentor == 0 ? 0x01 : 0x02);
		writeD(_list.size());
		for (MenteeInfo entry : _list)
		{
			writeD(entry.objectId);
			writeS(entry.name);
			writeD(entry.classId);
			writeD(entry.level);
			writeD(entry.online);
		}
	}
	
	private class MenteeInfo
	{
		public MenteeInfo()
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
