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

public class ExPartyPetWindowUpdate extends L2GameServerPacket
{
	private final int owner_obj_id;
	private final int npc_id;
	private final int _type;
	private final int curHp;
	private final int maxHp;
	private final int curMp;
	private final int maxMp;
	private int obj_id = 0;
	
	public ExPartyPetWindowUpdate(Summon summon)
	{
		obj_id = summon.getObjectId();
		owner_obj_id = summon.getPlayer().getObjectId();
		npc_id = summon.getTemplate().getId() + 1000000;
		_type = summon.getSummonType();
		curHp = (int) summon.getCurrentHp();
		maxHp = summon.getMaxHp();
		curMp = (int) summon.getCurrentMp();
		maxMp = summon.getMaxMp();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x19);
		writeD(obj_id);
		writeD(npc_id);
		writeC(_type);
		writeD(owner_obj_id);
		writeD(curHp);
		writeD(maxHp);
		writeD(curMp);
		writeD(maxMp);
	}
}