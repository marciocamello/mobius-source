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
package instances;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.utils.Location;

/**
 * @author pchayka
 */
public final class ZakenNight extends Reflection
{
	private static final int Zaken = 29022;
	private static final long initdelay = 480 * 1000L; // 480
	final Location[] zakenspawn =
	{
		new Location(55272, 219080, -2952),
		new Location(55272, 219080, -3224),
		new Location(55272, 219080, -3496),
	};
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		ThreadPoolManager.getInstance().schedule(new ZakenSpawn(this), initdelay + (Rnd.get(120, 240) * 1000L));
	}
	
	public final class ZakenSpawn extends RunnableImpl
	{
		Reflection _r;
		
		public ZakenSpawn(Reflection r)
		{
			_r = r;
		}
		
		@Override
		public void runImpl()
		{
			Location rndLoc = zakenspawn[Rnd.get(zakenspawn.length)];
			_r.addSpawnWithoutRespawn(Zaken, rndLoc, 0);
			
			for (int i = 0; i < 4; i++)
			{
				_r.addSpawnWithoutRespawn(20845, rndLoc, 200);
				_r.addSpawnWithoutRespawn(20847, rndLoc, 200);
			}
		}
	}
}