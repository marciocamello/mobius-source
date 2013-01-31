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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExDuelAskStart extends L2GameServerPacket
{
	/**
	 * Field _requestor.
	 */
	String _requestor;
	/**
	 * Field _isPartyDuel.
	 */
	int _isPartyDuel;
	
	/**
	 * Constructor for ExDuelAskStart.
	 * @param requestor String
	 * @param isPartyDuel int
	 */
	public ExDuelAskStart(String requestor, int isPartyDuel)
	{
		_requestor = requestor;
		_isPartyDuel = isPartyDuel;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x4c);
		writeS(_requestor);
		writeD(_isPartyDuel);
	}
}
