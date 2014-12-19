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

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Nache
 */
public final class Baylor extends Reflection
{
	private static final int BAYLOR = 29213;
	private static final int SCENE_MONSTER = 29104;
	private static final int CAMERA = 29120;
	private static final int CRYSTAL_GUARD = 29215;
	public static final Skill INVINCIBILITY_ACTIVATION = SkillTable.getInstance().getInfo(14190, 1);
	
	static final int[][] SCENE_MONSTERS =
	{
		{
			153312,
			142048,
			-12800,
			0
		},
		{
			153333,
			142176,
			-12800,
			61439
		},
		{
			153360,
			141936,
			-12800,
			8192
		},
		{
			153727,
			141875,
			-12800,
			24500
		},
		{
			153472,
			141840,
			-12800,
			12288
		},
		{
			153600,
			141824,
			-12800,
			16384
		},
		{
			153776,
			142224,
			-12800,
			40960
		},
		{
			153808,
			141984,
			-12800,
			28672
		},
		{
			153824,
			142112,
			-12800,
			32768
		},
		{
			153520,
			142336,
			-12800,
			49152
		},
		{
			153424,
			142288,
			-12800,
			57344
		},
		{
			153666,
			142312,
			-12800,
			45055
		}
	};
	
	static final int[][] CRYSTAL_GUARD_LOCATION =
	{
		{
			154404,
			140596,
			-12711,
			44732
		},
		{
			153574,
			140402,
			-12711,
			44732
		},
		{
			152105,
			141230,
			-12711,
			44732
		},
		{
			151877,
			142095,
			-12711,
			44732
		},
		{
			152109,
			142920,
			-12711,
			44732
		},
		{
			152730,
			143555,
			-12711,
			44732
		},
		{
			154439,
			143538,
			-12711,
			44732
		},
		{
			155246,
			142068,
			-12711,
			44732
		}
	};
	
	static final int[] CRYSTAL_DOORS =
	{
		24220009,
		24220011,
		24220012,
		24220014,
		24220015,
		24220016,
		24220017,
		24220019
	};
	
