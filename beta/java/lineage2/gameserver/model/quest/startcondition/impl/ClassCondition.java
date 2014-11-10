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
package lineage2.gameserver.model.quest.startcondition.impl;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.quest.startcondition.ICheckStartCondition;
import lineage2.gameserver.utils.Util;

/**
 * @author blacksmoke
 */
public class ClassCondition implements ICheckStartCondition
{
	private final int[] _classId;
	
	/**
	 * Constructor for ClassCondition.
	 * @param classId int[]
	 */
	public ClassCondition(int... classId)
	{
		_classId = classId;
	}
	
	/**
	 * Method checkCondition.
	 * @param player Player
	 * @return boolean
	 * @see lineage2.gameserver.model.quest.startcondition.ICheckStartCondition#checkCondition(Player)
	 */
	@Override
	public boolean checkCondition(Player player)
	{
		return Util.contains(_classId, player.getClassId().getId());
	}
}
