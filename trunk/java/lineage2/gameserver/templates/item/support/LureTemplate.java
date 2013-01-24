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
package lineage2.gameserver.templates.item.support;

import java.util.Map;

import lineage2.commons.collections.MultiValueSet;

public class LureTemplate
{
	private final int _itemId;
	private final int _lengthBonus;
	private final double _revisionNumber;
	private final double _rateBonus;
	private final LureType _lureType;
	private final Map<FishGroup, Integer> _chances;
	
	@SuppressWarnings("unchecked")
	public LureTemplate(MultiValueSet<String> set)
	{
		_itemId = set.getInteger("item_id");
		_lengthBonus = set.getInteger("length_bonus");
		_revisionNumber = set.getDouble("revision_number");
		_rateBonus = set.getDouble("rate_bonus");
		_lureType = set.getEnum("type", LureType.class);
		_chances = (Map<FishGroup, Integer>) set.get("chances");
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public int getLengthBonus()
	{
		return _lengthBonus;
	}
	
	public double getRevisionNumber()
	{
		return _revisionNumber;
	}
	
	public double getRateBonus()
	{
		return _rateBonus;
	}
	
	public LureType getLureType()
	{
		return _lureType;
	}
	
	public Map<FishGroup, Integer> getChances()
	{
		return _chances;
	}
}
