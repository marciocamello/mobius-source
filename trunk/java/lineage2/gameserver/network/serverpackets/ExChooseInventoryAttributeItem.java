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

import gnu.trove.list.array.TIntArrayList;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.etcitems.AttributeStoneInfo;
import lineage2.gameserver.model.items.etcitems.AttributeStoneManager;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExChooseInventoryAttributeItem extends L2GameServerPacket
{
	/**
	 * Field _attributableItems.
	 */
	private final TIntArrayList _attributableItems;
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _stoneLvl.
	 */
	private final int _stoneLvl;
	/**
	 * Field _att.
	 */
	private final int[] _att;
	
	/**
	 * Constructor for ExChooseInventoryAttributeItem.
	 * @param player Player
	 * @param item ItemInstance
	 */
	public ExChooseInventoryAttributeItem(Player player, ItemInstance item)
	{
		_attributableItems = new TIntArrayList();
		ItemInstance[] items = player.getInventory().getItems();
		for (ItemInstance _item : items)
		{
			if ((_item.getCrystalType() != ItemTemplate.Grade.NONE) && (_item.isArmor() || _item.isWeapon()))
			{
				_attributableItems.add(_item.getObjectId());
			}
		}
		_itemId = item.getItemId();
		_att = new int[6];
		AttributeStoneInfo asi = AttributeStoneManager.getStoneInfo(_itemId);
		_att[asi.getElement().getId()] = 1;
		_stoneLvl = asi.getStoneLevel();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x62);
		writeD(_itemId);
		for (int i : _att)
		{
			writeD(i);
		}
		writeD(_stoneLvl);
		writeD(_attributableItems.size());
		for (int itemObjId : _attributableItems.toArray())
		{
			writeD(itemObjId);
		}
	}
}
