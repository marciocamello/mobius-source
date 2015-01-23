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
package ai;

import instances.IstinaInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.components.NpcStringId;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius and Nache
 */
public class Istina extends Fighter
{
	// Npcs
	private static final int ISTINA_LIGHT = 29195;
	private static final int ISTINA_HARD = 29196;
	private static final int ISTINAS_CREATION = 23125;
	private static final int SEALING_ENERGY = 19036;
	
	// Skills
	private static final Skill DEATH_BLOW = SkillTable.getInstance().getInfo(14219, 1);
	private static final Skill ISTINA_MARK = SkillTable.getInstance().getInfo(14218, 1);
	static final Skill BARRIER_OF_REFLECTION = SkillTable.getInstance().getInfo(14215, 1);
	static final Skill FLOOD = SkillTable.getInstance().getInfo(14220, 1);
	static final Skill MANIFESTATION_OF_AUTHORITY = SkillTable.getInstance().getInfo(14289, 1);
	static final Skill ACID_ERUPTION2 = SkillTable.getInstance().getInfo(14222, 1);
	static final Skill ACID_ERUPTION3 = SkillTable.getInstance().getInfo(14223, 1);
	
	// Ring Zone (Trigger)
	private final int RED_RING = 14220101;
	private final int BLUE_RING = 14220102;
	private final int GREEN_RING = 14220103;
	
	// Ring Locations
	final Zone RED_RING_LOC;
	final Zone BLUE_RING_LOC;
	final Zone GREEN_RING_LOC;
	
	// Items
	public static final int ISTINA_CRYSTAL = 37506;
	
	boolean isHard = false;
	ScheduledFuture<?> _effectCheckTask = null;
	boolean _authorityLock = false;
	boolean _hasFlood = false;
	boolean _hasBarrier = false;
	static Zone _zone;
	int _ring;
	private boolean finishLock = false;
	private boolean lastHitLock = false;
	private final double[] hpPercentTriggers =
	{
		0.9D,
		0.8D,
		0.7D,
		0.6D,
		0.5D,
		0.45D,
		0.4D,
		0.35D,
		0.3D,
		0.25D,
		0.2D,
		0.15D,
		0.1D
	};
	
	public Istina(NpcInstance actor)
	{
		super(actor);
		_zone = ReflectionManager.getInstance().get(getActor().getReflectionId()).getZone("[Isthina_epic]");
		RED_RING_LOC = ReflectionManager.getInstance().get(getActor().getReflectionId()).getZone("[Isthina_red_zone]");
		BLUE_RING_LOC = ReflectionManager.getInstance().get(getActor().getReflectionId()).getZone("[Isthina_blue_zone]");
		GREEN_RING_LOC = ReflectionManager.getInstance().get(getActor().getReflectionId()).getZone("[Isthina_green_zone]");
	}
	
	@Override
	public boolean isGlobalAI()
	{
		return false;
	}
	
	@Override
	protected void onEvtSpawn()
	{
		if (getActor().getId() == ISTINA_HARD)
		{
			isHard = true;
		}
		
		super.onEvtSpawn();
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance npc = getActor();
		
		if (_effectCheckTask == null)
		{
			_effectCheckTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new EffectCheckTask(npc), 0, 2000);
		}
		
		final double lastPercentHp = (npc.getCurrentHp() + damage) / npc.getMaxHp();
		final double currentPercentHp = npc.getCurrentHp() / npc.getMaxHp();
		
		if ((!lastHitLock) && (currentPercentHp <= 0.05D))
		{
			lastHitLock = true;
			onPercentHpReached(npc, 5);
		}
		else
		{
			for (double trigger : hpPercentTriggers)
			{
				if ((lastPercentHp > trigger) && (currentPercentHp <= trigger))
				{
					onPercentHpReached(npc, (int) (trigger * 100));
				}
			}
		}
		
		if (!finishLock)
		{
			final double seed = Rnd.get(1, 100);
			if ((seed < 2) && (!_authorityLock))
			{
				authorityField(npc);
			}
		}
		
