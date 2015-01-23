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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.templates.InstantZone;
import lineage2.gameserver.utils.Location;
import npc.model.PathfinderInstance;

public final class KamalokaNightmare extends Reflection
{
	private static final int PATHFINDER = 32485;
	private static final int RANK_1_MIN_POINTS = 500;
	private static final int RANK_2_MIN_POINTS = 2500;
	private static final int RANK_3_MIN_POINTS = 4500;
	private static final int RANK_4_MIN_POINTS = 5500;
	private static final int RANK_5_MIN_POINTS = 7000;
	private static final int RANK_6_MIN_POINTS = 9000;
	private final int _playerId;
	private Future<?> _expireTask;
	private int killedKanabions = 0;
	private int killedDoplers = 0;
	private int killedVoiders = 0;
	int delay_after_spawn = 0;
	boolean is_spawn_possible = true;
	
	public KamalokaNightmare(Player player)
	{
		_playerId = player.getObjectId();
	}
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		InstantZone iz = getInstancedZone();
		
		if (iz != null)
		{
			int time_limit = iz.getTimelimit() * 1000 * 60;
			delay_after_spawn = time_limit / 3;
			startPathfinderTimer(time_limit - delay_after_spawn);
		}
	}
	
	@Override
	protected void onCollapse()
	{
		super.onCollapse();
		stopPathfinderTimer();
	}
	
	public void addKilledKanabion(int type)
	{
		switch (type)
		{
			case 1:
				killedKanabions++;
				break;
			
			case 2:
				killedDoplers++;
				break;
			
			case 3:
				killedVoiders++;
				break;
		}
	}
	
	public int getRank()
	{
		int total = (killedKanabions * 10) + (killedDoplers * 20) + (killedVoiders * 50);
		
		if (total >= RANK_6_MIN_POINTS)
		{
			return 6;
		}
		else if (total >= RANK_5_MIN_POINTS)
		{
			return 5;
		}
		else if (total >= RANK_4_MIN_POINTS)
		{
			return 4;
		}
		else if (total >= RANK_3_MIN_POINTS)
		{
			return 3;
		}
		else if (total >= RANK_2_MIN_POINTS)
		{
			return 2;
		}
		else if (total >= RANK_1_MIN_POINTS)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	public void startPathfinderTimer(long timeInMillis)
	{
		if (_expireTask != null)
		{
			_expireTask.cancel(false);
			_expireTask = null;
		}
		
		_expireTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				try
				{
					is_spawn_possible = false;
					
					for (Spawner s : KamalokaNightmare.this.getSpawns().toArray(new Spawner[KamalokaNightmare.this.getSpawns().size()]))
					{
						s.deleteAll();
					}
					
					KamalokaNightmare.this.getSpawns().clear();
					List<GameObject> delete = new ArrayList<>();
					lock.lock();
					
					try
					{
						for (GameObject o : _objects)
						{
							if (!o.isPlayable())
							{
								delete.add(o);
							}
						}
					}
					finally
					{
						lock.unlock();
					}
					
					for (GameObject o : delete)
					{
						o.deleteMe();
					}
					
					Player p = (Player) GameObjectsStorage.findObject(getPlayerId());
					
					if (p != null)
					{
						p.getPlayer().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTE_S_YOU_WILL_BE_FORCED_OUT_OF_THE_DUNGEON_WHEN_THE_TIME_EXPIRES).addInt(delay_after_spawn / 60000));
						InstantZone iz = KamalokaNightmare.this.getInstancedZone();
						
						if (iz != null)
						{
							String loc = iz.getAddParams().getString("pathfinder_loc", null);
							
							if (loc != null)
							{
								PathfinderInstance npc = new PathfinderInstance(IdFactory.getInstance().getNextId(), NpcHolder.getInstance().getTemplate(PATHFINDER));
								npc.setSpawnedLoc(Location.parseLoc(loc));
								npc.setReflection(KamalokaNightmare.this);
								npc.spawnMe(npc.getSpawnedLoc());
							}
						}
					}
					else
					{
						collapse();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}, timeInMillis);
	}
	
	public void stopPathfinderTimer()
	{
		if (_expireTask != null)
		{
			_expireTask.cancel(false);
			_expireTask = null;
		}
	}
	
	public int getPlayerId()
	{
		return _playerId;
	}
	
	@Override
	public boolean canChampions()
	{
		return false;
	}
	
	public boolean isSpawnPossible()
	{
		return is_spawn_possible;
	}
}