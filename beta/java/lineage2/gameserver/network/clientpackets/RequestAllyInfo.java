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
package lineage2.gameserver.network.clientpackets;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.tables.ClanTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestAllyInfo extends L2GameClientPacket
{
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		
		if (player == null)
		{
			return;
		}
		
		Alliance ally = player.getAlliance();
		
		if (ally == null)
		{
			return;
		}
		
		int clancount = 0;
		Clan leaderclan = player.getAlliance().getLeader();
		clancount = ClanTable.getInstance().getAlliance(leaderclan.getAllyId()).getMembers().length;
		int[] online = new int[clancount + 1];
		int[] count = new int[clancount + 1];
		Clan[] clans = player.getAlliance().getMembers();
		
		for (int i = 0; i < clancount; i++)
		{
			online[i + 1] = clans[i].getOnlineMembers(0).size();
			count[i + 1] = clans[i].getAllSize();
			online[0] += online[i + 1];
			count[0] += count[i + 1];
		}
		
		List<L2GameServerPacket> packets = new ArrayList<>(7 + (5 * clancount));
		packets.add(SystemMessage.getSystemMessage(SystemMessageId.ALLIANCE_INFORMATION));
		packets.add(SystemMessage.getSystemMessage(SystemMessageId.ALLIANCE_NAME_S1).addString(player.getClan().getAlliance().getAllyName()));
		packets.add(SystemMessage.getSystemMessage(SystemMessageId.CONNECTION_S1_TOTAL_S2).addInt(online[0]).addInt(count[0]));
		packets.add(SystemMessage.getSystemMessage(SystemMessageId.ALLIANCE_LEADER_S2_OF_S1).addString(leaderclan.getName()).addString(leaderclan.getLeaderName()));
		packets.add(SystemMessage.getSystemMessage(SystemMessageId.AFFILIATED_CLANS_TOTAL_S1_CLAN_S).addInt(clancount));
		packets.add(SystemMessage.getSystemMessage(SystemMessageId.CLAN_INFORMATION));
		
		for (int i = 0; i < clancount; i++)
		{
			packets.add(SystemMessage.getSystemMessage(SystemMessageId.CLAN_NAME_S1).addString(clans[i].getName()));
			packets.add(SystemMessage.getSystemMessage(SystemMessageId.CLAN_LEADER_S1).addString(clans[i].getLeaderName()));
			packets.add(SystemMessage.getSystemMessage(SystemMessageId.CLAN_LEVEL_S1).addInt(clans[i].getLevel()));
			packets.add(SystemMessage.getSystemMessage(SystemMessageId.CONNECTION_S1_TOTAL_S2).addInt(online[i + 1]).addInt(count[i + 1]));
			packets.add(SystemMessage.getSystemMessage(SystemMessageId.EMPTY4));
		}
		
		packets.add(SystemMessage.getSystemMessage(SystemMessageId.EMPTY3));
		player.sendPacket(packets);
	}
}