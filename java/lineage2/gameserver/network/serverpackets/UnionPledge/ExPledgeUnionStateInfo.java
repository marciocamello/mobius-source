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
package lineage2.gameserver.network.serverpackets.UnionPledge;

import lineage2.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author Smo
 */
public class ExPledgeUnionStateInfo extends L2GameServerPacket
{
	private final int _unionType;
	private final int _collectNum;
	private final int _successNum;
	
	public ExPledgeUnionStateInfo(int unionType, int collectNum, int successNum)
	{
		_unionType = unionType;
		_collectNum = collectNum;
		_successNum = successNum;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x138);
		writeD(_unionType);
		writeD(_collectNum);
		writeD(_successNum);
	}
}
