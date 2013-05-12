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

public class ExBlockUpSetList extends L2GameServerPacket
{
	private final int BlockUpType = 0; // TODO
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x98);
		writeD(BlockUpType);
		switch (BlockUpType)
		{
			case 0:
				// dd
				// d[dS]
				// d[dS]
				break;
			case 1:
				// dddS
				break;
			case 2:
				// ddd
				break;
			case 3:
				// d
				break;
			case 4: // nothing
				break;
			case 5:
				// ddd
				break;
			case -1: // nothing
				break;
		}
	}
}