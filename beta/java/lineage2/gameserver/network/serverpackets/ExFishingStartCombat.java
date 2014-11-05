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

import lineage2.gameserver.model.Creature;

/**
 * Format (ch)dddcc
 */
public class ExFishingStartCombat extends L2GameServerPacket
{
	int _time;
	int _hp;
	int _lureType;
	int _deceptiveMode;
	int _mode;
	private final int char_obj_id;
	
	public ExFishingStartCombat(Creature character, int time, int hp, int mode, int lureType, int deceptiveMode)
	{
		char_obj_id = character.getObjectId();
		_time = time;
		_hp = hp;
		_mode = mode;
		_lureType = lureType;
		_deceptiveMode = deceptiveMode;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x27);
		writeD(char_obj_id);
		writeD(_time);
		writeD(_hp);
		writeC(_mode); // mode: 0 = resting, 1 = fighting
		writeC(_lureType); // 0 = newbie lure, 1 = normal lure, 2 = night lure
		writeC(_deceptiveMode); // Fish Deceptive Mode: 0 = no, 1 = yes
	}
}