		super.onEvtAttacked(attacker, damage);
	}
	
	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		Creature target = prepareTarget();
		
		if (target == null)
		{
			return false;
		}
		
		NpcInstance npc = getActor();
		
		if (npc.isDead())
		{
			return false;
		}
		
		double distance = npc.getDistance(target);
		
		if ((Rnd.get() <= 0.2D) && !npc.isCastingNow())
		{
			final Map<Skill, Integer> skills = new HashMap<>();
			addDesiredSkill(skills, target, distance, ISTINA_MARK);
			addDesiredSkill(skills, target, distance, DEATH_BLOW);
			
			final Skill skill = selectTopSkill(skills);
			if ((skill != null) && !skill.isOffensive())
			{
				target = npc;
			}
			
			return chooseTaskAndTargets(skill, target, distance);
		}
		
		return chooseTaskAndTargets(null, target, distance);
	}
	
	public void onPercentHpReached(NpcInstance npc, int percent)
	{
		final int npcId = npc.getId();
		
		if ((percent == 5) && (!finishLock))
		{
			finishLock = true;
			npc.setIsInvul(true);
			npc.startParalyzed();
			npc.teleToLocation(new Location(-177128, 147224, -11414, 16383), npc.getReflection());
			npc.setTargetable(false);
			
			for (Player player : npc.getReflection().getPlayers())
			{
				if ((player.getTarget() != null))
				{
					player.setTarget(null);
					player.abortAttack(true, true);
					player.abortCast(true, true);
					player.sendActionFailed();
				}
			}
			
			IstinaInstance refl = null;
			if (npc.getReflection() instanceof IstinaInstance)
			{
				refl = (IstinaInstance) npc.getReflection();
			}
			if (refl != null)
			{
				refl.presentBallista(npc);
			}
			
			return;
		}
		
		if ((npcId == ISTINA_LIGHT) || (npcId == ISTINA_HARD))
		{
			ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					IstinaInstance refl = null;
					if (npc.getReflection() instanceof IstinaInstance)
					{
						refl = (IstinaInstance) npc.getReflection();
					}
					if (refl != null)
					{
						if ((refl.acidEruptionCameras != null) && !refl.acidEruptionCameras.isEmpty())
						{
							for (NpcInstance acidEruption : refl.acidEruptionCameras)
							{
								acidEruption.setIsInvul(true);
								acidEruption.setRandomWalk(false);
								
								for (Player player : refl.getPlayers())
								{
									if (Rnd.chance(50) && !npc.isCastingNow())
									{
										acidEruption.teleToLocation(player.getLoc(), refl);
										acidEruption.setTarget(acidEruption);
										Skill skill = Rnd.chance(50) ? ACID_ERUPTION3 : ACID_ERUPTION2;
										acidEruption.doCast(skill, acidEruption, true);
									}
								}
							}
						}
					}
					ThreadPoolManager.getInstance().schedule(this, 20000);
				}
			}, 20000);
		}
		
		if ((Rnd.get() <= 0.1D) && !npc.isCastingNow())
		{
			if (npc.getEffectList().getEffectsBySkill(BARRIER_OF_REFLECTION) == null)
			{
				npc.doCast(BARRIER_OF_REFLECTION, npc, false);
			}
			else if (npc.getEffectList().getEffectsBySkill(FLOOD) == null)
			{
				npc.doCast(FLOOD, npc, false);
			}
		}
		
		if (npcId == ISTINA_HARD)
		{
			if ((percent <= 50) && ((percent % 5) == 0))
			{
				npc.getReflection().addSpawnWithoutRespawn(ISTINAS_CREATION, npc.getLoc(), 0);
			}
			else
			{
				npc.getReflection().addSpawnWithoutRespawn(SEALING_ENERGY, npc.getLoc(), 0);
			}
			for (Player p : npc.getReflection().getPlayers())
			{
				npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 1000);
			}
		}
	}
	
	private void authorityField(final NpcInstance npc)
	{
		_authorityLock = true;
		double seed = Rnd.get();
		final int ring = (seed >= 0.33D) && (seed < 0.66D) ? 1 : seed < 0.33D ? 0 : 2;
		_ring = ring;
		
		if (seed < 0.33D)
		{
			npc.broadcastPacket(new ExShowScreenMessage(NpcStringId.ISTINA_S_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_GREEN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
		}
		else if ((seed >= 0.33D) && (seed < 0.66D))
		{
			npc.broadcastPacket(new ExShowScreenMessage(NpcStringId.ISTINA_S_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_BLUE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
		}
		else
		{
			npc.broadcastPacket(new ExShowScreenMessage(NpcStringId.ISTINA_S_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_RED, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
		}
		npc.broadcastPacket(new PlaySound("istina.istina_voice_01"));
		ThreadPoolManager.getInstance().schedule(new runAuthorityRing(npc), 500);
	}
	
	/**
	 * @author Mobius
	 */
	private class EffectCheckTask extends RunnableImpl
	{
		private final NpcInstance _npc;
		
		/**
		 * Constructor for EffectCheckTask.
		 * @param npc NpcInstance
		 */
		EffectCheckTask(NpcInstance npc)
		{
			_npc = npc;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_npc == null)
			{
				if (_effectCheckTask != null)
				{
					_effectCheckTask.cancel(false);
				}
			}
			
			boolean hasBarrier = false;
			boolean hasFlood = false;
			
			if (_npc.getEffectList().getEffectsBySkillId(BARRIER_OF_REFLECTION.getId()) != null)
			{
				hasBarrier = true;
				
				if (hasFlood)
				{
					return;
				}
			}
			else
			{
				if (_npc.getEffectList().getEffectsBySkillId(FLOOD.getId()) != null)
				{
					hasFlood = true;
				}
				
				if (hasBarrier)
				{
					return;
				}
			}
			
			if ((_hasBarrier) && (!hasBarrier))
			{
				_npc.setNpcState(2);
				_npc.setNpcState(0);
				_npc.broadcastPacket(new ExShowScreenMessage(NpcStringId.THERE_IS_STILL_LOTS_OF_TIME_LEFT_DO_NOT_STOP_HERE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
			}
			else if ((!_hasBarrier) && (hasBarrier))
			{
				_npc.setNpcState(1);
			}
			
			if ((_hasFlood) && (hasFlood))
			{
				_npc.broadcastPacket(new ExShowScreenMessage(NpcStringId.ISTINA_GETS_FURIOUS_AND_RECKLESSLY_CRAZY, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
			}
			else if ((_hasFlood) && (!hasFlood))
			{
				_npc.broadcastPacket(new ExShowScreenMessage(NpcStringId.BERSERKER_OF_ISTINA_HAS_BEEN_DISABLED, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class runAuthorityRing extends RunnableImpl
	{
		private final NpcInstance _npc;
		
		/**
		 * Constructor for runAuthorityRing.
		 * @param npc NpcInstance
		 */
		runAuthorityRing(NpcInstance npc)
		{
			_npc = npc;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final NpcInstance npc = _npc;
			Zone zones;
			
			if (_ring == 2)
			{
				npc.broadcastPacket(new EventTrigger(GREEN_RING, true));
				npc.broadcastPacket(new EventTrigger(RED_RING, true));
				zones = GREEN_RING_LOC;
			}
			else if (_ring == 0)
			{
				npc.broadcastPacket(new EventTrigger(BLUE_RING, true));
				npc.broadcastPacket(new EventTrigger(GREEN_RING, true));
				zones = BLUE_RING_LOC;
			}
			else
			{
				npc.broadcastPacket(new EventTrigger(RED_RING, true));
				npc.broadcastPacket(new EventTrigger(BLUE_RING, true));
				zones = RED_RING_LOC;
			}
			
			for (Player player : _zone.getInsidePlayers())
			{
				if (!player.isInZone(zones))
				{
					MANIFESTATION_OF_AUTHORITY.getEffects(npc, player, false, false);
				}
			}
			
			_authorityLock = false;
			ThreadPoolManager.getInstance().schedule(new removeAuthorityRing(npc), 16000); // 16.0 Secs to remove Triggers
		}
	}
	
	private class removeAuthorityRing extends RunnableImpl
	{
		private final NpcInstance _npc;
		
		removeAuthorityRing(NpcInstance npc)
		{
			_npc = npc;
		}
		
		@Override
		public void runImpl()
		{
			final NpcInstance npc = _npc;
			npc.broadcastPacket(new EventTrigger(GREEN_RING, false));
			npc.broadcastPacket(new EventTrigger(RED_RING, false));
			npc.broadcastPacket(new EventTrigger(BLUE_RING, false));
		}
	}
	
	@Override
	protected void onEvtDead(Creature killer)
	{
		NpcInstance npc = getActor();
		if (npc.getId() == ISTINA_HARD)
		{
			for (Player p : killer.getReflection().getPlayers())
			{
				ItemFunctions.addItem(p, ISTINA_CRYSTAL, 1, true);
			}
		}
		super.onEvtDead(killer);
	}
}