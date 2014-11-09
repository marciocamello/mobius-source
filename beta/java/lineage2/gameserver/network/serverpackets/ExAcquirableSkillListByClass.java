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
import java.util.Map;

import lineage2.gameserver.data.xml.holder.SkillAcquireHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author ALF
 * @date 17.07.2012
 */
public class ExAcquirableSkillListByClass extends L2GameServerPacket
{
	private final Collection<SkillLearn> allskills;
	
	public ExAcquirableSkillListByClass(Player player)
	{
		allskills = SkillAcquireHolder.getInstance().getAvailableAllSkills(player);
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x90);
		writeH(allskills.size());
		
		for (SkillLearn sk : allskills)
		{
			Skill skill = SkillTable.getInstance().getInfo(sk.getId(), sk.getLevel());
			
			if (skill == null)
			{
				continue;
			}
			
			writeD(sk.getId());
			writeD(sk.getLevel());
			writeQ(sk.getCost());
			writeC(sk.getMinLevel());
			writeC(sk.getDualClassMinLvl());
			
			Map<Integer, Long> req = sk.getRequiredItems();
			writeD(req.size());
			for (Integer key : req.keySet())
			{
				writeD(key);
				writeQ(req.get(key));
			}
			
			Skill relskill = SkillTable.getInstance().getInfo(sk.getId(), sk.getLevel());
			if ((relskill != null) && relskill.isRelationSkill())
			{
				int[] _dels = relskill.getRelationSkills();
				writeC(_dels.length); // deletedSkillsSize
				
				for (int skillId : _dels)
				{
					writeD(skillId); // skillId
					writeD(SkillTable.getInstance().getBaseLevel(skillId)); // skillLvl
				}
			}
			else
			{
				writeD(0x00);
			}
		}
	}
}
