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
public class ExPutCommissionResultForVariationMake extends L2GameServerPacket
{
	/**
	 * Field _unk3. Field _unk1. Field _gemstoneObjId.
	 */
	private final int _gemstoneObjId, _unk1, _unk3;
	/**
	 * Field _unk2. Field _gemstoneCount.
	 */
	private final long _gemstoneCount, _unk2;
	
	/**
	 * Constructor for ExPutCommissionResultForVariationMake.
	 * @param gemstoneObjId int
	 * @param count long
	 */
	public ExPutCommissionResultForVariationMake(int gemstoneObjId, long count)
	{
		_gemstoneObjId = gemstoneObjId;
		_unk1 = 1;
		_gemstoneCount = count;
		_unk2 = 1;
		_unk3 = 1;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x55);
		writeD(_gemstoneObjId);
		writeD(_unk1);
		writeQ(_gemstoneCount);
		writeQ(_unk2);
		writeD(_unk3);
	}
}
