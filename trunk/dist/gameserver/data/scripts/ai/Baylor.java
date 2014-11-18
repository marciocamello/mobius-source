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

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.instancemanager.HellboundManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class Baylor extends DefaultAI
{
	final Skill Berserk;
	final Skill Invincible;
	final Skill Imprison;
	final Skill GroundStrike;
	final Skill JumpAttack;
	final Skill StrongPunch;
	final Skill Stun1;
	final Skill Stun2;
	final Skill Stun3;
	static final int PresentationBalor2 = 5402;
	static final int PresentationBalor3 = 5403;
	static final int PresentationBalor4 = 5404;
	static final int PresentationBalor10 = 5410;
	static final int PresentationBalor11 = 5411;
	static final int PresentationBalor12 = 5412;
	static private final int Water_Dragon_Claw = 2360;
	private boolean _isUsedInvincible = false;
	private int _claw_count = 0;
	private long _last_claw_time = 0;
	
	/**
	 * @author Mobius
	 */
	private class SpawnSocial extends RunnableImpl
	{
		/**
		 * Constructor for SpawnSocial.
		 */
		SpawnSocial()
		{
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final NpcInstance actor = getActor();
			
			if (actor != null)
			{
				actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, PresentationBalor2, 1, 4000, 0));
			}
		}
	}
	
	/**
	 * Constructor for Baylor.
	 * @param actor NpcInstance
	 */
	public Baylor(NpcInstance actor)
	{
		super(actor);
		final TIntObjectHashMap<Skill> skills = getActor().getTemplate().getSkills();
		Berserk = skills.get(5224);
		Invincible = skills.get(5225);
		Imprison = skills.get(5226);
		GroundStrike = skills.get(5227);
		JumpAttack = skills.get(5228);
		StrongPunch = skills.get(5229);
		Stun1 = skills.get(5230);
		Stun2 = skills.get(5231);
		Stun3 = skills.get(5232);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		ThreadPoolManager.getInstance().schedule(new SpawnSocial(), 20000);
		super.onEvtSpawn();
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		final NpcInstance actor = getActor();
		
		if (actor.isDead() || (skill == null) || (caster == null))
		{
			return;
		}
		
		if ((System.currentTimeMillis() - _last_claw_time) > 5000)
		{
			_claw_count = 0;
		}
		
		if (skill.getId() == Water_Dragon_Claw)
		{
			_claw_count++;
			_last_claw_time = System.currentTimeMillis();
		}
		
		final Player player = caster.getPlayer();
		
		if (player == null)
		{
			return;
		}
		
		int count = 1;
		final Party party = player.getParty();
		
		if (party != null)
		{
			count = party.getMemberCount();
		}
		
		if (_claw_count >= count)
		{
			_claw_count = 0;
			actor.getEffectList().stopEffect(Invincible);
			Functions.npcSay(actor, NpcString.NO_ONE_IS_GOING_TO_SURVIVE);
		}
	}
	
	/**
	 * Method createNewTask.
	 * @return boolean
	 */
	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		Creature target = prepareTarget();
		
		if (target == null)
		{
			return false;
		}
		
		final NpcInstance actor = getActor();
		
		if (actor.isDead())
		{
			return false;
		}
		
		final double distance = actor.getDistance(target);
		final double actor_hp_precent = actor.getCurrentHpPercents();
		
		if ((actor_hp_precent < 30) && !_isUsedInvincible)
		{
			_isUsedInvincible = true;
			addTaskBuff(actor, Invincible);
			Functions.npcSay(actor, NpcString.DEMON_KING_BELETH_GIVE_ME_THE_POWER_AAAHH);
			return true;
		}
		
		final int rnd_per = Rnd.get(100);
		
		if ((rnd_per < 7) && (actor.getEffectList().getEffectsBySkill(Berserk) == null))
		{
			addTaskBuff(actor, Berserk);
			Functions.npcSay(actor, NpcString.DEMON_KING_BELETH_GIVE_ME_THE_POWER_AAAHH);
			return true;
		}
		
		if ((rnd_per < 15) || ((rnd_per < 33) && (actor.getEffectList().getEffectsBySkill(Berserk) != null)))
		{
			return chooseTaskAndTargets(StrongPunch, target, distance);
		}
		
		if (!actor.isAMuted() && (rnd_per < 50))
		{
			return chooseTaskAndTargets(null, target, distance);
		}
		
		final Map<Skill, Integer> skills = new HashMap<>();
		addDesiredSkill(skills, target, distance, GroundStrike);
		addDesiredSkill(skills, target, distance, JumpAttack);
		addDesiredSkill(skills, target, distance, StrongPunch);
		addDesiredSkill(skills, target, distance, Stun1);
		addDesiredSkill(skills, target, distance, Stun2);
		addDesiredSkill(skills, target, distance, Stun3);
		final Skill skill = selectTopSkill(skills);
		
		if ((skill != null) && !skill.isOffensive())
		{
			target = actor;
		}
		
		return chooseTaskAndTargets(skill, target, distance);
	}
	
	/**
	 * Method maybeMoveToHome.
	 * @return boolean
	 */
	@Override
	protected boolean maybeMoveToHome()
	{
		return false;
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		if (HellboundManager.getConfidence() < 1)
		{
			HellboundManager.setConfidence(1);
		}
		
		super.onEvtDead(killer);
	}
}
