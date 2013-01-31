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

import java.util.List;

import lineage2.gameserver.model.Manor;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowManorDefaultInfo extends L2GameServerPacket
{
	/**
	 * Field _crops.
	 */
	private List<Integer> _crops = null;
	
	/**
	 * Constructor for ExShowManorDefaultInfo.
	 */
	public ExShowManorDefaultInfo()
	{
		_crops = Manor.getInstance().getAllCrops();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x25);
		writeC(0);
		writeD(_crops.size());
		for (int cropId : _crops)
		{
			writeD(cropId);
			writeD(Manor.getInstance().getSeedLevelByCrop(cropId));
			writeD(Manor.getInstance().getSeedBasicPriceByCrop(cropId));
			writeD(Manor.getInstance().getCropBasicPrice(cropId));
			writeC(1);
			writeD(Manor.getInstance().getRewardItem(cropId, 1));
			writeC(1);
			writeD(Manor.getInstance().getRewardItem(cropId, 2));
		}
	}
}
