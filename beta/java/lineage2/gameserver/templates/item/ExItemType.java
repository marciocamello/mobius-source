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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mobius
 */
public enum ExItemType
{
	EMPTY0(0),
	SWORD(0),
	MAGIC_SWORD(0),
	DAGGER(0),
	RAPIER(0),
	BIG_SWORD(0),
	ANCIENT_SWORD(0),
	DUAL_SWORD(0),
	DUAL_DAGGER(0),
	BLUNT_WEAPON(0),
	MAGIC_BLUNT_WEAPON(0),
	BIG_BLUNT_WEAPON(0),
	BIG_MAGIC_BLUNT_WEAPON(0),
	DUAL_BLUNT_WEAPON(0),
	BOW(0),
	CROSSBOW(0),
	HAND_TO_HAND(0),
	POLE(0),
	OTHER_WEAPON(0),
	HELMET(1),
	UPPER_PIECE(1),
	LOWER_PIECE(1),
	FULL_BODY(1),
	GLOVES(1),
	FEET(1),
	SHIELD(1),
	SIGIL(1),
	UNDERWEAR(1),
	CLOAK(1),
	RING(2),
	EARRING(2),
	NECKLACE(2),
	BELT(2),
	BRACELET(2),
	HAIR_ACCESSORY(2),
	POTION(3),
	SCROLL_ENCHANT_WEAPON(3),
	SCROLL_ENCHANT_ARMOR(3),
	SCROLL_OTHER(3),
	SOULSHOT(3),
	SPIRITSHOT(3),
	EMPTY41(3),
	PET_EQUIPMENT(4),
	PET_SUPPLIES(4),
	CRYSTAL(5),
	RECIPE(5),
	CRAFTING_MAIN_INGRIDIENTS(5),
	LIFE_STONE(5),
	SOUL_CRYSTAL(5),
	ATTRIBUTE_STONE(5),
	WEAPON_ENCHANT_STONE(5),
	ARMOR_ENCHANT_STONE(5),
	SPELLBOOK(5),
	GEMSTONE(5),
	POUCH(5),
	PIN(5),
	MAGIC_RUNE_CLIP(5),
	MAGIC_ORNAMENT(5),
	DYES(5),
	OTHER_ITEMS(5);
	
	public static final int WEAPON_MASK = 0;
	public static final int ARMOR_MASK = 1;
	public static final int ACCESSORIES_MASK = 2;
	public static final int SUPPLIES_MASK = 3;
	public static final int PET_GOODS_MASK = 4;
	public static final int ETC_MASK = 5;
	private int mask;
	
	/**
	 * Constructor for ExItemType.
	 * @param mask int
	 */
	ExItemType(int mask)
	{
		this.mask = mask;
	}
	
	/**
	 * Method getMask.
	 * @return int
	 */
	public int getMask()
	{
		return mask;
	}
	
	/**
	 * Method getTypesForMask.
	 * @param mask int
	 * @return ExItemType[]
	 */
	public static ExItemType[] getTypesForMask(int mask)
	{
		if ((mask < WEAPON_MASK) && (mask > ETC_MASK))
		{
			return new ExItemType[0];
		}
		
		List<ExItemType> list = new ArrayList<>();
		
		for (ExItemType exType : values())
		{
			if (exType.getMask() == mask)
			{
				list.add(exType);
			}
		}
		
		return list.toArray(new ExItemType[list.size()]);
	}
	
	/**
	 * Method valueOf.
	 * @param ordinal int
	 * @return ExItemType
	 */
	public static ExItemType valueOf(int ordinal)
	{
		for (ExItemType type : values())
		{
			if (type.ordinal() == ordinal)
			{
				return type;
			}
		}
		
		return null;
	}
}
