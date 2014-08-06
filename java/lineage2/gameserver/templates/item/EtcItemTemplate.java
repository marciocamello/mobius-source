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
package lineage2.gameserver.templates.item;

import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.ArmorTemplate.ArmorType;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EtcItemTemplate extends ItemTemplate
{
	/**
	 * @author Mobius
	 */
	public enum EtcItemType implements ItemType
	{
		ARROW(1, "Arrow"),
		MATERIAL(2, "Material"),
		PET_COLLAR(3, "PetCollar"),
		POTION(4, "Potion"),
		RECIPE(5, "Recipe"),
		SCROLL(6, "Scroll"),
		QUEST(7, "Quest"),
		MONEY(8, "Money"),
		OTHER(9, "Other"),
		SPELLBOOK(10, "Spellbook"),
		SEED(11, "Seed"),
		BAIT(12, "Bait"),
		SHOT(13, "Shot"),
		BOLT(14, "Bolt"),
		RUNE(15, "Rune"),
		HERB(16, "Herb"),
		MERCENARY_TICKET(17, "Mercenary Ticket"),
		UNLIMITED_ARROW(18, "Unlimited Arrow");
		private final long _mask;
		private final String _name;
		
		/**
		 * Constructor for EtcItemType.
		 * @param id int
		 * @param name String
		 */
		EtcItemType(int id, String name)
		{
			_mask = 1L << (id + WeaponType.VALUES.length + ArmorType.VALUES.length);
			_name = name;
		}
		
		/**
		 * Method mask.
		 * @return long * @see lineage2.gameserver.templates.item.ItemType#mask()
		 */
		@Override
		public long mask()
		{
			return _mask;
		}
		
		/**
		 * Method toString.
		 * @return String
		 */
		@Override
		public String toString()
		{
			return _name;
		}
	}
	
	/**
	 * Constructor for EtcItemTemplate.
	 * @param set StatsSet
	 */
	public EtcItemTemplate(StatsSet set)
	{
		super(set);
		type = set.getEnum("type", EtcItemType.class);
		_type1 = TYPE1_ITEM_QUESTITEM_ADENA;
		
		switch (getItemType())
		{
			case QUEST:
				_type2 = TYPE2_QUEST;
				break;
			
			case MONEY:
				_type2 = TYPE2_MONEY;
				break;
			
			default:
				_type2 = TYPE2_OTHER;
				break;
		}
	}
	
	/**
	 * Method getItemType.
	 * @return EtcItemType
	 */
	@Override
	public EtcItemType getItemType()
	{
		return (EtcItemType) super.type;
	}
	
	/**
	 * Method getItemMask.
	 * @return long
	 */
	@Override
	public long getItemMask()
	{
		return getItemType().mask();
	}
	
	/**
	 * Method isShadowItem.
	 * @return boolean
	 */
	@Override
	public final boolean isShadowItem()
	{
		return false;
	}
	
	/**
	 * Method canBeEnchanted.
	 * @return boolean
	 */
	@Override
	public final boolean canBeEnchanted()
	{
		return false;
	}
}
