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

import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.GlobalEvent;

public class ZoneObject implements InitableObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String _name;
	private Zone _zone;
	
	public ZoneObject(String name)
	{
		_name = name;
	}
	
	@Override
	public void initObject(GlobalEvent e)
	{
		Reflection r = e.getReflection();
		_zone = r.getZone(_name);
	}
	
	public void setActive(boolean a)
	{
		_zone.setActive(a);
	}
	
	public void setActive(boolean a, GlobalEvent event)
	{
		setActive(a);
	}
	
	public Zone getZone()
	{
		return _zone;
	}
	
	public List<Player> getInsidePlayers()
	{
		return _zone.getInsidePlayers();
	}
	
	public boolean checkIfInZone(Creature c)
	{
		return _zone.checkIfInZone(c);
	}
}
