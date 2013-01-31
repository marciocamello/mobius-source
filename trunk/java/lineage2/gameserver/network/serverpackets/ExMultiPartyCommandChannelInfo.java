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

import lineage2.gameserver.model.CommandChannel;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExMultiPartyCommandChannelInfo extends L2GameServerPacket
{
	/**
	 * Field ChannelLeaderName.
	 */
	private final String ChannelLeaderName;
	/**
	 * Field MemberCount.
	 */
	private final int MemberCount;
	/**
	 * Field parties.
	 */
	private final List<ChannelPartyInfo> parties;
	
	/**
	 * Constructor for ExMultiPartyCommandChannelInfo.
	 * @param channel CommandChannel
	 */
	public ExMultiPartyCommandChannelInfo(CommandChannel channel)
	{
		ChannelLeaderName = channel.getChannelLeader().getName();
		MemberCount = channel.getMemberCount();
		parties = new ArrayList<>();
		for (Party party : channel.getParties())
		{
			Player leader = party.getPartyLeader();
			if (leader != null)
			{
				parties.add(new ChannelPartyInfo(leader.getName(), leader.getObjectId(), party.getMemberCount()));
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x31);
		writeS(ChannelLeaderName);
		writeD(0);
		writeD(MemberCount);
		writeD(parties.size());
		for (ChannelPartyInfo party : parties)
		{
			writeS(party.Leader_name);
			writeD(party.Leader_obj_id);
			writeD(party.MemberCount);
		}
	}
	
	/**
	 * @author Mobius
	 */
	static class ChannelPartyInfo
	{
		/**
		 * Field Leader_name.
		 */
		public String Leader_name;
		/**
		 * Field MemberCount.
		 */
		/**
		 * Field Leader_obj_id.
		 */
		public int Leader_obj_id, MemberCount;
		
		/**
		 * Constructor for ChannelPartyInfo.
		 * @param _Leader_name String
		 * @param _Leader_obj_id int
		 * @param _MemberCount int
		 */
		public ChannelPartyInfo(String _Leader_name, int _Leader_obj_id, int _MemberCount)
		{
			Leader_name = _Leader_name;
			Leader_obj_id = _Leader_obj_id;
			MemberCount = _MemberCount;
		}
	}
}
