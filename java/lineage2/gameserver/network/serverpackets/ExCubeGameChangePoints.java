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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExCubeGameChangePoints extends L2GameServerPacket
{
	/**
	 * Field _timeLeft.
	 */
	int _timeLeft;
	/**
	 * Field _bluePoints.
	 */
	int _bluePoints;
	/**
	 * Field _redPoints.
	 */
	int _redPoints;
	
	/**
	 * Constructor for ExCubeGameChangePoints.
	 * @param timeLeft int
	 * @param bluePoints int
	 * @param redPoints int
	 */
	public ExCubeGameChangePoints(int timeLeft, int bluePoints, int redPoints)
	{
		_timeLeft = timeLeft;
		_bluePoints = bluePoints;
		_redPoints = redPoints;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x98);
		writeD(0x02);
		writeD(_timeLeft);
		writeD(_bluePoints);
		writeD(_redPoints);
	}
}
