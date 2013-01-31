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
public class RankPoints
{
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _monthlyPoints.
	 */
	private long _monthlyPoints;
	/**
	 * Field _totalPoints.
	 */
	private long _totalPoints;
	
	/**
	 * Constructor for RankPoints.
	 * @param id int
	 */
	public RankPoints(int id)
	{
		_id = id;
	}
	
	/**
	 * Constructor for RankPoints.
	 * @param id int
	 * @param totalPoints long
	 */
	public RankPoints(int id, long totalPoints)
	{
		_id = id;
		_totalPoints = totalPoints;
	}
	
	/**
	 * Method getMonthlyPoints.
	 * @return long
	 */
	public long getMonthlyPoints()
	{
		return _monthlyPoints;
	}
	
	/**
	 * Method setMonthlyPoints.
	 * @param _monthlyPoints long
	 */
	public void setMonthlyPoints(long _monthlyPoints)
	{
		this._monthlyPoints = _monthlyPoints;
	}
	
	/**
	 * Method getTotalPoints.
	 * @return long
	 */
	public long getTotalPoints()
	{
		return _totalPoints;
	}
	
	/**
	 * Method setTotalPoints.
	 * @param _totalPoints long
	 */
	public void setTotalPoints(long _totalPoints)
	{
		this._totalPoints = _totalPoints;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method addMonthlyPoints.
	 * @param count long
	 */
	public void addMonthlyPoints(long count)
	{
		_monthlyPoints += count;
	}
	
	/**
	 * Method addTotalPoints.
	 * @param count long
	 */
	public void addTotalPoints(long count)
	{
		_totalPoints += count;
	}
}
