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
package lineage2.gameserver.tables;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.Collection;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SubClass;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.Race;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SubClassTable
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SubClassTable.class);
	/**
	 * Field _instance.
	 */
	private static SubClassTable _instance;
	/**
	 * Field _subClasses.
	 */
	private TIntObjectHashMap<TIntArrayList> _subClasses;
	
	/**
	 * Constructor for SubClassTable.
	 */
	public SubClassTable()
	{
		init();
	}
	
	/**
	 * Method getInstance.
	 * @return SubClassTable
	 */
	public static SubClassTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new SubClassTable();
		}
		return _instance;
	}
	
	/**
	 * Method init.
	 */
	private void init()
	{
		_subClasses = new TIntObjectHashMap<>();
		for (ClassId baseClassId : ClassId.VALUES)
		{
			if (baseClassId.isOfLevel(ClassLevel.First))
			{
				continue;
			}
			TIntArrayList availSubs = new TIntArrayList();
			for (ClassId subClassId : ClassId.VALUES)
			{
				if (!subClassId.isOfLevel(ClassLevel.Second))
				{
					continue;
				}
				if (!areClassesComportable(baseClassId, subClassId))
				{
					continue;
				}
				availSubs.add(subClassId.getId());
			}
			availSubs.sort();
			_subClasses.put(baseClassId.getId(), availSubs);
		}
		_log.info("SubClassTable: Loaded " + _subClasses.size() + " sub-classes variations.");
	}
	
	/**
	 * Method getAvailableSubClasses.
	 * @param player Player
	 * @param classId int
	 * @return int[]
	 */
	public int[] getAvailableSubClasses(Player player, int classId)
	{
		TIntArrayList subClassesList = _subClasses.get(classId);
		if ((subClassesList == null) || subClassesList.isEmpty())
		{
			return new int[0];
		}
		loop:
		for (int clsId : subClassesList.toArray())
		{
			ClassId subClassId = ClassId.VALUES[clsId];
			Collection<SubClass> playerSubClasses = player.getSubClassList().values();
			for (SubClass playerSubClass : playerSubClasses)
			{
				ClassId playerSubClassId = ClassId.VALUES[playerSubClass.getClassId()];
				if (!areClassesComportable(playerSubClassId, subClassId))
				{
					subClassesList.remove(clsId);
					continue loop;
				}
			}
			if (subClassId.isOfRace(Race.kamael))
			{
				if (((player.getSex() == 1) && (subClassId == ClassId.M_SOUL_BREAKER)) || ((player.getSex() == 0) && (subClassId == ClassId.F_SOUL_BREAKER)))
				{
					subClassesList.remove(clsId);
					continue;
				}
				if ((subClassId == ClassId.INSPECTOR) && (player.getSubClassList().size() < 3))
				{
					subClassesList.remove(clsId);
					continue;
				}
			}
		}
		return subClassesList.toArray();
	}
	
	/**
	 * Method areClassesComportable.
	 * @param baseClassId ClassId
	 * @param subClassId ClassId
	 * @return boolean
	 */
	public static boolean areClassesComportable(ClassId baseClassId, ClassId subClassId)
	{
		if (baseClassId == subClassId)
		{
			return false;
		}
		if (baseClassId.getType2() == subClassId.getType2())
		{
			return false;
		}
		if (baseClassId.isOfRace(Race.kamael) != subClassId.isOfRace(Race.kamael))
		{
			return false;
		}
		if ((baseClassId.isOfRace(Race.elf) && subClassId.isOfRace(Race.darkelf)) || (baseClassId.isOfRace(Race.darkelf) && subClassId.isOfRace(Race.elf)))
		{
			return false;
		}
		if ((subClassId == ClassId.OVERLORD) || (subClassId == ClassId.WARSMITH))
		{
			return false;
		}
		return true;
	}
}
