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
package lineage2.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.enums.AcquireType;

/**
 * Reworked: VISTALL
 */
public class AcquireSkillList extends L2GameServerPacket
{
	private final AcquireType _type;
	private final List<Skill> _skills;
	
	private class Skill
	{
		final int id;
		final int nextLevel;
		final int maxLevel;
		final int cost;
		final int requirements;
		final int subUnit;
		
		Skill(int id, int nextLevel, int maxLevel, int cost, int requirements, int subUnit)
		{
			this.id = id;
			this.nextLevel = nextLevel;
			this.maxLevel = maxLevel;
			this.cost = cost;
			this.requirements = requirements;
			this.subUnit = subUnit;
		}
	}
	
	public AcquireSkillList(AcquireType type, int size)
	{
		_skills = new ArrayList<>(size);
		_type = type;
	}
	
	public void addSkill(int id, int nextLevel, int maxLevel, int Cost, int requirements, int subUnit)
	{
		_skills.add(new Skill(id, nextLevel, maxLevel, Cost, requirements, subUnit));
	}
	
	public void addSkill(int id, int nextLevel, int maxLevel, int Cost, int requirements)
	{
		_skills.add(new Skill(id, nextLevel, maxLevel, Cost, requirements, 0));
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0xFA);
		writeD(_type.ordinal());
		writeD(_skills.size());
		
		for (Skill temp : _skills)
		{
			writeD(temp.id);
			writeD(temp.nextLevel);
			writeD(temp.maxLevel);
			writeD(temp.cost);
			writeD(temp.requirements);
			
			if (_type == AcquireType.SUB_UNIT)
			{
				writeD(temp.subUnit);
			}
		}
	}
}