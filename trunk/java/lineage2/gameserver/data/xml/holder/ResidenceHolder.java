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
package lineage2.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.residence.Residence;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.TreeIntObjectMap;

@SuppressWarnings("unchecked")
public final class ResidenceHolder extends AbstractHolder
{
	private static ResidenceHolder _instance = new ResidenceHolder();
	private final IntObjectMap<Residence> _residences = new TreeIntObjectMap<>();
	private final Map<Class, List<Residence>> _fastResidencesByType = new HashMap<>(4);
	
	public static ResidenceHolder getInstance()
	{
		return _instance;
	}
	
	private ResidenceHolder()
	{
	}
	
	public void addResidence(Residence r)
	{
		_residences.put(r.getId(), r);
	}
	
	public <R extends Residence> R getResidence(int id)
	{
		return (R) _residences.get(id);
	}
	
	public <R extends Residence> R getResidence(Class<R> type, int id)
	{
		Residence r = getResidence(id);
		if ((r == null) || (r.getClass() != type))
		{
			return null;
		}
		return (R) r;
	}
	
	public <R extends Residence> List<R> getResidenceList(Class<R> t)
	{
		return (List<R>) _fastResidencesByType.get(t);
	}
	
	public Collection<Residence> getResidences()
	{
		return _residences.values();
	}
	
	public <R extends Residence> R getResidenceByObject(Class<? extends Residence> type, GameObject object)
	{
		return (R) getResidenceByCoord(type, object.getX(), object.getY(), object.getZ(), object.getReflection());
	}
	
	public <R extends Residence> R getResidenceByCoord(Class<R> type, int x, int y, int z, Reflection ref)
	{
		Collection<Residence> residences = type == null ? getResidences() : (Collection<Residence>) getResidenceList(type);
		for (Residence residence : residences)
		{
			if (residence.checkIfInZone(x, y, z, ref))
			{
				return (R) residence;
			}
		}
		return null;
	}
	
	public <R extends Residence> R findNearestResidence(Class<R> clazz, int x, int y, int z, Reflection ref, int offset)
	{
		Residence residence = getResidenceByCoord(clazz, x, y, z, ref);
		if (residence == null)
		{
			double closestDistance = offset;
			double distance;
			for (Residence r : getResidenceList(clazz))
			{
				distance = r.getZone().findDistanceToZone(x, y, z, false);
				if (closestDistance > distance)
				{
					closestDistance = distance;
					residence = r;
				}
			}
		}
		return (R) residence;
	}
	
	public void callInit()
	{
		for (Residence r : getResidences())
		{
			r.init();
		}
	}
	
	private void buildFastLook()
	{
		for (Residence residence : _residences.values())
		{
			List<Residence> list = _fastResidencesByType.get(residence.getClass());
			if (list == null)
			{
				_fastResidencesByType.put(residence.getClass(), (list = new ArrayList<>()));
			}
			list.add(residence);
		}
	}
	
	@Override
	public void log()
	{
		buildFastLook();
		info("total size: " + _residences.size());
		for (Map.Entry<Class, List<Residence>> entry : _fastResidencesByType.entrySet())
		{
			info(" - load " + entry.getValue().size() + " " + entry.getKey().getSimpleName().toLowerCase() + "(s).");
		}
	}
	
	@Override
	public int size()
	{
		return 0;
	}
	
	@Override
	public void clear()
	{
		_residences.clear();
		_fastResidencesByType.clear();
	}
}
