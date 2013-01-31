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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PledgeReceivePowerInfo extends L2GameServerPacket
{
	/**
	 * Field PowerGrade.
	 */
	private final int PowerGrade;
	/**
	 * Field privs.
	 */
	private int privs;
	/**
	 * Field member_name.
	 */
	private final String member_name;
	
	/**
	 * Constructor for PledgeReceivePowerInfo.
	 * @param member UnitMember
	 */
	public PledgeReceivePowerInfo(UnitMember member)
	{
		PowerGrade = member.getPowerGrade();
		member_name = member.getName();
		if (member.isClanLeader())
		{
			privs = Clan.CP_ALL;
		}
		else
		{
			RankPrivs temp = member.getClan().getRankPrivs(member.getPowerGrade());
			if (temp != null)
			{
				privs = temp.getPrivs();
			}
			else
			{
				privs = 0;
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x3d);
		writeD(PowerGrade);
		writeS(member_name);
		writeD(privs);
	}
}
