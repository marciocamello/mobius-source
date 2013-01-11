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

import java.util.Set;

import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.residences.SiegeToggleNpcInstance;
import lineage2.gameserver.utils.Location;

public class SiegeToggleNpcObject implements SpawnableObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final SiegeToggleNpcInstance _toggleNpc;
	private final Location _location;
	
	public SiegeToggleNpcObject(int id, int fakeNpcId, Location loc, int hp, Set<String> set)
	{
		_location = loc;
		_toggleNpc = (SiegeToggleNpcInstance) NpcHolder.getInstance().getTemplate(id).getNewInstance();
		_toggleNpc.initFake(fakeNpcId);
		_toggleNpc.setMaxHp(hp);
		_toggleNpc.setZoneList(set);
	}
	
	@Override
	public void spawnObject(GlobalEvent event)
	{
		_toggleNpc.decayFake();
		if (event.isInProgress())
		{
			_toggleNpc.addEvent(event);
		}
		else
		{
			_toggleNpc.removeEvent(event);
		}
		_toggleNpc.setCurrentHp(_toggleNpc.getMaxHp(), true);
		_toggleNpc.spawnMe(_location);
	}
	
	@Override
	public void despawnObject(GlobalEvent event)
	{
		_toggleNpc.removeEvent(event);
		_toggleNpc.decayFake();
		_toggleNpc.decayMe();
	}
	
	@Override
	public void refreshObject(GlobalEvent event)
	{
	}
	
	public SiegeToggleNpcInstance getToggleNpc()
	{
		return _toggleNpc;
	}
	
	public boolean isAlive()
	{
		return _toggleNpc.isVisible();
	}
}
