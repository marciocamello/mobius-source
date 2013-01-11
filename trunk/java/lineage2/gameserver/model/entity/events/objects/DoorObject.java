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
package lineage2.gameserver.model.entity.events.objects;

import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.DoorInstance;

public class DoorObject implements SpawnableObject, InitableObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int _id;
	private DoorInstance _door;
	private boolean _weak;
	
	public DoorObject(int id)
	{
		_id = id;
	}
	
	@Override
	public void initObject(GlobalEvent e)
	{
		_door = e.getReflection().getDoor(_id);
	}
	
	@Override
	public void spawnObject(GlobalEvent event)
	{
		refreshObject(event);
	}
	
	@Override
	public void despawnObject(GlobalEvent event)
	{
		Reflection ref = event.getReflection();
		if (ref == ReflectionManager.DEFAULT)
		{
			refreshObject(event);
		}
		else
		{
		}
	}
	
	@Override
	public void refreshObject(GlobalEvent event)
	{
		if (!event.isInProgress())
		{
			_door.removeEvent(event);
		}
		else
		{
			_door.addEvent(event);
		}
		if (_door.getCurrentHp() <= 0)
		{
			_door.decayMe();
			_door.spawnMe();
		}
		_door.setCurrentHp(_door.getMaxHp() * (isWeak() ? 0.5 : 1.), true);
		close(event);
	}
	
	public int getUId()
	{
		return _door.getDoorId();
	}
	
	public int getUpgradeValue()
	{
		return _door.getUpgradeHp();
	}
	
	public void setUpgradeValue(GlobalEvent event, int val)
	{
		_door.setUpgradeHp(val);
		refreshObject(event);
	}
	
	public void open(GlobalEvent e)
	{
		_door.openMe(null, !e.isInProgress());
	}
	
	public void close(GlobalEvent e)
	{
		_door.closeMe(null, !e.isInProgress());
	}
	
	public DoorInstance getDoor()
	{
		return _door;
	}
	
	public boolean isWeak()
	{
		return _weak;
	}
	
	public void setWeak(boolean weak)
	{
		_weak = weak;
	}
}
