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
package lineage2.gameserver.templates.skill.restoration;

import java.util.ArrayList;
import java.util.List;

public final class RestorationGroup
{
	private final double _chance;
	private final List<RestorationItem> _restorationItems;
	
	public RestorationGroup(double chance)
	{
		_chance = chance;
		_restorationItems = new ArrayList<>();
	}
	
	public double getChance()
	{
		return _chance;
	}
	
	public void addRestorationItem(RestorationItem item)
	{
		_restorationItems.add(item);
	}
	
	public List<RestorationItem> getRestorationItems()
	{
		return _restorationItems;
	}
}