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
package ai.Freya;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class FreyaStandNormal extends Fighter
{
	private static final int Skill_EternalBlizzard = 6274;
	private long _eternalblizzardReuseTimer = 0;
	private static final int _eternalblizzardReuseDelay = 60;
	private static final int Skill_IceBall = 6278;
	private long _iceballReuseTimer = 0;
	private static final int _iceballReuseDelay = 10;
	private static final int Skill_SummonElemental = 6277;
	private long _summonReuseTimer = 0;
	private static final int _summonReuseDelay = 50;
	private static final int Skill_SelfNova = 6279;
	private long _selfnovaReuseTimer = 0;
	private static final int _selfnovaReuseDelay = 60;
	private static final int Skill_DeathSentence = 6280;
	private long _deathsentenceReuseTimer = 0;
	private static final int _deathsentenceReuseDelay = 40;
	private static final int Skill_ReflectMagic = 6282;
	private long _reflectReuseTimer = 0;
	private static final int _reflectReuseDelay = 40;
	private static final int Skill_IceStorm = 6283;
	private long _icestormReuseTimer = 0;
	private static final int _icestormReuseDelay = 50;
	private static final int Skill_Anger = 6285;
	private long _angerReuseTimer = 0;
	private static final int _angerReuseDelay = 30;
	private long _dispelTimer = 0;
	private static final int _dispelReuseDelay = 7;
	private long _idleDelay = 0;
	private long lastFactionNotifyTime = 0;
	
	/**
	 * Constructor for FreyaStandNormal.
	 * @param actor NpcInstance
	 */
	public FreyaStandNormal(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = 7000;
	}
	
	/**
	 * Method thinkAttack.
	 */
	@Override
	protected void thinkAttack()
	{
		final NpcInstance actor = getActor();
		final Creature topDamager = actor.getAggroList().getTopDamager();
		final Creature randomHated = actor.getAggroList().getRandomHated();
		final Creature mostHated = actor.getAggroList().getMostHated();
		
		if (!actor.isCastingNow() && (_eternalblizzardReuseTimer < System.currentTimeMillis()))
		{
			actor.doCast(SkillTable.getInstance().getInfo(Skill_EternalBlizzard, 1), actor, true);
			final Reflection r = getActor().getReflection();
			
			for (Player p : r.getPlayers())
			{
				p.sendPacket(new ExShowScreenMessage(NpcString.I_FEEL_STRONG_MAGIC_FLOW, 3000, ScreenMessageAlign.MIDDLE_CENTER, true));
			}
			
			_eternalblizzardReuseTimer = System.currentTimeMillis() + (_eternalblizzardReuseDelay * 1000L);
		}
		
		if (!actor.isCastingNow() && !actor.isMoving && (_iceballReuseTimer < System.currentTimeMillis()))
		{
			if ((topDamager != null) && !topDamager.isDead() && topDamager.isInRangeZ(actor, 1000))
			{
				actor.doCast(SkillTable.getInstance().getInfo(Skill_IceBall, 1), topDamager, true);
				_iceballReuseTimer = System.currentTimeMillis() + (_iceballReuseDelay * 1000L);
			}
		}
		
		if (!actor.isCastingNow() && (_summonReuseTimer < System.currentTimeMillis()))
		{
			actor.doCast(SkillTable.getInstance().getInfo(Skill_SummonElemental, 1), actor, true);
			
			for (NpcInstance guard : getActor().getAroundNpc(800, 100))
			{
				guard.altOnMagicUseTimer(guard, SkillTable.getInstance().getInfo(Skill_SummonElemental, 1));
			}
			
			_summonReuseTimer = System.currentTimeMillis() + (_summonReuseDelay * 1000L);
		}
		
		if (!actor.isCastingNow() && (_selfnovaReuseTimer < System.currentTimeMillis()))
		{
			actor.doCast(SkillTable.getInstance().getInfo(Skill_SelfNova, 1), actor, true);
			_selfnovaReuseTimer = System.currentTimeMillis() + (_selfnovaReuseDelay * 1000L);
		}
		
		if (!actor.isCastingNow() && (_reflectReuseTimer < System.currentTimeMillis()))
		{
			actor.doCast(SkillTable.getInstance().getInfo(Skill_ReflectMagic, 1), actor, true);
			_reflectReuseTimer = System.currentTimeMillis() + (_reflectReuseDelay * 1000L);
		}
		
		if (!actor.isCastingNow() && (_icestormReuseTimer < System.currentTimeMillis()))
		{
			actor.doCast(SkillTable.getInstance().getInfo(Skill_IceStorm, 1), actor, true);
			_icestormReuseTimer = System.currentTimeMillis() + (_icestormReuseDelay * 1000L);
		}
		
		if (!actor.isCastingNow() && !actor.isMoving && (_deathsentenceReuseTimer < System.currentTimeMillis()))
		{
			if ((randomHated != null) && !randomHated.isDead() && randomHated.isInRangeZ(actor, 1000))
			{
				actor.doCast(SkillTable.getInstance().getInfo(Skill_DeathSentence, 1), randomHated, true);
				_deathsentenceReuseTimer = System.currentTimeMillis() + (_deathsentenceReuseDelay * 1000L);
			}
		}
		
		if (!actor.isCastingNow() && !actor.isMoving && (_angerReuseTimer < System.currentTimeMillis()))
		{
			actor.doCast(SkillTable.getInstance().getInfo(Skill_Anger, 1), actor, true);
			_angerReuseTimer = System.currentTimeMillis() + (_angerReuseDelay * 1000L);
			
			if ((mostHated != null) && (randomHated != null) && (actor.getAggroList().getCharMap().size() > 1))
			{
				actor.getAggroList().remove(mostHated, true);
				actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, randomHated, 900000);
			}
		}
		
		if (_dispelTimer < System.currentTimeMillis())
		{
			for (Effect e : actor.getEffectList().getAllEffects())
			{
				if ((e != null) && e.isOffensive())
				{
					e.exit();
				}
			}
			
			_dispelTimer = System.currentTimeMillis() + (_dispelReuseDelay * 1000L);
		}
		
		if (_idleDelay > 0)
		{
			_idleDelay = 0;
		}
		
		if ((System.currentTimeMillis() - lastFactionNotifyTime) > _minFactionNotifyInterval)
		{
			lastFactionNotifyTime = System.currentTimeMillis();
			
			for (NpcInstance npc : actor.getReflection().getNpcs())
			{
				if (npc.isMonster() && (!npc.equals(actor)))
				{
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, actor.getAggroList().getMostHated(), 5);
				}
			}
		}
		
		super.thinkAttack();
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		final long generalReuse = System.currentTimeMillis() + 30000L;
		_eternalblizzardReuseTimer += generalReuse + (Rnd.get(1, 20) * 1000L);
		_iceballReuseTimer += generalReuse + (Rnd.get(1, 20) * 1000L);
		_summonReuseTimer += generalReuse + (Rnd.get(1, 20) * 1000L);
		_selfnovaReuseTimer += generalReuse + (Rnd.get(1, 20) * 1000L);
		_reflectReuseTimer += generalReuse + (Rnd.get(1, 20) * 1000L);
		_icestormReuseTimer += generalReuse + (Rnd.get(1, 20) * 1000L);
		_deathsentenceReuseTimer += generalReuse + (Rnd.get(1, 20) * 1000L);
		_angerReuseTimer += generalReuse + (Rnd.get(1, 20) * 1000L);
		final Reflection r = getActor().getReflection();
		
		for (Player p : r.getPlayers())
		{
			this.notifyEvent(CtrlEvent.EVT_AGGRESSION, p, 2);
		}
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if ((_idleDelay == 0) && !getActor().isCurrentHpFull())
		{
			_idleDelay = System.currentTimeMillis();
		}
		
		final Reflection ref = getActor().getReflection();
		
		if (!getActor().isDead() && (_idleDelay > 0) && ((_idleDelay + 60000) < System.currentTimeMillis()))
		{
			if (!ref.isDefault())
			{
				for (Player p : ref.getPlayers())
				{
					p.sendMessage("Freya has won the battle and banished the intruders.");
				}
				
				ref.collapse();
			}
		}
		
		super.thinkActive();
		return true;
	}
	
	/**
	 * Method teleportHome.
	 */
	@Override
	protected void teleportHome()
	{
	}
}
