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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class StatusUpdate extends L2GameServerPacket
{
	/**
	 * Field CUR_HP.
	 */
	public final static int CUR_HP = 0x09;
	/**
	 * Field MAX_HP.
	 */
	public final static int MAX_HP = 0x0a;
	/**
	 * Field CUR_MP.
	 */
	public final static int CUR_MP = 0x0b;
	/**
	 * Field MAX_MP.
	 */
	public final static int MAX_MP = 0x0c;
	/**
	 * Field CUR_LOAD.
	 */
	public final static int CUR_LOAD = 0x0e;
	/**
	 * Field MAX_LOAD.
	 */
	public final static int MAX_LOAD = 0x0f;
	/**
	 * Field PVP_FLAG.
	 */
	public final static int PVP_FLAG = 0x1a;
	/**
	 * Field KARMA.
	 */
	public final static int KARMA = 0x1b;
	/**
	 * Field CUR_CP.
	 */
	public final static int CUR_CP = 0x21;
	/**
	 * Field MAX_CP.
	 */
	public final static int MAX_CP = 0x22;
	/**
	 * Field DAMAGE.
	 */
	public final static int DAMAGE = 0x23;
	/**
	 * Field _objectId.
	 */
	private final int _objectId;
	/**
	 * Field _playerId.
	 */
	private final int _playerId;
	/**
	 * Field _attributes.
	 */
	private final List<Attribute> _attributes = new ArrayList<>();
	
	/**
	 * @author Mobius
	 */
	class Attribute
	{
		/**
		 * Field id.
		 */
		public final int id;
		/**
		 * Field value.
		 */
		public final int value;
		
		/**
		 * Constructor for Attribute.
		 * @param id int
		 * @param value int
		 */
		Attribute(int id, int value)
		{
			this.id = id;
			this.value = value;
		}
	}
	
	/**
	 * Constructor for StatusUpdate.
	 * @param objectId int
	 */
	public StatusUpdate(int objectId)
	{
		_objectId = objectId;
		_playerId = 0;
	}
	
	/**
	 * Constructor for StatusUpdate.
	 * @param objectId int
	 * @param playerId int
	 */
	public StatusUpdate(int objectId, int playerId)
	{
		_objectId = objectId;
		_playerId = playerId;
	}
	
	/**
	 * Method addAttribute.
	 * @param id int
	 * @param level int
	 * @return StatusUpdate
	 */
	public StatusUpdate addAttribute(int id, int level)
	{
		_attributes.add(new Attribute(id, level));
		return this;
	}
	
	/**
	 * Method writeImpl.
	 */
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
	
	/**
	 * Method hasAttributes.
	 * @return boolean
	 */
	public boolean hasAttributes()
	{
		return !_attributes.isEmpty();
	}
}
