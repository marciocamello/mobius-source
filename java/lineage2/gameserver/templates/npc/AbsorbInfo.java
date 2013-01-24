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
package lineage2.gameserver.templates.npc;

import gnu.trove.set.hash.TIntHashSet;

public class AbsorbInfo
{
	public static enum AbsorbType
	{
		LAST_HIT,
		PARTY_ONE,
		PARTY_ALL,
		PARTY_RANDOM
	}
	
	private final boolean _skill;
	private final AbsorbType _absorbType;
	private final int _chance;
	private final int _cursedChance;
	private final TIntHashSet _levels;
	
	public AbsorbInfo(boolean skill, AbsorbType absorbType, int chance, int cursedChance, int min, int max)
	{
		_skill = skill;
		_absorbType = absorbType;
		_chance = chance;
		_cursedChance = cursedChance;
		_levels = new TIntHashSet(max - min);
		for (int i = min; i <= max; i++)
		{
			_levels.add(i);
		}
	}
	
	public boolean isSkill()
	{
		return _skill;
	}
	
	public AbsorbType getAbsorbType()
	{
		return _absorbType;
	}
	
	public int getChance()
	{
		return _chance;
	}
	
	public int getCursedChance()
	{
		return _cursedChance;
	}
	
	public boolean canAbsorb(int le)
	{
		return _levels.contains(le);
	}
}
