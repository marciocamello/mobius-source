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
package lineage2.gameserver.model.items.etcitems;

import lineage2.gameserver.templates.item.ItemTemplate.Grade;

public class EnchantScrollInfo
{
	private int itemId;
	private EnchantScrollType type;
	private EnchantScrollTarget target;
	private Grade grade;
	private int chance, min, safe, max;
	
	public int getItemId()
	{
		return itemId;
	}
	
	public void setItemId(int itemId)
	{
		this.itemId = itemId;
	}
	
	public EnchantScrollType getType()
	{
		return type;
	}
	
	public void setType(EnchantScrollType type)
	{
		this.type = type;
	}
	
	public Grade getGrade()
	{
		return grade;
	}
	
	public void setGrade(Grade grade)
	{
		this.grade = grade;
	}
	
	public int getMin()
	{
		return min;
	}
	
	public void setMin(int min)
	{
		this.min = min;
	}
	
	public int getSafe()
	{
		return safe;
	}
	
	public void setSafe(int safe)
	{
		this.safe = safe;
	}
	
	public int getChance()
	{
		return chance;
	}
	
	public void setChance(int chance)
	{
		this.chance = chance;
	}
	
	public int getMax()
	{
		return max;
	}
	
	public void setMax(int max)
	{
		this.max = max;
	}
	
	public EnchantScrollTarget getTarget()
	{
		return target;
	}
	
	public void setTarget(EnchantScrollTarget target)
	{
		this.target = target;
	}
}
