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
package lineage2.gameserver.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lineage2.gameserver.model.base.Race;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SkillLearn implements Comparable<SkillLearn>
{
	private final int _id;
	private final int _level;
	private final int _minLevel;
	private final int _cost;
	private final int _itemId;
	private final long _itemCount;
	private final boolean _clicked;
	private final boolean _deprecated;
	private final Race _race;
	private final Map<Integer, Long> required_items;
	private final List<Integer> delete_skills;
	private final int _dualClassMinLvl;
	
	/**
	 * Constructor for SkillLearn.
	 * @param id int
	 * @param lvl int
	 * @param minLvl int
	 * @param cost int
	 * @param itemId int
	 * @param itemCount long
	 * @param clicked boolean
	 * @param deprecated
	 * @param race
	 * @param required_items Map<Integer,Long>
	 * @param delete_skills List<Integer>
	 */
	public SkillLearn(int id, int lvl, int minLvl, int cost, int itemId, long itemCount, boolean clicked, boolean deprecated, Race race, Map<Integer, Long> required_items, List<Integer> delete_skills)
	{
		_id = id;
		_level = lvl;
		_minLevel = minLvl;
		_cost = cost;
		_itemId = itemId;
		_itemCount = itemCount;
		_clicked = clicked;
		_deprecated = deprecated;
		_race = race;
		this.required_items = required_items;
		this.delete_skills = delete_skills;
		_dualClassMinLvl = 0; // TODO: Implement this !!!
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		return _level;
	}
	
	/**
	 * Method getMinLevel.
	 * @return int
	 */
	public int getMinLevel()
	{
		return _minLevel;
	}
	
	/**
	 * Method getDualClassMinLvl.
	 * @return int
	 */
	public int getDualClassMinLvl()
	{
		return _dualClassMinLvl;
	}
	
	/**
	 * Method getCost.
	 * @return int
	 */
	public int getCost()
	{
		return _cost;
	}
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return _itemId;
	}
	
	/**
	 * Method getItemCount.
	 * @return long
	 */
	public long getItemCount()
	{
		return _itemCount;
	}
	
	/**
	 * Method isClicked.
	 * @return boolean
	 */
	public boolean isClicked()
	{
		return _clicked;
	}
	
	/**
	 * Method isClicked.
	 * @return boolean
	 */
	public boolean isDeprecated()
	{
		return _deprecated;
	}
	
	public Race getRace()
	{
		return _race;
	}
	
	public boolean isOfRace(Race race)
	{
		return (_race == null) || (_race == race);
	}
	
	/**
	 * Method compareTo.
	 * @param o SkillLearn
	 * @return int
	 */
	@Override
	public int compareTo(SkillLearn o)
	{
		if (getId() == o.getId())
		{
			return getLevel() - o.getLevel();
		}
		
		return getId() - o.getId();
	}
	
	/**
	 * Method getRequiredItems.
	 * @return Map<Integer,Long>
	 */
	public Map<Integer, Long> getRequiredItems()
	{
		return Collections.unmodifiableMap(required_items);
	}
	
	/**
	 * Method getDeleteSkills.
	 * @return List<Integer>
	 */
	public List<Integer> getDeleteSkills()
	{
		return Collections.unmodifiableList(delete_skills);
	}
	
	/**
	 * Method getRemovedSkillsForPlayer.
	 * @param player Player
	 * @return List<Skill>
	 */
	public List<Skill> getRemovedSkillsForPlayer(Player player)
	{
		List<Skill> skills = new ArrayList<>();
		
		for (int skill_id : getDeleteSkills())
		{
			if (player.getKnownSkill(skill_id) != null)
			{
				skills.add(player.getKnownSkill(skill_id));
			}
		}
		
		return skills;
	}
}
