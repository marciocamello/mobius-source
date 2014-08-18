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

import lineage2.gameserver.model.base.EnchantSkillLearn;
import lineage2.gameserver.tables.SkillTreeTable;

public class ExEnchantSkillInfo extends L2GameServerPacket
{
	private final List<Integer> _routes;
	private final int _id;
	private final int _level;
	private int _canAdd;
	private int canDecrease;
	
	public ExEnchantSkillInfo(int id, int level)
	{
		_routes = new ArrayList<>();
		_id = id;
		_level = level;
		
		// skill already enchanted?
		if (_level > 100)
		{
			canDecrease = 1;
			// get detail for next level
			EnchantSkillLearn esd = SkillTreeTable.getSkillEnchant(_id, _level + 1);
			
			// if it exists add it
			if (esd != null)
			{
				addEnchantSkillDetail(esd.getLevel());
				_canAdd = 1;
			}
			
			for (EnchantSkillLearn el : SkillTreeTable.getEnchantsForChange(_id, _level))
			{
				addEnchantSkillDetail(el.getLevel());
			}
		}
		else
		{
			// not already enchanted
			for (EnchantSkillLearn esd : SkillTreeTable.getFirstEnchantsForSkill(_id))
			{
				addEnchantSkillDetail(esd.getLevel());
				_canAdd = 1;
			}
		}
	}
	
	private void addEnchantSkillDetail(int level)
	{
		_routes.add(level);
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x2a);
		writeD(_id);
		writeD(_level);
		writeD(_canAdd); // can add enchant
		writeD(canDecrease); // can decrease enchant
		writeD(_routes.size());
		
		for (Integer route : _routes)
		{
			writeD(route);
		}
	}
}