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

public class ExPledgeCrestLarge extends L2GameServerPacket
{
	private final int _crestId;
	private final int _clanId;
	private final byte[] _data;
	private final int _i;
	
	public ExPledgeCrestLarge(int clanId, int crestId, byte[] data, int i)
	{
		_clanId = clanId;
		_crestId = crestId;
		_data = data;
		_i = i;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0x1B);
		writeD(0x00);
		writeD(_clanId);
		writeD(_crestId);
		writeD(_i); // split number
		writeD(65664); // total size
		writeD(_data.length); // split size
		writeB(_data); // split data
	}
}