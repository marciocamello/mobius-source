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
package lineage2.gameserver.templates.skill.restoration;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;

public final class RestorationInfo
{
	private final int _skillId;
	private final int _skillLvl;
	private final int _itemConsumeId;
	private final int _itemConsumeCount;
	private final List<RestorationGroup> _restorationGroups;
	
	public RestorationInfo(int skillId, int skillLvl, int itemConsumeId, int itemConsumeCount)
	{
		_skillId = skillId;
		_skillLvl = skillLvl;
		_itemConsumeId = itemConsumeId;
		_itemConsumeCount = itemConsumeCount;
		_restorationGroups = new ArrayList<>();
	}
	
	public int getSkillId()
	{
		return _skillId;
	}
	
	public int getSkillLvl()
	{
		return _skillLvl;
	}
	
	public int getItemConsumeId()
	{
		return _itemConsumeId;
	}
	
	public int getItemConsumeCount()
	{
		return _itemConsumeCount;
	}
	
	public void addRestorationGroup(RestorationGroup group)
	{
		_restorationGroups.add(group);
	}
	
	public List<RestorationItem> getRandomGroupItems()
	{
		double chancesAmount = 0.0D;
		for (RestorationGroup group : _restorationGroups)
		{
			chancesAmount += group.getChance();
		}
		if (Rnd.chance(chancesAmount))
		{
			double chanceMod = (100.0D - chancesAmount) / _restorationGroups.size();
			List<RestorationGroup> successGroups = new ArrayList<>();
			int tryCount = 0;
			while (successGroups.isEmpty())
			{
				tryCount++;
				for (RestorationGroup group : _restorationGroups)
				{
					if ((tryCount % 10) == 0)
					{
						chanceMod += 1.0D;
					}
					if (Rnd.chance(group.getChance() + chanceMod))
					{
						successGroups.add(group);
					}
				}
			}
			RestorationGroup[] groupsArray = successGroups.toArray(new RestorationGroup[successGroups.size()]);
			return groupsArray[Rnd.get(groupsArray.length)].getRestorationItems();
		}
		return new ArrayList<>(0);
	}
}