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
package lineage2.gameserver.model.items.etcitems;

import lineage2.gameserver.model.base.Element;

public class AttributeStoneInfo
{
	private int itemId;
	private int minArmor;
	private int maxArmor;
	private int minWeapon;
	private int maxWeapon;
	private int incArmor;
	private int incWeapon;
	private int incWeaponArmor;
	private Element element;
	private int chance;
	
	public int getItemId()
	{
		return itemId;
	}
	
	public void setItemId(int itemId)
	{
		this.itemId = itemId;
	}
	
	public int getMinArmor()
	{
		return minArmor;
	}
	
	public void setMinArmor(int min)
	{
		minArmor = min;
	}
	
	public int getMaxArmor()
	{
		return maxArmor;
	}
	
	public void setMaxArmor(int max)
	{
		maxArmor = max;
	}
	
	public int getMinWeapon()
	{
		return minWeapon;
	}
	
	public void setMinWeapon(int minWeapon)
	{
		this.minWeapon = minWeapon;
	}
	
	public int getMaxWeapon()
	{
		return maxWeapon;
	}
	
	public void setMaxWeapon(int maxWeapon)
	{
		this.maxWeapon = maxWeapon;
	}
	
	public int getIncArmor()
	{
		return incArmor;
	}
	
	public void setIncArmor(int incArmor)
	{
		this.incArmor = incArmor;
	}
	
	public int getIncWeapon()
	{
		return incWeapon;
	}
	
	public void setIncWeapon(int incWeapon)
	{
		this.incWeapon = incWeapon;
	}
	
	public int getincWeaponArmor()
	{
		return incWeaponArmor;
	}
	
	public void setincWeaponArmor(int intWeaponArmor)
	{
		incWeaponArmor = intWeaponArmor;
	}
	
	public Element getElement()
	{
		return element;
	}
	
	public void setElement(Element element)
	{
		this.element = element;
	}
	
	public int getChance()
	{
		return chance;
	}
	
	public void setChance(int chance)
	{
		this.chance = chance;
	}
	
	public int getStoneLevel()
	{
		return maxWeapon / 50;
	}
}
