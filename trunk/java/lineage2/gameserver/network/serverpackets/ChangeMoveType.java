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
public class ChangeMoveType extends L2GameServerPacket
{
	/**
	 * Field WALK.
	 */
	public static int WALK = 0;
	/**
	 * Field RUN.
	 */
	public static int RUN = 1;
	/**
	 * Field _chaId.
	 */
	private final int _chaId;
	/**
	 * Field _running.
	 */
	private final boolean _running;
	
	/**
	 * Constructor for ChangeMoveType.
	 * @param cha Creature
	 */
	public ChangeMoveType(Creature cha)
	{
		_chaId = cha.getObjectId();
		_running = cha.isRunning();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x28);
		writeD(_chaId);
		writeD(_running ? 1 : 0);
		writeD(0);
	}
}
