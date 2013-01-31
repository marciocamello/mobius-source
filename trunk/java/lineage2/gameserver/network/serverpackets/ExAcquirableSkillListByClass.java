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

import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExAcquirableSkillListByClass extends L2GameServerPacket
{
	/**
	 * Field allskills.
	 */
	private final Collection<SkillLearn> allskills;
	
	/**
	 * Constructor for ExAcquirableSkillListByClass.
	 * @param player Player
	 */
	public ExAcquirableSkillListByClass(Player player)
	{
		allskills = SkillAcquireHolder.getInstance().getAvailableAllSkills(player);
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeH(0xf9);
		writeD(allskills.size());
		for (SkillLearn sk : allskills)
		{
			writeD(sk.getId());
			writeD(sk.getLevel());
			writeD(sk.getCost());
			writeH(sk.getMinLevel());
			writeD(sk.getRequiredItems().size());
			for (int itemId : sk.getRequiredItems().keySet())
			{
				writeD(itemId);
				writeQ(sk.getRequiredItems().get(itemId));
			}
			Skill skkill = SkillTable.getInstance().getInfo(sk.getId(), sk.getLevel());
			if ((skkill != null) && skkill.isRelationSkill())
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
}
