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
import lineage2.gameserver.model.entity.Reflection;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExListPartyMatchingWaitingRoom extends L2GameServerPacket
{
	/**
	 * Field _waitingList.
	 */
	private List<PartyMatchingWaitingInfo> _waitingList = Collections.emptyList();
	/**
	 * Field _fullSize.
	 */
	private final int _fullSize;
	
	/**
	 * Constructor for ExListPartyMatchingWaitingRoom.
	 * @param searcher Player
	 * @param minLevel int
	 * @param maxLevel int
	 * @param page int
	 * @param classes int[]
	 */
	public ExListPartyMatchingWaitingRoom(Player searcher, int minLevel, int maxLevel, int page, int[] classes)
	{
		int first = (page - 1) * 64;
		int firstNot = page * 64;
		int i = 0;
		List<Player> temp = MatchingRoomManager.getInstance().getWaitingList(minLevel, maxLevel, classes);
		_fullSize = temp.size();
		_waitingList = new ArrayList<>(_fullSize);
		for (Player pc : temp)
		{
			if ((i < first) || (i >= firstNot))
			{
				continue;
			}
			_waitingList.add(new PartyMatchingWaitingInfo(pc));
			i++;
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x36);
		writeD(_fullSize);
		writeD(_waitingList.size());
		for (PartyMatchingWaitingInfo waiting_info : _waitingList)
		{
			writeS(waiting_info.name);
			writeD(waiting_info.classId);
			writeD(waiting_info.level);
			writeD(waiting_info.currentInstance);
			writeD(waiting_info.instanceReuses.length);
			for (int i : waiting_info.instanceReuses)
			{
				writeD(i);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class PartyMatchingWaitingInfo
	{
		/**
		 * Field currentInstance.
		 */
		/**
		 * Field level.
		 */
		/**
		 * Field classId.
		 */
		public final int classId, level, currentInstance;
		/**
		 * Field name.
		 */
		public final String name;
		/**
		 * Field instanceReuses.
		 */
		public final int[] instanceReuses;
		
		/**
		 * Constructor for PartyMatchingWaitingInfo.
		 * @param member Player
		 */
		public PartyMatchingWaitingInfo(Player member)
		{
			name = member.getName();
			classId = member.getClassId().getId();
			level = member.getLevel();
			Reflection ref = member.getReflection();
			currentInstance = ref == null ? 0 : ref.getInstancedZoneId();
			instanceReuses = ArrayUtils.toArray(member.getInstanceReuses().keySet());
		}
	}
}
