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
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowUsmVideo;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;

/**
 * @author Iqman + GW + Nache
 */
public class OctavisInstance extends Reflection
{
	public static final Location ENTRANCE = new Location(210651, 119052, -9996);
	public static final Location LAIR_ENTRANCE = new Location(208404, 120572, -10014);
	public static final Location OCTAVIS_SPAWN = new Location(207069, 120580, -10008);
	public static final Location LAIR_CENTER = new Location(207190, 120574, -10009);
	
	public static final int INSTANCE_ID_LIGHT = 180;
	private static final int INSTANCE_ID_HARD = 181;
	
	private static final int VOLCANO_ZONE = 19161;
	private static final int OCTAVIS_POWER = 18984;
	
	private static final int OCTAVIS_LIGHT_FIRST = 29191;
	private static final int OCTAVIS_LIGHT_BEAST = 29192;
	private static final int OCTAVIS_LIGHT_SECOND = 29193;
	private static final int OCTAVIS_LIGHT_THIRD = 29194;
	
	private static final int OCTAVIS_HARD_FIRST = 29209;
	private static final int OCTAVIS_HARD_BEAST = 29210;
	private static final int OCTAVIS_HARD_SECOND = 29211;
	private static final int OCTAVIS_HARD_THIRD = 29212;
	
	private static final int OCTAVIS_GLADIATOR = 22928;
	private static final int ARENA_BEAST = 22929;
	public static final int OCTAVIS_SCIENTIST = 22930;
	
	public static final int[] BEAST_DOORS =
	{
		26210101,
		26210102,
		26210103,
		26210104,
		26210105,
		26210106
	};
	
	public static final int[][] OCTAVIS_SCIENTIST_SPAWN =
	{
		{
			207820,
			120312,
			-10008,
			28144
		},
		{
			207450,
			119936,
			-10008,
			19504
		},
		{
			207817,
			120832,
			-10008,
			36776
		},
		{
			206542,
			120306,
			-10008,
			4408
		},
		{
			206923,
			119936,
			-10008,
			12008
		},
		{
			207458,
			121218,
			-10008,
			44440
		},
		{
			206923,
			121216,
			-10008,
			53504
		},
		{
			206620,
			120568,
			-10008,
			800
		},
		{
			207194,
			121082,
			-10008,
			49000
		},
		{
			207197,
			120029,
			-10008,
			17080
		},
		{
			207776,
			120577,
			-10008,
			33016
		},
		{
			206541,
			120848,
			-10008,
			60320
		}
	};
	
	public static final int[][] OCTAVIS_GLADIATOR_SPAWN =
	{
		{
			206519,
			118937,
			-9976,
			12416
		},
		{
			207865,
			118937,
			-9976,
			19232
		},
		{
			208829,
			119896,
			-9976,
			28264
		},
		{
			208825,
			121260,
			-9976,
			38080
		},
		{
			207875,
			122209,
			-9976,
			44144
		},
		{
			206507,
			122208,
			-9976,
			54680
		}
	};
	
	public static final int[][] ARENA_BEAST_SPAWN =
	{
		{
			206692,
			119375,
			-10008,
			0
		},
		{
			208418,
			120065,
			-10008,
			0
		},
		{
			207700,
			121810,
			-10008,
			0
		}
	};
	
	public static final int[][] OUTROOM_LOCATIONS =
	{
		{
			206849,
			119744,
			-10014
		},
		{
			207524,
			119765,
			-10014
		},
		{
			208002,
			120238,
			-10016
		},
		{
			207995,
			120911,
			-10016
		},
		{
			207524,
			121377,
			-10014
		},
		{
			206861,
			121375,
			-10014
		}
	};
	
	private final ZoneListener _epicZoneListener = new ZoneListener();
	public boolean isHardInstance = false;
	public final CurrentHpListener _currentHpListenerFistsStage = new CurrentHpListener();
	public final CurrentHpListener _currentHpListenerOctavisRide = new CurrentHpListener();
	public NpcInstance octavis = null;
	public NpcInstance octavisBeast = null;
	public List<NpcInstance> volcanos = new ArrayList<>();
	public NpcInstance octavisPower = null;
	public int arenaBeastSpawnNumber = 0;
	public int status = 0;
	
