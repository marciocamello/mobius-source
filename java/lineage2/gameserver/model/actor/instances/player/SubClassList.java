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
package lineage2.gameserver.model.actor.instances.player;

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import lineage2.gameserver.dao.CharacterSubclassDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SubClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubClassList
{
	private static final Logger _log = LoggerFactory.getLogger(SubClassList.class);
	public static final int MAX_SUB_COUNT = 4;
	private final TreeMap<Integer, SubClass> _listByIndex = new TreeMap<>();
	private final TreeMap<Integer, SubClass> _listByClassId = new TreeMap<>();
	private final Player _owner;
	private SubClass _baseSubClass = null;
	private SubClass _activeSubClass = null;
	private int _lastFreeIndex = 1;
	
	public SubClassList(Player owner)
	{
		_owner = owner;
	}
	
	public void restore()
	{
		_listByIndex.clear();
		_listByClassId.clear();
		List<SubClass> subclasses = CharacterSubclassDAO.getInstance().restore(_owner);
		int lastFreeIndex = 2;
		for (SubClass sub : subclasses)
		{
			if (sub == null)
			{
				continue;
			}
			if (size() >= MAX_SUB_COUNT)
			{
				_log.warn("SubClassList:restore: Limit is subclass! Player: " + _owner.getName() + "(" + _owner.getObjectId() + ")");
				break;
			}
			if (sub.isActive())
			{
				_activeSubClass = sub;
			}
			if (sub.isBase())
			{
				_baseSubClass = sub;
				sub.setIndex(1);
			}
			else
			{
				sub.setIndex(lastFreeIndex);
				lastFreeIndex++;
			}
			if (_listByIndex.containsKey(sub.getIndex()))
			{
				_log.warn("SubClassList:restore: Duplicate index in player subclasses! Player: " + _owner.getName() + "(" + _owner.getObjectId() + ")");
			}
			_listByIndex.put(sub.getIndex(), sub);
			if (_listByClassId.containsKey(sub.getClassId()))
			{
				_log.warn("SubClassList:restore: Duplicate class_id in player subclasses! Player: " + _owner.getName() + "(" + _owner.getObjectId() + ")");
			}
			_listByClassId.put(sub.getClassId(), sub);
		}
		_lastFreeIndex = lastFreeIndex;
		if (_listByIndex.size() != _listByClassId.size())
		{
			_log.warn("SubClassList:restore: The size of the lists do not match! Player: " + _owner.getName() + "(" + _owner.getObjectId() + ")");
		}
	}
	
	public Collection<SubClass> values()
	{
		return _listByIndex.values();
	}
	
	public SubClass getByClassId(int classId)
	{
		return _listByClassId.get(classId);
	}
	
	public SubClass getByIndex(int index)
	{
		return _listByIndex.get(index);
	}
	
	public void removeByClassId(int classId)
	{
		if (!_listByClassId.containsKey(classId))
		{
			return;
		}
		int index = _listByClassId.get(classId).getIndex();
		_listByIndex.remove(index);
		_listByClassId.remove(classId);
	}
	
	public SubClass getActiveSubClass()
	{
		return _activeSubClass;
	}
	
	public SubClass getBaseSubClass()
	{
		return _baseSubClass;
	}
	
	public boolean isBaseClassActive()
	{
		return _activeSubClass == _baseSubClass;
	}
	
	public boolean haveSubClasses()
	{
		return size() > 1;
	}
	
	public boolean changeSubClassId(int oldClassId, int newClassId)
	{
		if (!_listByClassId.containsKey(oldClassId))
		{
			return false;
		}
		if (_listByClassId.containsKey(newClassId))
		{
			return false;
		}
		SubClass sub = _listByClassId.get(oldClassId);
		sub.setClassId(newClassId);
		_listByClassId.remove(oldClassId);
		_listByClassId.put(sub.getClassId(), sub);
		return true;
	}
	
	public boolean add(SubClass sub)
	{
		if (sub == null)
		{
			return false;
		}
		if (size() >= MAX_SUB_COUNT)
		{
			return false;
		}
		if (_listByClassId.containsKey(sub.getClassId()))
		{
			return false;
		}
		sub.setIndex(_lastFreeIndex);
		_lastFreeIndex++;
		_listByIndex.put(sub.getIndex(), sub);
		_listByClassId.put(sub.getClassId(), sub);
		return true;
	}
	
	public SubClass changeActiveSubClass(int classId)
	{
		if (!_listByClassId.containsKey(classId))
		{
			return null;
		}
		if (_activeSubClass == null)
		{
			return null;
		}
		_activeSubClass.setActive(false);
		SubClass sub = _listByClassId.get(classId);
		sub.setActive(true);
		_activeSubClass = sub;
		return sub;
	}
	
	public boolean containsClassId(int classId)
	{
		return _listByClassId.containsKey(classId);
	}
	
	public int size()
	{
		return _listByIndex.size();
	}
	
	@Override
	public String toString()
	{
		return "SubClassList[owner=" + _owner.getName() + "]";
	}
}
