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
package lineage2.gameserver.templates;

import java.util.Collections;
import java.util.List;

import lineage2.gameserver.model.entity.events.objects.BoatPoint;
import lineage2.gameserver.network.serverpackets.components.SceneMovie;
import lineage2.gameserver.utils.Location;

public class AirshipDock
{
	public static class AirshipPlatform
	{
		private final SceneMovie _oustMovie;
		private final Location _oustLoc;
		private final Location _spawnLoc;
		private List<BoatPoint> _arrivalPoints = Collections.emptyList();
		private List<BoatPoint> _departPoints = Collections.emptyList();
		
		public AirshipPlatform(SceneMovie movie, Location oustLoc, Location spawnLoc, List<BoatPoint> arrival, List<BoatPoint> depart)
		{
			_oustMovie = movie;
			_oustLoc = oustLoc;
			_spawnLoc = spawnLoc;
			_arrivalPoints = arrival;
			_departPoints = depart;
		}
		
		public SceneMovie getOustMovie()
		{
			return _oustMovie;
		}
		
		public Location getOustLoc()
		{
			return _oustLoc;
		}
		
		public Location getSpawnLoc()
		{
			return _spawnLoc;
		}
		
		public List<BoatPoint> getArrivalPoints()
		{
			return _arrivalPoints;
		}
		
		public List<BoatPoint> getDepartPoints()
		{
			return _departPoints;
		}
	}
	
	private final int _id;
	private List<BoatPoint> _teleportList = Collections.emptyList();
	private List<AirshipPlatform> _platformList = Collections.emptyList();
	
	public AirshipDock(int id, List<BoatPoint> teleport, List<AirshipPlatform> platformList)
	{
		_id = id;
		_teleportList = teleport;
		_platformList = platformList;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public List<BoatPoint> getTeleportList()
	{
		return _teleportList;
	}
	
	public AirshipPlatform getPlatform(int id)
	{
		return _platformList.get(id);
	}
}
