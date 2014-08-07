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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.model.base.AcquireType;

/**
 * Reworked: VISTALL
 */
public class AcquireSkillInfo extends L2GameServerPacket
{
	private final SkillLearn _learn;
	private final AcquireType _type;
	private List<Require> _reqs = Collections.emptyList();
	
	public AcquireSkillInfo(AcquireType type, SkillLearn learn)
	{
		_type = type;
		_learn = learn;
		
		if (_learn.getItemId() != 0)
		{
			_reqs = new ArrayList<>(1);
			_reqs.add(new Require(99, _learn.getItemId(), _learn.getItemCount(), 50));
		}
	}
	
	@Override
	public void writeImpl()
	{
		writeC(0x91);
		writeD(_learn.getId());
		writeD(_learn.getLevel());
		writeD(_learn.getCost()); // sp/rep
		writeD(_type.ordinal());
		writeD(_reqs.size()); // requires size
		
		for (Require temp : _reqs)
		{
			writeD(temp.type);
			writeD(temp.itemId);
			writeQ(temp.count);
			writeD(temp.unk);
		}
	}
	
	private static class Require
	{
		public int itemId;
		public long count;
		public int type;
		public int unk;
		
		public Require(int pType, int pItemId, long pCount, int pUnk)
		{
			itemId = pItemId;
			type = pType;
			count = pCount;
			unk = pUnk;
		}
	}
}