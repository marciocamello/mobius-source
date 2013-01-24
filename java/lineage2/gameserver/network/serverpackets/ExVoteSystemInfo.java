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

public class ExVoteSystemInfo extends L2GameServerPacket
{
	private final int _receivedRec, _givingRec, _time, _bonusPercent;
	private final boolean _showTimer;
	
	public ExVoteSystemInfo(Player player)
	{
		_receivedRec = player.getRecomLeft();
		_givingRec = player.getRecomHave();
		_time = player.getRecomBonusTime();
		_bonusPercent = player.getRecomBonus();
		_showTimer = !player.isRecomTimerActive() || player.isHourglassEffected();
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xC9);
		writeD(_receivedRec);
		writeD(_givingRec);
		writeD(_time);
		writeD(_bonusPercent);
		writeD(_showTimer ? 0x01 : 0x00);
	}
}
