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

import java.util.Collection;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GMViewSkillInfo extends L2GameServerPacket
{
	/**
	 * Field _charName.
	 */
	private final String _charName;
	/**
	 * Field _skills.
	 */
	private final Collection<Skill> _skills;
	/**
	 * Field _targetChar.
	 */
	private final Player _targetChar;
	
	/**
	 * Constructor for GMViewSkillInfo.
	 * @param cha Player
	 */
	public GMViewSkillInfo(Player cha)
	{
		_charName = cha.getName();
		_skills = cha.getAllSkills();
		_targetChar = cha;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x97);
		writeS(_charName);
		writeD(_skills.size());
		for (Skill skill : _skills)
		{
			writeD(skill.isPassive() ? 1 : 0);
			writeD(skill.getDisplayLevel());
			writeD(skill.getId());
			writeC(_targetChar.isUnActiveSkill(skill.getId()) ? 0x01 : 0x00);
			writeC(SkillTable.getInstance().getMaxLevel(skill.getId()) > 100 ? 1 : 0);
		}
	}
}
