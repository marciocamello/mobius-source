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

/**
 * @author KilRoy
 */
public class ExLoadStatWorldRank extends L2GameServerPacket
{
	private final int _section;
	private final int _subSection;
	private final List<CharacterStatistic> _monthlyData;
	private final List<CharacterStatistic> _generalData;
	
	public ExLoadStatWorldRank(int section, int subSection, List<CharacterStatistic> generalData, List<CharacterStatistic> monthlyData)
	{
		_section = section;
		_subSection = subSection;
		_generalData = generalData;
		_monthlyData = monthlyData;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x101);
		
		writeD(_section);
		writeD(_subSection);
		
		writeD(_monthlyData.size());
		for (int i = 0, monthlyDataSize = _monthlyData.size(); i < monthlyDataSize; i++)
		{
			CharacterStatistic statistic = _monthlyData.get(i);
			writeH(i + 1);
			writeD(statistic.getObjId());
			writeS(statistic.getName());
			writeQ(statistic.getValue());
			writeH(0);
			writeD(statistic.getClanObjId());
			writeD(statistic.getClanCrestId());
		}
		
		writeD(_generalData.size());
		for (int i = 0, generalDataSize = _generalData.size(); i < generalDataSize; i++)
		{
			CharacterStatistic statistic = _generalData.get(i);
			writeH(i + 1);
			writeD(statistic.getObjId());
			writeS(statistic.getName());
			writeQ(statistic.getValue());
			writeH(0);
			writeD(statistic.getObjId());
			writeD(statistic.getClanCrestId());
		}
	}
}
