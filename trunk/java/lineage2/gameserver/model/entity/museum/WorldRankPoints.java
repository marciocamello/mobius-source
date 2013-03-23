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
 * @date 21.08.2012
 */
public class WorldRankPoints
{
	private int objId;
	private String name;
	private long points;
	private int clanId;
	private int crestId;
	
	public WorldRankPoints(int _objId, String _name, long _points, int _clanId, int _crestId)
	{
		objId = _objId;
		name = _name;
		points = _points;
		clanId = _clanId;
		crestId = _crestId;
	}
	
	public WorldRankPoints(int _objId, String _name, long _points)
	{
		objId = _objId;
		name = _name;
		points = _points;
		clanId = 0;
		crestId = 0;
	}
	
	public int getObjId()
	{
		return objId;
	}
	
	public void setObjId(int objId)
	{
		this.objId = objId;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public long getPoints()
	{
		return points;
	}
	
	public void setPoints(long points)
	{
		this.points = points;
	}
	
	public int getCrestId()
	{
		return crestId;
	}
	
	public void setCrestId(int crestId)
	{
		this.crestId = crestId;
	}
	
	public int getClanId()
	{
		return clanId;
	}
	
	public void setClanId(int clanId)
	{
		this.clanId = clanId;
	}
}
