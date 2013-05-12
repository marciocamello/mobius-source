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

public class ExRegenMax extends L2GameServerPacket
{
	private final double _max;
	private final int _count;
	private final int _time;
	
	public ExRegenMax(double max, int count, int time)
	{
		_max = max * .66;
		_count = count;
		_time = time;
	}
	
	public static final int POTION_HEALING_GREATER = 16457;
	public static final int POTION_HEALING_MEDIUM = 16440;
	public static final int POTION_HEALING_LESSER = 16416;
	
	/**
	 * Пример пакета - Пришло после использования Healing Potion (инфа для Interlude, в Kamael пакет не изменился)
	 * <p/>
	 * FE 01 00 01 00 00 00 0F 00 00 00 03 00 00 00 00 00 00 00 00 00 38 40 // Healing Potion FE 01 00 01 00 00 00 0F 00 00 00 03 00 00 00 00 00 00 00 00 00 49 40 // Greater Healing Potion FE 01 00 01 00 00 00 0F 00 00 00 03 00 00 00 00 00 00 00 00 00 20 40 // Lesser Healing Potion
	 * <p/>
	 * FE - тип 01 00 - субтип 01 00 00 00 - хз что 0F 00 00 00 - count? 03 00 00 00 - время? 00 00 00 00 00 00 38 40 - максимум?
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