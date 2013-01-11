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
package lineage2.gameserver.model;

public class IconEffect
{
	private final int _skillId;
	private final int _level;
	private final int _duration;
	private final int _obj;
	
	public IconEffect(int skillId, int level, int duration, int obj)
	{
		_skillId = skillId;
		_level = level;
		_duration = duration;
		_obj = obj;
	}
	
	public int getSkillId()
	{
		return _skillId;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public int getDuration()
	{
		return _duration;
	}
	
	public int getObj()
	{
		return _obj;
	}
}
