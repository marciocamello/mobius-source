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

import lineage2.gameserver.model.Summon;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PetStatusUpdate extends L2GameServerPacket
{
	/**
	 * Field level. Field obj_id. Field type.
	 */
	private final int type, obj_id, level;
	/**
	 * Field curMp. Field maxMp. Field curHp. Field maxHp. Field curFed. Field maxFed.
	 */
	private final int maxFed, curFed, maxHp, curHp, maxMp, curMp;
	/**
	 * Field exp_next_lvl. Field exp_this_lvl. Field exp.
	 */
	private final long exp, exp_this_lvl, exp_next_lvl;
	/**
	 * Field _loc.
	 */
	private final Location _loc;
	/**
	 * Field title.
	 */
	private final String title;
	
	/**
	 * Constructor for PetStatusUpdate.
	 * @param summon Summon
	 */
	public PetStatusUpdate(final Summon summon)
	{
		type = summon.getSummonType();
		obj_id = summon.getObjectId();
		_loc = summon.getLoc();
		title = summon.getTitle();
		curHp = (int) summon.getCurrentHp();
		maxHp = summon.getMaxHp();
		curMp = (int) summon.getCurrentMp();
		maxMp = summon.getMaxMp();
		curFed = summon.getCurrentFed();
		maxFed = summon.getMaxFed();
		level = summon.getLevel();
		exp = summon.getExp();
		exp_this_lvl = summon.getExpForThisLevel();
		exp_next_lvl = summon.getExpForNextLevel();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xb6);
		writeD(type);
		writeD(obj_id);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeS(title);
		writeD(curFed);
		writeD(maxFed);
		writeD(curHp);
		writeD(maxHp);
		writeD(curMp);
		writeD(maxMp);
		writeD(level);
		writeQ(exp);
		writeQ(exp_this_lvl);
		writeQ(exp_next_lvl);
		writeD(0x00);
	}
}
