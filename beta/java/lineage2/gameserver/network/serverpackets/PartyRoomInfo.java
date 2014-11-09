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

import lineage2.gameserver.model.matching.MatchingRoom;

public class PartyRoomInfo extends L2GameServerPacket
{
	private final int _id;
	private final int _minLevel;
	private final int _maxLevel;
	private final int _lootDist;
	private final int _maxMembers;
	private final int _location;
	private final String _title;
	
	public PartyRoomInfo(MatchingRoom room)
	{
		_id = room.getId();
		_minLevel = room.getMinLevel();
		_maxLevel = room.getMaxLevel();
		_lootDist = room.getLootType();
		_maxMembers = room.getMaxMembersSize();
		_location = room.getLocationId();
		_title = room.getTopic();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x9d);
		writeD(_id); // room id
		writeD(_maxMembers); // max members
		writeD(_minLevel); // min level
		writeD(_maxLevel); // max level
		writeD(_lootDist); // loot distribution 1-Random 2-Random includ. etc
		writeD(_location); // location
		writeS(_title); // room name
		writeH(0x105); // 603 // Unknown
	}
}