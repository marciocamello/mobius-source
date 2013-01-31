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
public class ExCubeGameExtendedChangePoints extends L2GameServerPacket
{
	/**
	 * Field _timeLeft.
	 */
	private final int _timeLeft;
	/**
	 * Field _bluePoints.
	 */
	private final int _bluePoints;
	/**
	 * Field _redPoints.
	 */
	private final int _redPoints;
	/**
	 * Field _isRedTeam.
	 */
	private final boolean _isRedTeam;
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _playerPoints.
	 */
	private final int _playerPoints;
	
	/**
	 * Constructor for ExCubeGameExtendedChangePoints.
	 * @param timeLeft int
	 * @param bluePoints int
	 * @param redPoints int
	 * @param isRedTeam boolean
	 * @param player Player
	 * @param playerPoints int
	 */
	public ExCubeGameExtendedChangePoints(int timeLeft, int bluePoints, int redPoints, boolean isRedTeam, Player player, int playerPoints)
	{
		_timeLeft = timeLeft;
		_bluePoints = bluePoints;
		_redPoints = redPoints;
		_isRedTeam = isRedTeam;
		_objectId = player.getObjectId();
		_playerPoints = playerPoints;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x98);
		writeD(0x00);
		writeD(_timeLeft);
		writeD(_bluePoints);
		writeD(_redPoints);
		writeD(_isRedTeam ? 0x01 : 0x00);
		writeD(_objectId);
		writeD(_playerPoints);
	}
}
