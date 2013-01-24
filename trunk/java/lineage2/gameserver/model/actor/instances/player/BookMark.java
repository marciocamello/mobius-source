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
package lineage2.gameserver.model.actor.instances.player;

import lineage2.gameserver.utils.Location;

public class BookMark
{
	public final int x, y, z;
	private int icon;
	private String name, acronym;
	
	public BookMark(Location loc, int aicon, String aname, String aacronym)
	{
		this(loc.x, loc.y, loc.z, aicon, aname, aacronym);
	}
	
	public BookMark(int _x, int _y, int _z, int aicon, String aname, String aacronym)
	{
		x = _x;
		y = _y;
		z = _z;
		setIcon(aicon);
		setName(aname);
		setAcronym(aacronym);
	}
	
	public BookMark setIcon(int val)
	{
		icon = val;
		return this;
	}
	
	public int getIcon()
	{
		return icon;
	}
	
	public BookMark setName(String val)
	{
		name = val.length() > 32 ? val.substring(0, 32) : val;
		return this;
	}
	
	public String getName()
	{
		return name;
	}
	
	public BookMark setAcronym(String val)
	{
		acronym = val.length() > 4 ? val.substring(0, 4) : val;
		return this;
	}
	
	public String getAcronym()
	{
		return acronym;
	}
}
