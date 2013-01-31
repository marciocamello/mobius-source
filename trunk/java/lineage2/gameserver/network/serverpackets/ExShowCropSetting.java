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

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.Manor;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.templates.manor.CropProcure;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowCropSetting extends L2GameServerPacket
{
	/**
	 * Field _manorId.
	 */
	private final int _manorId;
	/**
	 * Field _count.
	 */
	private final int _count;
	/**
	 * Field _cropData.
	 */
	private final long[] _cropData;
	
	/**
	 * Constructor for ExShowCropSetting.
	 * @param manorId int
	 */
	public ExShowCropSetting(int manorId)
	{
		_manorId = manorId;
		Castle c = ResidenceHolder.getInstance().getResidence(Castle.class, _manorId);
		List<Integer> crops = Manor.getInstance().getCropsForCastle(_manorId);
		_count = crops.size();
		_cropData = new long[_count * 14];
		int i = 0;
		for (int cr : crops)
		{
			_cropData[(i * 14) + 0] = cr;
			_cropData[(i * 14) + 1] = Manor.getInstance().getSeedLevelByCrop(cr);
			_cropData[(i * 14) + 2] = Manor.getInstance().getRewardItem(cr, 1);
			_cropData[(i * 14) + 3] = Manor.getInstance().getRewardItem(cr, 2);
			_cropData[(i * 14) + 4] = Manor.getInstance().getCropPuchaseLimit(cr);
			_cropData[(i * 14) + 5] = 0;
			_cropData[(i * 14) + 6] = (Manor.getInstance().getCropBasicPrice(cr) * 60) / 100;
			_cropData[(i * 14) + 7] = Manor.getInstance().getCropBasicPrice(cr) * 10;
			CropProcure cropPr = c.getCrop(cr, CastleManorManager.PERIOD_CURRENT);
			if (cropPr != null)
			{
				_cropData[(i * 14) + 8] = cropPr.getStartAmount();
				_cropData[(i * 14) + 9] = cropPr.getPrice();
				_cropData[(i * 14) + 10] = cropPr.getReward();
			}
			else
			{
				_cropData[(i * 14) + 8] = 0;
				_cropData[(i * 14) + 9] = 0;
				_cropData[(i * 14) + 10] = 0;
			}
			cropPr = c.getCrop(cr, CastleManorManager.PERIOD_NEXT);
			if (cropPr != null)
			{
				_cropData[(i * 14) + 11] = cropPr.getStartAmount();
				_cropData[(i * 14) + 12] = cropPr.getPrice();
				_cropData[(i * 14) + 13] = cropPr.getReward();
			}
			else
			{
				_cropData[(i * 14) + 11] = 0;
				_cropData[(i * 14) + 12] = 0;
				_cropData[(i * 14) + 13] = 0;
			}
			i++;
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	public void writeImpl()
	{
		writeEx(0x2b);
		writeD(_manorId);
		writeD(_count);
		for (int i = 0; i < _count; i++)
		{
			writeD((int) _cropData[(i * 14) + 0]);
			writeD((int) _cropData[(i * 14) + 1]);
			writeC(1);
			writeD((int) _cropData[(i * 14) + 2]);
			writeC(1);
			writeD((int) _cropData[(i * 14) + 3]);
			writeD((int) _cropData[(i * 14) + 4]);
			writeD((int) _cropData[(i * 14) + 5]);
			writeD((int) _cropData[(i * 14) + 6]);
			writeD((int) _cropData[(i * 14) + 7]);
			writeQ(_cropData[(i * 14) + 8]);
			writeQ(_cropData[(i * 14) + 9]);
			writeC((int) _cropData[(i * 14) + 10]);
			writeQ(_cropData[(i * 14) + 11]);
			writeQ(_cropData[(i * 14) + 12]);
			writeC((int) _cropData[(i * 14) + 13]);
		}
	}
}
