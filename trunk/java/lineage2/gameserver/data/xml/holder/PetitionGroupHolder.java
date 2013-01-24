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

import java.util.Collection;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.petition.PetitionMainGroup;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

public class PetitionGroupHolder extends AbstractHolder
{
	private static PetitionGroupHolder _instance = new PetitionGroupHolder();
	private final IntObjectMap<PetitionMainGroup> _petitionGroups = new HashIntObjectMap<>();
	
	public static PetitionGroupHolder getInstance()
	{
		return _instance;
	}
	
	private PetitionGroupHolder()
	{
	}
	
	public void addPetitionGroup(PetitionMainGroup g)
	{
		_petitionGroups.put(g.getId(), g);
	}
	
	public PetitionMainGroup getPetitionGroup(int val)
	{
		return _petitionGroups.get(val);
	}
	
	public Collection<PetitionMainGroup> getPetitionGroups()
	{
		return _petitionGroups.values();
	}
	
	@Override
	public int size()
	{
		return _petitionGroups.size();
	}
	
	@Override
	public void clear()
	{
		_petitionGroups.clear();
	}
}
