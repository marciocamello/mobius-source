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
import lineage2.gameserver.model.worldstatistics.CharacterStatistic;

public class ExLoadStatHotLink extends L2GameServerPacket
{
	private final int categoryId;
	private final int subCatId;
	private final List<CharacterStatistic> globalStatistic;
	private final List<CharacterStatistic> monthlyStatistic;
	
	public ExLoadStatHotLink(int categoryId, int subCatId, List<CharacterStatistic> globalStatistic, List<CharacterStatistic> monthlyStatistic)
	{
		this.categoryId = categoryId;
		this.subCatId = subCatId;
		this.globalStatistic = globalStatistic;
		this.monthlyStatistic = monthlyStatistic;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x103);
		writeD(categoryId); // catId
		writeD(subCatId); // subCatId
		// Monthly
		writeD(monthlyStatistic.size()); // loop count (always 5)
		
		for (int i = 0; i < monthlyStatistic.size(); i++)
		{
			CharacterStatistic statistic = monthlyStatistic.get(i);
			writeH(i + 1); // rating pos
			writeD(statistic.getObjId()); // objId
			writeS(statistic.getName()); // CharName
			writeQ(statistic.getValue()); // Value
			writeH(0x00);// TODO: Поднялся или опустился в рейтинге
			writeD(0x00);
			writeD(0x00);
		}
		
		// General
		writeD(globalStatistic.size()); // loop count (always 5)
		
		for (int i = 0; i < globalStatistic.size(); i++)
		{
			CharacterStatistic statistic = globalStatistic.get(i);
			writeH(i + 1); // rating pos
			writeD(statistic.getObjId()); // objId
			writeS(statistic.getName()); // CharName
			writeQ(statistic.getValue()); // Value
			writeH(0x00);// TODO: Поднялся или опустился в рейтинге
			writeD(0x00);
			writeD(0x00);
		}
	}
}