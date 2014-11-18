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

import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.TradeItem;

/**
 * @author blacksmoke
 */
public abstract class AbstractItemPacket extends L2GameServerPacket
{
	private int check_Augmentation;
	private int check_ElementType;
	private int check_EnchantOption;
	private int check_Visual;
	
	protected void writeItem(TradeItem item)
	{
		writeItem(new ItemInfo(item));
	}
	
	protected void writeItem(ItemInstance item)
	{
		writeItem(new ItemInfo(item));
	}
	
	protected void writeItem(ItemInfo item)
	{
		check_Augmentation = 0;
		check_ElementType = 0;
		check_EnchantOption = 0;
		check_Visual = 0;
		
		if (item.getAugmentationId() > 0)
		{
			check_Augmentation = 1;
		}
		if (item.getVisualId() > 0)
		{
			check_Visual = 8;
		}
		if (item.getAttackElement() > 0)
		{
			check_ElementType = 2;
		}
		else
		{
			if ((item.getDefenceEarth() > 0) || (item.getDefenceFire() > 0) || (item.getDefenceHoly() > 0) || (item.getDefenceUnholy() > 0) || (item.getDefenceWater() > 0) || (item.getDefenceWind() > 0))
			{
				check_ElementType = 2;
			}
		}
		for (int op : item.getEnchantOptions())
		{
			if (op > 0)
			{
				check_EnchantOption = 4;
			}
		}
		// ddcQcchQccddc
		// hh
		// hhhhhhhh
		// hhh
		writeC(check_Augmentation + check_Visual + check_ElementType + check_EnchantOption + 0);
		writeD(item.getObjectId()); // ObjectId
		writeD(item.getId()); // ItemId
		writeC(item.getEquipSlot()); // T1
		writeQ(item.getCount()); // Quantity
		writeC(item.getItem().getType2()); // Item Type 2 : 00-weapon, 01-shield/armor, 02-ring/earring/necklace, 03-questitem, 04-adena, 05-item
		writeC(item.getCustomType1()); // Filler (always 0)
		writeH(item.isEquipped() ? 1 : 0); // Equipped : 00-No, 01-yes
		writeQ(item.getItem().getBodyPart()); // Slot : 0006-lr.ear, 0008-neck, 0030-lr.finger, 0040-head, 0100-l.hand, 0200-gloves, 0400-chest, 0800-pants, 1000-feet, 4000-r.hand, 8000-r.hand
		writeC(item.getEnchantLevel()); // Enchant level (pet level shown in control item)
		writeC(item.getCustomType2()); // Pet name exists or not shown in control item
		writeD(item.getShadowLifeTime());
		writeD(item.getTemporalLifeTime());
		writeC(0x01);
		if (check_Augmentation > 0)
		{
			writeD(item.getAugmentationId());
		}
		if (check_Visual > 0)
		{
			writeD(item.getVisualId());
		}
		writeItemElementalAndEnchant(item);
	}
	
	protected void writeItemElementalAndEnchant(ItemInfo item)
	{
		if (check_ElementType > 0)
		{
			writeH(item.getAttackElement());
			writeH(item.getAttackElementValue());
			writeH(item.getDefenceFire());
			writeH(item.getDefenceWater());
			writeH(item.getDefenceWind());
			writeH(item.getDefenceEarth());
			writeH(item.getDefenceHoly());
			writeH(item.getDefenceUnholy());
		}
		// Enchant Effects
		if (check_EnchantOption > 0)
		{
			writeH(item.getEnchantOptions()[0]);
			writeH(item.getEnchantOptions()[1]);
			writeH(item.getEnchantOptions()[2]);
		}
	}
}
