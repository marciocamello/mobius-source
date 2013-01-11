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

import lineage2.gameserver.data.xml.holder.StaticObjectHolder;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.StaticObjectInstance;

public class StaticObjectObject implements SpawnableObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int _uid;
	private StaticObjectInstance _instance;
	
	public StaticObjectObject(int id)
	{
		_uid = id;
	}
	
	@Override
	public void spawnObject(GlobalEvent event)
	{
		_instance = StaticObjectHolder.getInstance().getObject(_uid);
	}
	
	@Override
	public void despawnObject(GlobalEvent event)
	{
	}
	
	@Override
	public void refreshObject(GlobalEvent event)
	{
		if (!event.isInProgress())
		{
			_instance.removeEvent(event);
		}
		else
		{
			_instance.addEvent(event);
		}
	}
	
	public void setMeshIndex(int id)
	{
		_instance.setMeshIndex(id);
		_instance.broadcastInfo(false);
	}
	
	public int getUId()
	{
		return _uid;
	}
}
