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
package lineage2.gameserver.model.quest.campaign;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.taskmanager.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CampaignTask extends RunnableImpl
{
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(CampaignTask.class);
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _progress.
	 */
	private final int _progress;
	/**
	 * Field _totalProgress.
	 */
	private final int _totalProgress;
	
	/**
	 * Constructor for CampaignTask.
	 * @param id int
	 * @param progress int
	 * @param totalProgress int
	 * @param time long
	 */
	public CampaignTask(int id, int progress, int totalProgress, long time)
	{
		_id = id;
		_progress = progress;
		_totalProgress = totalProgress;
	}
	
	/**
	 * Field _task.
	 */
	Task _task;
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method getCurrentProgress.
	 * @return int
	 */
	public int getCurrentProgress()
	{
		return _progress;
	}
	
	/**
	 * Method getTask.
	 * @return Task
	 */
	public Task getTask()
	{
		return _task;
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
	}
	
	/**
	 * Method getTotalProgress.
	 * @return int
	 */
	public int getTotalProgress()
	{
		return _totalProgress;
	}
}
