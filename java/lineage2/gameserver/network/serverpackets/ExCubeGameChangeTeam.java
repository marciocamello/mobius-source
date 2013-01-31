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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExCubeGameChangeTeam extends L2GameServerPacket
{
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _fromRedTeam.
	 */
	private final boolean _fromRedTeam;
	
	/**
	 * Constructor for ExCubeGameChangeTeam.
	 * @param player Player
	 * @param fromRedTeam boolean
	 */
	public ExCubeGameChangeTeam(Player player, boolean fromRedTeam)
	{
		_objectId = player.getObjectId();
		_fromRedTeam = fromRedTeam;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x97);
		writeD(0x05);
		writeD(_objectId);
		writeD(_fromRedTeam ? 0x01 : 0x00);
		writeD(_fromRedTeam ? 0x00 : 0x01);
	}
}
