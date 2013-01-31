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
public class RestartResponse extends L2GameServerPacket
{
	/**
	 * Field FAIL. Field OK.
	 */
	public static final RestartResponse OK = new RestartResponse(1), FAIL = new RestartResponse(0);
	/**
	 * Field _message.
	 */
	private final String _message;
	/**
	 * Field _param.
	 */
	private final int _param;
	
	/**
	 * Constructor for RestartResponse.
	 * @param param int
	 */
	public RestartResponse(int param)
	{
		_message = "bye";
		_param = param;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x71);
		writeD(_param);
		writeS(_message);
	}
}
