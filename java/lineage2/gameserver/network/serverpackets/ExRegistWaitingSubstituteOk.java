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

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;

/**
 * @author ALF
 * @date 22.08.2012 ddddddd
 */
public class ExRegistWaitingSubstituteOk extends L2GameServerPacket
{
	private final int x;
	private final int y;
	private final int z;
	private final int classId;
	
	public ExRegistWaitingSubstituteOk(Party _party, Player _player)
	{
		Player leader = _party.getPartyLeader();
		x = leader.getX();
		y = leader.getY();
		z = leader.getZ();
		classId = _player.getClassId().getId();
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x105);
		writeD(x); // x
		writeD(y); // y
		writeD(z); // z
		writeD(0x00); // unk
		writeD(classId); // ClassId
		writeD(0x00); // unk1
		writeD(0x00); // unk2
	}
}
