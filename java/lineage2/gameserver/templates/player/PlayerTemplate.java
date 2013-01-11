/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.templates.player;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.base.Sex;
import lineage2.gameserver.templates.CharTemplate;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.StartItem;
import lineage2.gameserver.utils.Location;

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
	
	public Race getRace()
	{
		return _race;
	}
	
	public Sex getSex()
	{
		return _sex;
	}
	
	public StatAttributes getMinAttr()
	{
		return _minAttr;
	}
	
	public StatAttributes getMaxAttr()
	{
		return _maxAttr;
	}
	
	@Override
	public StatAttributes getBaseAttr()
	{
		return _baseAttr;
	}
	
	public BaseArmorDefence getArmDef()
	{
		return _armDef;
	}
	
	public BaseJewelDefence getJewlDef()
	{
		return _jewlDef;
	}
	
	public int getBaseRandDam()
	{
		return _baseRandDam;
	}
	
	public double getBaseBreathBonus()
	{
		return _baseBreathBonus;
	}
	
	public double getBaseSafeFallHeight()
	{
		return _baseSafeFallHeight;
	}
	
	public double getBaseHpReg(int lvl)
	{
		return getLvlUpData(lvl).getHP();
	}
	
	public double getBaseMpReg(int lvl)
	{
		return getLvlUpData(lvl).getMP();
	}
	
	public double getBaseCpReg(int lvl)
	{
		return getLvlUpData(lvl).getCP();
	}
	
	public double getBaseFlyRunSpd()
	{
		return _baseFlyRunSpd;
	}
	
	public double getBaseFlyWalkSpd()
	{
		return _baseFlyWalkSpd;
	}
	
	public double getBaseRideRunSpd()
	{
		return _baseRideRunSpd;
	}
	
	public double getBaseRideWalkSpd()
	{
		return _baseRideWalkSpd;
	}
	
	public LvlUpData getLvlUpData(int lvl)
	{
		return _lvlUpData.get(lvl);
	}
	
	public StartItem[] getStartItems()
	{
		return _startItems.toArray(new StartItem[_startItems.size()]);
	}
	
	public Location getStartLocation()
	{
		return _startLocs.get(Rnd.get(_startLocs.size()));
	}
}
