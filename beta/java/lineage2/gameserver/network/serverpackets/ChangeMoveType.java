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
 * 0000: 3e 2a 89 00 4c 01 00 00 00 .|...
 * <p/>
 * format dd
 */
public class ChangeMoveType extends L2GameServerPacket
{
	private static final int WALK = 0;
	private static final int RUN = 1;
	private final int _chaId;
	private final boolean _running;
	
	public ChangeMoveType(Creature cha)
	{
		_chaId = cha.getObjectId();
		_running = cha.isRunning();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x28);
		writeD(_chaId);
		writeD(_running ? RUN : WALK);
		writeD(0); // c2
	}
}