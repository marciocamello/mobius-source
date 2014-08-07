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

import lineage2.gameserver.model.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Smo
 */
public class RequestInzonePartyInfoHistory extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestInzonePartyInfoHistory.class);
	
	@Override
	protected void readImpl()
	{
	}
	
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		
		if (player == null)
		{
			return;
		}
		
		_log.info("[IMPLEMENT ME!] RequestInzonePartyInfoHistory (maybe trigger)");
	}
	
	@Override
	public String getType()
	{
		return "[C] D0:9A RequestInzonePartyInfoHistory";
	}
}
