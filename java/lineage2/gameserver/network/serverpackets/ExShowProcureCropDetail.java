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
import java.util.Map;
import java.util.TreeMap;

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.templates.manor.CropProcure;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowProcureCropDetail extends L2GameServerPacket
{
	/**
	 * Field _cropId.
	 */
	private final int _cropId;
	/**
	 * Field _castleCrops.
	 */
	private final Map<Integer, CropProcure> _castleCrops;
	
	/**
	 * Constructor for ExShowProcureCropDetail.
	 * @param cropId int
	 */
	public ExShowProcureCropDetail(int cropId)
	{
		_cropId = cropId;
		_castleCrops = new TreeMap<>();
		List<Castle> castleList = ResidenceHolder.getInstance().getResidenceList(Castle.class);
		for (Castle c : castleList)
		{
			CropProcure cropItem = c.getCrop(_cropId, CastleManorManager.PERIOD_CURRENT);
			if ((cropItem != null) && (cropItem.getAmount() > 0))
			{
				_castleCrops.put(c.getId(), cropItem);
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	public void writeImpl()
	{
		writeEx(0x78);
		writeD(_cropId);
		writeD(_castleCrops.size());
		for (int manorId : _castleCrops.keySet())
		{
			CropProcure crop = _castleCrops.get(manorId);
			writeD(manorId);
			writeQ(crop.getAmount());
			writeQ(crop.getPrice());
			writeC(crop.getReward());
		}
	}
}
