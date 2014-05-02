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
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * Этот пакет отвечает за анимацию высасывания душ из трупов
 * @author SYS
 */
public class SpawnEmitter extends L2GameServerPacket
{
	private final int _monsterObjId;
	private final int _playerObjId;
	
	public SpawnEmitter(NpcInstance monster, Player player)
	{
		_playerObjId = player.getObjectId();
		_monsterObjId = monster.getObjectId();
	}
	
	@Override
	protected final void writeImpl()
	{
		// ddd
		writeEx(0x5e);
		
		writeD(_monsterObjId);
		writeD(_playerObjId);
		writeD(0x00); // unk
	}
}