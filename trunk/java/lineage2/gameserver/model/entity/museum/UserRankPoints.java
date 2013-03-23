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
 * @author ALF
 */
public class UserRankPoints
{
	private final int _id;
	private long _monthlyPoints;
	private long _totalPoints;
	
	public UserRankPoints(int id)
	{
		_id = id;
	}
	
	public UserRankPoints(int id, long totalPoints)
	{
		_id = id;
		_totalPoints = totalPoints;
	}
	
	public long getMonthlyPoints()
	{
		return _monthlyPoints;
	}
	
	public void setMonthlyPoints(long _monthlyPoints)
	{
		this._monthlyPoints = _monthlyPoints;
	}
	
	public long getTotalPoints()
	{
		return _totalPoints;
	}
	
	public void setTotalPoints(long _totalPoints)
	{
		this._totalPoints = _totalPoints;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public void addMonthlyPoints(long count)
	{
		_monthlyPoints += count;
	}
	
	public void addTotalPoints(long count)
	{
		_totalPoints += count;
	}
}
