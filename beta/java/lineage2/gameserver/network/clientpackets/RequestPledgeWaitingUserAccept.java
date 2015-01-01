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

import lineage2.gameserver.instancemanager.ClanEntryManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.JoinPledge;
import lineage2.gameserver.network.serverpackets.UserInfo;

public class RequestPledgeWaitingUserAccept extends L2GameClientPacket
{
	private boolean _acceptRequest;
	private int _playerId;
	private int _clanId;
	
	@Override
	protected void readImpl()
	{
		_acceptRequest = readD() == 1;
		_playerId = readD();
		_clanId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		
		if ((activeChar == null) || (activeChar.getClan() == null))
		{
			return;
		}
		
		if (_acceptRequest)
		{
			final Player player = World.getPlayer(_playerId);
			if (player != null)
			{
				final Clan clan = activeChar.getClan();
				player.setClan(clan);
				player.sendPacket(new JoinPledge(_clanId));
				final UserInfo ui = new UserInfo(player);
				player.sendPacket(ui);
				player.broadcastUserInfo();
				
				ClanEntryManager.getInstance().removePlayerApplication(clan.getClanId(), _playerId);
			}
		}
		else
		{
			ClanEntryManager.getInstance().removePlayerApplication(activeChar.getClanId(), _playerId);
		}
		
	}
}