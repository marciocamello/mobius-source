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

import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.RankPrivs;
import lineage2.gameserver.model.pledge.UnitMember;

public class PledgeReceivePowerInfo extends L2GameServerPacket
{
	private final int _power_grade;
	private int _privs;
	private final String _member_name;
	
	public PledgeReceivePowerInfo(UnitMember member)
	{
		_power_grade = member.getPowerGrade();
		_member_name = member.getName();
		
		if (member.isClanLeader())
		{
			_privs = Clan.CP_ALL;
		}
		else
		{
			RankPrivs temp = member.getClan().getRankPrivs(member.getPowerGrade());
			
			if (temp != null)
			{
				_privs = temp.getPrivs();
			}
			else
			{
				_privs = 0;
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0x3E);
		writeD(_power_grade);
		writeS(_member_name);
		writeD(_privs);
	}
}