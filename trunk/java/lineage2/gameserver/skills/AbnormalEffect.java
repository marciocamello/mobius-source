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
package lineage2.gameserver.skills;

import java.util.NoSuchElementException;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum AbnormalEffect
{
	/**
	 * Field NULL.
	 */
	NULL("null", 0x0),
	/**
	 * Field BLEEDING.
	 */
	BLEEDING("bleeding", 0x00000001),
	/**
	 * Field POISON.
	 */
	POISON("poison", 0x00000002),
	/**
	 * Field REDCIRCLE.
	 */
	REDCIRCLE("redcircle", 0x00000004),
	/**
	 * Field ICE.
	 */
	ICE("ice", 0x00000008),
	/**
	 * Field AFFRAID.
	 */
	AFFRAID("affraid", 0x00000010),
	/**
	 * Field CONFUSED.
	 */
	CONFUSED("confused", 0x00000020),
	/**
	 * Field STUN.
	 */
	STUN("stun", 0x00000040),
	/**
	 * Field SLEEP.
	 */
	SLEEP("sleep", 0x00000080),
	/**
	 * Field MUTED.
	 */
	MUTED("muted", 0x00000100),
	/**
	 * Field ROOT.
	 */
	ROOT("root", 0x00000200),
	/**
	 * Field HOLD_1.
	 */
	HOLD_1("hold1", 0x00000400),
	/**
	 * Field HOLD_2.
	 */
	HOLD_2("hold2", 0x00000800),
	/**
	 * Field UNKNOWN_13.
	 */
	UNKNOWN_13("unk13", 0x00001000),
	/**
	 * Field BIG_HEAD.
	 */
	BIG_HEAD("bighead", 0x00002000),
	/**
	 * Field FLAME.
	 */
	FLAME("flame", 0x00004000),
	/**
	 * Field UNKNOWN_16.
	 */
	UNKNOWN_16("unk16", 0x00008000),
	/**
	 * Field GROW.
	 */
	GROW("grow", 0x00010000),
	/**
	 * Field FLOATING_ROOT.
	 */
	FLOATING_ROOT("floatroot", 0x00020000),
	/**
	 * Field DANCE_STUNNED.
	 */
	DANCE_STUNNED("dancestun", 0x00040000),
	/**
	 * Field FIREROOT_STUN.
	 */
	FIREROOT_STUN("firerootstun", 0x00080000),
	/**
	 * Field STEALTH.
	 */
	STEALTH("shadow", 0x00100000),
	/**
	 * Field IMPRISIONING_1.
	 */
	IMPRISIONING_1("imprison1", 0x00200000),
	/**
	 * Field IMPRISIONING_2.
	 */
	IMPRISIONING_2("imprison2", 0x00400000),
	/**
	 * Field MAGIC_CIRCLE.
	 */
	MAGIC_CIRCLE("magiccircle", 0x00800000),
	/**
	 * Field ICE2.
	 */
	ICE2("ice2", 0x01000000),
	/**
	 * Field EARTHQUAKE.
	 */
	EARTHQUAKE("earthquake", 0x02000000),
	/**
	 * Field UNKNOWN_27.
	 */
	UNKNOWN_27("unk27", 0x04000000),
	/**
	 * Field INVULNERABLE.
	 */
	INVULNERABLE("invul1", 0x08000000),
	/**
	 * Field VITALITY.
	 */
	VITALITY("vitality", 0x10000000),
	/**
	 * Field REAL_TARGET.
	 */
	REAL_TARGET("realtarget", 0x20000000),
	/**
	 * Field DEATH_MARK.
	 */
	DEATH_MARK("deathmark", 0x40000000),
	/**
	 * Field SOUL_SHOCK.
	 */
	SOUL_SHOCK("soulshock", 0x80000000),
	/**
	 * Field S_INVULNERABLE.
	 */
	S_INVULNERABLE("invul2", 0x00000001, true),
	/**
	 * Field S_AIR_STUN.
	 */
	S_AIR_STUN("redglow", 0x00000002, true),
	/**
	 * Field S_AIR_ROOT.
	 */
	S_AIR_ROOT("redglow2", 0x00000004, true),
	/**
	 * Field S_BAGUETTE_SWORD.
	 */
	S_BAGUETTE_SWORD("baguettesword", 0x00000008, true),
	/**
	 * Field S_YELLOW_AFFRO.
	 */
	S_YELLOW_AFFRO("yellowafro", 0x00000010, true),
	/**
	 * Field S_PINK_AFFRO.
	 */
	S_PINK_AFFRO("pinkafro", 0x00000020, true),
	/**
	 * Field S_BLACK_AFFRO.
	 */
	S_BLACK_AFFRO("blackafro", 0x00000040, true),
	/**
	 * Field S_UNKNOWN8.
	 */
	S_UNKNOWN8("sunk8", 0x00000080, true),
	/**
	 * Field S_STIGMA.
	 */
	S_STIGMA("stigma", 0x00000100, true),
	/**
	 * Field S_UNKNOWN10.
	 */
	S_UNKNOWN10("sunk10", 0x00000200, true),
	/**
	 * Field FROZEN_PILLAR.
	 */
	FROZEN_PILLAR("frozenpillar", 0x00000400, true),
	/**
	 * Field S_UNKNOWN12.
	 */
	S_UNKNOWN12("sunk12", 0x00000800, true),
	/**
	 * Field S_DESTINO_SET.
	 */
	S_DESTINO_SET("vesper_red", 0x00001000, true),
	/**
	 * Field S_VESPER_SET.
	 */
	S_VESPER_SET("vesper_noble", 0x00002000, true),
	/**
	 * Field S_SOA_RESP.
	 */
	S_SOA_RESP("soa_respawn", 0x00004000, true),
	/**
	 * Field S_ARCANE_SHIELD.
	 */
	S_ARCANE_SHIELD("arcane_invul", 0x00008000, true),
	/**
	 * Field S_UNKNOWN17.
	 */
	S_UNKNOWN17("sunk17", 0x00010000, true),
	/**
	 * Field S_UNKNOWN18.
	 */
	S_UNKNOWN18("sunk18", 0x00020000, true),
	/**
	 * Field S_UNKNOWN19.
	 */
	S_UNKNOWN19("sunk19", 0x00040000, true),
	/**
	 * Field S_NAVIT.
	 */
	S_NAVIT("nevitSystem", 0x00080000, true),
	/**
	 * Field S_UNKNOWN21.
	 */
	S_UNKNOWN21("sunk21", 0x00100000, true),
	/**
	 * Field S_UNKNOWN22.
	 */
	S_UNKNOWN22("sunk22", 0x00200000, true),
	/**
	 * Field S_UNKNOWN23.
	 */
	S_UNKNOWN23("sunk23", 0x00400000, true),
	/**
	 * Field S_UNKNOWN24.
	 */
	S_UNKNOWN24("sunk24", 0x00800000, true),
	/**
	 * Field S_UNKNOWN25.
	 */
	S_UNKNOWN25("sunk25", 0x01000000, true),
	/**
	 * Field S_UNKNOWN26.
	 */
	S_UNKNOWN26("sunk26", 0x02000000, true),
	/**
	 * Field S_UNKNOWN27.
	 */
	S_UNKNOWN27("sunk27", 0x04000000, true),
	/**
	 * Field S_UNKNOWN28.
	 */
	S_UNKNOWN28("sunk28", 0x08000000, true),
	/**
	 * Field S_UNKNOWN29.
	 */
	S_UNKNOWN29("sunk29", 0x10000000, true),
	/**
	 * Field S_UNKNOWN30.
	 */
	S_UNKNOWN30("sunk30", 0x20000000, true),
	/**
	 * Field S_UNKNOWN31.
	 */
	S_UNKNOWN31("sunk31", 0x40000000, true),
	/**
	 * Field S_UNKNOWN32.
	 */
	S_UNKNOWN32("sunk32", 0x80000000, true),
	/**
	 * Field E_AFRO_1.
	 */
	E_AFRO_1("afrobaguette1", 0x000001, false, true),
	/**
	 * Field E_AFRO_2.
	 */
	E_AFRO_2("afrobaguette2", 0x000002, false, true),
	/**
	 * Field E_AFRO_3.
	 */
	E_AFRO_3("afrobaguette3", 0x000004, false, true),
	/**
	 * Field E_EVASWRATH.
	 */
	E_EVASWRATH("evaswrath", 0x000008, false, true),
	/**
	 * Field E_HEADPHONE.
	 */
	E_HEADPHONE("headphone", 0x000010, false, true),
	/**
	 * Field E_VESPER_1.
	 */
	E_VESPER_1("vesper1", 0x000020, false, true),
	/**
	 * Field E_VESPER_2.
	 */
	E_VESPER_2("vesper2", 0x000040, false, true),
	/**
	 * Field E_VESPER_3.
	 */
	E_VESPER_3("vesper3", 0x000080, false, true);
	/**
	 * Field _mask.
	 */
	private final int _mask;
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _special.
	 */
	private final boolean _special;
	/**
	 * Field _event.
	 */
	private final boolean _event;
	
	/**
	 * Constructor for AbnormalEffect.
	 * @param name String
	 * @param mask int
	 */
	private AbnormalEffect(String name, int mask)
	{
		_name = name;
		_mask = mask;
		_special = false;
		_event = false;
	}
	
	/**
	 * Constructor for AbnormalEffect.
	 * @param name String
	 * @param mask int
	 * @param special boolean
	 */
	private AbnormalEffect(String name, int mask, boolean special)
	{
		_name = name;
		_mask = mask;
		_special = special;
		_event = false;
	}
	
	/**
	 * Constructor for AbnormalEffect.
	 * @param name String
	 * @param mask int
	 * @param special boolean
	 * @param event boolean
	 */
	private AbnormalEffect(String name, int mask, boolean special, boolean event)
	{
		_name = name;
		_mask = mask;
		_special = special;
		_event = event;
	}
	
	/**
	 * Method getMask.
	 * @return int
	 */
	public final int getMask()
	{
		return _mask;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public final String getName()
	{
		return _name;
	}
	
	/**
	 * Method isSpecial.
	 * @return boolean
	 */
	public final boolean isSpecial()
	{
		return _special;
	}
	
	/**
	 * Method isEvent.
	 * @return boolean
	 */
	public final boolean isEvent()
	{
		return _event;
	}
	
	/**
	 * Method getByName.
	 * @param name String
	 * @return AbnormalEffect
	 */
	public static AbnormalEffect getByName(String name)
	{
		for (AbnormalEffect eff : AbnormalEffect.values())
		{
			if (eff.getName().equals(name))
			{
				return eff;
			}
		}
		throw new NoSuchElementException("AbnormalEffect not found for name: '" + name + "'.\n Please check " + AbnormalEffect.class.getCanonicalName());
	}
}
