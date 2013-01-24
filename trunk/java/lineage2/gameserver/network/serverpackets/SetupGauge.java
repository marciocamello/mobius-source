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

import lineage2.gameserver.model.Creature;

public class SetupGauge extends L2GameServerPacket
{
	public static final int BLUE_DUAL = 0;
	public static final int BLUE = 1;
	public static final int BLUE_MINI = 2;
	public static final int GREEN_MINI = 3;
	public static final int RED_MINI = 4;
	private final int _charId;
	private final int _dat1;
	private int _time;
	
	public static enum Colors
	{
		BLUE_DUAL,
		BLUE,
		BLUE_MINI,
		GREEN_MINI,
		RED_MINI
	}
	
	public SetupGauge(Creature character, int dat1, int time)
	{
		_charId = character.getObjectId();
		_dat1 = dat1;
		_time = time;
	}
	
	public SetupGauge(Creature character, Colors color, int time, int lostTime)
	{
		_charId = character.getObjectId();
		_dat1 = color.ordinal();
		_time = time;
		_time = lostTime;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x6b);
		writeD(_charId);
		writeD(_dat1);
		writeD(_time);
		writeD(_time);
	}
}
