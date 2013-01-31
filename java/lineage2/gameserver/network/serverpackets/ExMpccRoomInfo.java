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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExMpccRoomInfo extends L2GameServerPacket
{
	/**
	 * Field _index.
	 */
	private final int _index;
	/**
	 * Field _memberSize.
	 */
	private final int _memberSize;
	/**
	 * Field _minLevel.
	 */
	private final int _minLevel;
	/**
	 * Field _maxLevel.
	 */
	private final int _maxLevel;
	/**
	 * Field _lootType.
	 */
	private final int _lootType;
	/**
	 * Field _locationId.
	 */
	private final int _locationId;
	/**
	 * Field _topic.
	 */
	private final String _topic;
	
	/**
	 * Constructor for ExMpccRoomInfo.
	 * @param matching MatchingRoom
	 */
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
	
	/**
	 * Method writeImpl.
	 */
	@Override
	public void writeImpl()
	{
		writeEx(0x9B);
		writeD(_index);
		writeD(_memberSize);
		writeD(_minLevel);
		writeD(_maxLevel);
		writeD(_lootType);
		writeD(_locationId);
		writeS(_topic);
	}
}
