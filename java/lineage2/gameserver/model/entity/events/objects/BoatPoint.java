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

import lineage2.gameserver.utils.Location;

import org.dom4j.Element;

public class BoatPoint extends Location
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _speed1;
	private int _speed2;
	private final int _fuel;
	private boolean _teleport;
	
	public BoatPoint(int x, int y, int z, int h, int speed1, int speed2, int fuel, boolean teleport)
	{
		super(x, y, z, h);
		_speed1 = speed1;
		_speed2 = speed2;
		_fuel = fuel;
		_teleport = teleport;
	}
	
	public int getSpeed1()
	{
		return _speed1;
	}
	
	public int getSpeed2()
	{
		return _speed2;
	}
	
	public int getFuel()
	{
		return _fuel;
	}
	
	public boolean isTeleport()
	{
		return _teleport;
	}
	
	public static BoatPoint parse(Element element)
	{
		int speed1 = element.attributeValue("speed1") == null ? 0 : Integer.parseInt(element.attributeValue("speed1"));
		int speed2 = element.attributeValue("speed2") == null ? 0 : Integer.parseInt(element.attributeValue("speed2"));
		int x = Integer.parseInt(element.attributeValue("x"));
		int y = Integer.parseInt(element.attributeValue("y"));
		int z = Integer.parseInt(element.attributeValue("z"));
		int h = element.attributeValue("h") == null ? 0 : Integer.parseInt(element.attributeValue("h"));
		int fuel = element.attributeValue("fuel") == null ? 0 : Integer.parseInt(element.attributeValue("fuel"));
		boolean teleport = Boolean.parseBoolean(element.attributeValue("teleport"));
		return new BoatPoint(x, y, z, h, speed1, speed2, fuel, teleport);
	}
	
	public void setSpeed1(int speed1)
	{
		_speed1 = speed1;
	}
	
	public void setSpeed2(int speed2)
	{
		_speed2 = speed2;
	}
	
	public void setTeleport(boolean teleport)
	{
		_teleport = teleport;
	}
}
