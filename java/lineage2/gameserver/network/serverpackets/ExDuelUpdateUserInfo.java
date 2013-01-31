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
public class ExDuelUpdateUserInfo extends L2GameServerPacket
{
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field maxCp. Field curCp. Field maxMp. Field curMp. Field maxHp. Field curHp. Field level. Field class_id. Field obj_id.
	 */
	private final int obj_id, class_id, level, curHp, maxHp, curMp, maxMp, curCp, maxCp;
	
	/**
	 * Constructor for ExDuelUpdateUserInfo.
	 * @param attacker Player
	 */
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
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x50);
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
