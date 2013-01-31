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

import lineage2.gameserver.model.base.AcquireType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AcquireSkillList extends L2GameServerPacket
{
	/**
	 * Field _type.
	 */
	private final AcquireType _type;
	/**
	 * Field _skills.
	 */
	private final List<Skill> _skills;
	
	/**
	 * @author Mobius
	 */
	class Skill
	{
		/**
		 * Field id.
		 */
		public int id;
		/**
		 * Field nextLevel.
		 */
		public int nextLevel;
		/**
		 * Field maxLevel.
		 */
		public int maxLevel;
		/**
		 * Field cost.
		 */
		public int cost;
		/**
		 * Field requirements.
		 */
		public int requirements;
		/**
		 * Field subUnit.
		 */
		public int subUnit;
		
		/**
		 * Constructor for Skill.
		 * @param id int
		 * @param nextLevel int
		 * @param maxLevel int
		 * @param cost int
		 * @param requirements int
		 * @param subUnit int
		 */
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
	
	/**
	 * Constructor for AcquireSkillList.
	 * @param type AcquireType
	 * @param size int
	 */
	public AcquireSkillList(AcquireType type, int size)
	{
		_skills = new ArrayList<>(size);
		_type = type;
	}
	
	/**
	 * Method addSkill.
	 * @param id int
	 * @param nextLevel int
	 * @param maxLevel int
	 * @param Cost int
	 * @param requirements int
	 * @param subUnit int
	 */
	public void addSkill(int id, int nextLevel, int maxLevel, int Cost, int requirements, int subUnit)
	{
		_skills.add(new Skill(id, nextLevel, maxLevel, Cost, requirements, subUnit));
	}
	
	/**
	 * Method addSkill.
	 * @param id int
	 * @param nextLevel int
	 * @param maxLevel int
	 * @param Cost int
	 * @param requirements int
	 */
	public void addSkill(int id, int nextLevel, int maxLevel, int Cost, int requirements)
	{
		_skills.add(new Skill(id, nextLevel, maxLevel, Cost, requirements, 0));
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x90);
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
