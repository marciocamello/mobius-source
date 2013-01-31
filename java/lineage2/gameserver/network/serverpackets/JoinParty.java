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
public class JoinParty extends L2GameServerPacket
{
	/**
	 * Field SUCCESS.
	 */
	public static final L2GameServerPacket SUCCESS = new JoinParty(1);
	/**
	 * Field FAIL.
	 */
	public static final L2GameServerPacket FAIL = new JoinParty(0);
	/**
	 * Field _response.
	 */
	private final int _response;
	
	/**
	 * Constructor for JoinParty.
	 * @param response int
	 */
	public JoinParty(int response)
	{
		_response = response;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x3A);
		writeD(_response);
		writeD(0x00);
	}
}
