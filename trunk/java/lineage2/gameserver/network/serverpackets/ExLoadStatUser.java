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

import lineage2.gameserver.model.worldstatistics.CharacterStatisticElement;

/**
 * @author KilRoy
 */
public class ExLoadStatUser extends L2GameServerPacket
{
	private final List<CharacterStatisticElement> list;
	
	public ExLoadStatUser(List<CharacterStatisticElement> list)
	{
		this.list = list;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x102);
		writeD(list.size());
		
		for (CharacterStatisticElement stat : list)
		{
			writeD(stat.getCategoryType().getClientId());
			writeD(stat.getCategoryType().getSubcat());
			writeQ(stat.getMonthlyValue());
			writeQ(stat.getValue());
		}
	}
}