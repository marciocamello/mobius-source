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
package lineage2.gameserver.model.entity.museum;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum RankType
{
	/**
	 * Field ACQUIRED_XP.
	 */
	ACQUIRED_XP(0),
	/**
	 * Field ACQUIRED_ADENA.
	 */
	ACQUIRED_ADENA(1),
	/**
	 * Field PLAY_DURATION.
	 */
	PLAY_DURATION(2),
	/**
	 * Field BATTLE_DURATION.
	 */
	BATTLE_DURATION(3),
	/**
	 * Field PRIVATE_STORE_SALES.
	 */
	PRIVATE_STORE_SALES(11),
	/**
	 * Field QUEST_CLEAR.
	 */
	QUEST_CLEAR(12),
	/**
	 * Field NUMBER_OF_DEATH.
	 */
	NUMBER_OF_DEATH(20),
	/**
	 * Field NUMBER_OF_MONSTER_KILLINGS.
	 */
	NUMBER_OF_MONSTER_KILLINGS(1000),
	/**
	 * Field MOSTER_KILL_XP.
	 */
	MOSTER_KILL_XP(1001),
	/**
	 * Field NUMBER_OF_DEATHS_BY_MONSTERS.
	 */
	NUMBER_OF_DEATHS_BY_MONSTERS(1002),
	/**
	 * Field PK_COUNT.
	 */
	PK_COUNT(2004),
	/**
	 * Field PVP_COUNT.
	 */
	PVP_COUNT(2005),
	/**
	 * Field PK.
	 */
	PK(2002),
	/**
	 * Field PVP.
	 */
	PVP(2001),
	/**
	 * Field VLADENIA.
	 */
	VLADENIA(3004);
	/**
	 * Field _id.
	 */
	private final int _id;
	
	/**
	 * Constructor for RankType.
	 * @param id int
	 */
	RankType(int id)
	{
		_id = id;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
}
