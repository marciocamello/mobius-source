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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExEnchantSkillList extends L2GameServerPacket
{
	/**
	 * @author Mobius
	 */
	public enum EnchantSkillType
	{
		/**
		 * Field NORMAL.
		 */
		NORMAL,
		/**
		 * Field SAFE.
		 */
		SAFE,
		/**
		 * Field UNTRAIN.
		 */
		UNTRAIN,
		/**
		 * Field CHANGE_ROUTE.
		 */
		CHANGE_ROUTE,
	}
	
	/**
	 * Field _skills.
	 */
	private final List<Skill> _skills;
	/**
	 * Field _type.
	 */
	private final EnchantSkillType _type;
	
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
		 * Field level.
		 */
		public int level;
		
		/**
		 * Constructor for Skill.
		 * @param id int
		 * @param nextLevel int
		 */
		Skill(int id, int nextLevel)
		{
			this.id = id;
			level = nextLevel;
		}
	}
	
	/**
	 * Method addSkill.
	 * @param id int
	 * @param level int
	 */
	public void addSkill(int id, int level)
	{
		_skills.add(new Skill(id, level));
	}
	
	/**
	 * Constructor for ExEnchantSkillList.
	 * @param type EnchantSkillType
	 */
	public ExEnchantSkillList(EnchantSkillType type)
	{
		_type = type;
		_skills = new ArrayList<>();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x29);
		writeD(_type.ordinal());
		writeD(_skills.size());
		for (Skill sk : _skills)
		{
			writeD(sk.id);
			writeD(sk.level);
		}
	}
}
