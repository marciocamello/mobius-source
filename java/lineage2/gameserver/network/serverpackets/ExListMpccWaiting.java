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
import java.util.Collection;
import java.util.List;

import lineage2.gameserver.instancemanager.MatchingRoomManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.matching.MatchingRoom;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExListMpccWaiting extends L2GameServerPacket
{
	/**
	 * Field PAGE_SIZE. (value is 10)
	 */
	private static final int PAGE_SIZE = 10;
	/**
	 * Field _fullSize.
	 */
	private final int _fullSize;
	/**
	 * Field _list.
	 */
	private final List<MatchingRoom> _list;
	
	/**
	 * Constructor for ExListMpccWaiting.
	 * @param player Player
	 * @param page int
	 * @param location int
	 * @param allLevels boolean
	 */
	public ExListMpccWaiting(Player player, int page, int location, boolean allLevels)
	{
		int first = (page - 1) * PAGE_SIZE;
		int firstNot = page * PAGE_SIZE;
		int i = 0;
		Collection<MatchingRoom> all = MatchingRoomManager.getInstance().getMatchingRooms(MatchingRoom.CC_MATCHING, location, allLevels, player);
		_fullSize = all.size();
		_list = new ArrayList<>(PAGE_SIZE);
		for (MatchingRoom c : all)
		{
			if ((i < first) || (i >= firstNot))
			{
				continue;
			}
			_list.add(c);
			i++;
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	public void writeImpl()
	{
		writeEx(0x9C);
		writeD(_fullSize);
		writeD(_list.size());
		for (MatchingRoom room : _list)
		{
			writeD(room.getId());
			writeS(room.getTopic());
			writeD(room.getPlayers().size());
			writeD(room.getMinLevel());
			writeD(room.getMaxLevel());
			writeD(1);
			writeD(room.getMaxMembersSize());
			Player leader = room.getLeader();
			writeS(leader == null ? StringUtils.EMPTY : leader.getName());
		}
	}
}
