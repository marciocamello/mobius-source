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
package lineage2.gameserver.network.clientpackets.PledgeRecruit;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.clientpackets.L2GameClientPacket;

public class RequestPledgeDraftListSearch extends L2GameClientPacket
{
	@SuppressWarnings("unused")
	private int _minLevel;
	@SuppressWarnings("unused")
	private int _maxLevel;
	@SuppressWarnings("unused")
	private int _role;
	@SuppressWarnings("unused")
	private String _charName;
	@SuppressWarnings("unused")
	private int _sortType;
	@SuppressWarnings("unused")
	private int _sortOrder;
	
	@Override
	protected void readImpl()
	{
		_minLevel = readD();
		_maxLevel = readD();
		_role = readD();
		_charName = readS();
		_sortType = readD();
		_sortOrder = readD();
	}
	
	@Override
	protected void runImpl()
	{
		Player activeChar = (getClient()).getActiveChar();
		
		if (activeChar == null)
		{
			// empty if block
		}
	}
}