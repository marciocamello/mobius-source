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
package lineage2.gameserver.utils;

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SiegeUtils
{
	/**
	 * Method addSiegeSkills.
	 * @param character Player
	 */
	public static void addSiegeSkills(Player character)
	{
		character.addSkill(SkillTable.getInstance().getInfo(246, 1), false);
		character.addSkill(SkillTable.getInstance().getInfo(247, 1), false);
		if (character.isNoble())
		{
			character.addSkill(SkillTable.getInstance().getInfo(326, 1), false);
		}
		if ((character.getClan() != null) && (character.getClan().getCastle() > 0))
		{
			character.addSkill(SkillTable.getInstance().getInfo(844, 1), false);
			character.addSkill(SkillTable.getInstance().getInfo(845, 1), false);
		}
	}
	
	/**
	 * Method removeSiegeSkills.
	 * @param character Player
	 */
	public static void removeSiegeSkills(Player character)
	{
		character.removeSkill(SkillTable.getInstance().getInfo(246, 1), false);
		character.removeSkill(SkillTable.getInstance().getInfo(247, 1), false);
		character.removeSkill(SkillTable.getInstance().getInfo(326, 1), false);
		if ((character.getClan() != null) && (character.getClan().getCastle() > 0))
		{
			character.removeSkill(SkillTable.getInstance().getInfo(844, 1), false);
			character.removeSkill(SkillTable.getInstance().getInfo(845, 1), false);
		}
	}
	
	/**
	 * Method getCanRide.
	 * @return boolean
	 */
	public static boolean getCanRide()
	{
		for (Residence residence : ResidenceHolder.getInstance().getResidences())
		{
			if ((residence != null) && residence.getSiegeEvent().isInProgress())
			{
				return false;
			}
		}
		return true;
	}
}
