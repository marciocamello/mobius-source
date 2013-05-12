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

public class L2Friend extends L2GameServerPacket
{
	private final boolean _add, _online;
	private final String _name;
	private final int _object_id;
	
	public L2Friend(Player player, boolean add)
	{
		_add = add;
		_name = player.getName();
		_object_id = player.getObjectId();
		_online = true;
	}
	
	public L2Friend(String name, boolean add, boolean online, int object_id)
	{
		_name = name;
		_add = add;
		_object_id = object_id;
		_online = online;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x76);
		writeD(_add ? 1 : 3); // 1 - добавить друга в спикок, 3 удалить друга со
								// списка
		writeD(0); // и снова тут идет ID персонажа в списке оффа, не object id
		writeS(_name);
		writeD(_online ? 1 : 0); // онлайн или оффлайн
		writeD(_object_id); // object_id if online
	}
}