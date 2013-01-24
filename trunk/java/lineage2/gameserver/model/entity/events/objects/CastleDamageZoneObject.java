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
package lineage2.gameserver.model.entity.events.objects;

public class CastleDamageZoneObject extends ZoneObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final long _price;
	
	public CastleDamageZoneObject(String name, long price)
	{
		super(name);
		_price = price;
	}
	
	public long getPrice()
	{
		return _price;
	}
}
