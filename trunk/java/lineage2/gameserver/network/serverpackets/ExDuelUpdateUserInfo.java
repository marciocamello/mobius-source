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
 * chsddddddddd FE type 4F 00 ex_type 4E 00 65 00 6C 00 75 00 44 00 69 00 6D 00 00 00 name 04 A6 C0 4C objectID????? 2C 00 00 00 class_id 06 00 00 00 level 00 01 00 00 cur_hp 00 01 00 00 max_hp 4B 00 00 00 cur_mp 4B 00 00 00 max_mp 80 00 00 00 cur_cp 80 00 00 00 max_cp
 */
public class ExDuelUpdateUserInfo extends L2GameServerPacket
{
	private final String _name;
	private final int obj_id, class_id, level, curHp, maxHp, curMp, maxMp, curCp, maxCp;
	
	public ExDuelUpdateUserInfo(Player attacker)
	{
		_name = attacker.getName();
		obj_id = attacker.getObjectId();
		class_id = attacker.getClassId().getId();
		level = attacker.getLevel();
		curHp = (int) attacker.getCurrentHp();
		maxHp = attacker.getMaxHp();
		curMp = (int) attacker.getCurrentMp();
		maxMp = attacker.getMaxMp();
		curCp = (int) attacker.getCurrentCp();
		maxCp = attacker.getMaxCp();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x51);
		writeS(_name);
		writeD(obj_id);
		writeD(class_id);
		writeD(level);
		writeD(curHp);
		writeD(maxHp);
		writeD(curMp);
		writeD(maxMp);
		writeD(curCp);
		writeD(maxCp);
	}
}