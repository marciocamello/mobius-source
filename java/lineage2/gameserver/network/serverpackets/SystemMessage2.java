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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SystemMessage2 extends SysMsgContainer<SystemMessage2>
{
	/**
	 * Constructor for SystemMessage2.
	 * @param message SystemMsg
	 */
	public SystemMessage2(SystemMsg message)
	{
		super(message);
	}
	
	/**
	 * Method obtainItems.
	 * @param itemId int
	 * @param count long
	 * @param enchantLevel int
	 * @return SystemMessage2
	 */
	public static SystemMessage2 obtainItems(int itemId, long count, int enchantLevel)
	{
		if (itemId == 57)
		{
			return new SystemMessage2(SystemMsg.YOU_HAVE_EARNED_S1_ADENA).addLong(count);
		}
		if (count > 1)
		{
			return new SystemMessage2(SystemMsg.YOU_HAVE_EARNED_S2_S1S).addItemName(itemId).addLong(count);
		}
		if (enchantLevel > 0)
		{
			return new SystemMessage2(SystemMsg.YOU_HAVE_OBTAINED_A_S1_S2).addInteger(enchantLevel).addItemName(itemId);
		}
		return new SystemMessage2(SystemMsg.YOU_HAVE_EARNED_S1).addItemName(itemId);
	}
	
	/**
	 * Method obtainItems.
	 * @param item ItemInstance
	 * @return SystemMessage2
	 */
	public static SystemMessage2 obtainItems(ItemInstance item)
	{
		return obtainItems(item.getItemId(), item.getCount(), item.isEquipable() ? item.getEnchantLevel() : 0);
	}
	
	/**
	 * Method obtainItemsBy.
	 * @param itemId int
	 * @param count long
	 * @param enchantLevel int
	 * @param target Creature
	 * @return SystemMessage2
	 */
	public static SystemMessage2 obtainItemsBy(int itemId, long count, int enchantLevel, Creature target)
	{
		if (count > 1)
		{
			return new SystemMessage2(SystemMsg.C1_HAS_OBTAINED_S3_S2).addName(target).addItemName(itemId).addLong(count);
		}
		if (enchantLevel > 0)
		{
			return new SystemMessage2(SystemMsg.C1_HAS_OBTAINED_S2S3).addName(target).addInteger(enchantLevel).addItemName(itemId);
		}
		return new SystemMessage2(SystemMsg.C1_HAS_OBTAINED_S2).addName(target).addItemName(itemId);
	}
	
	/**
	 * Method obtainItemsBy.
	 * @param item ItemInstance
	 * @param target Creature
	 * @return SystemMessage2
	 */
	public static SystemMessage2 obtainItemsBy(ItemInstance item, Creature target)
	{
		return obtainItemsBy(item.getItemId(), item.getCount(), item.isEquipable() ? item.getEnchantLevel() : 0, target);
	}
	
	/**
	 * Method removeItems.
	 * @param itemId int
	 * @param count long
	 * @return SystemMessage2
	 */
	public static SystemMessage2 removeItems(int itemId, long count)
	{
		if (itemId == 57)
		{
			return new SystemMessage2(SystemMsg.S1_ADENA_DISAPPEARED).addLong(count);
		}
		if (count > 1)
		{
			return new SystemMessage2(SystemMsg.S2_S1_HAS_DISAPPEARED).addItemName(itemId).addLong(count);
		}
		return new SystemMessage2(SystemMsg.S1_HAS_DISAPPEARED).addItemName(itemId);
	}
	
	/**
	 * Method removeItems.
	 * @param item ItemInstance
	 * @return SystemMessage2
	 */
	public static SystemMessage2 removeItems(ItemInstance item)
	{
		return removeItems(item.getItemId(), item.getCount());
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x62);
		writeElements();
	}
}
