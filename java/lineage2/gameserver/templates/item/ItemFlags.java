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
package lineage2.gameserver.templates.item;

public enum ItemFlags
{
	DESTROYABLE(true),
	DROPABLE(true),
	FREIGHTABLE(false),
	AUGMENTABLE(true),
	ENCHANTABLE(true),
	ATTRIBUTABLE(true),
	SELLABLE(true),
	TRADEABLE(true),
	STOREABLE(true);
	public static final ItemFlags[] VALUES = values();
	private final int _mask;
	private final boolean _defaultValue;
	
	ItemFlags(boolean defaultValue)
	{
		_defaultValue = defaultValue;
		_mask = 1 << ordinal();
	}
	
	public int mask()
	{
		return _mask;
	}
	
	public boolean getDefaultValue()
	{
		return _defaultValue;
	}
}
