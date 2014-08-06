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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.skills.TimeStamp;

public class SkillCoolTime extends L2GameServerPacket
{
	private List<Skill> _list = Collections.emptyList();
	
	public SkillCoolTime(Player player)
	{
		Collection<TimeStamp> list = player.getSkillReuses();
		_list = new ArrayList<>(list.size());
		
		for (TimeStamp stamp : list)
		{
			if (!stamp.hasNotPassed())
			{
				continue;
			}
			
			lineage2.gameserver.model.Skill skill = player.getKnownSkill(stamp.getId());
			
			if (skill == null)
			{
				continue;
			}
			
			Skill sk = new Skill();
			sk.skillId = skill.getId();
			sk.level = skill.getLevel();
			sk.reuseBase = (int) Math.round(stamp.getReuseBasic() / 1000.);
			sk.reuseCurrent = (int) Math.round(stamp.getReuseCurrent() / 1000.);
			_list.add(sk);
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xc7); // packet type
		writeD(_list.size()); // Size of list
		
		for (int i = 0; i < _list.size(); i++)
		{
			Skill sk = _list.get(i);
			writeD(sk.skillId); // Skill Id
			writeD(sk.level); // Skill Level
			writeD(sk.reuseBase); // Total reuse delay, seconds
			writeD(sk.reuseCurrent); // Time remaining, seconds
		}
	}
	
	private static class Skill
	{
		/**
		 *
		 */
		public Skill()
		{
			// TODO Auto-generated constructor stub
		}
		
		public int skillId;
		public int level;
		public int reuseBase;
		public int reuseCurrent;
	}
}