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
package lineage2.gameserver.model.actor.instances.player;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SubClass;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.ClassType;
import lineage2.gameserver.model.base.Race;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SubClassInfo
{
	private static final Set<ClassId> MAIN_SUBCLASS_SET;
	private static final Set<ClassId> MAIN_SUBCLASS_SET1;
	private static final Set<ClassId> KAMAEL_SUBCLASS_SET;
	private static final Set<ClassId> BANNED_SUBCLASSES = EnumSet.of(ClassId.OVERLORD, ClassId.WARSMITH);
	private static final Set<ClassId> SUBCLASS_SET_1 = EnumSet.of(ClassId.DARK_AVENGER, ClassId.PALADIN, ClassId.TEMPLE_KNIGHT, ClassId.SHILLEN_KNIGHT);
	private static final Set<ClassId> SUBCLASS_SET_2 = EnumSet.of(ClassId.TREASURE_HUNTER, ClassId.ABYSS_WALKER, ClassId.PLAIN_WALKER);
	private static final Set<ClassId> SUBCLASS_SET_3 = EnumSet.of(ClassId.HAWKEYE, ClassId.SILVER_RANGER, ClassId.PHANTOM_RANGER);
	private static final Set<ClassId> SUBCLASS_SET_4 = EnumSet.of(ClassId.WARLOCK, ClassId.ELEMENTAL_SUMMONER, ClassId.PHANTOM_SUMMONER);
	private static final Set<ClassId> SUBCLASS_SET_5 = EnumSet.of(ClassId.SORCERER, ClassId.SPELLSINGER, ClassId.SPELLHOWLER);
	private static final Set<ClassId> SUBCLASS_SET_6 = EnumSet.of(ClassId.INSPECTOR);
	
	private static final EnumMap<ClassId, Set<ClassId>> SUBCLASS_SET_MAP = new EnumMap<>(ClassId.class);
	
	/**
	 * Method getAvailableSubClasses.
	 * @param player Player
	 * @param npcRace Race
	 * @param npcTeachType ClassType
	 * @param isNew boolean
	 * @return Set<ClassId>
	 */
	public static Set<ClassId> getAvailableSubClasses(Player player, Race npcRace, ClassType npcTeachType, boolean isNew)
	{
		Set<ClassId> availSubs = null;
		Set<ClassId> availSubs1 = null;
		Race race = player.getRace();
		
		if (race == Race.kamael)
		{
			availSubs = EnumSet.copyOf(KAMAEL_SUBCLASS_SET);
		}
		else
		{
			ClassId classId = player.getClassId();
			
			if ((classId.isOfLevel(ClassLevel.Second)) || (classId.isOfLevel(ClassLevel.Third)) || (classId.isOfLevel(ClassLevel.Fourth)))
			{
				availSubs = EnumSet.copyOf(MAIN_SUBCLASS_SET);
				availSubs1 = EnumSet.copyOf(MAIN_SUBCLASS_SET1);
				availSubs.removeAll(BANNED_SUBCLASSES);
				availSubs.remove(classId);
				availSubs1.removeAll(BANNED_SUBCLASSES);
				availSubs1.remove(classId);
				
				switch (race.ordinal())
				{
					case 1:
						availSubs.removeAll(getSet(Race.darkelf, ClassLevel.Second));
						break;
					
					case 2:
						availSubs.removeAll(getSet(Race.elf, ClassLevel.Second));
						break;
				}
				
				switch (race.ordinal())
				{
					case 1:
						availSubs1.removeAll(getSet(Race.darkelf, ClassLevel.Fourth));
						break;
					
					case 2:
						availSubs1.removeAll(getSet(Race.elf, ClassLevel.Fourth));
						break;
				}
				
				Set<?> unavailableClasses = SUBCLASS_SET_MAP.get(classId);
				
				if (unavailableClasses != null)
				{
					availSubs.removeAll(unavailableClasses);
					availSubs1.removeAll(unavailableClasses);
				}
			}
		}
		
		int charClassId = player.getBaseClassId();
		ClassId currClass = ClassId.VALUES[charClassId];
		
		if (availSubs == null)
		{
			return Collections.emptySet();
		}
		
		availSubs.remove(currClass);
		
		for (ClassId availSub : availSubs)
		{
			for (SubClass subClass : player.getSubClassList().values())
			{
				if (availSub.getId() == subClass.getClassId())
				{
					availSubs.remove(availSub);
					continue;
				}
				
				ClassId parent = ClassId.VALUES[availSub.ordinal()].getParent(player.getSex());
				
				if ((parent != null) && (parent.getId() == subClass.getClassId()))
				{
					availSubs.remove(availSub);
					continue;
				}
				
				ClassId subParent = ClassId.VALUES[subClass.getClassId()].getParent(player.getSex());
				
				if ((subParent != null) && (subParent.getId() == availSub.getId()))
				{
					availSubs.remove(availSub);
				}
				
				if ((availSub.getType2() == ClassId.VALUES[subClass.getClassId()].getType2()) && subClass.isBase())
				{
					availSubs.remove(availSub);
				}
			}
			
			if (availSub.isOfRace(Race.kamael))
			{
				if ((player.getSex() == 1) && (availSub == ClassId.M_SOUL_BREAKER))
				{
					availSubs.remove(availSub);
				}
				
				if ((player.getSex() == 0) && (availSub == ClassId.F_SOUL_BREAKER))
				{
					availSubs.remove(availSub);
				}
				
				if (((currClass == ClassId.BERSERKER) || (currClass == ClassId.DOOMBRINGER) || (currClass == ClassId.ARBALESTER) || (currClass == ClassId.TRICKSTER)) && (((player.getSex() == 1) && (availSub == ClassId.M_SOUL_BREAKER)) || ((player.getSex() == 0) && (availSub == ClassId.F_SOUL_BREAKER))))
				{
					availSubs.remove(availSub);
				}
				
				if (availSub == ClassId.INSPECTOR)
				{
					if (player.getSubClassList().size() < (isNew ? 3 : 4))
					{
						availSubs.remove(availSub);
					}
				}
			}
		}
		
		return availSubs;
	}
	
	/**
	 * Method getSet.
	 * @param race Race
	 * @param level ClassLevel
	 * @return EnumSet<ClassId>
	 */
	private static EnumSet<ClassId> getSet(Race race, ClassLevel level)
	{
		EnumSet<ClassId> allOf = EnumSet.noneOf(ClassId.class);
		
		for (ClassId classId : EnumSet.allOf(ClassId.class))
		{
			if (((race == null) || (classId.isOfRace(race))) && ((level == null) || (classId.isOfLevel(level))))
			{
				allOf.add(classId);
			}
		}
		
		return allOf;
	}
	
	/**
	 * Method areClassesComportable.
	 * @param c1 ClassId
	 * @param c2 ClassId
	 * @return boolean
	 */
	public static boolean areClassesComportable(ClassId c1, ClassId c2)
	{
		if (c1.isOfRace(Race.kamael) != c2.isOfRace(Race.kamael))
		{
			return false;
		}
		
		if (((c1.isOfRace(Race.elf)) && (c2.isOfRace(Race.darkelf))) || ((c1.isOfRace(Race.darkelf)) && (c2.isOfRace(Race.elf))))
		{
			return false;
		}
		
		if ((c1 == ClassId.OVERLORD) || (c1 == ClassId.WARSMITH) || (c2 == ClassId.OVERLORD) || (c2 == ClassId.WARSMITH))
		{
			return false;
		}
		
		return SUBCLASS_SET_MAP.get(c1) != SUBCLASS_SET_MAP.get(c2);
	}
	
	static
	{
		KAMAEL_SUBCLASS_SET = getSet(Race.kamael, ClassLevel.Second);
		MAIN_SUBCLASS_SET = getSet(null, ClassLevel.Second);
		MAIN_SUBCLASS_SET1 = getSet(null, ClassLevel.Fourth);
		MAIN_SUBCLASS_SET.removeAll(BANNED_SUBCLASSES);
		MAIN_SUBCLASS_SET.removeAll(KAMAEL_SUBCLASS_SET);
		SUBCLASS_SET_MAP.put(ClassId.DARK_AVENGER, SUBCLASS_SET_1);
		SUBCLASS_SET_MAP.put(ClassId.HELL_KNIGHT, SUBCLASS_SET_1);
		SUBCLASS_SET_MAP.put(ClassId.PALADIN, SUBCLASS_SET_1);
		SUBCLASS_SET_MAP.put(ClassId.PHOENIX_KNIGHT, SUBCLASS_SET_1);
		SUBCLASS_SET_MAP.put(ClassId.TEMPLE_KNIGHT, SUBCLASS_SET_1);
		SUBCLASS_SET_MAP.put(ClassId.EVAS_TEMPLAR, SUBCLASS_SET_1);
		SUBCLASS_SET_MAP.put(ClassId.SHILLEN_KNIGHT, SUBCLASS_SET_1);
		SUBCLASS_SET_MAP.put(ClassId.SHILLIEN_TEMPLAR, SUBCLASS_SET_1);
		SUBCLASS_SET_MAP.put(ClassId.TREASURE_HUNTER, SUBCLASS_SET_2);
		SUBCLASS_SET_MAP.put(ClassId.ADVENTURER, SUBCLASS_SET_2);
		SUBCLASS_SET_MAP.put(ClassId.ABYSS_WALKER, SUBCLASS_SET_2);
		SUBCLASS_SET_MAP.put(ClassId.GHOST_HUNTER, SUBCLASS_SET_2);
		SUBCLASS_SET_MAP.put(ClassId.PLAIN_WALKER, SUBCLASS_SET_2);
		SUBCLASS_SET_MAP.put(ClassId.WIND_RIDER, SUBCLASS_SET_2);
		SUBCLASS_SET_MAP.put(ClassId.HAWKEYE, SUBCLASS_SET_3);
		SUBCLASS_SET_MAP.put(ClassId.SAGITTARIUS, SUBCLASS_SET_3);
		SUBCLASS_SET_MAP.put(ClassId.SILVER_RANGER, SUBCLASS_SET_3);
		SUBCLASS_SET_MAP.put(ClassId.MOONLIGHT_SENTINEL, SUBCLASS_SET_3);
		SUBCLASS_SET_MAP.put(ClassId.PHANTOM_RANGER, SUBCLASS_SET_3);
		SUBCLASS_SET_MAP.put(ClassId.GHOST_SENTINEL, SUBCLASS_SET_3);
		SUBCLASS_SET_MAP.put(ClassId.WARLOCK, SUBCLASS_SET_4);
		SUBCLASS_SET_MAP.put(ClassId.ARCANA_LORD, SUBCLASS_SET_4);
		SUBCLASS_SET_MAP.put(ClassId.ELEMENTAL_SUMMONER, SUBCLASS_SET_4);
		SUBCLASS_SET_MAP.put(ClassId.ELEMENTAL_MASTER, SUBCLASS_SET_4);
		SUBCLASS_SET_MAP.put(ClassId.PHANTOM_SUMMONER, SUBCLASS_SET_4);
		SUBCLASS_SET_MAP.put(ClassId.SPECTRAL_MASTER, SUBCLASS_SET_4);
		SUBCLASS_SET_MAP.put(ClassId.SORCERER, SUBCLASS_SET_5);
		SUBCLASS_SET_MAP.put(ClassId.ARCHMAGE, SUBCLASS_SET_5);
		SUBCLASS_SET_MAP.put(ClassId.SPELLSINGER, SUBCLASS_SET_5);
		SUBCLASS_SET_MAP.put(ClassId.MYSTIC_MUSE, SUBCLASS_SET_5);
		SUBCLASS_SET_MAP.put(ClassId.SPELLHOWLER, SUBCLASS_SET_5);
		SUBCLASS_SET_MAP.put(ClassId.STORM_SCREAMER, SUBCLASS_SET_5);
		SUBCLASS_SET_MAP.put(ClassId.SOULTAKER, EnumSet.of(ClassId.NECROMANCER));
		SUBCLASS_SET_MAP.put(ClassId.DOOMBRINGER, SUBCLASS_SET_6);
		SUBCLASS_SET_MAP.put(ClassId.M_SOUL_HOUND, SUBCLASS_SET_6);
		SUBCLASS_SET_MAP.put(ClassId.F_SOUL_HOUND, SUBCLASS_SET_6);
		SUBCLASS_SET_MAP.put(ClassId.TRICKSTER, SUBCLASS_SET_6);
		SUBCLASS_SET_MAP.put(ClassId.DUELIST, EnumSet.of(ClassId.GLADIATOR));
		SUBCLASS_SET_MAP.put(ClassId.DREADNOUGHT, EnumSet.of(ClassId.WARLORD));
		SUBCLASS_SET_MAP.put(ClassId.TITAN, EnumSet.of(ClassId.DESTROYER));
		SUBCLASS_SET_MAP.put(ClassId.GRAND_KHAVATARI, EnumSet.of(ClassId.TYRANT));
		SUBCLASS_SET_MAP.put(ClassId.CARDINAL, EnumSet.of(ClassId.BISHOP));
		SUBCLASS_SET_MAP.put(ClassId.EVAS_SAINT, EnumSet.of(ClassId.ELDER));
		SUBCLASS_SET_MAP.put(ClassId.HIEROPHANT, EnumSet.of(ClassId.PROPHET));
		SUBCLASS_SET_MAP.put(ClassId.SWORD_MUSE, EnumSet.of(ClassId.SWORDSINGER));
		SUBCLASS_SET_MAP.put(ClassId.SPECTRAL_DANCER, EnumSet.of(ClassId.BLADEDANCER));
		SUBCLASS_SET_MAP.put(ClassId.DOMINATOR, EnumSet.of(ClassId.OVERLORD));
		SUBCLASS_SET_MAP.put(ClassId.DOOMCRYER, EnumSet.of(ClassId.WARCRYER));
	}
}
