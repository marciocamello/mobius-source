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

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NpcSay extends NpcStringContainer
{
	/**
	 * Field _objId.
	 */
	private final int _objId;
	/**
	 * Field _type.
	 */
	private final int _type;
	/**
	 * Field _id.
	 */
	private final int _id;
	
	/**
	 * Constructor for NpcSay.
	 * @param npc NpcInstance
	 * @param chatType ChatType
	 * @param text String
	 */
	public NpcSay(NpcInstance npc, ChatType chatType, String text)
	{
		this(npc, chatType, NpcString.NONE, text);
	}
	
	/**
	 * Constructor for NpcSay.
	 * @param npc NpcInstance
	 * @param chatType ChatType
	 * @param npcString NpcString
	 * @param params String[]
	 */
	public NpcSay(NpcInstance npc, ChatType chatType, NpcString npcString, String... params)
	{
		super(npcString, params);
		_objId = npc.getObjectId();
		_id = npc.getNpcId();
		_type = chatType.ordinal();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x30);
		writeD(_objId);
		writeD(_type);
		writeD(1000000 + _id);
		writeElements();
	}
}
