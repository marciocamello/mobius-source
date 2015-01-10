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
package lineage2.gameserver.data.xml.holder;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.items.etcitems.AppearanceStone;
import lineage2.gameserver.templates.item.support.EnchantScroll;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

public class EnchantItemHolder extends AbstractHolder
{
	private static final EnchantItemHolder _instance = new EnchantItemHolder();
	
	private final IntObjectMap<EnchantScroll> _enchantScrolls = new HashIntObjectMap<>();
	private final IntObjectMap<AppearanceStone> _appearanceStones = new HashIntObjectMap<>();
	
	private EnchantItemHolder()
	{
	}
	
	public static EnchantItemHolder getInstance()
	{
		return _instance;
	}
	
	public void addEnchantScroll(EnchantScroll enchantScroll)
	{
		_enchantScrolls.put(enchantScroll.getId(), enchantScroll);
	}
	
	public void addAppearanceStone(AppearanceStone appearanceStone)
	{
		_appearanceStones.put(appearanceStone.getItemId(), appearanceStone);
	}
	
	public EnchantScroll getEnchantScroll(int id)
	{
		return _enchantScrolls.get(id);
	}
	
	public AppearanceStone getAppearanceStone(int id)
	{
		return _appearanceStones.get(id);
	}
	
	public int[] getEnchantScrolls()
	{
		return _enchantScrolls.keySet().toArray();
	}
	
	public int[] getAppearanceStones()
	{
		return _appearanceStones.keySet().toArray();
	}
	
	@Override
	public void log()
	{
		info("loaded " + _enchantScrolls.size() + " enchant scroll(s) count.");
		info("loaded " + _appearanceStones.size() + " appearance stone(s) count.");
	}
	
	@Override
	public int size()
	{
		return 0;
	}
	
	@Override
	public void clear()
	{
		_enchantScrolls.clear();
		_appearanceStones.clear();
	}
}