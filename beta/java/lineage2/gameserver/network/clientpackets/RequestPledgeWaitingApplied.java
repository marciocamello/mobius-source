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

import java.util.OptionalInt;

import lineage2.gameserver.instancemanager.ClanEntryManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExPledgeWaitingListApplied;

public class RequestPledgeWaitingApplied extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		// Nothing to read
	}
	
	@Override
	protected void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		
		if ((activeChar == null) || (activeChar.getClan() == null))
		{
			return;
		}
		
		OptionalInt clanId = ClanEntryManager.getInstance().getClanIdForPlayerApplication(activeChar.getObjectId());
		
		if (clanId.isPresent())
		{
			activeChar.sendPacket(new ExPledgeWaitingListApplied(clanId.getAsInt(), activeChar.getObjectId()));
		}
	}
}