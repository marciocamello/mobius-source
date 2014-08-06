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
package lineage2.gameserver.taskmanager.tasks;

import java.util.Calendar;
import lineage2.gameserver.instancemanager.WorldStatisticsManager;
import lineage2.gameserver.taskmanager.Task;
import lineage2.gameserver.taskmanager.TaskManager;
import lineage2.gameserver.taskmanager.TaskTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Дмитрий
 * @date 11.10.12 23:37
 */
public class WorldStatisticUpdate extends Task
{
	private static final Logger _log = LoggerFactory.getLogger(WorldStatisticUpdate.class);
	private static final String NAME = "world_statistic_update";
	private static final int DAY_OF_MONTH = 1;
	
	@Override
	public void initializate()
	{
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_GLOBAL_TASK, "1", "06:30:00", "");
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(TaskManager.ExecutedTask task)
	{
		if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == DAY_OF_MONTH)
		{
			_log.info("World statistic task: launched.");
			WorldStatisticsManager.getInstance().resetMonthlyStatistic();
			_log.info("World statistic task: completed.");
		}
	}
}
