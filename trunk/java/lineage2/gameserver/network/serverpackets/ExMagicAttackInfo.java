/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;

public class ExMagicAttackInfo extends L2GameServerPacket
{
	Player player;
	
	public ExMagicAttackInfo(Player _player)
	{
		player = _player;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xFA);
		writeD(player.getObjectId());
		writeD(player.getTargetId());
		writeD(0x01);
	}
}
