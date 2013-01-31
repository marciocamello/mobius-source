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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExPledgeCrestLarge extends L2GameServerPacket
{
	/**
	 * Field _crestId.
	 */
	private final int _crestId;
	/**
	 * Field _data.
	 */
	private final byte[] _data;
	
	/**
	 * Constructor for ExPledgeCrestLarge.
	 * @param crestId int
	 * @param data byte[]
	 */
	public ExPledgeCrestLarge(int crestId, byte[] data)
	{
		_crestId = crestId;
		_data = data;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x1b);
		writeD(0x00);
		writeD(_crestId);
		writeD(_data.length);
		writeB(_data);
	}
}
