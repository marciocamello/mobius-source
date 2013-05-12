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

public class ExMpccRoomInfo extends L2GameServerPacket
{
	private final int _index;
	private final int _memberSize;
	private final int _minLevel;
	private final int _maxLevel;
	private final int _lootType;
	private final int _locationId;
	private final String _topic;
	
	public ExMpccRoomInfo(MatchingRoom matching)
	{
		_index = matching.getId();
		_locationId = matching.getLocationId();
		_topic = matching.getTopic();
		_minLevel = matching.getMinLevel();
		_maxLevel = matching.getMaxLevel();
		_memberSize = matching.getMaxMembersSize();
		_lootType = matching.getLootType();
	}
	
	@Override
	public void writeImpl()
	{
		writeEx(0x9C);
		//
		writeD(_index); // index
		writeD(_memberSize); // member size 1-50
		writeD(_minLevel); // min level
		writeD(_maxLevel); // max level
		writeD(_lootType); // loot type
		writeD(_locationId); // location id as party room
		writeS(_topic); // topic
	}
}