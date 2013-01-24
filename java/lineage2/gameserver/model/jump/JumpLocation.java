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
package lineage2.gameserver.model.jump;

import lineage2.gameserver.utils.Location;

public class JumpLocation
{
	private final String zoneName;
	private final int id;
	private final boolean is_last;
	private final int[] routes;
	private final Location location;
	
	public JumpLocation(String zoneName, int id, boolean is_last, int[] routes, Location location)
	{
		this.zoneName = zoneName;
		this.id = id;
		this.is_last = is_last;
		this.routes = routes;
		this.location = location;
	}
	
	public String getZoneName()
	{
		return zoneName;
	}
	
	public int getId()
	{
		return id;
	}
	
	public boolean isLast()
	{
		return is_last;
	}
	
	public int[] getRoutes()
	{
		return routes;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public int getX()
	{
		return location.getX();
	}
	
	public int getY()
	{
		return location.getY();
	}
	
	public int getZ()
	{
		return location.getZ();
	}
}
