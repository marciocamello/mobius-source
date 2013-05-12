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

/**
 * Format:(c) dddddds
 */
public class ListPartyWaiting extends L2GameServerPacket
{
	private final Collection<MatchingRoom> _rooms;
	private final int _fullSize;
	
	public ListPartyWaiting(int region, boolean allLevels, int page, Player activeChar)
	{
		int first = (page - 1) * 64;
		int firstNot = page * 64;
		_rooms = new ArrayList<>();
		
		int i = 0;
		List<MatchingRoom> temp = MatchingRoomManager.getInstance().getMatchingRooms(MatchingRoom.PARTY_MATCHING, region, allLevels, activeChar);
		_fullSize = temp.size();
		for (MatchingRoom room : temp)
		{
			if ((i < first) || (i >= firstNot))
			{
				continue;
			}
			_rooms.add(room);
			i++;
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x9c);
		writeD(_fullSize);
		writeD(_rooms.size());
		
		for (MatchingRoom room : _rooms)
		{
			writeD(room.getId()); // room id
			writeS(room.getLeader() == null ? "None" : room.getLeader().getName());
			writeD(room.getLocationId());
			writeD(room.getMinLevel()); // min level
			writeD(room.getMaxLevel()); // max level
			writeD(room.getMaxMembersSize()); // max members coun
			writeS(room.getTopic()); // room name
			
			Collection<Player> players = room.getPlayers();
			writeD(players.size()); // members count
			for (Player player : players)
			{
				writeD(player.getClassId().getId());
				writeS(player.getName());
			}
		}
	}
}