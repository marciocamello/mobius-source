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
package lineage2.gameserver.network.clientpackets;

import java.util.List;
import lineage2.gameserver.instancemanager.WorldStatisticsManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.worldstatistics.CategoryType;
import lineage2.gameserver.model.worldstatistics.CharacterStatistic;
import lineage2.gameserver.network.serverpackets.ExLoadStatWorldRank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestWorldStatistics extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestWorldStatistics.class);
	private int _section;
	private int _subSection;
	
	@Override
	protected void readImpl()
	{
		_section = readD();
		_subSection = readD();
	}
	
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		CategoryType cat = CategoryType.getCategoryById(_section, _subSection);
		
		if (cat == null)
		{
			_log.warn("RequestWorldStatistics: Not found category for section: " + _section + " subsection: " + _subSection);
			return;
		}
		
		List<CharacterStatistic> generalStatisticList = WorldStatisticsManager.getInstance().getStatisticTop(cat, true, WorldStatisticsManager.STATISTIC_TOP_PLAYER_LIMIT);
		List<CharacterStatistic> monthlyStatisticList = WorldStatisticsManager.getInstance().getStatisticTop(cat, false, WorldStatisticsManager.STATISTIC_TOP_PLAYER_LIMIT);
		activeChar.sendPacket(new ExLoadStatWorldRank(_section, _subSection, generalStatisticList, monthlyStatisticList));
	}
}