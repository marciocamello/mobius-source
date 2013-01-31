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

import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SysString;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Say2 extends NpcStringContainer
{
	/**
	 * Field _type.
	 */
	private final ChatType _type;
	/**
	 * Field _sysString.
	 */
	private SysString _sysString;
	/**
	 * Field _systemMsg.
	 */
	private SystemMsg _systemMsg;
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _charName.
	 */
	private String _charName;
	
	/**
	 * Constructor for Say2.
	 * @param objectId int
	 * @param type ChatType
	 * @param st SysString
	 * @param sm SystemMsg
	 */
	public Say2(int objectId, ChatType type, SysString st, SystemMsg sm)
	{
		super(NpcString.NONE);
		_objectId = objectId;
		_type = type;
		_sysString = st;
		_systemMsg = sm;
	}
	
	/**
	 * Constructor for Say2.
	 * @param objectId int
	 * @param type ChatType
	 * @param charName String
	 * @param text String
	 */
	public Say2(int objectId, ChatType type, String charName, String text)
	{
		this(objectId, type, charName, NpcString.NONE, text);
	}
	
	/**
	 * Constructor for Say2.
	 * @param objectId int
	 * @param type ChatType
	 * @param charName String
	 * @param npcString NpcString
	 * @param params String[]
	 */
	public Say2(int objectId, ChatType type, String charName, NpcString npcString, String... params)
	{
		super(npcString, params);
		_objectId = objectId;
		_type = type;
		_charName = charName;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x4A);
		writeD(_objectId);
		writeD(_type.ordinal());
		switch (_type)
		{
			case SYSTEM_MESSAGE:
				writeD(_sysString.getId());
				writeD(_systemMsg.getId());
				break;
			default:
				writeS(_charName);
				writeElements();
				break;
		}
	}
}
