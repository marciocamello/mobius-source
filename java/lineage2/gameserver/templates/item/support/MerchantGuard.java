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
package lineage2.gameserver.templates.item.support;

import org.napile.primitive.sets.IntSet;

public class MerchantGuard
{
	private final int _itemId;
	private final int _npcId;
	private final int _max;
	
	public MerchantGuard(int itemId, int npcId, int max, IntSet ssq)
	{
		_itemId = itemId;
		_npcId = npcId;
		_max = max;
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public int getNpcId()
	{
		return _npcId;
	}
	
	public int getMax()
	{
		return _max;
	}
}
