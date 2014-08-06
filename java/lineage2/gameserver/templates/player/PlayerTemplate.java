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
package lineage2.gameserver.templates.player;

import java.util.List;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.base.Sex;
import lineage2.gameserver.templates.CharTemplate;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.StartItem;
import lineage2.gameserver.utils.Location;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class PlayerTemplate extends CharTemplate
{
	private final Race _race;
	private final Sex _sex;
	private final StatAttributes _minAttr;
	private final StatAttributes _maxAttr;
	private final StatAttributes _baseAttr;
	private final BaseArmorDefence _armDef;
	private final BaseJewelDefence _jewlDef;
	@SuppressWarnings("unused")
	private final double collision_radius;
	@SuppressWarnings("unused")
	private final double collision_height;
	private int _baseRandDam = 0;
	private final double _baseSafeFallHeight;
	private double _baseBreathBonus = 0;
	private double _baseFlyRunSpd = 0;
	private double _baseFlyWalkSpd = 0;
	private double _baseRideRunSpd = 0;
	private double _baseRideWalkSpd = 0;
	private final List<Location> _startLocs;
	private final List<StartItem> _startItems;
	private final TIntObjectHashMap<LvlUpData> _lvlUpData;
	
	/**
	 * Constructor for PlayerTemplate.
	 * @param set StatsSet
	 * @param race Race
	 * @param sex Sex
	 * @param minAttr StatAttributes
	 * @param maxAttr StatAttributes
	 * @param baseAttr StatAttributes
	 * @param armDef BaseArmorDefence
	 * @param jewlDef BaseJewelDefence
	 * @param startLocs List<Location>
	 * @param startItems List<StartItem>
	 * @param lvlUpData TIntObjectHashMap<LvlUpData>
	 */
	public PlayerTemplate(StatsSet set, Race race, Sex sex, StatAttributes minAttr, StatAttributes maxAttr, StatAttributes baseAttr, BaseArmorDefence armDef, BaseJewelDefence jewlDef, List<Location> startLocs, List<StartItem> startItems, TIntObjectHashMap<LvlUpData> lvlUpData)
	{
		super(set);
		_race = race;
		_sex = sex;
		_minAttr = minAttr;
		_maxAttr = maxAttr;
		_baseAttr = baseAttr;
		_armDef = armDef;
		_jewlDef = jewlDef;
		_startLocs = startLocs;
		_startItems = startItems;
		_lvlUpData = lvlUpData;
		collision_radius = set.getDouble("collision_radius");
		collision_height = set.getDouble("collision_height");
		_baseRandDam = set.getInteger("baseRandDam");
		_baseSafeFallHeight = set.getDouble("baseSafeFallHeight");
		_baseBreathBonus = set.getDouble("baseBreathBonus");
		_baseFlyRunSpd = set.getDouble("baseFlyRunSpd");
		_baseFlyWalkSpd = set.getDouble("baseFlyWalkSpd");
		_baseRideRunSpd = set.getDouble("baseRideRunSpd");
		_baseRideWalkSpd = set.getDouble("baseRideWalkSpd");
	}
	
	/**
	 * Method getRace.
	 * @return Race
	 */
	public Race getRace()
	{
		return _race;
	}
	
	/**
	 * Method getSex.
	 * @return Sex
	 */
	public Sex getSex()
	{
		return _sex;
	}
	
	/**
	 * Method getMinAttr.
	 * @return StatAttributes
	 */
	public StatAttributes getMinAttr()
	{
		return _minAttr;
	}
	
	/**
	 * Method getMaxAttr.
	 * @return StatAttributes
	 */
	public StatAttributes getMaxAttr()
	{
		return _maxAttr;
	}
	
	/**
	 * Method getBaseAttr.
	 * @return StatAttributes
	 */
	@Override
	public StatAttributes getBaseAttr()
	{
		return _baseAttr;
	}
	
	/**
	 * Method getArmDef.
	 * @return BaseArmorDefence
	 */
	public BaseArmorDefence getArmDef()
	{
		return _armDef;
	}
	
	/**
	 * Method getJewlDef.
	 * @return BaseJewelDefence
	 */
	public BaseJewelDefence getJewlDef()
	{
		return _jewlDef;
	}
	
	/**
	 * Method getBaseRandDam.
	 * @return int
	 */
	public int getBaseRandDam()
	{
		return _baseRandDam;
	}
	
	/**
	 * Method getBaseBreathBonus.
	 * @return double
	 */
	public double getBaseBreathBonus()
	{
		return _baseBreathBonus;
	}
	
	/**
	 * Method getBaseSafeFallHeight.
	 * @return double
	 */
	public double getBaseSafeFallHeight()
	{
		return _baseSafeFallHeight;
	}
	
	/**
	 * Method getBaseHpReg.
	 * @param lvl int
	 * @return double
	 */
	public double getBaseHpReg(int lvl)
	{
		return getLvlUpData(lvl).getHP();
	}
	
	/**
	 * Method getBaseMpReg.
	 * @param lvl int
	 * @return double
	 */
	public double getBaseMpReg(int lvl)
	{
		return getLvlUpData(lvl).getMP();
	}
	
	/**
	 * Method getBaseCpReg.
	 * @param lvl int
	 * @return double
	 */
	public double getBaseCpReg(int lvl)
	{
		return getLvlUpData(lvl).getCP();
	}
	
	/**
	 * Method getBaseFlyRunSpd.
	 * @return double
	 */
	public double getBaseFlyRunSpd()
	{
		return _baseFlyRunSpd;
	}
	
	/**
	 * Method getBaseFlyWalkSpd.
	 * @return double
	 */
	public double getBaseFlyWalkSpd()
	{
		return _baseFlyWalkSpd;
	}
	
	/**
	 * Method getBaseRideRunSpd.
	 * @return double
	 */
	public double getBaseRideRunSpd()
	{
		return _baseRideRunSpd;
	}
	
	/**
	 * Method getBaseRideWalkSpd.
	 * @return double
	 */
	public double getBaseRideWalkSpd()
	{
		return _baseRideWalkSpd;
	}
	
	/**
	 * Method getLvlUpData.
	 * @param lvl int
	 * @return LvlUpData
	 */
	public LvlUpData getLvlUpData(int lvl)
	{
		return _lvlUpData.get(lvl);
	}
	
	/**
	 * Method getStartItems.
	 * @return StartItem[]
	 */
	public StartItem[] getStartItems()
	{
		return _startItems.toArray(new StartItem[_startItems.size()]);
	}
	
	/**
	 * Method getStartLocation.
	 * @return Location
	 */
	public Location getStartLocation()
	{
		return _startLocs.get(Rnd.get(_startLocs.size()));
	}
}
