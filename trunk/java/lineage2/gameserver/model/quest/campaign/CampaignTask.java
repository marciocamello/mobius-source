/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.model.quest.campaign;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.taskmanager.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CampaignTask extends RunnableImpl
{
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(CampaignTask.class);
	private final int _id;
	private final int _progress;
	private final int _totalProgress;
	
	public CampaignTask(int id, int progress, int totalProgress, long time)
	{
		_id = id;
		_progress = progress;
		_totalProgress = totalProgress;
	}
	
	Task _task;
	
	public int getId()
	{
		return _id;
	}
	
	public int getCurrentProgress()
	{
		return _progress;
	}
	
	public Task getTask()
	{
		return _task;
	}
	
	@Override
	public void runImpl()
	{
	}
	
	public int getTotalProgress()
	{
		return _totalProgress;
	}
}
