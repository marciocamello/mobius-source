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

import lineage2.gameserver.model.instances.DoorInstance;

/**
 * 60 d6 6d c0 4b door id 8f 14 00 00 x b7 f1 00 00 y 60 f2 ff ff z 00 00 00 00 ??
 * <p/>
 * format dddd rev 377 ID:%d X:%d Y:%d Z:%d ddddd rev 419
 */
public class DoorInfo extends L2GameServerPacket
{
	private final int obj_id, door_id, view_hp;
	
	@Deprecated
	public DoorInfo(DoorInstance door)
	{
		obj_id = door.getObjectId();
		door_id = door.getDoorId();
		view_hp = door.isHPVisible() ? 1 : 0;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4C);
		writeD(obj_id);
		writeD(door_id);
		writeD(view_hp); // отображать ли хп у двери или стены
	}
}