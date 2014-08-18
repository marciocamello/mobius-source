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

public class ExPartyPetWindowAdd extends L2GameServerPacket
{
	private final int ownerId;
	private final int npcId;
	private final int type;
	private final int curHp;
	private final int maxHp;
	private final int curMp;
	private final int maxMp;
	private final int level;
	private final int summonId;
	private final String name;
	
	public ExPartyPetWindowAdd(Summon summon)
	{
		summonId = summon.getObjectId();
		ownerId = summon.getPlayer().getObjectId();
		npcId = summon.getTemplate().npcId + 1000000;
		type = summon.getSummonType();
		name = summon.getName();
		curHp = (int) summon.getCurrentHp();
		maxHp = summon.getMaxHp();
		curMp = (int) summon.getCurrentMp();
		maxMp = summon.getMaxMp();
		level = summon.getLevel();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x18);
		writeD(summonId);
		writeD(npcId);
		writeD(type);
		writeD(ownerId);
		writeS(name);
		writeD(curHp);
		writeD(maxHp);
		writeD(curMp);
		writeD(maxMp);
		writeD(level);
	}
}