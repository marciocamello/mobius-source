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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SetupGauge extends L2GameServerPacket
{
	/**
	 * Field BLUE_DUAL. (value is 0)
	 */
	public static final int BLUE_DUAL = 0;
	/**
	 * Field BLUE. (value is 1)
	 */
	public static final int BLUE = 1;
	/**
	 * Field BLUE_MINI. (value is 2)
	 */
	public static final int BLUE_MINI = 2;
	/**
	 * Field GREEN_MINI. (value is 3)
	 */
	public static final int GREEN_MINI = 3;
	/**
	 * Field RED_MINI. (value is 4)
	 */
	public static final int RED_MINI = 4;
	/**
	 * Field _charId.
	 */
	private final int _charId;
	/**
	 * Field _dat1.
	 */
	private final int _dat1;
	/**
	 * Field _time.
	 */
	private int _time;
	
	/**
	 * @author Mobius
	 */
	public static enum Colors
	{
		/**
		 * Field BLUE_DUAL.
		 */
		BLUE_DUAL,
		/**
		 * Field BLUE.
		 */
		BLUE,
		/**
		 * Field BLUE_MINI.
		 */
		BLUE_MINI,
		/**
		 * Field GREEN_MINI.
		 */
		GREEN_MINI,
		/**
		 * Field RED_MINI.
		 */
		RED_MINI
	}
	
	/**
	 * Constructor for SetupGauge.
	 * @param character Creature
	 * @param dat1 int
	 * @param time int
	 */
	public SetupGauge(Creature character, int dat1, int time)
	{
		_charId = character.getObjectId();
		_dat1 = dat1;
		_time = time;
	}
	
	/**
	 * Constructor for SetupGauge.
	 * @param character Creature
	 * @param color Colors
	 * @param time int
	 * @param lostTime int
	 */
	public SetupGauge(Creature character, Colors color, int time, int lostTime)
	{
		_charId = character.getObjectId();
		_dat1 = color.ordinal();
		_time = time;
		_time = lostTime;
	}
	
	/**
	 * Method writeImpl.
	 */
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
