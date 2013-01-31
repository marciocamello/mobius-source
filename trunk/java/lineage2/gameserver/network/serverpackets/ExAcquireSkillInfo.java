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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExAcquireSkillInfo extends L2GameServerPacket
{
	/**
	 * Field skillLearn.
	 */
	private final SkillLearn skillLearn;
	
	/**
	 * Constructor for ExAcquireSkillInfo.
	 * @param player Player
	 * @param skillLearn SkillLearn
	 */
	public ExAcquireSkillInfo(Player player, SkillLearn skillLearn)
	{
		this.skillLearn = skillLearn;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0xFB);
		writeD(skillLearn.getId());
		writeD(skillLearn.getLevel());
		writeD(skillLearn.getCost());
		writeH(skillLearn.getMinLevel());
		writeD(skillLearn.getRequiredItems().size());
		for (int itemId : skillLearn.getRequiredItems().keySet())
		{
			writeD(itemId);
			writeQ(skillLearn.getRequiredItems().get(itemId));
		}
		Skill skkill = SkillTable.getInstance().getInfo(skillLearn.getId(), skillLearn.getLevel());
		if (skkill.isRelationSkill())
		{
			int[] _ss = skkill.getRelationSkills();
			writeD(_ss.length);
			for (int skillId : _ss)
			{
				writeD(skillId);
				writeD(SkillTable.getInstance().getBaseLevel(skillId));
			}
		}
		else
		{
			writeD(0x00);
		}
	}
}
