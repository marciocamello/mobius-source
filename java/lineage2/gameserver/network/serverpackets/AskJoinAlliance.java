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
public class AskJoinAlliance extends L2GameServerPacket
{
	/**
	 * Field _requestorName.
	 */
	private final String _requestorName;
	/**
	 * Field _requestorAllyName.
	 */
	private final String _requestorAllyName;
	/**
	 * Field _requestorId.
	 */
	private final int _requestorId;
	
	/**
	 * Constructor for AskJoinAlliance.
	 * @param requestorId int
	 * @param requestorName String
	 * @param requestorAllyName String
	 */
	public AskJoinAlliance(int requestorId, String requestorName, String requestorAllyName)
	{
		_requestorName = requestorName;
		_requestorAllyName = requestorAllyName;
		_requestorId = requestorId;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xBB);
		writeD(_requestorId);
		writeS(_requestorName);
		writeS("");
		writeS(_requestorAllyName);
	}
}
