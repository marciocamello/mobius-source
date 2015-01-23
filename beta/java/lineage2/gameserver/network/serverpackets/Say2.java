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
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.network.serverpackets.components.SysString;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;

public class Say2 extends NpcStringContainer
{
	private final ChatType _type;
	private SysString _sysString;
	private SystemMessageId _systemMsg;
	private final int _objectId;
	private String _charName;
	
	public Say2(int objectId, ChatType type, SysString st, SystemMessageId sm)
	{
		super(NpcStringId.STRING_NONE);
		_objectId = objectId;
		_type = type;
		_sysString = st;
		_systemMsg = sm;
	}
	
	public Say2(int objectId, ChatType type, String charName, String text)
	{
		this(objectId, type, charName, NpcStringId.STRING_NONE, text);
	}
	
	public Say2(int objectId, ChatType type, String charName, NpcStringId npcString, String... params)
	{
		super(npcString, params);
		_objectId = objectId;
		_type = type;
		_charName = charName;
	}
	
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