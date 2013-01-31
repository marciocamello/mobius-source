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

import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MonRaceInfo extends L2GameServerPacket
{
	/**
	 * Field _unknown1.
	 */
	private final int _unknown1;
	/**
	 * Field _unknown2.
	 */
	private final int _unknown2;
	/**
	 * Field _monsters.
	 */
	private final NpcInstance[] _monsters;
	/**
	 * Field _speeds.
	 */
	private final int[][] _speeds;
	
	/**
	 * Constructor for MonRaceInfo.
	 * @param unknown1 int
	 * @param unknown2 int
	 * @param monsters NpcInstance[]
	 * @param speeds int[][]
	 */
	public MonRaceInfo(int unknown1, int unknown2, NpcInstance[] monsters, int[][] speeds)
	{
		_unknown1 = unknown1;
		_unknown2 = unknown2;
		_monsters = monsters;
		_speeds = speeds;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xe3);
		writeD(_unknown1);
		writeD(_unknown2);
		writeD(8);
		for (int i = 0; i < 8; i++)
		{
			writeD(_monsters[i].getObjectId());
			writeD(_monsters[i].getTemplate().npcId + 1000000);
			writeD(14107);
			writeD(181875 + (58 * (7 - i)));
			writeD(-3566);
			writeD(12080);
			writeD(181875 + (58 * (7 - i)));
			writeD(-3566);
			writeF(_monsters[i].getColHeight());
			writeF(_monsters[i].getColRadius());
			writeD(120);
			for (int j = 0; j < 20; j++)
			{
				writeC(_unknown1 == 0 ? _speeds[i][j] : 0);
			}
			writeD(0);
			writeD(0x00);
		}
	}
}
