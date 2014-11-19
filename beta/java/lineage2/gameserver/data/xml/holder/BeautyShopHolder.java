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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.beautyshop.BeautyShopSet;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Smo
 */
public final class BeautyShopHolder extends AbstractHolder
{
	private static final BeautyShopHolder _instance = new BeautyShopHolder();
	private static final TIntObjectHashMap<BeautyShopSet> _sets = new TIntObjectHashMap<>();
	
	public static BeautyShopHolder getInstance()
	{
		return _instance;
	}
	
	public void addSet(BeautyShopSet set)
	{
		if (_sets.containsKey(set.getId()))
		{
			warn("Duplicate set declaration, set id - " + set.getId());
		}
		
		_sets.put(set.getId(), set);
	}
	
	private BeautyShopSet getSet(int id)
	{
		return (_sets.get(id));
	}
	
	public BeautyShopSet getSet(Player player)
	{
		int id = -1;
		
		switch (player.getRace())
		{
			case human:
				if (!(player.getClassId().isMage()))
				{
					if (player.getSex() == 0)
					{
						id = 1;
					}
					else
					{
						id = 0;
					}
				}
				else
				{
					if (player.getSex() == 0)
					{
						id = 9;
					}
					else
					{
						id = 8;
					}
				}
				break;
			
			case darkelf:
				if (player.getSex() == 0)
				{
					id = 3;
				}
				else
				{
					id = 2;
				}
				break;
			
			case dwarf:
				if (player.getSex() == 0)
				{
					id = 5;
				}
				else
				{
					id = 4;
				}
				break;
			
			case elf:
				if (player.getSex() == 0)
				{
					id = 7;
				}
				else
				{
					id = 6;
				}
				break;
			
			case orc:
				if (!(player.getClassId().isMage()))
				{
					if (player.getSex() == 0)
					{
						id = 11;
					}
					else
					{
						id = 10;
					}
				}
				else
				{
					if (player.getSex() == 0)
					{
						id = 13;
					}
					else
					{
						id = 12;
					}
				}
				break;
			
			case kamael:
				if (player.getSex() == 0)
				{
					id = 15;
				}
				else
				{
					id = 14;
				}
				break;
			
			case ertheia:
				id = 17;
				break;
		}
		
		return getSet(id);
	}
	
	@Override
	public int size()
	{
		return _sets.size();
	}
	
	@Override
	public void clear()
	{
		_sets.clear();
	}
	
	@Override
	public void log()
	{
		info(String.format("loaded %d beauty shop set(s) count.", new Object[]
		{
			Integer.valueOf(size())
		}));
	}
}
