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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;

public class ExStorageMaxCount extends L2GameServerPacket
{
	private final int _inventory;
	private final int _warehouse;
	private final int _clan;
	private int _privateSell;
	private final int _privateBuy;
	private final int _recipeDwarven;
	private final int _recipeCommon;
	private final int _inventoryExtraSlots;
	private final int _questItemsLimit;
	
	public ExStorageMaxCount(Player player)
	{
		_inventory = player.getInventoryLimit();
		_warehouse = player.getWarehouseLimit();
		_clan = Config.WAREHOUSE_SLOTS_CLAN;
		_privateBuy = _privateSell = player.getTradeLimit();
		_recipeDwarven = player.getDwarvenRecipeLimit();
		_recipeCommon = player.getCommonRecipeLimit();
		_inventoryExtraSlots = player.getBeltInventoryIncrease();
		_questItemsLimit = Config.QUEST_INVENTORY_MAXIMUM;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x2f);
		writeD(_inventory);
		writeD(_warehouse);
		writeD(_clan);
		writeD(_privateSell);
		writeD(_privateBuy);
		writeD(_recipeDwarven);
		writeD(_recipeCommon);
		writeD(_inventoryExtraSlots); // belt inventory slots increase count
		writeD(_questItemsLimit); // quests list by off 100 maximum
		writeD(40); // Unknown (40 - offlike)
		writeD(40); // Unknown (40 - offlike)
	}
}