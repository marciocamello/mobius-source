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
package lineage2.gameserver.tables;

import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.model.Skill;
import lineage2.gameserver.skills.SkillsEngine;
import gnu.trove.map.hash.TIntIntHashMap;

/**
 * @author Mobius
 */
public class SkillTable
{
	private static final SkillTable _instance = new SkillTable();
	public final Map<Integer, Integer> identifySkills = new HashMap<>();
	private Map<Integer, Skill> _skills;
	private TIntIntHashMap _maxLevelsTable;
	private TIntIntHashMap _baseLevelsTable;
	
	/**
	 * Method getInstance.
	 * @return SkillTable
	 */
	public static final SkillTable getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method load.
	 */
	public void load()
	{
		_skills = SkillsEngine.getInstance().loadAllSkills();
		makeLevelsTable();
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		load();
	}
	
	/**
	 * Method getInfo.
	 * @param skillId int
	 * @param level int
	 * @return Skill
	 */
	public Skill getInfo(int skillId, int level)
	{
		return _skills.get(getSkillHashCode(skillId, level));
	}
	
	/**
	 * Method getMaxLevel.
	 * @param skillId int
	 * @return int
	 */
	public int getMaxLevel(int skillId)
	{
		return _maxLevelsTable.get(skillId);
	}
	
	/**
	 * Method getBaseLevel.
	 * @param skillId int
	 * @return int
	 */
	public int getBaseLevel(int skillId)
	{
		return _baseLevelsTable.get(skillId);
	}
	
	/**
	 * Method getSkillHashCode.
	 * @param skill Skill
	 * @return int
	 */
	public static int getSkillHashCode(Skill skill)
	{
		return SkillTable.getSkillHashCode(skill.getId(), skill.getLevel());
	}
	
	/**
	 * Method getSkillHashCode.
	 * @param skillId int
	 * @param skillLevel int
	 * @return int
	 */
	public static int getSkillHashCode(int skillId, int skillLevel)
	{
		return (skillId * 1000) + skillLevel;
	}
	
	/**
	 * Method makeLevelsTable.
	 */
	private void makeLevelsTable()
	{
		_maxLevelsTable = new TIntIntHashMap();
		_baseLevelsTable = new TIntIntHashMap();
		
		for (Skill s : _skills.values())
		{
			int skillId = s.getId();
			int level = s.getLevel();
			int maxLevel = _maxLevelsTable.get(skillId);
			
			if (level > maxLevel)
			{
				_maxLevelsTable.put(skillId, level);
			}
			
			if (_baseLevelsTable.get(skillId) == 0)
			{
				_baseLevelsTable.put(skillId, s.getBaseLevel());
			}
		}
	}
}
