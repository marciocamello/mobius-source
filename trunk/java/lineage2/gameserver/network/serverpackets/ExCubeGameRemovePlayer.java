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

import lineage2.gameserver.model.Player;

/**
 * Format: (chd) ddd d: always -1 d: player team d: player object id
 */
public class ExCubeGameRemovePlayer extends L2GameServerPacket
{
	private final int _objectId;
	private final boolean _isRedTeam;
	
	public ExCubeGameRemovePlayer(Player player, boolean isRedTeam)
	{
		_objectId = player.getObjectId();
		_isRedTeam = isRedTeam;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x98);
		writeD(0x02);
		writeD(0xffffffff);
		writeD(_isRedTeam ? 0x01 : 0x00);
		writeD(_objectId);
	}
}