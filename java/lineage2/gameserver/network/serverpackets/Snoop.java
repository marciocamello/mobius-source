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
public class Snoop extends L2GameServerPacket
{
	/**
	 * Field _convoID.
	 */
	private final int _convoID;
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _type.
	 */
	private final int _type;
	/**
	 * Field _fStringId.
	 */
	private final int _fStringId;
	/**
	 * Field _speaker.
	 */
	private final String _speaker;
	/**
	 * Field _params.
	 */
	private final String[] _params;
	
	/**
	 * Constructor for Snoop.
	 * @param id int
	 * @param name String
	 * @param type int
	 * @param speaker String
	 * @param msg String
	 * @param fStringId int
	 * @param params String[]
	 */
	public Snoop(int id, String name, int type, String speaker, String msg, int fStringId, String... params)
	{
		_convoID = id;
		_name = name;
		_type = type;
		_speaker = speaker;
		_fStringId = fStringId;
		_params = params;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xdb);
		writeD(_convoID);
		writeS(_name);
		writeD(0x00);
		writeD(_type);
		writeS(_speaker);
		writeD(_fStringId);
		for (String param : _params)
		{
			writeS(param);
		}
	}
}
