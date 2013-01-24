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
package lineage2.gameserver.data.xml.holder;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.templates.DoorTemplate;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

public final class DoorHolder extends AbstractHolder
{
	private static final DoorHolder _instance = new DoorHolder();
	private final IntObjectMap<DoorTemplate> _doors = new HashIntObjectMap<>();
	
	public static DoorHolder getInstance()
	{
		return _instance;
	}
	
	public void addTemplate(DoorTemplate door)
	{
		_doors.put(door.getNpcId(), door);
	}
	
	public DoorTemplate getTemplate(int doorId)
	{
		return _doors.get(doorId);
	}
	
	public IntObjectMap<DoorTemplate> getDoors()
	{
		return _doors;
	}
	
	@Override
	public int size()
	{
		return _doors.size();
	}
	
	@Override
	public void clear()
	{
		_doors.clear();
	}
}
