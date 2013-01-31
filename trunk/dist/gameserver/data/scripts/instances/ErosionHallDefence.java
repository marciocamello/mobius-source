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

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.lang.ArrayUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;
import quests._697_DefendtheHallofErosion;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ErosionHallDefence extends Reflection
{
	/**
	 * Field AliveTumor. (value is 18708)
	 */
	private static final int AliveTumor = 18708;
	/**
	 * Field DeadTumor. (value is 32535)
	 */
	private static final int DeadTumor = 32535;
	/**
	 * Field UnstableSeed. (value is 32541)
	 */
	private static final int UnstableSeed = 32541;
	/**
	 * Field RegenerationCoffin. (value is 18709)
	 */
	private static final int RegenerationCoffin = 18709;
	/**
	 * Field SoulWagon. (value is 25636)
	 */
	private static final int SoulWagon = 25636;
	/**
	 * Field zoneEventTriggers.
	 */
	private final int[] zoneEventTriggers = ArrayUtils.createAscendingArray(14240001, 14240012);
	/**
	 * Field startZoneListener.
	 */
	private final ZoneListener startZoneListener = new ZoneListener();
	/**
	 * Field conquestBegun.
	 */
	boolean conquestBegun = false;
	/**
	 * Field deathListener.
	 */
	private final DeathListener deathListener = new DeathListener();
	/**
	 * Field failureTask. Field aliveTumorSpawnTask. Field coffinSpawnTask. Field agressionTask. Field timerTask.
	 */
	private ScheduledFuture<?> timerTask, agressionTask, coffinSpawnTask, aliveTumorSpawnTask, failureTask;
	/**
	 * Field startTime.
	 */
	long startTime;
	/**
	 * Field tumorRespawnTime.
	 */
	long tumorRespawnTime;
	/**
	 * Field conquestEnded.
	 */
	boolean conquestEnded = false;
	/**
	 * Field tumorKillCount.
	 */
	private int tumorKillCount;
	/**
	 * Field soulwagonSpawned.
	 */
	private boolean soulwagonSpawned = false;
	
	/**
	 * Method onCreate.
	 */
	@Override
	protected void onCreate()
	{
		super.onCreate();
		getZone("[soi_hoe_attack_pc_vicera_7]").addListener(startZoneListener);
		tumorRespawnTime = 3 * 60 * 1000L;
		tumorKillCount = 0;
	}
	
	/**
	 * Method conquestBegins.
	 */
	void conquestBegins()
	{
		for (Player p : getPlayers())
		{
			p.sendPacket(new ExShowScreenMessage(NpcString.YOU_CAN_HEAR_THE_UNDEAD_OF_EKIMUS_RUSHING_TOWARD_YOU, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HALL_OF_EROSION.getId(), "#" + NpcString.DEFEND.getId()));
		}
		spawnByGroup("soi_hoe_defence_lifeseed");
		spawnByGroup("soi_hoe_defence_tumor");
		spawnByGroup("soi_hoe_defence_wards");
		invokeDeathListener();
		spawnByGroup("soi_hoe_defence_mob_1");
		spawnByGroup("soi_hoe_defence_mob_2");
		spawnByGroup("soi_hoe_defence_mob_3");
		spawnByGroup("soi_hoe_defence_mob_4");
		spawnByGroup("soi_hoe_defence_mob_5");
		spawnByGroup("soi_hoe_defence_mob_6");
		spawnByGroup("soi_hoe_defence_mob_7");
		spawnByGroup("soi_hoe_defence_mob_8");
		agressionTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if (!conquestEnded)
				{
					notifyAttackSeed();
				}
			}
		}, 15000L, 25000L);
		coffinSpawnTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if (!conquestEnded)
				{
					for (NpcInstance npc : getAllByNpcId(DeadTumor, true))
					{
						spawnCoffin(npc);
					}
				}
			}
		}, 1000L, 60000L);
		aliveTumorSpawnTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if (!conquestEnded)
				{
					despawnByGroup("soi_hoe_defence_tumor");
					spawnByGroup("soi_hoe_defence_alivetumor");
					handleTumorHp(0.5);
					for (Player p : getPlayers())
					{
						p.sendPacket(new ExShowScreenMessage(NpcString.THE_TUMOR_INSIDE_S1_HAS_COMPLETELY_REVIVED_, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HALL_OF_EROSION.getId()));
					}
					invokeDeathListener();
				}
			}
		}, tumorRespawnTime);
		startTime = System.currentTimeMillis();
		timerTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new TimerTask(), 298 * 1000L, 5 * 60 * 1000L);
	}
	
	/**
	 * @author Mobius
	 */
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (!conquestBegun)
			{
				conquestBegun = true;
				conquestBegins();
			}
		}
		
		/**
		 * Method onZoneLeave.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
		 */
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class DeathListener implements OnDeathListener
	{
		/**
		 * Constructor for DeathListener.
		 */
		public DeathListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onDeath.
		 * @param self Creature
		 * @param killer Creature
		 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
		 */
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (!self.isNpc())
			{
				return;
			}
			if (self.getNpcId() == AliveTumor)
			{
				((NpcInstance) self).dropItem(killer.getPlayer(), 13797, Rnd.get(2, 5));
				final NpcInstance deadTumor = addSpawnWithoutRespawn(DeadTumor, self.getLoc(), 0);
				notifyTumorDeath();
				self.deleteMe();
				for (Player p : getPlayers())
				{
					p.sendPacket(new ExShowScreenMessage(NpcString.THE_TUMOR_INSIDE_S1_HAS_BEEN_DESTROYED_NTHE_NEARBY_UNDEAD_THAT_WERE_ATTACKING_SEED_OF_LIFE_START_LOSING_THEIR_ENERGY_AND_RUN_AWAY, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HALL_OF_EROSION.getId()));
				}
				ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						deadTumor.deleteMe();
						addSpawnWithoutRespawn(AliveTumor, deadTumor.getLoc(), 0);
						handleTumorHp(0.25);
						invokeDeathListener();
						for (Player p : getPlayers())
						{
							p.sendPacket(new ExShowScreenMessage(NpcString.THE_TUMOR_INSIDE_S1_HAS_COMPLETELY_REVIVED_, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HALL_OF_EROSION.getId()));
						}
					}
				}, tumorRespawnTime);
			}
			else if (self.getNpcId() == SoulWagon)
			{
				if (getAllByNpcId(SoulWagon, true).size() > 0)
				{
					rescheduleFailureTask(60000L);
				}
				else
				{
					conquestConclusion(true);
				}
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class TimerTask extends RunnableImpl
	{
		/**
		 * Constructor for TimerTask.
		 */
		public TimerTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			long time = ((startTime + (25 * 60 * 1000L)) - System.currentTimeMillis()) / 60000;
			if (time == 0)
			{
				conquestConclusion(false);
			}
			else
			{
				for (Player p : getPlayers())
				{
					p.sendPacket(new ExShowScreenMessage(NpcString.S1_MINUTES_ARE_REMAINING, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, String.valueOf(((startTime + (25 * 60 * 1000L)) - System.currentTimeMillis()) / 60000)));
				}
			}
		}
	}
	
	/**
	 * Method notifyAttackSeed.
	 */
	void notifyAttackSeed()
	{
		for (final NpcInstance npc : getNpcs())
		{
			NpcInstance seed = getNearestSeed(npc);
			if (seed != null)
			{
				if (npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
				{
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, seed, 100);
					ThreadPoolManager.getInstance().schedule(new RunnableImpl()
					{
						@Override
						public void runImpl()
						{
							npc.getAggroList().clear(true);
							npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
							npc.moveToLocation(Location.findAroundPosition(npc, 400), 0, false);
						}
					}, 7000L);
				}
			}
		}
	}
	
	/**
	 * Method notifyCoffinDeath.
	 */
	public void notifyCoffinDeath()
	{
		tumorRespawnTime -= 5 * 1000L;
	}
	
	/**
	 * Method spawnCoffin.
	 * @param tumor NpcInstance
	 */
	void spawnCoffin(NpcInstance tumor)
	{
		addSpawnWithoutRespawn(RegenerationCoffin, new Location(tumor.getLoc().x, tumor.getLoc().y, tumor.getLoc().z, Location.getRandomHeading()), 250);
	}
	
	/**
	 * Method getNearestSeed.
	 * @param mob NpcInstance
	 * @return NpcInstance
	 */
	private NpcInstance getNearestSeed(NpcInstance mob)
	{
		for (NpcInstance npc : mob.getAroundNpc(900, 300))
		{
			if ((npc.getNpcId() == UnstableSeed) && (mob.getZone(Zone.ZoneType.poison) == npc.getZone(Zone.ZoneType.poison)))
			{
				return npc;
			}
		}
		return null;
	}
	
	/**
	 * Method invokeDeathListener.
	 */
	void invokeDeathListener()
	{
		for (NpcInstance npc : getNpcs())
		{
			npc.addListener(deathListener);
		}
	}
	
	/**
	 * Method conquestConclusion.
	 * @param win boolean
	 */
	void conquestConclusion(boolean win)
	{
		if (conquestEnded)
		{
			return;
		}
		cancelTimers();
		conquestEnded = true;
		clearReflection(15, true);
		if (win)
		{
			setReenterTime(System.currentTimeMillis());
		}
		for (Player p : getPlayers())
		{
			if (win)
			{
				QuestState qs = p.getQuestState(_697_DefendtheHallofErosion.class);
				if ((qs != null) && (qs.getCond() == 1))
				{
					qs.set("defenceDone", 1);
				}
			}
			p.sendPacket(new ExShowScreenMessage(win ? NpcString.CONGRATULATIONS_YOU_HAVE_SUCCEEDED_AT_S1_S2_THE_INSTANCE_WILL_SHORTLY_EXPIRE : NpcString.YOU_HAVE_FAILED_AT_S1_S2, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HALL_OF_EROSION.getId(), "#" + NpcString.DEFEND.getId()));
		}
	}
	
	/**
	 * Method handleTumorHp.
	 * @param percent double
	 */
	void handleTumorHp(double percent)
	{
		for (NpcInstance npc : getAllByNpcId(AliveTumor, true))
		{
			npc.setCurrentHp(npc.getMaxHp() * percent, false);
		}
	}
	
	/**
	 * Method notifyTumorDeath.
	 */
	void notifyTumorDeath()
	{
		tumorKillCount++;
		if ((tumorKillCount > 4) && !soulwagonSpawned)
		{
			soulwagonSpawned = true;
			spawnByGroup("soi_hoe_defence_soulwagon");
			for (NpcInstance npc : getAllByNpcId(SoulWagon, true))
			{
				Functions.npcShout(npc, NpcString.HA_HA_HA);
				NpcInstance seed = getNearestSeed(npc);
				if (seed != null)
				{
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, seed, 100);
				}
				rescheduleFailureTask(180000L);
			}
			invokeDeathListener();
		}
	}
	
	/**
	 * Method rescheduleFailureTask.
	 * @param time long
	 */
	void rescheduleFailureTask(long time)
	{
		if (failureTask != null)
		{
			failureTask.cancel(false);
			failureTask = null;
		}
		failureTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				conquestConclusion(false);
			}
		}, time);
	}
	
	/**
	 * Method cancelTimers.
	 */
	private void cancelTimers()
	{
		if (timerTask != null)
		{
			timerTask.cancel(false);
		}
		if (agressionTask != null)
		{
			agressionTask.cancel(false);
		}
		if (coffinSpawnTask != null)
		{
			coffinSpawnTask.cancel(false);
		}
		if (aliveTumorSpawnTask != null)
		{
			aliveTumorSpawnTask.cancel(false);
		}
		if (failureTask != null)
		{
			failureTask.cancel(false);
		}
	}
	
	/**
	 * Method onPlayerEnter.
	 * @param player Player
	 */
	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
		for (int i : zoneEventTriggers)
		{
			player.sendPacket(new EventTrigger(i, true));
		}
	}
	
	/**
	 * Method onCollapse.
	 */
	@Override
	protected void onCollapse()
	{
		cancelTimers();
		super.onCollapse();
	}
}
