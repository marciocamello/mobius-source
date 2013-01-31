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

import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class L2Friend extends L2GameServerPacket
{
	/**
	 * Field _online. Field _add.
	 */
	private final boolean _add, _online;
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _object_id.
	 */
	private final int _object_id;
	
	/**
	 * Constructor for L2Friend.
	 * @param player Player
	 * @param add boolean
	 */
	public L2Friend(Player player, boolean add)
	{
		_add = add;
		_name = player.getName();
		_object_id = player.getObjectId();
		_online = true;
	}
	
	/**
	 * Constructor for L2Friend.
	 * @param name String
	 * @param add boolean
	 * @param online boolean
	 * @param object_id int
	 */
	public L2Friend(String name, boolean add, boolean online, int object_id)
	{
		_name = name;
		_add = add;
		_object_id = object_id;
		_online = online;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x76);
		writeD(_add ? 1 : 3);
		writeD(0);
		writeS(_name);
		writeD(_online ? 1 : 0);
		writeD(_object_id);
	}
}
