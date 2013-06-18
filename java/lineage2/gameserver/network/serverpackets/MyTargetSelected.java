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

import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.MonsterInstance;

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
	final int _color;
	final int _obj;
	
	public MyTargetSelected(final Player player, final GameObject obj)
	{
		_obj = obj.getObjectId();
		_color = obj.isMonster() ? player.getLevel() - ((MonsterInstance) obj).getLevel() : 0;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xb9);
		writeD(_obj);
		writeH(_color);
		writeD(0x00);
	}
}
