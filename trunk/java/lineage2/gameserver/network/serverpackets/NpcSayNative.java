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

public class NpcSayNative extends L2GameServerPacket
{
	private final int _id;
	private final int _objId;
	private final int _msgId;
	
	public NpcSayNative(NpcInstance npc, int msgId)
	{
		_msgId = msgId;
		_objId = npc.getObjectId();
		_id = npc.getNpcId();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0x30);
		writeD(_objId);
		writeD(0x16);
		writeD(1000000 + _id);
		writeD(_msgId);
	}
}
