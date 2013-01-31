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
package lineage2.gameserver.stats.funcs;

import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.tables.EnchantHPBonusTable;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.ItemType;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FuncEnchant extends Func
{
	/**
	 * Constructor for FuncEnchant.
	 * @param stat Stats
	 * @param order int
	 * @param owner Object
	 * @param value double
	 */
	public FuncEnchant(Stats stat, int order, Object owner, double value)
	{
		super(stat, order, owner);
	}
	
	/**
	 * Method calc.
	 * @param env Env
	 */
	@Override
	public void calc(Env env)
	{
		ItemInstance item = (ItemInstance) owner;
		int enchant = item.getEnchantLevel();
		int overenchant = Math.max(0, enchant - 3);
		switch (stat)
		{
			case SHIELD_DEFENCE:
			case MAGIC_DEFENCE:
			case POWER_DEFENCE:
			{
				env.value += enchant + (overenchant * 2);
				return;
			}
			case MAX_HP:
			{
				env.value += EnchantHPBonusTable.getInstance().getHPBonus(item);
				return;
			}
			case MAGIC_ATTACK:
			{
				switch (item.getTemplate().getCrystalType().cry)
				{
					case ItemTemplate.CRYSTAL_R:
						env.value += 5 * (enchant + overenchant);
						break;
					case ItemTemplate.CRYSTAL_S:
						env.value += 4 * (enchant + overenchant);
						break;
					case ItemTemplate.CRYSTAL_A:
						env.value += 3 * (enchant + overenchant);
						break;
					case ItemTemplate.CRYSTAL_B:
						env.value += 3 * (enchant + overenchant);
						break;
					case ItemTemplate.CRYSTAL_C:
						env.value += 3 * (enchant + overenchant);
						break;
					case ItemTemplate.CRYSTAL_D:
					case ItemTemplate.CRYSTAL_NONE:
						env.value += 2 * (enchant + overenchant);
						break;
				}
				return;
			}
			case POWER_ATTACK:
			{
				ItemType itemType = item.getItemType();
				boolean isBow = (itemType == WeaponType.BOW) || (itemType == WeaponType.CROSSBOW);
				boolean isSword = ((itemType == WeaponType.DUALFIST) || (itemType == WeaponType.DUAL) || (itemType == WeaponType.BIGSWORD) || (itemType == WeaponType.SWORD) || (itemType == WeaponType.RAPIER) || (itemType == WeaponType.ANCIENTSWORD)) && (item.getTemplate().getBodyPart() == ItemTemplate.SLOT_LR_HAND);
				switch (item.getTemplate().getCrystalType().cry)
				{
					case ItemTemplate.CRYSTAL_R:
						if (isBow)
						{
							env.value += 12 * (enchant + overenchant);
						}
						else if (isSword)
						{
							env.value += 7 * (enchant + overenchant);
						}
						else
						{
							env.value += 6 * (enchant + overenchant);
						}
						break;
					case ItemTemplate.CRYSTAL_S:
						if (isBow)
						{
							env.value += 10 * (enchant + overenchant);
						}
						else if (isSword)
						{
							env.value += 6 * (enchant + overenchant);
						}
						else
						{
							env.value += 5 * (enchant + overenchant);
						}
						break;
					case ItemTemplate.CRYSTAL_A:
						if (isBow)
						{
							env.value += 8 * (enchant + overenchant);
						}
						else if (isSword)
						{
							env.value += 5 * (enchant + overenchant);
						}
						else
						{
							env.value += 4 * (enchant + overenchant);
						}
						break;
					case ItemTemplate.CRYSTAL_B:
					case ItemTemplate.CRYSTAL_C:
						if (isBow)
						{
							env.value += 6 * (enchant + overenchant);
						}
						else if (isSword)
						{
							env.value += 4 * (enchant + overenchant);
						}
						else
						{
							env.value += 3 * (enchant + overenchant);
						}
						break;
					case ItemTemplate.CRYSTAL_D:
					case ItemTemplate.CRYSTAL_NONE:
						if (isBow)
						{
							env.value += 4 * (enchant + overenchant);
						}
						else
						{
							env.value += 2 * (enchant + overenchant);
						}
						break;
				}
			}
		}
	}
}
