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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.RankPrivs;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ManagePledgePower extends L2GameServerPacket
{
	/**
	 * Field privs. Field _clanId. Field _action.
	 */
	private final int _action, _clanId, privs;
	
	/**
	 * Constructor for ManagePledgePower.
	 * @param player Player
	 * @param action int
	 * @param rank int
	 */
	public ManagePledgePower(Player player, int action, int rank)
	{
		_clanId = player.getClanId();
		_action = action;
		RankPrivs temp = player.getClan().getRankPrivs(rank);
		privs = temp == null ? 0 : temp.getPrivs();
		player.sendPacket(new PledgeReceiveUpdatePower(privs));
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x2a);
		writeD(_clanId);
		writeD(_action);
		writeD(privs);
	}
}
