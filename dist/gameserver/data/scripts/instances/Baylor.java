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
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.utils.Location;

/**
 * @author Awakeninger
 */
public class Baylor extends Reflection
{
	private static final int Baylor = 29213;
	private static final int Golem1 = 23123;
	private static final int Golem2 = 23123;
	private static final int Golem3 = 23123;
	private static final int Golem4 = 23123;
	// private static final int Door =
	Location Golem1Loc = new Location(152648, 142968, -12762);
	Location Golem2Loc = new Location(152664, 141160, -12762);
	Location Golem3Loc = new Location(154488, 141160, -12762);
	Location Golem4Loc = new Location(154488, 143000, -12762);
	Location vullockspawn1 = new Location(153256, 142056, -12762, 0);
	Location vullockspawn2 = new Location(153912, 142088, -12762, 32767);
	private static final long BeforeDelay = 60 * 1000L;
	private static final long BeforeDelayVDO = 47 * 1000L;
	
	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
		ThreadPoolManager.getInstance().schedule(new FirstStage(), BeforeDelayVDO);
		ThreadPoolManager.getInstance().schedule(new BaylorSpawn(this), BeforeDelay);
	}
	
	// ExStartScenePlayer.SCENE_BALROG_OPENING
	// player.showQuestMovie(SceneMovie.si_barlog_opening);
	private class FirstStage extends RunnableImpl
	{
		public FirstStage()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void runImpl()
		{
			
			// closeDoor(Door);
			
			for (Player player : getPlayers())
			{
				player.showQuestMovie(ExStartScenePlayer.SCENE_SC_NOBLE_OPENING);
			}
			_log.info("43 Video");
			
		}
	}
	
	public class BaylorSpawn extends RunnableImpl
	{
		Reflection _r;
		
		public BaylorSpawn(Reflection r)
		{
			_r = r;
		}
		
		@Override
		public void runImpl()
		{
			Location Loc1 = Golem1Loc;
			Location Loc2 = Golem2Loc;
			Location Loc3 = Golem3Loc;
			Location Loc4 = Golem4Loc;
			Location Loc = vullockspawn1;
			Location Loc5 = vullockspawn2;
			_r.addSpawnWithoutRespawn(Baylor, Loc, 0);
			_r.addSpawnWithoutRespawn(Baylor, Loc5, 0);
			_r.addSpawnWithoutRespawn(Golem1, Loc1, 0);
			_r.addSpawnWithoutRespawn(Golem2, Loc2, 0);
			_r.addSpawnWithoutRespawn(Golem3, Loc3, 0);
			_r.addSpawnWithoutRespawn(Golem4, Loc4, 0);
		}
	}
}