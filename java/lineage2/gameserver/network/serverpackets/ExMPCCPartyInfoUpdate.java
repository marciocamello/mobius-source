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
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExMPCCPartyInfoUpdate extends L2GameServerPacket
{
	/**
	 * Field _party.
	 */
	private final Party _party;
	/**
	 * Field _leader.
	 */
	Player _leader;
	/**
	 * Field _count. Field _mode.
	 */
	private final int _mode, _count;
	
	/**
	 * Constructor for ExMPCCPartyInfoUpdate.
	 * @param party Party
	 * @param mode int
	 */
	public ExMPCCPartyInfoUpdate(Party party, int mode)
	{
		_party = party;
		_mode = mode;
		_count = _party.getMemberCount();
		_leader = _party.getPartyLeader();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeEx(0x5b);
		writeS(_leader.getName());
		writeD(_leader.getObjectId());
		writeD(_count);
		writeD(_mode);
	}
}
