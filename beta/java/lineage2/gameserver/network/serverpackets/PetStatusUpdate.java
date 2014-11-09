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

public class PetStatusUpdate extends L2GameServerPacket
{
	private final int type;
	private final int type2;
	private final int obj_id;
	private final int level;
	private final int maxFed;
	private final int curFed;
	private final int maxHp;
	private final int curHp;
	private final int maxMp;
	private final int curMp;
	private final long exp;
	private final long exp_this_lvl;
	private final long exp_next_lvl;
	private final Location _loc;
	private final String title;
	
	public PetStatusUpdate(final Summon summon, int addType)
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
		type2 = addType;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xb6);
		writeD(type);
		writeD(obj_id);
		writeD(_loc.getX());
		writeD(_loc.getY());
		writeD(_loc.getZ());
		writeS(title);
		writeD(curFed);
		writeD(maxFed);
		writeD(curHp);
		writeD(maxHp);
		writeD(curMp);
		writeD(maxMp);
		writeD(level);
		writeQ(exp);
		writeQ(exp_this_lvl);// 0% absolute value
		writeQ(exp_next_lvl);// 100% absolute value
		writeD(type2);
	}
}