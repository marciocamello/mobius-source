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
 * @author ALF
 * @date 22.08.2012
 */
public class ExRegistPartySubstitute extends L2GameServerPacket
{
	public static final int REGISTER_OK = 1;
	public static final int REGISTER_TIMEOUT = 0;
	private final int _objId;
	private final int _code;
	
	public ExRegistPartySubstitute(int objId, int code)
	{
		_objId = objId;
		_code = code;
	}
	
	@Override
	protected void writeImpl()
	{
		// writeEx(0x106); // TODO: blacksmoke - check for protocol 603
		writeD(_objId);
		writeD(_code);
	}
}
