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
public class ExCubeGameAddPlayer extends L2GameServerPacket
{
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _isRedTeam.
	 */
	boolean _isRedTeam;
	
	/**
	 * Constructor for ExCubeGameAddPlayer.
	 * @param player Player
	 * @param isRedTeam boolean
	 */
	public ExCubeGameAddPlayer(Player player, boolean isRedTeam)
	{
		_objectId = player.getObjectId();
		_name = player.getName();
		_isRedTeam = isRedTeam;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x97);
		writeD(0x01);
		writeD(0xffffffff);
		writeD(_isRedTeam ? 0x01 : 0x00);
		writeD(_objectId);
		writeS(_name);
	}
}
