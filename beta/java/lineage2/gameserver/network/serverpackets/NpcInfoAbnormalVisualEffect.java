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

import java.util.List;

import lineage2.gameserver.model.Creature;

/**
 * @author Mikhael(Nache)
 */
public class NpcInfoAbnormalVisualEffect extends L2GameServerPacket
{
	private final int _objectId;
	private final int _transformId;
	private final List<Integer> _aveList;
	
	public NpcInfoAbnormalVisualEffect(Creature npc)
	{
		_objectId = npc.getObjectId();
		_transformId = npc.getTransformation();
		_aveList = npc.getAveList();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4B);
		
		writeD(_objectId);
		writeD(_transformId);
		
		if (_aveList != null)
		{
			writeD(_aveList.size());
			
			for (int i : _aveList)
			{
				writeH(i);
			}
		}
	}
}
