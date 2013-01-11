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
package lineage2.gameserver.model.entity.museum;

public enum RankType
{
	ACQUIRED_XP(0),
	ACQUIRED_ADENA(1),
	PLAY_DURATION(2),
	BATTLE_DURATION(3),
	PRIVATE_STORE_SALES(11),
	QUEST_CLEAR(12),
	NUMBER_OF_DEATH(20),
	NUMBER_OF_MONSTER_KILLINGS(1000),
	MOSTER_KILL_XP(1001),
	NUMBER_OF_DEATHS_BY_MONSTERS(1002),
	PK_COUNT(2004),
	PVP_COUNT(2005),
	PK(2002),
	PVP(2001),
	VLADENIA(3004);
	private final int _id;
	
	RankType(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
}
