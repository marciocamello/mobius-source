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
 * <p/>
 * sample b9 73 5d 30 49 01 00 00 00 00 00
 * <p/>
 * format dhd (objectid, color, unk)
 * <p/>
 * color -xx -> -9 red
 * <p>
 * -8 -> -6 light-red
 * <p>
 * -5 -> -3 yellow
 * <p>
 * -2 -> 2 white
 * <p>
 * 3 -> 5 green
 * <p>
 * 6 -> 8 light-blue
 * <p>
 * 9 -> xx blue
 * <p>
 * <p/>
 * usually the color equals the level difference to the selected target
 */
public class MyTargetSelected extends L2GameServerPacket
{
	private final int _objectId;
	private final int _color;
	
	/**
	 * @param objectId of the target
	 * @param color
	 */
	public MyTargetSelected(int objectId, int color)
	{
		_objectId = objectId;
		_color = color;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xb9);
		writeD(_objectId);
		writeH(_color);
		writeD(0x00);
	}
}