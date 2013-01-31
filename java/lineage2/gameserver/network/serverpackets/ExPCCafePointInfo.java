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
public class ExPCCafePointInfo extends L2GameServerPacket
{
	/**
	 * Field _remainTime. Field _pcBangPoints. Field _pointType. Field _mPeriodType. Field _mAddPoint.
	 */
	private final int _mAddPoint, _mPeriodType, _pointType, _pcBangPoints, _remainTime;
	
	/**
	 * Constructor for ExPCCafePointInfo.
	 * @param player Player
	 * @param mAddPoint int
	 * @param mPeriodType int
	 * @param pointType int
	 * @param remainTime int
	 */
	public ExPCCafePointInfo(Player player, int mAddPoint, int mPeriodType, int pointType, int remainTime)
	{
		_pcBangPoints = player.getPcBangPoints();
		_mAddPoint = mAddPoint;
		_mPeriodType = mPeriodType;
		_pointType = pointType;
		_remainTime = remainTime;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x32);
		writeD(_pcBangPoints);
		writeD(_mAddPoint);
		writeC(_mPeriodType);
		writeD(_remainTime);
		writeC(_pointType);
	}
}
