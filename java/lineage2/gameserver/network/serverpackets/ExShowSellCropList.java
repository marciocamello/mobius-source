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

import lineage2.gameserver.model.Manor;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.templates.manor.CropProcure;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowSellCropList extends L2GameServerPacket
{
	/**
	 * Field _manorId.
	 */
	private int _manorId = 1;
	/**
	 * Field _cropsItems.
	 */
	private final Map<Integer, ItemInstance> _cropsItems;
	/**
	 * Field _castleCrops.
	 */
	private final Map<Integer, CropProcure> _castleCrops;
	
	/**
	 * Constructor for ExShowSellCropList.
	 * @param player Player
	 * @param manorId int
	 * @param crops List<CropProcure>
	 */
	public ExShowSellCropList(Player player, int manorId, List<CropProcure> crops)
	{
		_manorId = manorId;
		_castleCrops = new TreeMap<>();
		_cropsItems = new TreeMap<>();
		List<Integer> allCrops = Manor.getInstance().getAllCrops();
		for (int cropId : allCrops)
		{
			ItemInstance item = player.getInventory().getItemByItemId(cropId);
			if (item != null)
			{
				_cropsItems.put(cropId, item);
			}
		}
		for (CropProcure crop : crops)
		{
			if (_cropsItems.containsKey(crop.getId()) && (crop.getAmount() > 0))
			{
				_castleCrops.put(crop.getId(), crop);
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	public void writeImpl()
	{
		writeEx(0x2c);
		writeD(_manorId);
		writeD(_cropsItems.size());
		for (ItemInstance item : _cropsItems.values())
		{
			writeD(item.getObjectId());
			writeD(item.getItemId());
			writeD(Manor.getInstance().getSeedLevelByCrop(item.getItemId()));
			writeC(1);
			writeD(Manor.getInstance().getRewardItem(item.getItemId(), 1));
			writeC(1);
			writeD(Manor.getInstance().getRewardItem(item.getItemId(), 2));
			if (_castleCrops.containsKey(item.getItemId()))
			{
				CropProcure crop = _castleCrops.get(item.getItemId());
				writeD(_manorId);
				writeQ(crop.getAmount());
				writeQ(crop.getPrice());
				writeC(crop.getReward());
			}
			else
			{
				writeD(0xFFFFFFFF);
				writeQ(0);
				writeQ(0);
				writeC(0);
			}
			writeQ(item.getCount());
		}
	}
}
