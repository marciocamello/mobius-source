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
public class PartySmallWindowUpdate extends L2GameServerPacket
{
	/**
	 * Field level. Field class_id. Field obj_id.
	 */
	private final int obj_id, class_id, level;
	/**
	 * Field vitality. Field maxMp. Field curMp. Field maxHp. Field curHp. Field maxCp. Field curCp.
	 */
	private final int curCp, maxCp, curHp, maxHp, curMp, maxMp, vitality;
	/**
	 * Field obj_name.
	 */
	private final String obj_name;
	
	/**
	 * Constructor for PartySmallWindowUpdate.
	 * @param member Player
	 */
	public PartySmallWindowUpdate(Player member)
	{
		obj_id = member.getObjectId();
		obj_name = member.getName();
		curCp = (int) member.getCurrentCp();
		maxCp = member.getMaxCp();
		curHp = (int) member.getCurrentHp();
		maxHp = member.getMaxHp();
		curMp = (int) member.getCurrentMp();
		maxMp = member.getMaxMp();
		level = member.getLevel();
		class_id = member.getClassId().getId();
		vitality = member.getVitality();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x52);
		writeD(obj_id);
		writeS(obj_name);
		writeD(curCp);
		writeD(maxCp);
		writeD(curHp);
		writeD(maxHp);
		writeD(curMp);
		writeD(maxMp);
		writeD(level);
		writeD(class_id);
		writeD(vitality);
		writeD(0x00);
	}
}
