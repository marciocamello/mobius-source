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
public class ExPutIntensiveResultForVariationMake extends L2GameServerPacket
{
	/**
	 * Field _unk. Field _gemstoneItemId. Field _lifestoneItemId. Field _refinerItemObjId.
	 */
	private final int _refinerItemObjId, _lifestoneItemId, _gemstoneItemId, _unk;
	/**
	 * Field _gemstoneCount.
	 */
	private final long _gemstoneCount;
	
	/**
	 * Constructor for ExPutIntensiveResultForVariationMake.
	 * @param refinerItemObjId int
	 * @param lifeStoneId int
	 * @param gemstoneItemId int
	 * @param gemstoneCount long
	 */
	public ExPutIntensiveResultForVariationMake(int refinerItemObjId, int lifeStoneId, int gemstoneItemId, long gemstoneCount)
	{
		_refinerItemObjId = refinerItemObjId;
		_lifestoneItemId = lifeStoneId;
		_gemstoneItemId = gemstoneItemId;
		_gemstoneCount = gemstoneCount;
		_unk = 1;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x54);
		writeD(_refinerItemObjId);
		writeD(_lifestoneItemId);
		writeD(_gemstoneItemId);
		writeQ(_gemstoneCount);
		writeD(_unk);
	}
}
