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

public final class SkillLearn implements Comparable<SkillLearn>
{
	private final int _id;
	private final int _level;
	private final int _minLevel;
	private final int _cost;
	private final int _itemId;
	private final long _itemCount;
	private final boolean _clicked;
	private final Map<Integer, Long> required_items;
	private final List<Integer> delete_skills;
	
	public SkillLearn(int id, int lvl, int minLvl, int cost, int itemId, long itemCount, boolean clicked, Map<Integer, Long> required_items, List<Integer> delete_skills)
	{
		_id = id;
		_level = lvl;
		_minLevel = minLvl;
		_cost = cost;
		_itemId = itemId;
		_itemCount = itemCount;
		_clicked = clicked;
		this.required_items = required_items;
		this.delete_skills = delete_skills;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public int getMinLevel()
	{
		return _minLevel;
	}
	
	public int getCost()
	{
		return _cost;
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public long getItemCount()
	{
		return _itemCount;
	}
	
	public boolean isClicked()
	{
		return _clicked;
	}
	
	@Override
	public int compareTo(SkillLearn o)
	{
		if (getId() == o.getId())
		{
			return getLevel() - o.getLevel();
		}
		return getId() - o.getId();
	}
	
	public Map<Integer, Long> getRequiredItems()
	{
		return Collections.unmodifiableMap(required_items);
	}
	
	public List<Integer> getDeleteSkills()
	{
		return Collections.unmodifiableList(delete_skills);
	}
	
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
