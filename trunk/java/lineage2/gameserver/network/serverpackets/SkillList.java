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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Skill.SkillType;
import lineage2.gameserver.tables.SkillTreeTable;

/**
 * format d (dddc)
 */
public class SkillList extends L2GameServerPacket
{
	private final List<Skill> _skills;
	private final boolean canEnchant;
	private final Player activeChar;
	private final int _learnedSkill;
	
	public SkillList(Player p)
	{
		_skills = new ArrayList<>(p.getAllSkills());
		canEnchant = p.getTransformation() == 0;
		activeChar = p;
		_learnedSkill = 0;
	}
	
	public SkillList(Player p, int learnedSkill)
	{
		_skills = new ArrayList<>(p.getAllSkills());
		canEnchant = p.getTransformation() == 0;
		activeChar = p;
		_learnedSkill = learnedSkill;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x5f);
		writeD(_skills.size());
		
		for (Skill temp : _skills)
		{
			writeD(temp.isActive() || temp.isToggle() ? 0 : 1); // deprecated?
			writeD(temp.getDisplayLevel());
			writeD(temp.getDisplayId());
			writeD(temp.getSkillType() == SkillType.EMDAM ? temp.getDisplayId() : -1);
			writeC(activeChar.isUnActiveSkill(temp.getId()) ? 0x01 : 0x00);
			writeC(canEnchant ? SkillTreeTable.isEnchantable(temp) : 0);
		}
		
		writeD(_learnedSkill);
	}
}