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

import java.util.ArrayList;
import java.util.List;

public class StatusUpdate extends L2GameServerPacket
{
	public final static int CUR_HP = 0x09;
	public final static int MAX_HP = 0x0a;
	public final static int CUR_MP = 0x0b;
	public final static int MAX_MP = 0x0c;
	public final static int CUR_LOAD = 0x0e;
	public final static int MAX_LOAD = 0x0f;
	public final static int PVP_FLAG = 0x1a;
	public final static int KARMA = 0x1b;
	public final static int CUR_CP = 0x21;
	public final static int MAX_CP = 0x22;
	public final static int DAMAGE = 0x23;
	private final int _objectId;
	private final int _playerId;
	private final List<Attribute> _attributes = new ArrayList<>();
	
	class Attribute
	{
		public final int id;
		public final int value;
		
		Attribute(int id, int value)
		{
			this.id = id;
			this.value = value;
		}
	}
	
	public StatusUpdate(int objectId)
	{
		_objectId = objectId;
		_playerId = 0;
	}
	
	public StatusUpdate(int objectId, int playerId)
	{
		_objectId = objectId;
		_playerId = playerId;
	}
	
	public StatusUpdate addAttribute(int id, int level)
	{
		_attributes.add(new Attribute(id, level));
		return this;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x18);
		writeD(_objectId);
		writeD(_playerId);
		writeD(1);
		writeD(_attributes.size());
		for (Attribute temp : _attributes)
		{
			writeD(temp.id);
			writeD(temp.value);
		}
	}
	
	public boolean hasAttributes()
	{
		return !_attributes.isEmpty();
	}
}