	@Override
	protected void onCreate()
	{
		super.onCreate();
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				for (DoorInstance door : getDoors())
				{
					door.openMe();
				}
			}
		}, 10000L);
		isHardInstance = getInstancedZoneId() == INSTANCE_ID_HARD;
		getZone("[Octavis_epic]").addListener(_epicZoneListener);
	}
	
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (cha.isPlayer() && (status == 0))
			{
				ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						for (Player player : getPlayers())
						{
							player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_OCTABIS_OPENING);
						}
					}
				}, 15000L);
				ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						for (DoorInstance door : getDoors())
						{
							door.closeMe();
						}
						int octavisId = isHardInstance ? OCTAVIS_HARD_FIRST : OCTAVIS_LIGHT_FIRST;
						int beastId = isHardInstance ? OCTAVIS_LIGHT_BEAST : OCTAVIS_HARD_BEAST;
						octavis = addSpawnWithoutRespawn(octavisId, OCTAVIS_SPAWN, 0);
						octavis.addListener(_currentHpListenerFistsStage);
						octavisBeast = addSpawnWithoutRespawn(beastId, OCTAVIS_SPAWN, 0);
						octavisBeast.addListener(_currentHpListenerOctavisRide);
						
						for (byte i = 0; i < 4; i = (byte) (i + 1))
						{
							volcanos.add(addSpawnWithoutRespawn(VOLCANO_ZONE, OCTAVIS_SPAWN, 0));
						}
						octavisPower = addSpawnWithoutRespawn(OCTAVIS_POWER, OCTAVIS_SPAWN, 0);
					}
				}, 42000L);
				status = 1;
			}
		}
		
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
	
	public final class CurrentHpListener implements OnCurrentHpDamageListener
	{
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			if (((actor.getId() == OCTAVIS_LIGHT_FIRST) || (actor.getId() == OCTAVIS_HARD_FIRST)) && (status == 1))
			{
				if (actor.isDead())
				{
					return;
				}
				if ((actor.getCurrentHp() / actor.getMaxHp()) <= 0.01D)
				{
					nextSpawn();
					actor.deleteMe();
					actor.removeListener(_currentHpListenerFistsStage);
					actor.removeListener(_currentHpListenerOctavisRide);
				}
			}
			if ((actor.getId() == OCTAVIS_LIGHT_BEAST) || (actor.getId() == OCTAVIS_HARD_BEAST))
			{
				if (actor.isDead())
				{
					return;
				}
				if ((actor.getCurrentHp() / actor.getMaxHp()) < 0.5D)
				{
					actor.setIsInvul(true);
					actor.setLockedTarget(false);
				}
				else
				{
					actor.setIsInvul(false);
					actor.setLockedTarget(true);
				}
			}
		}
	}
	
	public void nextSpawn()
	{
		switch (status)
		{
			case 1:
				if (status == 1)
				{
					status = 2;
					for (Player player : getPlayers())
					{
						player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_OCTABIS_PHASECH_A);
					}
					if (octavisBeast != null)
					{
						octavisBeast.deleteMe();
					}
					ThreadPoolManager.getInstance().schedule(new RunnableImpl()
					{
						@SuppressWarnings("null")
						@Override
						public void runImpl()
						{
							int npcId = isHardInstance ? OCTAVIS_HARD_SECOND : OCTAVIS_LIGHT_SECOND;
							octavis = addSpawnWithoutRespawn(npcId, LAIR_CENTER, 0);
							
							for (int doorId : BEAST_DOORS)
							{
								DoorInstance door = getDoor(doorId);
								door.openMe();
							}
							
							for (int[] loc : OCTAVIS_GLADIATOR_SPAWN)
							{
								NpcInstance gladiator = addSpawnWithoutRespawn(OCTAVIS_GLADIATOR, new Location(loc[0], loc[1], loc[2], loc[3]), 0);
								gladiator.setRandomWalk(false);
								
								int[] selectedLoc = null;
								double selectedDistance = 0.0D;
								Location currentLoc = gladiator.getLoc();
								for (int[] outloc : OUTROOM_LOCATIONS)
								{
									if ((selectedLoc == null) || (selectedDistance > PositionUtils.calculateDistance(currentLoc.getX(), currentLoc.getY(), 0, outloc[0], outloc[1], 0, false)))
									{
										selectedLoc = outloc;
										selectedDistance = PositionUtils.calculateDistance(currentLoc.getX(), currentLoc.getY(), 0, selectedLoc[0], selectedLoc[1], 0, false);
									}
								}
								
								gladiator.setRunning();
								DefaultAI ai = (DefaultAI) gladiator.getAI();
								ai.addTaskMove(Location.findPointToStay(octavis.getLoc(), 50, gladiator.getGeoIndex()), true);
							}
							
							ThreadPoolManager.getInstance().schedule(new RunnableImpl()
							{
								@Override
								public void runImpl()
								{
									if ((octavis != null) && ((octavis.getId() == OCTAVIS_LIGHT_SECOND) || (octavis.getId() == OCTAVIS_HARD_SECOND)))
									{
										int offset = arenaBeastSpawnNumber % 3;
										for (int i = offset; i < (offset + 7); i++)
										{
											int[] loc = ARENA_BEAST_SPAWN[offset];
											NpcInstance beast = addSpawnWithoutRespawn(ARENA_BEAST, new Location(loc[offset], loc[1], loc[2], loc[3]), 0);
											beast.setRunning();
											beast.setRandomWalk(false);
											DefaultAI ai = (DefaultAI) beast.getAI();
											ai.addTaskMove(Location.findPointToStay(octavis.getLoc(), 50, beast.getGeoIndex()), true);
											for (Player p : beast.getReflection().getPlayers())
											{
												beast.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 1000);
											}
										}
										arenaBeastSpawnNumber += 1;
										
										ThreadPoolManager.getInstance().schedule(this, 180000L);
									}
								}
							}, 1000L);
						}
					}, 10000L);
				}
				break;
			
			case 2:
				if (status == 2)
				{
					status = 3;
					
					for (NpcInstance npc : getNpcs())
					{
						if ((npc.getId() == OCTAVIS_GLADIATOR) || (npc.getId() == ARENA_BEAST))
						{
							npc.deleteMe();
						}
					}
					
					for (Player player : getPlayers())
					{
						player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_OCTABIS_PHASECH_B);
					}
					
					ThreadPoolManager.getInstance().schedule(new RunnableImpl()
					{
						@Override
						public void runImpl()
						{
							int npcId = isHardInstance ? OCTAVIS_HARD_THIRD : OCTAVIS_LIGHT_THIRD;
							octavis = addSpawnWithoutRespawn(npcId, LAIR_CENTER, 0);
							
							for (int[] loc : OCTAVIS_SCIENTIST_SPAWN)
							{
								NpcInstance scientist = addSpawnWithoutRespawn(OCTAVIS_SCIENTIST, new Location(loc[0], loc[1], loc[2], loc[3]), 0);
								for (Player p : scientist.getReflection().getPlayers())
								{
									scientist.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 1000);
								}
							}
							arenaBeastSpawnNumber += 1;
						}
					}, 15000L);
				}
				break;
			
			case 3:
				if (status == 3)
				{
					status = 4;
					for (NpcInstance npc : getNpcs())
					{
						if ((npc.getId() == OCTAVIS_SCIENTIST) && (npc.getId() == VOLCANO_ZONE) && (npc.getId() == OCTAVIS_POWER))
						{
							npc.deleteMe();
						}
					}
					
					for (Player player : getPlayers())
					{
						player.showQuestMovie(ExStartScenePlayer.SCENE_BOSS_OCTABIS_ENDING);
					}
					
					ThreadPoolManager.getInstance().schedule(new RunnableImpl()
					{
						@Override
						public void runImpl()
						{
							for (Player player : getPlayers())
							{
								player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q005));
							}
							clearReflection(5, true);
						}
					}, 50000L);
				}
				break;
		}
	}
}
