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

public class Snoop extends L2GameServerPacket
{
	private final int _convoID;
	private final String _name;
	private final int _type;
	private final int _fStringId;
	private final String _speaker;
	private final String[] _params;
	
	public Snoop(int id, String name, int type, String speaker, String msg, int fStringId, String... params)
	{
		_convoID = id;
		_name = name;
		_type = type;
		_speaker = speaker;
		_fStringId = fStringId;
		_params = params;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xdb);
		writeD(_convoID);
		writeS(_name);
		writeD(0x00); // ??
		writeD(_type);
		writeS(_speaker);
		writeD(_fStringId);
		
		for (String param : _params)
		{
			writeS(param);
		}
	}
}