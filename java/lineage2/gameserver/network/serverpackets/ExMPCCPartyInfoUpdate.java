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

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;

/**
 * ch Sddd
 */
public class ExMPCCPartyInfoUpdate extends L2GameServerPacket
{
	private final Party _party;
	private final Player _leader;
	private final int _mode, _count;
	
	/**
	 * @param party
	 * @param mode 0 = Remove, 1 = Add
	 */
	public ExMPCCPartyInfoUpdate(Party party, int mode)
	{
		_party = party;
		_mode = mode;
		_count = _party.getMemberCount();
		_leader = _party.getPartyLeader();
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x5C);
		writeS(_leader.getName());
		writeD(_leader.getObjectId());
		writeD(_count);
		writeD(_mode); // mode 0 = Remove Party, 1 = AddParty, maybe more...
	}
}