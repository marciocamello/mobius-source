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
public class PartyRoomInfo extends L2GameServerPacket
{
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _minLevel.
	 */
	private final int _minLevel;
	/**
	 * Field _maxLevel.
	 */
	private final int _maxLevel;
	/**
	 * Field _lootDist.
	 */
	private final int _lootDist;
	/**
	 * Field _maxMembers.
	 */
	private final int _maxMembers;
	/**
	 * Field _location.
	 */
	private final int _location;
	/**
	 * Field _title.
	 */
	private final String _title;
	
	/**
	 * Constructor for PartyRoomInfo.
	 * @param room MatchingRoom
	 */
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
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x9d);
		writeD(_id);
		writeD(_maxMembers);
		writeD(_minLevel);
		writeD(_maxLevel);
		writeD(_lootDist);
		writeD(_location);
		writeS(_title);
	}
}
