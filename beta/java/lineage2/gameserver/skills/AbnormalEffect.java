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
	NULL("null", 0x0, 0),
	BLEEDING("bleeding", 0x000001, 1),
	POISON("poison", 0x000002, 2),
	REDCIRCLE("redcircle", 0x000004, 3),
	ICE("ice", 0x000008, 4),
	WIND("wind", 0x000010, 5),
	FEAR("fear", 0x000020, 6),
	STUN("stun", 0x000040, 7),
	SLEEP("sleep", 0x000080, 8),
	MUTED("mute", 0x000100, 9),
	ROOT("root", 0x000200, 10),
	HOLD_1("hold1", 0x000400, 11),
	HOLD_2("hold2", 0x000800, 12),
	UNKNOWN_13("unknown13", 0x001000, 13),
	BIG_HEAD("bighead", 0x002000, 14),
	FLAME("flame", 0x004000, 15),
	ARCANE_SHIELD("arcane_shield", 0x008000, 16),
	GROW("grow", 0x010000, 17),
	FLOATING_ROOT("floatroot", 0x020000, 18),
	DANCE_STUNNED("dancestun", 0x040000, 19),
	FIREROOT_STUN("firerootstun", 0x080000, 20),
	STEALTH("shadow", 0x100000, 21),
	IMPRISIONING_1("imprison1", 0x200000, 22),
	IMPRISIONING_2("imprison2", 0x400000, 23),
	MAGIC_CIRCLE("magiccircle", 0x800000, 24),
	ICE2("ice2", 0x1000000, 25),
	EARTHQUAKE("earthquake", 0x2000000, 26),
	UNKNOWN_27("unknown27", 0x4000000, 27),
	INVULNERABLE("invul1", 0x8000000, 28),
	VITALITY("vitality", 0x10000000, 29),
	REAL_TARGET("realtarget", 0x20000000, 30),
	DEATH_MARK("deathmark", 0x40000000, 31),
	SKULL_FEAR("soulshock", 0x80000000, 32),
	// Special effects
	S_INVINCIBLE("invul2", 0x000001, 33),
	S_AIR_STUN("airstun", 0x000002, 34),
	S_AIR_ROOT("airroot", 0x000004, 35),
	S_BAGUETTE_SWORD("baguettesword", 0x000008, 36),
	S_YELLOW_AFFRO("yellowafro", 0x000010, 37),
	S_PINK_AFFRO("pinkafro", 0x000020, 38),
	S_BLACK_AFFRO("blackafro", 0x000040, 39),
	S_UNKNOWN8("unknown8", 0x000080, 40),
	S_STIGMA_SHILIEN("stigma", 0x000100, 41),
	S_STAKATOROOT("stakatoroot", 0x000200, 42),
	S_FREEZING("frozenpillar", 0x000400, 43),
	S_VESPER_S("vesper_s", 0x000800, 44),
	S_VESPER_C("vesper_c", 0x001000, 45),
	S_VESPER_D("vesper_d", 0x002000, 46),
	S_47("s_47", 0x004000, 47),
	S_48("s_48", 0x008000, 48),
	S_HELLBINDING("hellbinding", 0x010000, 49),
	S_50("s_50", 0x020000, 50),
	S_51("s_51", 0x040000, 51),
	S_52("s_52", 0x080000, 52),
	S_53("s_53", 0x100000, 53),
	S_54("s_54", 0x200000, 57),
	S_55("s_55", 0x400000, 55),
	S_56("s_56", 0x800000, 56),
	S_57("s_57", 0x1000000, 57),
	S_58("s_58", 0x2000000, 58),
	S_59("s_59", 0x4000000, 59),
	S_60("s_60", 0x8000000, 60),
	S_61("s_61", 0x10000000, 61),
	S_ROLLINGTHUNDER("rollingThunder", 0x20000000, 62),
	S_63("s_63", 0x40000000, 63),
	S_64("s_64", 0x80000000, 64),
	S_65("s_65", 0x000001, 65),
	S_66("s_66", 0x000002, 66),
	S_67("s_67", 0x000004, 67),
	MUTE_LIFESTONE("mute_lifestone", 0x000001, 68), // Mute LifeStone
	AURA_SIGIL_1("aura_sigil_1", 0x000001, 69), // Sigil Aura 1
	AURA_SIGIL_2("aura_sigil_2", 0x000001, 70), // Sigil Aura 2
	TALISMAN_INSANITY("talismaninsanity", 0x000001, 71), // Talisman - Insanity
	TALISMAN_POWER1("talismanpower1", 0x100000, 72), // Talisman - 1
	TALISMAN_POWER2("talismanpower2", 0x200000, 73), // Talisman - 2
	TALISMAN_POWER3("talismanpower3", 0x300000, 74), // Talisman - 3
	TALISMAN_POWER4("talismanpower4", 0x400000, 75), // Talisman - 4
	TALISMAN_POWER5("talismanpower5", 0x500000, 76), // Talisman - 5
	// App. Abnormals
	CHAOS_FESTIVAL("chaosfestival", 0x000001, 77), // Chaos Festival
	APP_1("app1", 0x000001, 78), // App - 1
	APP_2("app2", 0x000001, 79), // App - 2
	APP_3("app3", 0x000001, 80), // App - 3
	APP_4("app4", 0x000001, 81), // App - 4
	APP_5("app5", 0x000001, 81), // App - 5
	APP_6("app6", 0x000001, 82), // App - 6
	APP_7("app7", 0x000001, 83), // App - 7
	APP_8("app8", 0x000001, 84), // App - 8
	APP_9("app9", 0x000001, 85), // Santa App
	APP_10("app10", 0x000001, 86), // App - 9
	APP_11("app11", 0x000001, 87), // App - 10
	APP_WEAPON("app_weapon", 0x000001, 88), // App Weapon
	APP_WEAPON1("app_weapon1", 0x000001, 88), // App Weapon
	// Unknown abnormal
	ABNORMAL_EVENT2("abnormal_event2", 0x000001, 89), // Event
	ABNORMAL_EVENT3("abnormal_event3", 0x000001, 90), // Event
	ABNORMAL_EVENT4("abnormal_event4", 0x000001, 91), // Event
	ABNORMAL_EVENT5("abnormal_event5", 0x000001, 92), // Event Star 0/5
	ABNORMAL_EVENT6("abnormal_event6", 0x000001, 93), // Event Star 1/5
	ABNORMAL_EVENT7("abnormal_event7", 0x000001, 94), // Event Star 2/5
	ABNORMAL_EVENT8("abnormal_event8", 0x000001, 95), // Event Star 3/5
	ABNORMAL_EVENT9("abnormal_event9", 0x000001, 96), // Event Star 4/5
	ABNORMAL_EVENT10("abnormal_event10", 0x000001, 97), // Event Star 5/5
	// New skills abnormals
	FACE_TO_FACE("facetoface", 0x000001, 98), // Skill Face to Face
	ICE_TEMPLE_KNIGHT("icetempleknight", 0x000001, 99), // Ice Abnormal
	// App 2
	ROBO_APP("robo_app", 0x000001, 100), // Robocop app.
	// Event
	ABNORMAL_EVENT11("abnormal_event11", 0x000001, 101), // Event
	ABNORMAL_EVENT12("abnormal_event12", 0x000001, 102), // Music Event
	ABNORMAL_EVENT13("abnormal_event13", 0x000001, 103), // Music Event
	ABNORMAL_EVENT14("abnormal_event14", 0x000001, 104), // Music Event
	ABNORMAL_EVENT15("abnormal_event15", 0x000001, 105), // Lineage II Icon Event
	// Christmas Event Effects
	U_SANTA_SOCKS_AVE("santa_socks_ave", 0x000001, 106),
	U_SANTA_TREE_AVE("santa_tree_ave", 0x000001, 107),
	U_SANTA_SNOWMAN_AVE("santa_snowman_ave", 0x000001, 108),
	EVENT_FIREWORK("event_firework", 0x000001, 109), // Firework Effect
	EVENT_GLOW("event_glow", 0x000001, 110), // Glow Effect
	EVENT_STIGMA("event_stigma", 0x000001, 111), // Stigma Effect
	// NEW ERTHEIA EFFECTS
	U_ER_WI_WINDSTUN_AVE("windstun_ave", 0x000001, 112),
	U_ER_WI_STORMSIGN2_AVE("stormsign2_ave", 0x000001, 113),
	U_ER_WI_STORMSIGN1_AVE("stormsign1_ave", 0x000001, 114),
	U_ER_WI_WINDHIDE_AVE("windhide_ave", 0x000001, 115),
	U_ER_WI_PSYPOWER_AVE("psypower_ave", 0x000001, 116),
	U_ER_WI_SQUALL_AVE("squall_ave", 0x000001, 117),
	U_ER_WI_ILLUWIND_AVE("illuwind_ave", 0x000001, 118),
	U_ER_WI_WIND_FORCE("wind_force", 0x000001, 119),
	U_ER_WI_ERTHEIA_1("ertheia_1", 0x000001, 120),
	U_ER_WI_ERTHEIA_2("ertheia_2", 0x000001, 121),
	U_ER_WI_ERTHEIA_3("ertheia_3", 0x000001, 122),
	U_ER_WI_ERTHEIA_4("ertheia_4", 0x000001, 123),
	U_ER_WI_ERTHEIA_5("ertheia_5", 0x000001, 124), // Ertheia maybe skill effect
	U_ER_WI_HOLD_AVE("hold_ave", 0x000001, 125), // Ertheia fighter skill effect
	U_ER_WI_LIGHTNINT2_AVE("lightning2_ave", 0x000001, 126), // Ertheia fighter skill effect
	U_ER_WA_SPACEREF_AVE("spaceref_ave", 0x000001, 127),
	U_HE_ASPECT_AVE("aspect_ave", 0x000001, 128),
	// App maybe effects
	APP_TAIWAN("app_taiwan", 0x000001, 130), // Taiwan Armor App
	APP_MILITARY("app_military", 0x000001, 131), // Military Armor App
	APP_IRONMAN("app_ironman", 0x000001, 132), // IronMan Armor App
	APP_LOLEFFECT("app_loleffect", 0x000001, 133), // LOL Armor App
	APP_ARMOR("app_armor", 0x000001, 134), // Armor App
	APP_WEAPON2("app_weapon2", 0x000001, 135), // Weapon App
	APP_WEAPON3("app_weapon3", 0x000001, 136), // Weapon App
	APP_WEAPON4("app_weapon4", 0x000001, 136), // Weapon App
	// Event effects
	E_AFRO_1("afrobaguette1", 0x000001, 0),
	E_AFRO_2("afrobaguette2", 0x000002, 0),
	E_AFRO_3("afrobaguette3", 0x000004, 0),
	E_EVASWRATH("evaswrath", 0x000008, 0),
	E_HEADPHONE("headphone", 0x000010, 0),
	E_VESPER_1("vesper1", 0x000020, 0),
	E_VESPER_2("vesper2", 0x000040, 0),
	E_VESPER_3("vesper3", 0x000080, 0),
	HUNTING_BONUS("hunting_bonus", 0x80000, 0),
	AVE_ADVENT_BLESSING("ave_advent_blessing", 0x080000, 0); // Add NevitAdvent
	private final int _mask;
	private final String _name;
	private final int _id;
	
	private AbnormalEffect(String name, int mask, int id)
	{
		_name = name;
		_mask = mask;
		_id = id;
	}
	
	/**
	 * Method getMask.
	 * @return integer
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
	 * Method getName.
	 * @return integer
	 */
	public final int getId()
	{
		return _id;
	}
	
	/**
	 * Method isSpecial.
	 * @return boolean
	 */
	public final boolean isSpecial()
	{
		return getId() > 32;
	}
	
	/**
	 * Method isEvent.
	 * @return boolean
	 */
	public final boolean isEvent()
	{
		return false;
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