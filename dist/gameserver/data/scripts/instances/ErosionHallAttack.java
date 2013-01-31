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

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.lang.ArrayUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.instancemanager.SoIManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ErosionHallAttack extends Reflection
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
	 * Field Cohemenes. (value is 25634)
	 */
	private static final int Cohemenes = 25634;
	/**
	 * Field RegenerationCoffin. (value is 18710)
	 */
	private static final int RegenerationCoffin = 18710;
	/**
	 * Field viceraZones.
	 */
	private final Zone[] viceraZones = new Zone[12];
	/**
	 * Field zoneEventTriggers.
	 */
	private final int[] zoneEventTriggers = ArrayUtils.createAscendingArray(14240001, 14240012);
	/**
	 * Field startZoneListener.
	 */
	private final ZoneListener startZoneListener = new ZoneListener();
	/**
	 * Field deathListener.
	 */
	private final DeathListener deathListener = new DeathListener();
	/**
	 * Field conquestBegun.
	 */
	boolean conquestBegun = false;
	/**
	 * Field conquestEnded.
	 */
	boolean conquestEnded = false;
	/**
	 * Field tumorRespawnTime.
	 */
	long tumorRespawnTime;
	/**
	 * Field cohemenes.
	 */
	NpcInstance cohemenes = null;
	/**
	 * Field startTime.
	 */
	long startTime;
	/**
	 * Field timerTask.
	 */
	private ScheduledFuture<?> timerTask;
	
	/**
	 * Method onCreate.
	 */
	@Override
	protected void onCreate()
	{
		super.onCreate();
		viceraZones[0] = getZone("[soi_hoe_attack_pc_vicera_1]");
		viceraZones[1] = getZone("[soi_hoe_attack_pc_vicera_2]");
		viceraZones[2] = getZone("[soi_hoe_attack_pc_vicera_3]");
		viceraZones[3] = getZone("[soi_hoe_attack_pc_vicera_4]");
		viceraZones[4] = getZone("[soi_hoe_attack_pc_vicera_5]");
		viceraZones[5] = getZone("[soi_hoe_attack_pc_vicera_6]");
		viceraZones[6] = getZone("[soi_hoe_attack_pc_vicera_7]");
		viceraZones[6].addListener(startZoneListener);
		viceraZones[7] = getZone("[soi_hoe_attack_pc_vicera_8]");
		viceraZones[8] = getZone("[soi_hoe_attack_pc_vicera_9]");
		viceraZones[9] = getZone("[soi_hoe_attack_pc_vicera_10]");
		viceraZones[10] = getZone("[soi_hoe_attack_pc_vicera_11]");
		viceraZones[11] = getZone("[soi_hoe_attack_pc_vicera_12]");
		for (Zone z : viceraZones)
		{
			z.setActive(true);
		}
		tumorRespawnTime = 3 * 60 * 1000L;
	}
	
	/**
	 * Method conquestBegins.
	 */
	void conquestBegins()
	{
		for (Player p : getPlayers())
		{
			p.sendPacket(new ExShowScreenMessage(NpcString.YOU_CAN_HEAR_THE_UNDEAD_OF_EKIMUS_RUSHING_TOWARD_YOU, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HALL_OF_EROSION.getId(), "#" + NpcString.ATTACK.getId()));
		}
		spawnByGroup("soi_hoe_attack_tumors");
		for (NpcInstance n : getAllByNpcId(AliveTumor, true))
		{
			n.setCurrentHp(n.getMaxHp() * .5, false);
		}
		spawnByGroup("soi_hoe_attack_symbols");
		spawnByGroup("soi_hoe_attack_wards");
		invokeDeathListener();
		spawnByGroup("soi_hoe_attack_mob_1");
		spawnByGroup("soi_hoe_attack_mob_2");
		spawnByGroup("soi_hoe_attack_mob_3");
		spawnByGroup("soi_hoe_attack_mob_4");
		spawnByGroup("soi_hoe_attack_mob_5");
		spawnByGroup("soi_hoe_attack_mob_6");
		spawnByGroup("soi_hoe_attack_mob_7");
		spawnByGroup("soi_hoe_attack_mob_8");
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
				NpcInstance deadTumor = addSpawnWithoutRespawn(DeadTumor, self.getLoc(), 0);
				self.deleteMe();
				notifyTumorDeath(deadTumor);
				ThreadPoolManager.getInstance().schedule(new TumorRevival(deadTumor), tumorRespawnTime);
				ThreadPoolManager.getInstance().schedule(new RegenerationCoffinSpawn(deadTumor), 20000L);
			}
			else if (self.getNpcId() == Cohemenes)
			{
				Functions.npcShout(cohemenes, NpcString.KEU);
				conquestConclusion(true);
				SoIManager.notifyCohemenesKill();
			}
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
			notifyTumorRevival(_deadTumor);
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
				for (Player p : getPlayers())
				{
					p.sendPacket(new ExShowScreenMessage(NpcString.S1_MINUTES_ARE_REMAINING, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, String.valueOf(((startTime + (25 * 60 * 1000L)) - System.currentTimeMillis()) / 60000)));
				}
			}
		}
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
	 * Method notifyCoffinDeath.
	 */
	public void notifyCoffinDeath()
	{
		tumorRespawnTime += 10 * 1000L;
	}
	
	/**
	 * Method notifyTumorDeath.
	 * @param tumor NpcInstance
	 */
	void notifyTumorDeath(NpcInstance tumor)
	{
		if (getAliveTumorCount() == 0)
		{
			cohemenesApperance();
		}
		else
		{
			for (Player p : getPlayers())
			{
				p.sendPacket(new ExShowScreenMessage(NpcString.THE_TUMOR_INSIDE_S1_HAS_BEEN_DESTROYED_NIN_ORDER_TO_DRAW_OUT_THE_COWARDLY_COHEMENES_YOU_MUST_DESTROY_ALL_THE_TUMORS, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HALL_OF_EROSION.getId()));
			}
		}
		manageRegenZone(getRoomId(tumor), true);
	}
	
	/**
	 * Method notifyTumorRevival.
	 * @param tumor NpcInstance
	 */
	void notifyTumorRevival(NpcInstance tumor)
	{
		if ((getAliveTumorCount() > 0) && (cohemenes != null) && !cohemenes.isDead())
		{
			cohemenes.getMinionList().deleteMinions();
			cohemenes.deleteMe();
		}
		for (Player p : getPlayers())
		{
			p.sendPacket(new ExShowScreenMessage(NpcString.THE_TUMOR_INSIDE_S1_HAS_COMPLETELY_REVIVED, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HALL_OF_EROSION.getId()));
		}
		manageRegenZone(getRoomId(tumor), false);
	}
	
	/**
	 * Method cohemenesApperance.
	 */
	private void cohemenesApperance()
	{
		for (Player p : getPlayers())
		{
			p.sendPacket(new ExShowScreenMessage(NpcString.ALL_THE_TUMORS_INSIDE_S1_HAVE_BEEN_DESTROYED_DRIVEN_INTO_A_CORNER_COHEMENES_APPEARS_CLOSE_BY, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HALL_OF_EROSION.getId()));
		}
		cohemenes = addSpawnWithoutRespawn(Cohemenes, getRandomSymbolLocation(), 0);
		cohemenes.getMinionList().spawnMinions();
		invokeDeathListener();
		Functions.npcShout(cohemenes, NpcString.CMON_CMON_SHOW_YOUR_FACE_YOU_LITTLE_RATS_LET_ME_SEE_WHAT_THE_DOOMED_WEAKLINGS_ARE_SCHEMING);
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
		despawnByGroup("soi_hoe_attack_symbols");
		despawnByGroup("soi_hoe_attack_wards");
		if ((cohemenes != null) && !cohemenes.isDead())
		{
			cohemenes.getMinionList().deleteMinions();
			cohemenes.deleteMe();
		}
		startCollapseTimer(15 * 60 * 1000L);
		if (win)
		{
			setReenterTime(System.currentTimeMillis());
		}
		for (Player p : getPlayers())
		{
			p.sendPacket(new SystemMessage2(SystemMsg.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addInteger(15));
			p.sendPacket(new ExShowScreenMessage(win ? NpcString.CONGRATULATIONS_YOU_HAVE_SUCCEEDED_AT_S1_S2_THE_INSTANCE_WILL_SHORTLY_EXPIRE : NpcString.YOU_HAVE_FAILED_AT_S1_S2, 8000, ExShowScreenMessage.ScreenMessageAlign.MIDDLE_CENTER, false, 1, -1, false, "#" + NpcString.HALL_OF_EROSION.getId(), "#" + NpcString.ATTACK.getId()));
		}
		for (NpcInstance npc : getNpcs())
		{
			if ((npc.getNpcId() == AliveTumor) || (npc.getNpcId() == DeadTumor))
			{
				npc.deleteMe();
			}
		}
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
	 * Method getRoomId.
	 * @param npc NpcInstance
	 * @return int
	 */
	private int getRoomId(NpcInstance npc)
	{
		int i = 0;
		if (npc.isInZone("[soi_hoe_attack_attackup1_1]"))
		{
			i = 1;
		}
		else if (npc.isInZone("[soi_hoe_attack_attackup1_2]"))
		{
			i = 2;
		}
		else if (npc.isInZone("[soi_hoe_attack_attackup1_3]"))
		{
			i = 3;
		}
		else if (npc.isInZone("[soi_hoe_attack_attackup1_4]"))
		{
			i = 4;
		}
		else if (npc.isInZone("[soi_hoe_attack_attackup1_5]"))
		{
			i = 5;
		}
		else if (npc.isInZone("[soi_hoe_attack_attackup1_6]"))
		{
			i = 6;
		}
		else if (npc.isInZone("[soi_hoe_attack_attackup1_7]"))
		{
			i = 7;
		}
		else if (npc.isInZone("[soi_hoe_attack_attackup1_8]"))
		{
			i = 8;
		}
		return i;
	}
	
	/**
	 * Method manageRegenZone.
	 * @param roomId int
	 * @param doActivate boolean
	 */
	private void manageRegenZone(int roomId, boolean doActivate)
	{
		switch (roomId)
		{
			case 1:
				getZone("[soi_hoe_attack_pc_regen_1]").setActive(doActivate);
				break;
			case 2:
				getZone("[soi_hoe_attack_pc_regen_2]").setActive(doActivate);
				break;
			case 3:
				getZone("[soi_hoe_attack_pc_regen_3]").setActive(doActivate);
				break;
			case 4:
				getZone("[soi_hoe_attack_pc_regen_4]").setActive(doActivate);
				break;
			case 5:
				getZone("[soi_hoe_attack_pc_regen_5]").setActive(doActivate);
				break;
			case 6:
				getZone("[soi_hoe_attack_pc_regen_6]").setActive(doActivate);
				break;
			case 7:
				getZone("[soi_hoe_attack_pc_regen_7]").setActive(doActivate);
				break;
			case 8:
				getZone("[soi_hoe_attack_pc_regen_8]").setActive(doActivate);
				break;
		}
	}
	
	/**
	 * Method getRandomSymbolLocation.
	 * @return Location
	 */
	private Location getRandomSymbolLocation()
	{
		List<NpcInstance> npclocations = getAllByNpcId(18780, true);
		if (!npclocations.isEmpty())
		{
			return Location.findPointToStay(npclocations.get(Rnd.get(npclocations.size())), 100, 250);
		}
		return new Location(-178418, 211653, -12029);
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
