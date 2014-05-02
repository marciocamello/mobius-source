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
import lineage2.gameserver.model.base.Race;
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
	
	public BeautyShopSet getSet(int id)
	{
		return (_sets.get(id));
	}
	
	public BeautyShopSet getSet(Player player)
	{
		int id = -1;
		if (player.getRace() == Race.human)
		{
			if (!(player.getClassId().isMage()))
			{
				switch (player.getSex())
				{
					case 0:
						id = 1;
						break;
					case 1:
						id = 0;
				}
				
			}
			else
			{
				switch (player.getSex())
				{
					case 0:
						id = 9;
						break;
					case 1:
						id = 8;
				}
				
			}
			
		}
		else if (player.getRace() == Race.darkelf)
		{
			switch (player.getSex())
			{
				case 0:
					id = 3;
					break;
				case 1:
					id = 2;
			}
			
		}
		else if (player.getRace() == Race.dwarf)
		{
			switch (player.getSex())
			{
				case 0:
					id = 5;
					break;
				case 1:
					id = 4;
			}
			
		}
		else if (player.getRace() == Race.elf)
		{
			switch (player.getSex())
			{
				case 0:
					id = 7;
					break;
				case 1:
					id = 6;
			}
			
		}
		else if (player.getRace() == Race.orc)
		{
			if (!(player.getClassId().isMage()))
			{
				switch (player.getSex())
				{
					case 0:
						id = 11;
						break;
					case 1:
						id = 10;
				}
				
			}
			else
			{
				switch (player.getSex())
				{
					case 0:
						id = 13;
						break;
					case 1:
						id = 12;
				}
				
			}
			
		}
		else if (player.getRace() == Race.kamael)
		{
			switch (player.getSex())
			{
				case 0:
					id = 15;
					break;
				case 1:
					id = 14;
			}
			
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
