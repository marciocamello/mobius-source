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
import lineage2.gameserver.model.WinnerStatueInstance;

/**
 * Пример дампа: 0000: 92 2c 05 10 58 77 bb 0f 00 00 00 00 00 00 00 00 .,..Xw.......... 0010: 54 ff ff b0 42 fe ff 14 ff ff ff 00 00 00 00 00 T...B........... 0020: 00 00 00 00 00 f0 3f 00 00 00 00 00 00 f0 3f 00 ......?.......?. 0030: 00 00 00 00 00 3e 40 00 00 00 00 00 00 3e 40 00 .....>@......>@.
 * 0040: 00 00 00 00 00 00 00 04 00 00 00 00 00 00 00 ...............
 */
public class ServerObjectInfo extends L2GameServerPacket
{
	private final int idTemplate;
	private final String name;
	private final boolean isAttackable;
	private final int x;
	private final int y;
	private final int z;
	private final int heading;
	private final double collisionRadius;
	private final double collisionHeight;
	private final int objectId;
	
	public ServerObjectInfo(WinnerStatueInstance statue, Creature actor)
	{
		objectId = statue.getObjectId();
		idTemplate = 1000000;
		name = statue.getTemplate().getName();
		isAttackable = statue.isAttackable(actor);
		x = statue.getX();
		y = statue.getY();
		z = statue.getZ();
		heading = statue.getHeading();
		collisionRadius = statue.getColRadius();
		collisionHeight = statue.getColHeight();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0x92);
		writeD(objectId);
		writeD(idTemplate + 1000000);
		writeS(name); // name
		writeD(isAttackable ? 1 : 0);
		writeD(x);
		writeD(y);
		writeD(z);
		writeD(heading);
		writeF(1.0); // movement multiplier
		writeF(1.0); // attack speed multiplier
		writeF(collisionRadius);
		writeF(collisionHeight);
		writeD(0); // Current HP
		writeD(0); // Max HP
		writeD(7); // Color
	}
}
