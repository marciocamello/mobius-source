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

public class MonRaceInfo extends L2GameServerPacket
{
	private final int _unknown1;
	private final int _unknown2;
	private final NpcInstance[] _monsters;
	private final int[][] _speeds;
	
	public MonRaceInfo(int unknown1, int unknown2, NpcInstance[] monsters, int[][] speeds)
	{
		/*
		 * -1 0 to initial the race 0 15322 to start race 13765 -1 in middle of race -1 0 to end the race
		 */
		_unknown1 = unknown1;
		_unknown2 = unknown2;
		_monsters = monsters;
		_speeds = speeds;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xe3);
		
		writeD(_unknown1);
		writeD(_unknown2);
		writeD(8);
		
		for (int i = 0; i < 8; i++)
		{
			// _log.info.println("MOnster "+(i+1)+" npcid "+_monsters[i].getNpcTemplate().getNpcId());
			writeD(_monsters[i].getObjectId()); // npcObjectID
			writeD(_monsters[i].getTemplate().npcId + 1000000); // npcID
			writeD(14107); // origin X
			writeD(181875 + (58 * (7 - i))); // origin Y
			writeD(-3566); // origin Z
			writeD(12080); // end X
			writeD(181875 + (58 * (7 - i))); // end Y
			writeD(-3566); // end Z
			writeF(_monsters[i].getColHeight()); // coll. height
			writeF(_monsters[i].getColRadius()); // coll. radius
			writeD(120); // ?? unknown
			for (int j = 0; j < 20; j++)
			{
				writeC(_unknown1 == 0 ? _speeds[i][j] : 0);
			}
			writeD(0);
			writeD(0x00); // ? GraciaFinal
		}
	}
}