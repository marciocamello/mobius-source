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
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.instancemanager.SoIManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncSet;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HeartInfinityAttack extends Reflection
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
	 * Field Ekimus. (value is 29150)
	 */
	private static final int Ekimus = 29150;
	/**
	 * Field Hound. (value is 29151)
	 */
	private static final int Hound = 29151;
	/**
	 * Field RegenerationCoffin. (value is 18710)
	 */
	private static final int RegenerationCoffin = 18710;
	/**
	 * Field tumorRespawnTime.
	 */
	long tumorRespawnTime;
	/**
	 * Field ekimus.
	 */
	private NpcInstance ekimus;
	/**
	 * Field hounds.
	 */
	private final List<NpcInstance> hounds = new ArrayList<>(2);
	/**
	 * Field houndBlocked.
	 */
	private boolean houndBlocked = false;
	/**
	 * Field conquestBegun.
	 */
	private boolean conquestBegun = false;
	/**
	 * Field conquestEnded.
	 */
	boolean conquestEnded = false;
	/**
	 * Field deathListener.
	 */
	private final DeathListener deathListener = new DeathListener();
	/**
	 * Field invoker.
	 */
	private Player invoker;
	/**
	 * Field timerTask.
	 */
	private ScheduledFuture<?> timerTask;
	/**
	 * Field startTime.
	 */
	long startTime;
	/**
	 * Field ekimusIdleTask.
	 */
	private ScheduledFuture<?> ekimusIdleTask;
	/**
	 * Field notifiedEkimusIdle.
	 */
	private boolean notifiedEkimusIdle = false;
	
	/**
	 * Method onCreate.
	 */
	@Override
	protected void onCreate()
	{
		super.onCreate();
		spawnByGroup("soi_hoi_attack_init");
		tumorRespawnTime = 150 * 1000L;
	}
	
	/**
	 * Method notifyEchmusEntrance.
	 * @param leader Player
	 */
	public void notifyEchmusEntrance(Player leader)
	{
		if (conquestBegun)
		{
			return;
		}
		conquestBegun = true;
		invoker = leader;
		for (Player p : getPlayers())
		{
			p.sendPacket(new ExShowScreenMessage(NpcString.YOU_WILL_PARTICIPATE_IN_S1_S2_SHORTLY, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HEART_OF_IMMORTALITY.getId(), "#" + NpcString.ATTACK.getId()));
		}
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				for (Player p : getPlayers())
				{
					p.showQuestMovie(ExStartScenePlayer.SCENE_ECHMUS_OPENING);
				}
				ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						conquestBegins();
					}
				}, 62500L);
			}
		}, 20000L);
	}
	
	/**
	 * Method conquestBegins.
	 */
	void conquestBegins()
	{
		despawnByGroup("soi_hoi_attack_init");
		spawnByGroup("soi_hoi_attack_mob_1");
		spawnByGroup("soi_hoi_attack_mob_2");
		spawnByGroup("soi_hoi_attack_mob_3");
		spawnByGroup("soi_hoi_attack_mob_4");
		spawnByGroup("soi_hoi_attack_mob_5");
		spawnByGroup("soi_hoi_attack_mob_6");
		spawnByGroup("soi_hoi_attack_tumors");
		for (NpcInstance n : getAllByNpcId(AliveTumor, true))
		{
			n.setCurrentHp(n.getMaxHp() * .5, false);
		}
		spawnByGroup("soi_hoi_attack_wards");
		ekimus = addSpawnWithoutRespawn(Ekimus, new Location(-179537, 208854, -15504, 16384), 0);
		hounds.add(addSpawnWithoutRespawn(Hound, new Location(-179224, 209624, -15504, 16384), 0));
		hounds.add(addSpawnWithoutRespawn(Hound, new Location(-179880, 209464, -15504, 16384), 0));
		handleEkimusStats();
		getZone("[soi_hoi_attack_attackup_1]").setActive(true);
		getZone("[soi_hoi_attack_attackup_2]").setActive(true);
		getZone("[soi_hoi_attack_attackup_3]").setActive(true);
		getZone("[soi_hoi_attack_attackup_4]").setActive(true);
		getZone("[soi_hoi_attack_attackup_5]").setActive(true);
		getZone("[soi_hoi_attack_attackup_6]").setActive(true);
		getZone("[soi_hoi_attack_defenceup_1]").setActive(true);
		getZone("[soi_hoi_attack_defenceup_2]").setActive(true);
		getZone("[soi_hoi_attack_defenceup_3]").setActive(true);
		getZone("[soi_hoi_attack_defenceup_4]").setActive(true);
		getZone("[soi_hoi_attack_defenceup_5]").setActive(true);
		getZone("[soi_hoi_attack_defenceup_6]").setActive(true);
		getDoor(14240102).openMe();
		for (Player p : getPlayers())
		{
			p.sendPacket(new ExShowScreenMessage(NpcString.YOU_CAN_HEAR_THE_UNDEAD_OF_EKIMUS_RUSHING_TOWARD_YOU, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HEART_OF_IMMORTALITY.getId(), "#" + NpcString.ATTACK.getId()));
		}
		if (invoker != null)
		{
			ekimus.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, invoker, 50000);
			Functions.npcShout(ekimus, NpcString.I_SHALL_ACCEPT_YOUR_CHALLENGE_S1_COME_AND_DIE_IN_THE_ARMS_OF_IMMORTALITY, invoker.getName());
		}
		invokeDeathListener();
		startTime = System.currentTimeMillis();
		timerTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new TimerTask(), 298 * 1000L, 5 * 60 * 1000L);
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
				NpcInstance deadTumor = addSpawnWithoutRespawn(DeadTumor, self.getLoc(), 0);
				self.deleteMe();
				notifyTumorDeath();
				ThreadPoolManager.getInstance().schedule(new TumorRevival(deadTumor), tumorRespawnTime);
				ThreadPoolManager.getInstance().schedule(new RegenerationCoffinSpawn(deadTumor), 20000L);
			}
			else if (self.getNpcId() == Ekimus)
			{
				conquestConclusion(true);
				SoIManager.notifyEkimusKill();
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class TumorRevival extends RunnableImpl
	{
		/**
		 * Field _deadTumor.
		 */
		NpcInstance _deadTumor;
		
		/**
		 * Constructor for TumorRevival.
		 * @param deadTumor NpcInstance
		 */
		public TumorRevival(NpcInstance deadTumor)
		{
			_deadTumor = deadTumor;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (conquestEnded)
			{
				return;
			}
			NpcInstance tumor = addSpawnWithoutRespawn(AliveTumor, _deadTumor.getLoc(), 0);
			tumor.setCurrentHp(tumor.getMaxHp() * .25, false);
			notifyTumorRevival();
			_deadTumor.deleteMe();
			invokeDeathListener();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class RegenerationCoffinSpawn extends RunnableImpl
	{
		/**
		 * Field _deadTumor.
		 */
		NpcInstance _deadTumor;
		
		/**
		 * Constructor for RegenerationCoffinSpawn.
		 * @param deadTumor NpcInstance
		 */
		public RegenerationCoffinSpawn(NpcInstance deadTumor)
		{
			_deadTumor = deadTumor;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (conquestEnded)
			{
				return;
			}
			for (int i = 0; i < 4; i++)
			{
				addSpawnWithoutRespawn(RegenerationCoffin, new Location(_deadTumor.getLoc().x, _deadTumor.getLoc().y, _deadTumor.getLoc().z, Location.getRandomHeading()), 250);
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
				if (time == 20)
				{
					spawnByGroup("soi_hoi_attack_bosses");
				}
				for (Player p : getPlayers())
				{
					p.sendPacket(new ExShowScreenMessage(NpcString.S1_MINUTES_ARE_REMAINING, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, String.valueOf(((startTime + (25 * 60 * 1000L)) - System.currentTimeMillis()) / 60000)));
				}
			}
		}
	}
	
	/**
	 * Method notifyTumorDeath.
	 */
	void notifyTumorDeath()
	{
		if (getAliveTumorCount() < 1)
		{
			houndBlocked = true;
			for (NpcInstance npc : hounds)
			{
				npc.block();
			}
			for (Player p : getPlayers())
			{
				p.sendPacket(new ExShowScreenMessage(NpcString.WITH_ALL_CONNECTIONS_TO_THE_TUMOR_SEVERED_EKIMUS_HAS_LOST_ITS_POWER_TO_CONTROL_THE_FERAL_HOUND, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false));
			}
		}
		else
		{
			for (Player p : getPlayers())
			{
				p.sendPacket(new ExShowScreenMessage(NpcString.THE_TUMOR_INSIDE_S1_THAT_HAS_PROVIDED_ENERGY_N_TO_EKIMUS_IS_DESTROYED, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HEART_OF_IMMORTALITY.getId()));
			}
		}
		handleEkimusStats();
	}
	
	/**
	 * Method notifyTumorRevival.
	 */
	void notifyTumorRevival()
	{
		if ((getAliveTumorCount() == 1) && houndBlocked)
		{
			houndBlocked = false;
			for (NpcInstance npc : hounds)
			{
				npc.unblock();
			}
			for (Player p : getPlayers())
			{
				p.sendPacket(new ExShowScreenMessage(NpcString.WITH_THE_CONNECTION_TO_THE_TUMOR_RESTORED_EKIMUS_HAS_REGAINED_CONTROL_OVER_THE_FERAL_HOUND, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false));
			}
		}
		else
		{
			for (Player p : getPlayers())
			{
				p.sendPacket(new ExShowScreenMessage(NpcString.THE_TUMOR_INSIDE_S1_HAS_BEEN_COMPLETELY_RESURRECTED_N_AND_STARTED_TO_ENERGIZE_EKIMUS_AGAIN, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HEART_OF_IMMORTALITY.getId()));
			}
		}
		handleEkimusStats();
	}
	
	/**
	 * Method getAliveTumorCount.
	 * @return int
	 */
	private int getAliveTumorCount()
	{
		return getAllByNpcId(AliveTumor, true).size();
	}
	
	/**
	 * Method notifyCoffinDeath.
	 */
	public void notifyCoffinDeath()
	{
		tumorRespawnTime += 8 * 1000L;
	}
	
	/**
	 * Method handleEkimusStats.
	 */
	private void handleEkimusStats()
	{
		double[] a = getStatMultiplier();
		ekimus.removeStatsOwner(this);
		ekimus.addStatFunc(new FuncSet(Stats.POWER_ATTACK, 0x30, this, ekimus.getTemplate().getBasePAtk() * 3));
		ekimus.addStatFunc(new FuncSet(Stats.MAGIC_ATTACK, 0x30, this, ekimus.getTemplate().getBaseMAtk() * 10));
		ekimus.addStatFunc(new FuncSet(Stats.POWER_DEFENCE, 0x30, this, ekimus.getTemplate().getBasePDef() * a[1]));
		ekimus.addStatFunc(new FuncSet(Stats.MAGIC_DEFENCE, 0x30, this, ekimus.getTemplate().getBaseMDef() * a[0]));
		ekimus.addStatFunc(new FuncSet(Stats.REGENERATE_HP_RATE, 0x30, this, ekimus.getTemplate().getBaseHpReg() * a[2]));
	}
	
	/**
	 * Method getStatMultiplier.
	 * @return double[]
	 */
	private double[] getStatMultiplier()
	{
		double[] a = new double[3];
		switch (getAliveTumorCount())
		{
			case 6:
				a[0] = 2;
				a[1] = 1;
				a[2] = 4;
				break;
			case 5:
				a[0] = 1.9;
				a[1] = 0.9;
				a[2] = 3.5;
				break;
			case 4:
				a[0] = 1.5;
				a[1] = 0.6;
				a[2] = 3.0;
				break;
			case 3:
				a[0] = 1.0;
				a[1] = 0.4;
				a[2] = 2.5;
				break;
			case 2:
				a[0] = 0.7;
				a[1] = 0.3;
				a[2] = 2.0;
				break;
			case 1:
				a[0] = 0.3;
				a[1] = 0.15;
				a[2] = 1.0;
				break;
			case 0:
				a[0] = 0.12;
				a[1] = 0.06;
				a[2] = 0.25;
				break;
		}
		return a;
	}
	
	/**
	 * Method conquestConclusion.
	 * @param win boolean
	 */
	void conquestConclusion(boolean win)
	{
		if (timerTask != null)
		{
			timerTask.cancel(false);
		}
		conquestEnded = true;
		despawnByGroup("soi_hoi_attack_wards");
		despawnByGroup("soi_hoi_attack_mob_1");
		despawnByGroup("soi_hoi_attack_mob_2");
		despawnByGroup("soi_hoi_attack_mob_3");
		despawnByGroup("soi_hoi_attack_mob_4");
		despawnByGroup("soi_hoi_attack_mob_5");
		despawnByGroup("soi_hoi_attack_mob_6");
		despawnByGroup("soi_hoi_attack_bosses");
		if ((ekimus != null) && !ekimus.isDead())
		{
			for (NpcInstance npc : hounds)
			{
				npc.deleteMe();
			}
			ekimus.deleteMe();
		}
		startCollapseTimer(15 * 60 * 1000L);
		if (win)
		{
			setReenterTime(System.currentTimeMillis());
		}
		for (Player p : getPlayers())
		{
			p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(15));
			p.sendPacket(new ExShowScreenMessage(win ? NpcString.CONGRATULATIONS_YOU_HAVE_SUCCEEDED_AT_S1_S2_THE_INSTANCE_WILL_SHORTLY_EXPIRE : NpcString.YOU_HAVE_FAILED_AT_S1_S2, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HEART_OF_IMMORTALITY.getId(), "#" + NpcString.ATTACK.getId()));
			p.showQuestMovie(win ? ExStartScenePlayer.SCENE_ECHMUS_SUCCESS : ExStartScenePlayer.SCENE_ECHMUS_FAIL);
		}
		for (NpcInstance npc : getNpcs())
		{
			if ((npc.getNpcId() == AliveTumor) || (npc.getNpcId() == DeadTumor) || (npc.getNpcId() == RegenerationCoffin))
			{
				npc.deleteMe();
			}
		}
	}
	
	/**
	 * Method notifyEkimusAttack.
	 */
	public void notifyEkimusAttack()
	{
		if (ekimusIdleTask != null)
		{
			ekimusIdleTask.cancel(false);
			ekimusIdleTask = null;
			notifiedEkimusIdle = false;
		}
	}
	
	/**
	 * Method notifyEkimusIdle.
	 */
	public void notifyEkimusIdle()
	{
		if (notifiedEkimusIdle)
		{
			return;
		}
		notifiedEkimusIdle = true;
		for (Player p : getPlayers())
		{
			p.sendPacket(new ExShowScreenMessage(NpcString.THERE_IS_NO_PARTY_CURRENTLY_CHALLENGING_EKIMUS, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "180"));
		}
		ekimusIdleTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				conquestConclusion(false);
			}
		}, 180000L);
	}
	
	/**
	 * Method notifyEkimusRoomEntrance.
	 */
	public void notifyEkimusRoomEntrance()
	{
		for (Playable playable : getZone("[soi_hoi_attack_echmusroom]").getInsidePlayables())
		{
			playable.teleToLocation(new Location(-179537, 211233, -15472));
		}
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				for (Player p : getPlayers())
				{
					p.sendPacket(new ExShowScreenMessage(NpcString.EKIMUS_HAS_SENSED_ABNORMAL_ACTIVITY, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false));
				}
			}
		}, 10000L);
	}
	
	/**
	 * Method onCollapse.
	 */
	@Override
	protected void onCollapse()
	{
		if (timerTask != null)
		{
			timerTask.cancel(false);
		}
		super.onCollapse();
	}
}