	final Location BaylorSpawn1 = new Location(153256, 142056, -12762, 32767);
	final Location BaylorSpawn2 = new Location(153912, 142088, -12762, 32767);
	final Location _pos = new Location(153569, 142075, -12711, 44732);
	final DeathListener _deathListener = new DeathListener();
	final CurrentHpListener _currentHpListener = new CurrentHpListener();
	final List<NpcInstance> _sceneMonsters = new ArrayList<>(3);
	NpcInstance Baylor1 = null;
	NpcInstance Baylor2 = null;
	NpcInstance Camera = null;
	public int killedBaylors = 0;
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		ThreadPoolManager.getInstance().schedule(new Movie(1), 30000);
	}
	
	private class Movie extends RunnableImpl
	{
		private int _taskId = 0;
		
		public Movie(int taskId)
		{
			_taskId = taskId;
		}
		
		@Override
		public void runImpl()
		{
			switch (_taskId)
			{
				case 1:
					Baylor1 = addSpawnWithoutRespawn(BAYLOR, new Location(153572, 142075, -12798), 0);
					Baylor1.startImmobilized();
					Camera = addSpawnWithoutRespawn(CAMERA, new Location(153273, 141400, -12798), 0);
					showSocialActionMovie(Camera, 700, -45, 160, 500, 15200, 0, 0, 1, 0, 0);
					ThreadPoolManager.getInstance().schedule(new Movie(2), 2000);
					break;
				case 2:
					for (int[] loc : SCENE_MONSTERS)
					{
						NpcInstance sceneMonster = addSpawnWithoutRespawn(SCENE_MONSTER, new Location(loc[0], loc[1], loc[2], loc[3]), 0);
						_sceneMonsters.add(sceneMonster);
						sceneMonster.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
						sceneMonster.startImmobilized();
					}
					ThreadPoolManager.getInstance().schedule(new Movie(3), 200);
					break;
				case 3:
					Baylor1.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
					Baylor1.broadcastPacket(new SocialAction(Baylor1.getObjectId(), 1));
					ThreadPoolManager.getInstance().schedule(new Movie(4), 11000);
					ThreadPoolManager.getInstance().schedule(new Movie(5), 19000);
					break;
				case 4:
					showSocialActionMovie(Baylor1, 500, -45, 170, 5000, 9000, 0, 0, 1, 0, 0);
					break;
				case 5:
					showSocialActionMovie(Baylor1, 300, 0, 120, 2000, 5000, 0, 0, 1, 0, 3);
					ThreadPoolManager.getInstance().schedule(new Movie(6), 4000);
					break;
				case 6:
					showSocialActionMovie(Baylor1, 747, 0, 160, 2000, 3000, 0, 0, 1, 0, 0);
					Baylor1.broadcastPacket(new MagicSkillUse(Baylor1, Baylor1, 5402, 1, 2000, 0));
					Baylor1.broadcastPacket(new SocialAction(Baylor1.getObjectId(), 1));
					for (NpcInstance sceneMonster : _sceneMonsters)
					{
						sceneMonster.doDie(sceneMonster);
					}
					ThreadPoolManager.getInstance().schedule(new Movie(7), 2000);
					break;
				case 7:
					Baylor1.teleToLocation(BaylorSpawn1);
					Baylor2 = addSpawnWithoutRespawn(BAYLOR, BaylorSpawn2, 0);
					for (Player pc : getPlayers())
					{
						pc.leaveMovieMode();
					}
					Camera.decayMe();
					Camera = null;
					_sceneMonsters.clear();
					Baylor1.stopImmobilized();
					Baylor1.addListener(_deathListener);
					Baylor1.addListener(_currentHpListener);
					Baylor2.addListener(_deathListener);
					Baylor2.addListener(_currentHpListener);
					closeDoor(24220008);
					ThreadPoolManager.getInstance().schedule(new Movie(8), 5000);
					break;
				case 8:
					for (int doorId : CRYSTAL_DOORS)
					{
						openDoor(doorId);
					}
					for (int[] loc : CRYSTAL_GUARD_LOCATION)
					{
						NpcInstance crystalGuard = addSpawnWithoutRespawn(CRYSTAL_GUARD, new Location(loc[0], loc[1], loc[2], loc[3]), 0);
						crystalGuard.setRunning();
						crystalGuard.moveToLocation(_pos, 300, false);
						crystalGuard.broadcastPacket(new SocialAction(crystalGuard.getObjectId(), 2));
					}
					break;
			}
		}
	}
	
	public class CurrentHpListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(final Creature actor, final double damage, final Creature attacker, Skill skill)
		{
			if (actor.getId() == BAYLOR)
			{
				double HpPercent = actor.getCurrentHpPercents();
				if ((HpPercent < 30) && (killedBaylors == 0))
				{
					Functions.npcSay(Baylor1, "Demon King Beleth, give me the power! Aaahh!!!");
					actor.doCast(INVINCIBILITY_ACTIVATION, actor, true);
				}
				else if (killedBaylors == 1)
				{
					actor.doCast(null, null, false);
				}
			}
		}
	}
	
	private class DeathListener implements OnDeathListener
	{
		public DeathListener()
		{
		}
		
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (self.isNpc() && (self.getId() == BAYLOR))
			{
				killedBaylors += 1;
				
				if (killedBaylors >= 2)
				{
					for (Player p : getPlayers())
					{
						p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(5));
					}
					startCollapseTimer(5 * 60 * 1000L);
				}
			}
		}
	}
	
	/**
	 * Shows a movie to the players in the lair.
	 * @param target - L2NpcInstance target is the center of this movie
	 * @param dist - int distance from target
	 * @param yaw - angle of movie (north = 90, south = 270, east = 0 , west = 180)
	 * @param pitch - pitch > 0 looks up / pitch < 0 looks down
	 * @param time - fast ++ or slow -- depends on the value
	 * @param duration - How long to watch the movie
	 * @param turn
	 * @param rise
	 * @param widescreen
	 * @param unk
	 * @param socialAction - 1,2,3,4 social actions / other values do nothing
	 */
	public void showSocialActionMovie(NpcInstance target, int dist, int yaw, int pitch, int time, int duration, int turn, int rise, int widescreen, int unk, int socialAction)
	{
		if (target == null)
		{
			return;
		}
		for (Player pc : getPlayers())
		{
			if (pc.getDistance(target) <= 2550)
			{
				pc.enterMovieMode();
				pc.specialCamera(target, dist, yaw, pitch, time, duration, turn, rise, widescreen, unk);
			}
			else
			{
				pc.leaveMovieMode();
			}
		}
		if ((socialAction > 0) && (socialAction < 5))
		{
			target.broadcastPacket(new SocialAction(target.getObjectId(), socialAction));
		}
	}
	
}