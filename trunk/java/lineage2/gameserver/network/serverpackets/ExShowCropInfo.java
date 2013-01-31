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
import lineage2.gameserver.templates.manor.CropProcure;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowCropInfo extends L2GameServerPacket
{
	/**
	 * Field _crops.
	 */
	private final List<CropProcure> _crops;
	/**
	 * Field _manorId.
	 */
	private final int _manorId;
	
	/**
	 * Constructor for ExShowCropInfo.
	 * @param manorId int
	 * @param crops List<CropProcure>
	 */
	public ExShowCropInfo(int manorId, List<CropProcure> crops)
	{
		_manorId = manorId;
		_crops = crops;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x24);
		writeC(0);
		writeD(_manorId);
		writeD(0);
		writeD(_crops.size());
		for (CropProcure crop : _crops)
		{
			writeD(crop.getId());
			writeQ(crop.getAmount());
			writeQ(crop.getStartAmount());
			writeQ(crop.getPrice());
			writeC(crop.getReward());
			writeD(Manor.getInstance().getSeedLevelByCrop(crop.getId()));
			writeC(1);
			writeD(Manor.getInstance().getRewardItem(crop.getId(), 1));
			writeC(1);
			writeD(Manor.getInstance().getRewardItem(crop.getId(), 2));
		}
	}
}
