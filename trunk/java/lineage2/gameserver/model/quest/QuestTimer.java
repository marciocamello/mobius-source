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
package lineage2.gameserver.model.quest;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.instances.NpcInstance;

public class QuestTimer extends RunnableImpl
{
	private final String _name;
	private final NpcInstance _npc;
	private long _time;
	private QuestState _qs;
	private ScheduledFuture<?> _schedule;
	
	public QuestTimer(String name, long time, NpcInstance npc)
	{
		_name = name;
		_time = time;
		_npc = npc;
	}
	
	void setQuestState(QuestState qs)
	{
		_qs = qs;
	}
	
	QuestState getQuestState()
	{
		return _qs;
	}
	
	void start()
	{
		_schedule = ThreadPoolManager.getInstance().schedule(this, _time);
	}
	
	@Override
	public void runImpl()
	{
		QuestState qs = getQuestState();
		if (qs != null)
		{
			qs.removeQuestTimer(getName());
			qs.getQuest().notifyEvent(getName(), qs, getNpc());
		}
	}
	
	void pause()
	{
		if (_schedule != null)
		{
			_time = _schedule.getDelay(TimeUnit.SECONDS);
			_schedule.cancel(false);
		}
	}
	
	void stop()
	{
		if (_schedule != null)
		{
			_schedule.cancel(false);
		}
	}
	
	public boolean isActive()
	{
		return (_schedule != null) && !_schedule.isDone();
	}
	
	public String getName()
	{
		return _name;
	}
	
	public long getTime()
	{
		return _time;
	}
	
	public NpcInstance getNpc()
	{
		return _npc;
	}
	
	@Override
	public final String toString()
	{
		return _name;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (o.getClass() != this.getClass())
		{
			return false;
		}
		return ((QuestTimer) o).getName().equals(getName());
	}
}
