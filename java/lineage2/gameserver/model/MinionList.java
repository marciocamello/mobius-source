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
package lineage2.gameserver.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.instances.MinionInstance;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.templates.npc.MinionData;

public class MinionList
{
	private final Set<MinionData> _minionData;
	private final Set<MinionInstance> _minions;
	private final Lock lock;
	private final MonsterInstance _master;
	
	public MinionList(MonsterInstance master)
	{
		_master = master;
		_minions = new HashSet<>();
		_minionData = new HashSet<>();
		_minionData.addAll(_master.getTemplate().getMinionData());
		lock = new ReentrantLock();
	}
	
	public boolean addMinion(MinionData m)
	{
		lock.lock();
		try
		{
			return _minionData.add(m);
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public boolean addMinion(MinionInstance m)
	{
		lock.lock();
		try
		{
			return _minions.add(m);
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public boolean hasAliveMinions()
	{
		lock.lock();
		try
		{
			for (MinionInstance m : _minions)
			{
				if (m.isVisible() && !m.isDead())
				{
					return true;
				}
			}
		}
		finally
		{
			lock.unlock();
		}
		return false;
	}
	
	public boolean hasMinions()
	{
		return _minionData.size() > 0;
	}
	
	public List<MinionInstance> getAliveMinions()
	{
		List<MinionInstance> result = new ArrayList<>(_minions.size());
		lock.lock();
		try
		{
			for (MinionInstance m : _minions)
			{
				if (m.isVisible() && !m.isDead())
				{
					result.add(m);
				}
			}
		}
		finally
		{
			lock.unlock();
		}
		return result;
	}
	
	public void spawnMinions()
	{
		lock.lock();
		try
		{
			int minionCount;
			int minionId;
			for (MinionData minion : _minionData)
			{
				minionId = minion.getMinionId();
				minionCount = minion.getAmount();
				for (MinionInstance m : _minions)
				{
					if (m.getNpcId() == minionId)
					{
						minionCount--;
					}
					if (m.isDead() || !m.isVisible())
					{
						m.refreshID();
						m.stopDecay();
						_master.spawnMinion(m);
					}
				}
				for (int i = 0; i < minionCount; i++)
				{
					MinionInstance m = new MinionInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(minionId));
					m.setLeader(_master);
					_master.spawnMinion(m);
					_minions.add(m);
				}
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public void unspawnMinions()
	{
		lock.lock();
		try
		{
			for (MinionInstance m : _minions)
			{
				m.decayMe();
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	
	public void deleteMinions()
	{
		lock.lock();
		try
		{
			for (MinionInstance m : _minions)
			{
				m.deleteMe();
			}
			_minions.clear();
		}
		finally
		{
			lock.unlock();
		}
	}
}
