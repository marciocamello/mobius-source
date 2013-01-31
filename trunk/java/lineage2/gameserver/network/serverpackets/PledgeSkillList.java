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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.SubUnit;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PledgeSkillList extends L2GameServerPacket
{
	/**
	 * Field _allSkills.
	 */
	private List<SkillInfo> _allSkills = Collections.emptyList();
	/**
	 * Field _unitSkills.
	 */
	private final List<UnitSkillInfo> _unitSkills = new ArrayList<>();
	
	/**
	 * Constructor for PledgeSkillList.
	 * @param clan Clan
	 */
	public PledgeSkillList(Clan clan)
	{
		Collection<Skill> skills = clan.getSkills();
		_allSkills = new ArrayList<>(skills.size());
		for (Skill sk : skills)
		{
			_allSkills.add(new SkillInfo(sk.getId(), sk.getLevel()));
		}
		for (SubUnit subUnit : clan.getAllSubUnits())
		{
			for (Skill sk : subUnit.getSkills())
			{
				_unitSkills.add(new UnitSkillInfo(subUnit.getType(), sk.getId(), sk.getLevel()));
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x3a);
		writeD(_allSkills.size());
		writeD(_unitSkills.size());
		for (SkillInfo info : _allSkills)
		{
			writeD(info._id);
			writeD(info._level);
		}
		for (UnitSkillInfo info : _unitSkills)
		{
			writeD(info._type);
			writeD(info._id);
			writeD(info._level);
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class SkillInfo
	{
		/**
		 * Field _level.
		 */
		/**
		 * Field _id.
		 */
		public int _id, _level;
		
		/**
		 * Constructor for SkillInfo.
		 * @param id int
		 * @param level int
		 */
		public SkillInfo(int id, int level)
		{
			_id = id;
			_level = level;
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class UnitSkillInfo extends SkillInfo
	{
		/**
		 * Field _type.
		 */
		final int _type;
		
		/**
		 * Constructor for UnitSkillInfo.
		 * @param type int
		 * @param id int
		 * @param level int
		 */
		public UnitSkillInfo(int type, int id, int level)
		{
			super(id, level);
			_type = type;
		}
	}
}
