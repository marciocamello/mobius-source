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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius + Nache
 */
public final class Baylor extends Fighter
{
	public static final int BAYLOR = 29213;
	private static final Skill Ground_Strike = SkillTable.getInstance().getInfo(5227, 1);
	private static final Skill JumpAttack = SkillTable.getInstance().getInfo(5228, 1);
	private static final Skill StrongPunch = SkillTable.getInstance().getInfo(5229, 1);
	private static final Skill ChainOfYoke = SkillTable.getInstance().getInfo(14508, 1);
	private static final Skill HPDrain = SkillTable.getInstance().getInfo(14509, 1);
	private static final Skill ExposeWeakness = SkillTable.getInstance().getInfo(14511, 1);
	private static final Skill Kick = SkillTable.getInstance().getInfo(14513, 1);
	
	/**
	 * Constructor for Baylor.
	 * @param actor NpcInstance
	 */
	public Baylor(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		super.onEvtAttacked(attacker, damage);
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
		
		if ((Rnd.get() <= 0.2D) && !actor.isCastingNow())
		{
			final Map<Skill, Integer> skills = new HashMap<>();
			addDesiredSkill(skills, target, distance, Ground_Strike);
			addDesiredSkill(skills, target, distance, ChainOfYoke);
			addDesiredSkill(skills, target, distance, ExposeWeakness);
			addDesiredSkill(skills, target, distance, JumpAttack);
			addDesiredSkill(skills, target, distance, HPDrain);
			addDesiredSkill(skills, target, distance, StrongPunch);
			addDesiredSkill(skills, target, distance, Kick);
			final Skill skill = selectTopSkill(skills);
			
			if ((skill != null) && !skill.isOffensive())
			{
				target = actor;
			}
			
			return chooseTaskAndTargets(skill, target, distance);
		}
		return chooseTaskAndTargets(null, target, distance);
	}
}
