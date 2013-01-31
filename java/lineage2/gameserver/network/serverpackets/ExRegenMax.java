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
public class ExRegenMax extends L2GameServerPacket
{
	/**
	 * Field _max.
	 */
	private final double _max;
	/**
	 * Field _count.
	 */
	private final int _count;
	/**
	 * Field _time.
	 */
	private final int _time;
	
	/**
	 * Constructor for ExRegenMax.
	 * @param max double
	 * @param count int
	 * @param time int
	 */
	public ExRegenMax(double max, int count, int time)
	{
		_max = max * .66;
		_count = count;
		_time = time;
	}
	
	/**
	 * Field POTION_HEALING_GREATER. (value is 16457)
	 */
	public static final int POTION_HEALING_GREATER = 16457;
	/**
	 * Field POTION_HEALING_MEDIUM. (value is 16440)
	 */
	public static final int POTION_HEALING_MEDIUM = 16440;
	/**
	 * Field POTION_HEALING_LESSER. (value is 16416)
	 */
	public static final int POTION_HEALING_LESSER = 16416;
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x01);
		writeD(1);
		writeD(_count);
		writeD(_time);
		writeF(_max);
	}
}
