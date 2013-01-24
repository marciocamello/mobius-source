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

import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncTemplate;
import lineage2.gameserver.templates.StatsSet;

public final class WeaponTemplate extends ItemTemplate
{
	private final int _soulShotCount;
	private final int _spiritShotCount;
	private final int _kamaelConvert;
	private final int _rndDam;
	private final int _atkReuse;
	private final int _mpConsume;
	private int _critical;
	
	public enum WeaponType implements ItemType
	{
		NONE(1, "Shield", null),
		SWORD(2, "Sword", Stats.SWORD_WPN_VULNERABILITY),
		BLUNT(3, "Blunt", Stats.BLUNT_WPN_VULNERABILITY),
		DAGGER(4, "Dagger", Stats.DAGGER_WPN_VULNERABILITY),
		BOW(5, "Bow", Stats.BOW_WPN_VULNERABILITY),
		POLE(6, "Pole", Stats.POLE_WPN_VULNERABILITY),
		ETC(7, "Etc", null),
		FIST(8, "Fist", Stats.FIST_WPN_VULNERABILITY),
		DUAL(9, "Dual Sword", Stats.DUAL_WPN_VULNERABILITY),
		DUALFIST(10, "Dual Fist", Stats.FIST_WPN_VULNERABILITY),
		BIGSWORD(11, "Big Sword", Stats.SWORD_WPN_VULNERABILITY),
		PET(12, "Pet", Stats.FIST_WPN_VULNERABILITY),
		ROD(13, "Rod", null),
		BIGBLUNT(14, "Big Blunt", Stats.BLUNT_WPN_VULNERABILITY),
		CROSSBOW(15, "Crossbow", Stats.CROSSBOW_WPN_VULNERABILITY),
		RAPIER(16, "Rapier", Stats.DAGGER_WPN_VULNERABILITY),
		ANCIENTSWORD(17, "Ancient Sword", Stats.SWORD_WPN_VULNERABILITY),
		DUALDAGGER(18, "Dual Dagger", Stats.DAGGER_WPN_VULNERABILITY),
		DUALBLUNT(19, "Dual Blunt", null);
		public final static WeaponType[] VALUES = values();
		private final long _mask;
		private final String _name;
		private final Stats _defence;
		
		private WeaponType(int id, String name, Stats defence)
		{
			_mask = 1L << id;
			_name = name;
			_defence = defence;
		}
		
		@Override
		public long mask()
		{
			return _mask;
		}
		
		public Stats getDefence()
		{
			return _defence;
		}
		
		@Override
		public String toString()
		{
			return _name;
		}
	}
	
	public WeaponTemplate(StatsSet set)
	{
		super(set);
		type = set.getEnum("type", WeaponType.class);
		_soulShotCount = set.getInteger("soulshots", 0);
		_spiritShotCount = set.getInteger("spiritshots", 0);
		_kamaelConvert = set.getInteger("kamael_convert", 0);
		_rndDam = set.getInteger("rnd_dam", 0);
		_atkReuse = set.getInteger("atk_reuse", type == WeaponType.BOW ? 1500 : type == WeaponType.CROSSBOW ? 820 : 0);
		_mpConsume = set.getInteger("mp_consume", 0);
		if (getItemType() == WeaponType.NONE)
		{
			_type1 = TYPE1_SHIELD_ARMOR;
			_type2 = TYPE2_SHIELD_ARMOR;
		}
		else
		{
			_type1 = TYPE1_WEAPON_RING_EARRING_NECKLACE;
			_type2 = TYPE2_WEAPON;
		}
		if (getItemType() == WeaponType.PET)
		{
			_type1 = ItemTemplate.TYPE1_WEAPON_RING_EARRING_NECKLACE;
			if (_bodyPart == ItemTemplate.SLOT_WOLF)
			{
				_type2 = ItemTemplate.TYPE2_PET_WOLF;
			}
			else if (_bodyPart == ItemTemplate.SLOT_GWOLF)
			{
				_type2 = ItemTemplate.TYPE2_PET_GWOLF;
			}
			else if (_bodyPart == ItemTemplate.SLOT_HATCHLING)
			{
				_type2 = ItemTemplate.TYPE2_PET_HATCHLING;
			}
			else
			{
				_type2 = ItemTemplate.TYPE2_PET_STRIDER;
			}
			_bodyPart = ItemTemplate.SLOT_R_HAND;
		}
	}
	
	@Override
	public WeaponType getItemType()
	{
		return (WeaponType) type;
	}
	
	@Override
	public long getItemMask()
	{
		return getItemType().mask();
	}
	
	public int getSoulShotCount()
	{
		return _soulShotCount;
	}
	
	public int getSpiritShotCount()
	{
		return _spiritShotCount;
	}
	
	public int getCritical()
	{
		return _critical;
	}
	
	public int getRandomDamage()
	{
		return _rndDam;
	}
	
	public int getAttackReuseDelay()
	{
		return _atkReuse;
	}
	
	public int getMpConsume()
	{
		return _mpConsume;
	}
	
	public int getAttackRange()
	{
		switch (getItemType())
		{
			case BOW:
				return 460;
			case CROSSBOW:
				return 360;
			case POLE:
				return 40;
			default:
				return 0;
		}
	}
	
	@Override
	public void attachFunc(FuncTemplate f)
	{
		if ((f._stat == Stats.CRITICAL_BASE) && (f._order == 0x08))
		{
			_critical = (int) Math.round(f._value / 10);
		}
		super.attachFunc(f);
	}
	
	public int getKamaelConvert()
	{
		return _kamaelConvert;
	}
}
